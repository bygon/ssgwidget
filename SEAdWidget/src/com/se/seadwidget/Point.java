package com.se.seadwidget;

import com.util.ATools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Point extends TitleActivity {
	private TextView apoint;
	private TextView npoint;
	private TextView idtxt;	
	private Button ok;
	private SharedPreferences pref;
	private ATools tools = new ATools();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.point);		
		pref = getSharedPreferences("ADWIDGET", Activity.MODE_PRIVATE); 	//계정등록 완료하면 포인트 뿌려준다.
		idtxt = (TextView)findViewById(R.id.idtxt);		
		
		if(pref.getString("ID",  "").length() > 0){
			idtxt.setText("안녕하세요! " + pref.getString("NAME",  "")  +  "님");
			Intent intent = getIntent(); 
			String Point = intent.getStringExtra("Point");
			String TotPoint = intent.getStringExtra("TotPoint");
						
			apoint = (TextView)findViewById(R.id.apoint); 
			npoint = (TextView)findViewById(R.id.npoint); 
			
			apoint.setText(tools.moneyFmt(TotPoint) + "P");
			npoint.setText(tools.moneyFmt(Point) + "P");	
			
			ok = (Button)findViewById(R.id.ok);
			ok.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {					
				    finish();
				}
			});		
			
		}else{
			idtxt.setText("계정을 등록해 주세요.");	
			
			ok = (Button)findViewById(R.id.ok);
			ok.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {					
				    finish();
				    Intent intent = new Intent("com.se.seadwidget.ACTION_ACCOUNT");
				    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);				    
				    startActivity(intent);
				}
			});		
		}
		
			
	}
	
}
