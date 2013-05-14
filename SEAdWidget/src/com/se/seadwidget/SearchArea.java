package com.se.seadwidget;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class SearchArea extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		TextView tv = new TextView(this);
		tv.setText("가까운 매장 찾기");
		tv.setTextSize(15);
		setContentView(tv);
	}
}
