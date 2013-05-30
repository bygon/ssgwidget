package com.se.seadwidget;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Menual extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manual);
	}
	
	/*
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		
		Intent mintent = new Intent(Const.ACTION_MENU);
		mintent.putExtra("Open", false);
		this.finish();
	}
	*/
}
