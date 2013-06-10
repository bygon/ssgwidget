package com.se.seadwidget;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
	private String Login_CODE = "0";
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
					/*
					
					// XML을 가져온다.
				    URL url;
				    try {
				    	url = new URL(getString(R.string.serverip) + getString(R.string.loginroot));
				        URLConnection connection;
				        connection = url.openConnection();

				        HttpURLConnection httpConnection = (HttpURLConnection)connection;
				        int responseCode = httpConnection.getResponseCode();
				        
				        if (responseCode == HttpURLConnection.HTTP_OK) {
				        	InputStream in = httpConnection.getInputStream(); 
				            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				            DocumentBuilder db = dbf.newDocumentBuilder();

				            Document dom = db.parse(in);
				            Element docEle = dom.getDocumentElement();
				            
				            //정보로 구성된 리스트를 얻어온다.
				            NodeList nl = docEle.getElementsByTagName("header");
				            if (nl != null && nl.getLength() > 0) {
				            	Element entry = (Element)nl.item(0);
			                    Element code = (Element)entry.getElementsByTagName("code").item(0);

			                    Login_CODE = code.getFirstChild().getNodeValue();
				            }
				        }
				    } catch (MalformedURLException e) {
				        e.printStackTrace();
				    } catch (IOException e) {
				        e.printStackTrace();
				    } catch (ParserConfigurationException e) {
				        e.printStackTrace();
				    } catch (SAXException e) {
				        e.printStackTrace();
				    } finally {
				    }
					
				    Toast.makeText(Account.this, "Login_CODE " + Login_CODE, Toast.LENGTH_SHORT).show();
				    
				    if(Login_CODE.equals("100")){
				    	SharedPreferences.Editor editor = pref.edit();
					    editor.putBoolean("ADING", true); 
					    editor.putString("ID", id.getText().toString()); 
					    editor.putString("PW", pw.getText().toString()); 
					    editor.commit();
					    
					    finish();
				    }else{
				    	Toast.makeText(Account.this, "로그인 정보가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
				    }
				    */
					
					SharedPreferences.Editor editor = pref.edit();
				    editor.putBoolean("ADING", true); 
				    editor.putString("ID", id.getText().toString()); 
				    editor.putString("PW", pw.getText().toString()); 
				    editor.commit();
				    
				    finish();
					
				}
			});
		}
	}
}
