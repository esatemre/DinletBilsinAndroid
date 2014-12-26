package com.ytu.android.dinletbilsin.util;

import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;

public class DinletBilsin {
	// SCREENS
	public final static int HOME = 1;
	public final static int PAST = 2;
	public final static int SETTINGS = 3;
	public final static int ABOUT = 4;
	public final static int RUN = 5;
	//public final static String SERVICE_URL = "http://dinletbilsin.apphb.com/api/searchmusic";
	//public final static String SERVICE_HASH_URL = "http://dinletbilsin.apphb.com/Home/SearchWithHash";
	public final static String SERVICE_UPLOAD_URL = "http://dinletbilsin.apphb.com/api/searchmusicandroid"; //test-2021
	//private static final File sd = Environment.getExternalStorageDirectory();
	//public final static String HISTORY_FILE = new File("dinletBilsin.preference").getAbsolutePath();
	//

	public static final boolean CheckInternetConnection(Context context) {
		Boolean isCheck = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("check_internet", true);
		if (isCheck) {
			ConnectivityManager cm = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);

			if (cm.getActiveNetworkInfo() != null
					&& cm.getActiveNetworkInfo().isAvailable()
					&& cm.getActiveNetworkInfo().isConnected()) {
				return true;

			} else {
				return false;
			}
		}
		else {
			return true;
		}
		
	}

	public static final boolean CheckForSaving(Context context) {
		
		Boolean isCheck = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("just_record_save", false);
		if (isCheck) 
			return true;

		else 
			return false;
	
	}
	
	public static final boolean SkipRecordForTest(Context context) {
		Boolean isCheck = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("skip_record_for_test", false);
		if (isCheck) 
			return true;

		else 
			return false;
	
	}
	public static final boolean AutoHistory(Context context) {
		Boolean isCheck = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("auto_history", false);
		if (isCheck) 
			return true;

		else 
			return false;
	
	}
	
	public static final int GetMaxHistory(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).getInt("history_number", 40);
	}
	
	public static final String GetLanguage(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).getString("language", "en");
	}
	
	public static void setContent(TextView view, String str) {
		view.setText(str);
	}
	
	public static final int GetRecordTime(Context context) {
		return Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).getString("record_timer", "12"));
	}
	
	public static void ChooseLanguage(Activity act) {
		String languageToLoad  = DinletBilsin.GetLanguage(act.getBaseContext());
	    Locale locale = new Locale((languageToLoad.length() > 2 ? "en" : languageToLoad)); 
	    Locale.setDefault(locale);
	    Configuration config = new Configuration();
	    config.locale = locale;
	    act.getBaseContext().getResources().updateConfiguration(config, act.getBaseContext().getResources().getDisplayMetrics());
	    act.getApplicationContext().getResources().updateConfiguration(config, act.getApplicationContext().getResources().getDisplayMetrics());
	}
	
	public static void buttonEffect(View button){
	    button.setOnTouchListener(new OnTouchListener() {

	        public boolean onTouch(View v, MotionEvent event) {
	            switch (event.getAction()) {
	                case MotionEvent.ACTION_DOWN: {
	                    v.getBackground().setColorFilter(0xe0f47521,PorterDuff.Mode.SRC_ATOP);
	                    v.invalidate();
	                    break;
	                }
	                case MotionEvent.ACTION_UP: {
	                    v.getBackground().clearColorFilter();
	                    v.invalidate();
	                    break;
	                }
	            }
	            return false;
	        }
	    });
	}
	
}
