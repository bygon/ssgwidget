package com.se.seadwidget;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.se.login.Login;
import com.util.ATools;

@SuppressLint("NewApi")
public class Account extends TitleActivity {
	private Button homepage;
	private Button login;
	private Button logout;
	private EditText id;
	private EditText pw;
	private TextView idtxt;
	private SharedPreferences pref;
	private boolean ADING = false;
	private HashMap MM = new HashMap();
	private ATools tools = new ATools();
	private static final boolean DEVELOPER_MODE = true;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (DEVELOPER_MODE) {			
			StrictMode.enableDefaults();		
		}
		
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
			
			idtxt.setText("현재 아래의 계정으로 등록되어 있습니다.\n" + pref.getString("NAME",  "")  +  "(" + pref.getString("ID",  "") + ")\n\n계정 변경을 원하실 경우, 아래 버튼을 눌러주세요.");
						
			logout.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					SharedPreferences.Editor editor = pref.edit();
					editor.remove("ADING");
					editor.remove("ID");
					editor.remove("PW");
					editor.remove("NAME");
					editor.remove("POINT");
					editor.remove("TOTPOINT");
					editor.remove("PUPDAY");
				    editor.commit();
				    
				    finish();				    
				    //Toast.makeText(Account.this, "ADING 제거 완료", Toast.LENGTH_SHORT).show();
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
					
					Login log = new Login(getString(R.string.serverip) + getString(R.string.loginroot));
					
					String errmsg = "";
					
					if(pw.getText().toString().equals("")){
						errmsg = "비밀번호를 입력하세요.";						
					}	
					
					if(id.getText().toString().equals("")){
						errmsg = "아이디를 입력하세요.";
					}									
					
					if(errmsg.equals("")){
						
						MM = log.Check(id.getText().toString(), pw.getText().toString());
						
						if(MM.size() > 0){
						
							if(MM.get("Login_CODE").toString().equals("100") || MM.get("Login_CODE").toString().equals("110")){
						    	SharedPreferences.Editor editor = pref.edit();
							    editor.putBoolean("ADING", true); 
							    editor.putString("ID", id.getText().toString()); 
							    editor.putString("PW", pw.getText().toString());
							    editor.putString("NAME", MM.get("MEMBER").toString());
							    editor.commit();
							    
							    finish();
						    }else{					    	
						    	errmsg =  "로그인 정보가 일치하지 않습니다.";
						    }
						}else{
							errmsg =  "계정정보를 확인 할 수 없습니다.";				    	
						}
					}
					
					
					if(!errmsg.equals("")){
						Toast toast = Toast.makeText(Account.this, errmsg, Toast.LENGTH_SHORT);
				    	int xOffset = 0;
				    	int yOffset = -120;	//키패드때문에 토스트 위치를 좀 올린다.
				    	toast.setGravity(Gravity.CENTER, xOffset, yOffset);
				    	toast.show();
					}
				}
			});
		}
	}
}
