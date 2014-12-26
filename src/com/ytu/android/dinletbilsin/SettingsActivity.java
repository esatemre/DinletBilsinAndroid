package com.ytu.android.dinletbilsin;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.ytu.android.dinletbilsin.util.DinletBilsin;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.KeyEvent;

public class SettingsActivity extends SherlockPreferenceActivity {
	private static int sett = R.xml.preference;
	OnSharedPreferenceChangeListener listener;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DinletBilsin.ChooseLanguage(this);
		setContentView(R.layout.activity_settings);

		// addPreferencesFromResource(R.xml.pref_general);

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			onCreatePreferenceActivity();
		} else {
			onCreatePreferenceFragment();
		}
		try {
			Preference pref = findPreference("just_record_save");

			final CheckBoxPreference cb = (CheckBoxPreference) findPreference("check_internet");
			if (cb != null && pref != null) {
				pref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
					@Override
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						boolean value = (Boolean) newValue;
						if (value) {
							cb.setChecked(false);
						} else {
							cb.setChecked(true);
						}
						return true;
					}
				});
			}
			listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
				public void onSharedPreferenceChanged(
						SharedPreferences prefs, String key) {
					Intent intent = getIntent();
					overridePendingTransition(0, 0);
					intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
					finish();
					overridePendingTransition(0, 0);
					startActivity(intent);
					MagicAppRestart.doRestart(SettingsActivity.this);
				}
			};

			SharedPreferences prefs = getSharedPreferences(getPackageName()
					+ "_preferences", MODE_PRIVATE);
			prefs.registerOnSharedPreferenceChangeListener(listener);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Wraps legacy {@link #onCreate(Bundle)} code for Android < 3 (i.e. API lvl
	 * < 11).
	 */
	@SuppressWarnings("deprecation")
	private void onCreatePreferenceActivity() {
		addPreferencesFromResource(sett);
	}

	/**
	 * Wraps {@link #onCreate(Bundle)} code for Android >= 3 (i.e. API lvl >=
	 * 11).
	 */
	@SuppressLint("NewApi")
	private void onCreatePreferenceFragment() {
		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, new PF()).commit();
	}

	@TargetApi(11)
	public static class PF extends PreferenceFragment {
		@Override
		public void onCreate(final Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(SettingsActivity.sett); // outer class
																// private
																// members seem
																// to be visible
																// for inner
																// class, and
																// making it
																// static made
																// things so
																// much easier
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(getString(R.string.back))
		// .setIcon(R.drawable.ic_compose)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// kaydet ve cik
		this.finish();
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
