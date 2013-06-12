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
			
			Intent intent = getIntent(); 
			String Point = intent.getStringExtra("Point");			
			
			//idtxt.setText("안녕하세요 " + pref.getString("NAME",  "")  +  "님!\nTODAY " + tools.moneyFmt(Point) + "포인트 적립되었습니다.");
			
			idtxt.setText("현재 아래의 계정으로 등록되어 있습니다.\n" + pref.getString("NAME",  "")  +  "(" + pref.getString("ID",  "") + ")\n\n계정 변경을 원하실 경우, 아래 버튼을 눌러주세요.");
						
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
					
					Login log = new Login(getString(R.string.serverip) + getString(R.string.loginroot));
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
					    	Toast toast = Toast.makeText(Account.this, "로그인 정보가 일치하지 않습니다.", Toast.LENGTH_SHORT);
					    	int xOffset = 0;
					    	int yOffset = -120;
					    	toast.setGravity(Gravity.CENTER, xOffset, yOffset);
					    	toast.show();
					    	
					    	//Toast.makeText(Account.this, "로그인 정보가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
					    }
					}else{
						Toast toast = Toast.makeText(Account.this, "로그인 정보가 일치하지 않습니다.", Toast.LENGTH_SHORT);
				    	int xOffset = 0;
				    	int yOffset = -120;
				    	toast.setGravity(Gravity.CENTER, xOffset, yOffset);
				    	toast.show();
				    	
						//Toast.makeText(Account.this, "로그인 정보가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
					}
				}
			});
		}
	}
}
