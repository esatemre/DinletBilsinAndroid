package com.ytu.android.dinletbilsin;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.ytu.android.dinletbilsin.util.DinletBilsin;
import com.ytu.android.dinletbilsin.util.History;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends SherlockActivity {
	
	private class MyActivityManager extends AsyncTask<Integer, Integer, Integer> {

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
	
	public String result;
	private Button btnHistory;
	private Button btnAgain;
	private TextView txtArtist;
	private TextView txtTrack;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Used to put dark icons on light action bar
		DinletBilsin.ChooseLanguage(this);
		menu.add(0, DinletBilsin.PAST, 1, getString(R.string.title_activity_past))
				.setIcon(R.drawable.ic_action_data_usage)
				.setShowAsAction(
						MenuItem.SHOW_AS_ACTION_IF_ROOM
								| MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		menu.add(0, DinletBilsin.SETTINGS, 2, getString(R.string.title_activity_settings))
				.setIcon(R.drawable.ic_action_settings)
				.setShowAsAction(
						MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW
								| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		return true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DinletBilsin.ChooseLanguage(this);
		setContentView(R.layout.activity_results);
		btnHistory = (Button) findViewById(R.id.btnHistory);
		btnAgain = (Button) findViewById(R.id.btnAgain);
		txtArtist = (TextView) findViewById(R.id.txtArtist);
		txtTrack = (TextView) findViewById(R.id.txtTrack);
		result = getIntent().getExtras().getString("info").replaceAll("\"", "");

		btnAgain.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if ((DinletBilsin.CheckInternetConnection(ResultActivity.this))|| (DinletBilsin.SkipRecordForTest(ResultActivity.this))) {
					new MyActivityManager(ResultActivity.this).execute(DinletBilsin.RUN);
				} else {
					AlertDialog alertMessage = new AlertDialog.Builder(
							ResultActivity.this).create();
					alertMessage.setTitle(getString(R.string.connection_error));
					alertMessage
							.setMessage(getString(R.string.please_connect_your_phone_to_wifi_or_3g));
					alertMessage.show();
				}

			}
		});

		setInfo();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		new MyActivityManager(this).execute(item.getItemId());
		return true;
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {		
			Intent a = new Intent(this, MainActivity.class);
			a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(a);
		}

		return super.onKeyDown(keyCode, event);
	}

	private void setInfo() {
		if (result.trim().compareToIgnoreCase("No match!") == 0) {
			btnHistory.setVisibility(TextView.INVISIBLE);// 1:invisible
															// 0:visible
			txtArtist.setVisibility(TextView.INVISIBLE);
			txtTrack.setVisibility(TextView.VISIBLE);
			DinletBilsin.setContent(txtTrack, result);

		} else {
			manageHistory();
			txtTrack.setVisibility(TextView.VISIBLE);
			if (result.contains("-")) {
				txtArtist.setVisibility(TextView.VISIBLE);
				String[] results = result.split("-");
				DinletBilsin.setContent(
						txtArtist,
						this.getString(R.string.artist) + " : "
								+ results[0].trim());
				DinletBilsin.setContent(
						txtTrack,
						this.getString(R.string.track) + " : "
								+ results[1].trim());
			} else {
				txtArtist.setVisibility(TextView.INVISIBLE);
				DinletBilsin.setContent(txtTrack,
						this.getString(R.string.track) + " : " + result);
			}
		}
	}

	private void manageHistory() {
		if (DinletBilsin.AutoHistory(this)) {
			btnHistory.setVisibility(TextView.INVISIBLE);
			History.addHistory(this, result);
		} else {
			btnHistory.setVisibility(TextView.VISIBLE);
			btnHistory.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					History.addHistory(ResultActivity.this, result);
				}
			});
		}
	}

}
