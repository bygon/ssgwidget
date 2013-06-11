package com.se.seadwidget;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TitleActivity extends Activity implements android.view.View.OnClickListener {

	private TextView _title;
	private Button closeBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		
		
	}
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onPostCreate(savedInstanceState);
		
		Intent intent = getIntent(); 
		String Title = intent.getStringExtra("Title");
		
		if(Title != null){
			//_title.setText(Title);	
		}
		
		closeBtn = (Button)findViewById(R.id.closeBtn);
		closeBtn.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.closeBtn:
			finish();
			break;
		
		}
	}

}
