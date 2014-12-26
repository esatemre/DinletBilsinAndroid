package com.ytu.android.dinletbilsin;

import java.util.LinkedList;
import java.util.Queue;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.ytu.android.dinletbilsin.util.DinletBilsin;
import com.ytu.android.dinletbilsin.util.History;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class PastActivity extends SherlockActivity {

	private ListView list;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Used to put dark icons on light action bar
		menu.add(0, DinletBilsin.PAST, 1, getString(R.string.clean))
		// .setIcon(R.drawable.ic_action_about)
				.setShowAsAction(
						MenuItem.SHOW_AS_ACTION_WITH_TEXT
								| MenuItem.SHOW_AS_ACTION_ALWAYS);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case DinletBilsin.PAST:
			Queue<String> list = new LinkedList<String>();
			History.save(this, list);
			RefreshList();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DinletBilsin.ChooseLanguage(this);
		setContentView(R.layout.activity_past);
		list = (ListView) findViewById(R.id.list);
		Queue<String> liste = History.load(this);
		final String[] liste2 = new String[liste.size()];
		liste.toArray(liste2);
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, liste2);

		list.setAdapter(arrayAdapter);

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				AlertDialog.Builder diyalogOlusturucu = new AlertDialog.Builder(
						PastActivity.this);
				final String str = liste2[position];
				final int pos = position;
				diyalogOlusturucu
						.setMessage(str)
						.setPositiveButton(getString(R.string.share),
								new OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										Intent sendIntent = new Intent();
										sendIntent
												.setAction(Intent.ACTION_SEND);
										sendIntent.putExtra(Intent.EXTRA_TEXT,
												str);
										sendIntent.setType("text/plain");
										startActivity(sendIntent);
									}
								})
						.setNegativeButton(getString(R.string.delete),
								new OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										History.deleteHistory(
												PastActivity.this, pos);
										RefreshList();
									}
								});
				diyalogOlusturucu.create().show();

			}
		});

	}

	private void RefreshList() {
		Intent intent = getIntent();
		finish();
		startActivity(intent);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

}
