/**
 * 
 */
package com.ytu.android.dinletbilsin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;

public class MagicAppRestart extends SherlockActivity {
	// Do not forget to add it to AndroidManifest.xml
	// <activity android:name="your.package.name.MagicAppRestart"/>
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.exit(0);
	}

	public static void doRestart(Activity anyActivity) {
		anyActivity.startActivity(new Intent(anyActivity
				.getApplicationContext(), MagicAppRestart.class));
	}
}
