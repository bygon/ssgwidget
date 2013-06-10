package com.se.seadwidget;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.util.ATools;

public class Account extends TitleActivity {
	private Button homepage;
	private Button login;
	private Button logout;
	private EditText id;
	private EditText pw;
	private TextView idtxt;
	private SharedPreferences pref;
	private boolean ADING = false;
	private ATools tools = new ATools();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		pref = getSharedPreferences("ADWIDGET", Activity.MODE_PRIVATE); 	//계정등록 완료하면 ADING을 넣어준다.
		if(pref != null){
			
		}else{
			SharedPreferences.Editor editor = pref.edit();
		    editor.putBoolean("ADING", false); 
		    editor.commit();
		}
		
		ADING = pref.getBoolean("ADING", false);
		
		if(ADING){
			setContentView(R.layout.myaccount);
			logout = (Button)findViewById(R.id.logout);
			idtxt = (TextView)findViewById(R.id.idtxt);
			
			Intent intent = getIntent(); 
			String Point = intent.getStringExtra("Point");			
			
			idtxt.setText("안녕하세요! " + pref.getString("ID",  "")  +  "님\nTODAY " + tools.moneyFmt(Point) + "포인트 적립되었습니다.");
			
			logout.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					SharedPreferences.Editor editor = pref.edit();
				    editor.putBoolean("ADING", false); 
				    editor.putString("ID", ""); 
				    editor.putString("PW", ""); 
				    editor.commit();
				    
				    //Toast.makeText(Account.this, "ADING 제거 완료", Toast.LENGTH_SHORT).show();
				    finish();
				}
			});			
		}else{
			setContentView(R.layout.account);
			
			homepage = (Button)findViewById(R.id.homepage);		
			login = (Button)findViewById(R.id.account_login);			
			id = (EditText)findViewById(R.id.id);
			pw = (EditText)findViewById(R.id.pw);
			
			homepage.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent i = new Intent(Intent.ACTION_VIEW);
				    Uri u = Uri.parse("http://www.shinsegaepoint.com");
				    i.setData(u);
				    startActivity(i);
				}
			});
			
			login.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					SharedPreferences.Editor editor = pref.edit();
				    editor.putBoolean("ADING", true); 
				    editor.putString("ID", id.getText().toString()); 
				    editor.putString("PW", pw.getText().toString()); 
				    editor.commit();
				    
				    //Toast.makeText(Account.this, "ADING 등록 완료", Toast.LENGTH_SHORT).show();
				    finish();
				}
			});
		}
	}
}
