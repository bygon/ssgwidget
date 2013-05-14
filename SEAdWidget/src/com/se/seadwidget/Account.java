package com.se.seadwidget;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class Account extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TextView tv = new TextView(this);
		tv.setText("계정을 등록하세요.");
		tv.setTextSize(15);
		setContentView(tv);
		
	}
}
