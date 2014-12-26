package com.ytu.android.dinletbilsin;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.ytu.android.dinletbilsin.util.DinletBilsin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends SherlockActivity {

	private class MyActivityManager extends
			AsyncTask<Integer, Integer, Integer> {

		Context context;
		Intent intent;

		public MyActivityManager(Context context) {
			this.context = context.getApplicationContext();
		}

		@Override
		protected Integer doInBackground(Integer... params) {
			intent = null;
			switch (params[0]) {
			case DinletBilsin.RUN:
				intent = new Intent(context, RunActivity.class);
				break;
			case DinletBilsin.PAST:
				intent = new Intent(context, PastActivity.class);
				break;
			case DinletBilsin.SETTINGS:
				intent = new Intent(context, SettingsActivity.class);
				break;
			case DinletBilsin.ABOUT:
				intent = new Intent(context, AboutActivity.class);
				break;
			default:
				break;
			}

			return 1;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		}

	}

	private ImageButton but;
	private ImageButton button;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Used to put dark icons on light action bar
		menu.add(0, DinletBilsin.PAST, 1,
				getString(R.string.title_activity_past))
				.setIcon(R.drawable.ic_action_data_usage)
				.setShowAsAction(
						MenuItem.SHOW_AS_ACTION_IF_ROOM
								| MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		menu.add(0, DinletBilsin.SETTINGS, 2,
				getString(R.string.title_activity_settings))
				.setIcon(R.drawable.ic_action_settings)
				.setShowAsAction(
						MenuItem.SHOW_AS_ACTION_IF_ROOM
								| MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		return true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DinletBilsin.ChooseLanguage(this);
		setContentView(R.layout.activity_main);
		setContent((TextView) findViewById(R.id.txtView));
		but = (ImageButton) findViewById(R.id.imageButton1);
		button = (ImageButton) findViewById(R.id.imageButton2);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new MyActivityManager(MainActivity.this)
						.execute(DinletBilsin.ABOUT);
			}
		});
		but.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (DinletBilsin.CheckInternetConnection(MainActivity.this)
						|| (DinletBilsin.SkipRecordForTest(MainActivity.this))) {
					new MyActivityManager(MainActivity.this)
							.execute(DinletBilsin.RUN);
				} else {
					AlertDialog alertMessage = new AlertDialog.Builder(
							MainActivity.this).create();
					alertMessage.setTitle(getString(R.string.connection_error));
					alertMessage
							.setMessage(getString(R.string.please_connect_your_phone_to_wifi_or_3g));
					alertMessage.show();
				}

			}
		});

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		new MyActivityManager(this).execute(item.getItemId());
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(getString(R.string.exit))
					.setMessage(getString(R.string.are_you_sure))
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setPositiveButton(getString(R.string.yes),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									android.os.Process
											.killProcess(android.os.Process
													.myPid());// exit
								}
							})
					.setNegativeButton(getString(R.string.no),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();// close
								}
							}).show();
		}
		if ((keyCode == KeyEvent.KEYCODE_HOME))
			moveTaskToBack(true);

		return super.onKeyDown(keyCode, event);
	}

	protected void setContent(TextView view) {
		view.setText(getString(R.string.welcome_to_dinlet_bilsin));
	}

}
