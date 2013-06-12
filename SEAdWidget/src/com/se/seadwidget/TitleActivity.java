package com.se.seadwidget;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class TitleActivity extends Activity implements android.view.View.OnClickListener {

	private Button closeBtn;
	private ImageView ll;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);			
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onPostCreate(savedInstanceState);
		
		Intent intent = getIntent(); 
		String src = intent.getStringExtra("src");
		
		ll = (ImageView)findViewById(R.id.ll);
		
		if(src != null){
			if(src.equals("sinc5_ui_02_abar1")){
				ll.setBackgroundDrawable(getResources().getDrawable(R.drawable.sinc5_ui_02_abar1));
			}else if(src.equals("sinc5_ui_02_abar2")){
				ll.setBackgroundDrawable(getResources().getDrawable(R.drawable.sinc5_ui_02_abar2));
			}else if(src.equals("sinc5_ui_abar_title_new1")){
				ll.setBackgroundDrawable(getResources().getDrawable(R.drawable.sinc5_ui_abar_title_new1));
			}else{
				ll.setBackgroundDrawable(getResources().getDrawable(R.drawable.sinc5_ui_abar_title_new1));
			}
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
