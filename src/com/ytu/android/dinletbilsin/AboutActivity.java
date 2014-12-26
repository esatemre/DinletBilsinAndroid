package com.ytu.android.dinletbilsin;

import com.actionbarsherlock.app.SherlockActivity;
import com.ytu.android.dinletbilsin.util.DinletBilsin;

import android.os.Bundle;

public class AboutActivity extends SherlockActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DinletBilsin.ChooseLanguage(this);
		setContentView(R.layout.activity_about);
	}

}
