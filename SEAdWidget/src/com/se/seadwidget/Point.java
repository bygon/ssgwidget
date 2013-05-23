package com.se.seadwidget;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class Point extends Activity {
	private TextView apoint;
	private TextView npoint;
	private TextView idtxt;
	private SharedPreferences pref;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.point);
		
		pref = getSharedPreferences("ADWIDGET", Activity.MODE_PRIVATE); 	//계정등록 완료하면 ADING을 넣어준다.
		idtxt = (TextView)findViewById(R.id.idtxt);		
		
		if(pref.getString("ID",  "").length() > 0){
			idtxt.setText("안녕하세요! " + pref.getString("ID",  "")  +  "님");
			Intent intent = getIntent(); 
			String Point = intent.getStringExtra("Point"); 
			
			apoint = (TextView)findViewById(R.id.apoint); 
			npoint = (TextView)findViewById(R.id.npoint); 
			
			apoint.setText(Point + "P");
			npoint.setText(Point + "P");
		}else{
			idtxt.setText("계정을 등록해 주세요.");
		}
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		
		Intent pintent = new Intent(Const.ACTION_POINT);
		pintent.putExtra("Open", false);
		this.finish();
	}
}
