package com.se.seadwidget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.se.img.ImageDownloaderAsynkTask;
import com.util.JSONParser;

public class SEAdWidget extends AppWidgetProvider {

	private static AlarmManager alarmManager; 	//주기적으로 이미지 변경하기 위해 타이머 둬서 호출한다.
	private static PendingIntent mSender;

	private static final String TAG = "SEAdWidget";
	private static Context context;
	private static int idx = 0;
	private static int point = 0;	
	private static Boolean isOpen = false;

	private static final int CUSTOMER_MENUAL  = 1;	//매뉴얼
	private static final int CUSTOMER_ACCOUNT = 2;	//계정등록
	private static final int CUSTOMER_POINT   = 3;	//내포인트	
	private static final int CUSTOMER_LINK    = 4;	//링크
	
	private static boolean ADING = false;			//광고 시작 변수
	private static List imageL = new ArrayList();	//정보 넣을 벡터
	private static HashMap imageMap = new HashMap();//정보 넣을 해쉬맵	
	private static String IMG = "";					//현재이미지
	private static String URL = "";					//현재광고	

	private static final int WIDGET_UPDATE_INTERVAL = 5000; // 5초마다 갱신	
	private static int rid = R.layout.widget_main;	//위젯 메인 레이아웃
	
	// JSON Node names 
	private static final String TAG_CONTACTS = "AD";
	// contacts JSONArray 
	private static JSONArray ADBOX = null;
		
	@Override
	public void onEnabled(Context context) {
		//Toast.makeText(context, "onEnabled", Toast.LENGTH_SHORT).show();		
		super.onEnabled(context);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		//Toast.makeText(context, "onUpdate > " + imageL.size() + " MENU " + isOpen, Toast.LENGTH_SHORT).show();
		Log.e("onUpdate", "onUpdate");
		
		//서버에서 광고정보를 가져오자..  나중에 다시 정리....
		if(imageL.size() <= 0 || imageL == null ){	//광고를 다보았다면 서버에서 다시 내려받자
			/*
			HashMap m1 = new HashMap();
			m1.put("IMG", "http://item.emart.co.kr/i/99/02/57/E000030570299_350_b.jpg");
			m1.put("URL", "http://m.emart.com/item/itemDetail.emart?item_id=E000030570299&ctg_id=6632784&shop_id=&emid=em_ma_04_02");		
			
			HashMap m2 = new HashMap();
			m2.put("IMG", "http://item.emart.co.kr/i/45/10/20/8808244201045_350_b.jpg");
			m2.put("URL", "http://m.emart.com/item/itemDetail.emart?item_id=8808244201045&ctg_id=6632784&shop_id=&emid=em_ma_04_02");		
			
			HashMap m3 = new HashMap();
			m3.put("IMG", "http://item.emart.co.kr/i/85/04/72/8809142720485_350_b.jpg");
			m3.put("URL", "http://m.emart.com/item/itemDetail.emart?item_id=8809142720485&ctg_id=6632784&shop_id=&emid=em_ma_04_01");		
			
			HashMap m4 = new HashMap();
			m4.put("IMG", "http://item.emart.co.kr/i/51/30/50/8801128503051_350_b.jpg");
			m4.put("URL", "http://m.emart.com/item/itemDetail.emart?item_id=8801128503051&ctg_id=6632784&shop_id=&emid=em_ts_01_10");
			
			imageL.add(m1);
			imageL.add(m2);
			imageL.add(m3);
			imageL.add(m4);
			*/
			
			try {	//이제 제이슨으로 바꿔보자~~~~~~~~ 일단 로컬 txt파일로
				//JSONParser jParser = new JSONParser();
			    //JSONObject json = jParser.getJSONFromUrl("http://www.naver.com?mode=ading");
				
				JSONParser jParser = new JSONParser();
				String page = jParser.getJSONFromTxt(this.context);	
				JSONObject json = new JSONObject(page);	
				ADBOX = json.getJSONArray(TAG_CONTACTS);
				
				for(int i = 0; i < ADBOX.length(); i++){ 
					json = ADBOX.getJSONObject(i); 

					HashMap map = new HashMap();					
					map.put("IMG", json.getString("IMG")); 
					map.put("URL", json.getString("URL")); 
					
					imageL.add(map);
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}

		this.context = context;
		super.onUpdate(context, appWidgetManager, appWidgetIds);	
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		//Toast.makeText(context, "onDeleted", Toast.LENGTH_SHORT).show();		
		super.onDeleted(context, appWidgetIds);
	}

	@Override
	public void onDisabled(Context context) {
		//Toast.makeText(context, "onDisabled", Toast.LENGTH_SHORT).show();		
		super.onDisabled(context);
	}

	/**
	 * UI 설정 이벤트 설정
	 */
	public void initUI(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		//Toast.makeText(context, "initUI " + isOpen + ADING, Toast.LENGTH_SHORT).show();
		
		Log.e("initUI", "initUI");
		RemoteViews views;
		SharedPreferences pref = context.getSharedPreferences("ADWIDGET", 0); 	//계정등록이 되었는지 확인하자.
		if(pref != null){
			ADING = pref.getBoolean("ADING", false);
		}		
		
		if(isOpen){	//메뉴 레이아웃 분개
			rid = R.layout.widget_main_r;
		}else{
			rid = R.layout.widget_main;
		}
		
		views = new RemoteViews(context.getPackageName(), rid);	//메뉴 누를 때		
		
		Intent mintent = new Intent(Const.ACTION_MENU);
		Intent sintent = new Intent(Const.ACTION_MENUAL);
		Intent aintent = new Intent(Const.ACTION_ACCOUNT);
		Intent pintent = new Intent(Const.ACTION_POINT);	
		Intent lintent = new Intent(Const.ACTION_LINK);
		
		PendingIntent mPIntent = PendingIntent.getBroadcast(context, 0, mintent, PendingIntent.FLAG_ONE_SHOT);	//메뉴이동
		PendingIntent sPIntent = PendingIntent.getBroadcast(context, 0, sintent, 0);	//메뉴얼 이동
		PendingIntent aPintent = PendingIntent.getBroadcast(context, 0, aintent, 0);	//계정등록 이동
		PendingIntent pPintent = PendingIntent.getBroadcast(context, 0, pintent, 0);	//계정등록 이동		
		PendingIntent lPintent = PendingIntent.getBroadcast(context, 0, lintent, 0);	//링크 이동

		views.setOnClickPendingIntent(R.id.menuBtn, mPIntent);	//메뉴
		views.setOnClickPendingIntent(R.id.menu1, sPIntent);	//이용안내
		views.setOnClickPendingIntent(R.id.menu2, aPintent);	//계정등록
		views.setOnClickPendingIntent(R.id.menu3, pPintent);	//내포인트		
		views.setOnClickPendingIntent(R.id.addImage, lPintent);	//링크
		//views.setViewVisibility(R.id.menu1, View.INVISIBLE);		
						
		if(ADING){		//계정등록 한다면.... 광고 넣어준다.
			idx++;
			point++;	//이미지 바뀔때마다 포인트를 줘볼까?
			
			if(imageL.size() > 0){
				HashMap m = new HashMap();	//가져온광고에서 첫번째꺼를 빼오고 지운다.... 다지우면 다시 가져오기
				m = (HashMap)imageL.get(0);
				IMG = m.get("IMG").toString();
				URL = m.get("URL").toString();
				imageL.remove(0);	//맨첫번째꺼를 계속 빼먹는다.
				
				//광고이미지를 비동기로 가져온다 		
				ImageDownloaderAsynkTask imageDownTask = new ImageDownloaderAsynkTask(IMG, views,appWidgetIds, appWidgetManager, this.context);
				imageDownTask.execute(IMG);
			}		
			
		}else{
			idx = 0;	//로그아웃시 광고 클리어
			URL = "";
			IMG = "";
			imageL.clear();
			
			views.setImageViewResource(R.id.addImage, R.drawable.initimg);			
			for (int appWidgetId : appWidgetIds) {
				appWidgetManager.updateAppWidget(appWidgetId, views);
			}
		}
	}
	
	/**
	 * 메뉴 설정
	 */
	public void initMenu(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {	//메뉴만 누를때 레이아웃만 변경하기
		//Toast.makeText(context, "initUI " + isOpen + ADING, Toast.LENGTH_SHORT).show();
		
		Log.e("initMenu", "initMenu");
		RemoteViews views;
		
		/*
		SharedPreferences pref = context.getSharedPreferences("ADWIDGET", 0); 	//계정등록이 되었는지 확인하자.
		if(pref != null){
			ADING = pref.getBoolean("ADING", false);
		}
		*/
		
		if(isOpen){	//메뉴 레이아웃 분개
			rid = R.layout.widget_main_r;
		}else{
			rid = R.layout.widget_main;
		}
		views = new RemoteViews(context.getPackageName(), rid);	//메뉴 누를 때		
		
		Intent mintent = new Intent(Const.ACTION_MENU);
		Intent sintent = new Intent(Const.ACTION_MENUAL);
		Intent aintent = new Intent(Const.ACTION_ACCOUNT);
		Intent pintent = new Intent(Const.ACTION_POINT);		
		Intent lintent = new Intent(Const.ACTION_LINK);
		
		PendingIntent mPIntent = PendingIntent.getBroadcast(context, 0, mintent, PendingIntent.FLAG_ONE_SHOT);	//메뉴이동
		PendingIntent sPIntent = PendingIntent.getBroadcast(context, 0, sintent, 0);	//메뉴얼 이동
		PendingIntent aPintent = PendingIntent.getBroadcast(context, 0, aintent, 0);	//계정등록 이동
		PendingIntent pPintent = PendingIntent.getBroadcast(context, 0, pintent, 0);	//계정등록 이동		
		PendingIntent lPintent = PendingIntent.getBroadcast(context, 0, lintent, 0);	//링크 이동

		views.setOnClickPendingIntent(R.id.menuBtn, mPIntent);	//메뉴
		views.setOnClickPendingIntent(R.id.menu1, sPIntent);	//이용안내
		views.setOnClickPendingIntent(R.id.menu2, aPintent);	//계정등록
		views.setOnClickPendingIntent(R.id.menu3, pPintent);	//내포인트		
		views.setOnClickPendingIntent(R.id.addImage, lPintent);	//링크
						
		if(ADING){		//계정등록 한다면.... 광고 넣어준다.			
			
			//현재 셋팅된 광고이미지를 비동기로 가져온다 		
			ImageDownloaderAsynkTask imageDownTask = new ImageDownloaderAsynkTask(IMG, views,appWidgetIds, appWidgetManager, this.context);
			imageDownTask.execute(IMG);
			
		}else{
			
			views.setImageViewResource(R.id.addImage, R.drawable.initimg);			
			for (int appWidgetId : appWidgetIds) {
				appWidgetManager.updateAppWidget(appWidgetId, views);
			}
		}
	}

	/**
	 * Receiver 수신
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		
		Context con;
		String action = intent.getAction();
		Log.d(TAG, "onReceive() action = " + action);
		
		// Default Recevier
		if (AppWidgetManager.ACTION_APPWIDGET_ENABLED.equals(action)) {

		} else if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(action)) {			
			con = context;
			removePreviousAlarm();
			Log.e("ACTION_APPWIDGET_UPDATE", "ACTION_APPWIDGET_UPDATE");
			//isOpen = intent.getBooleanExtra("Open", false);
			
			AppWidgetManager manager = AppWidgetManager.getInstance(context);
			initUI(context, manager, manager.getAppWidgetIds(new ComponentName(context, getClass())));

			// 알람 등록 : 현재 시간에서 지정한 시간 후에 이벤트 발생
			long firstTime = System.currentTimeMillis() + WIDGET_UPDATE_INTERVAL;
			mSender = PendingIntent.getBroadcast(context, 0, intent, 0);
			alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
			alarmManager.set(AlarmManager.RTC, firstTime, mSender);		
			
		} else if (AppWidgetManager.ACTION_APPWIDGET_DELETED.equals(action)) {
			// 등록한 알람 제거
			removePreviousAlarm();
			
		} else if (AppWidgetManager.ACTION_APPWIDGET_DISABLED.equals(action)) {
			// 등록한 알람 제거
			removePreviousAlarm();
			
		} else if (Const.ACTION_MENU.equals(action)) { // Custom Recevier			
			if(isOpen){	//메뉴 컨트롤
				isOpen = false;
			}else{
				isOpen = true;
			}
			
			AppWidgetManager manager = AppWidgetManager.getInstance(context);
			initMenu(context, manager, manager.getAppWidgetIds(new ComponentName(context, getClass())));
			
		} else if (Const.ACTION_MENUAL.equals(action)) {
			isOpen = false;
			callActivity(context, CUSTOMER_MENUAL);
			
		} else if (Const.ACTION_ACCOUNT.equals(action)) {
			isOpen = false;
			callActivity(context, CUSTOMER_ACCOUNT);
			
		} else if (Const.ACTION_POINT.equals(action)) {
			isOpen = false;
			callActivity(context, CUSTOMER_POINT);		
			
		} else if (Const.ACTION_LINK.equals(action)) {
			isOpen = false;
			if(URL.length() > 0){
				callActivity(context, CUSTOMER_LINK);
			}
		}
	}

	/**
	 * Activity 호출 (Intent.FLAG_ACTIVITY_NEW_TASK)
	 */
	private void callActivity(Context context, int pageIdx) {
		Intent intent = null;

		switch (pageIdx) {
			case CUSTOMER_MENUAL:
				intent = new Intent("com.se.seadwidget.ACTION_MENUAL");
				break;
				
			case CUSTOMER_ACCOUNT:
				intent = new Intent("com.se.seadwidget.ACTION_ACCOUNT");
				break;
				
			case CUSTOMER_POINT:
				intent = new Intent("com.se.seadwidget.ACTION_POINT");
				intent.putExtra("Point", point + ""); //포인트화면에 포인트 전달
				break;
				
			case CUSTOMER_LINK:
				intent = new Intent(Intent.ACTION_VIEW);
			    Uri u = Uri.parse(URL);	//광고클릭 시 해당 광고 사이트 호출
			    intent.setData(u);
				break;
				
			default:
				break;
		}

		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	public void removePreviousAlarm() {
		if (alarmManager != null && mSender != null) {
			mSender.cancel();
			alarmManager.cancel(mSender);
		}
	}	
	
}
