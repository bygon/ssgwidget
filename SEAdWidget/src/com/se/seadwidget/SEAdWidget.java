package com.se.seadwidget;

import java.util.Vector;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class SEAdWidget extends AppWidgetProvider {

	private static AlarmManager alarmManager; 	//주기적으로 이미지 변경하기 위해 타이머 둬서 호출한다.
	private static PendingIntent mSender;

	private static final String TAG = "SEAdWidget";
	private Context context;
	private static int idx = 0;
	private static int point = 0;
	
	private Boolean isChecked = false;

	private static final int CUSTOMER_MENUAL  = 1;	//매뉴얼
	private static final int CUSTOMER_ACCOUNT = 2;	//계정등록
	private static final int CUSTOMER_POINT   = 3;	//내포인트
	
	private static boolean ADING = false;			//광고 시작 변수
	private static Vector imagev;					//이미지 넣을 벡터

	private static final int WIDGET_UPDATE_INTERVAL = 5000; // 5초마다 갱신
	
		
	@Override
	public void onEnabled(Context context) {
		Toast.makeText(context, "onEnabled", Toast.LENGTH_SHORT).show();
		
		super.onEnabled(context);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		//Toast.makeText(context, "onUpdate", Toast.LENGTH_SHORT).show();
		
		imagev = new Vector();
		imagev.add(R.drawable.adimg01);
		imagev.add(R.drawable.adimg02);
		imagev.add(R.drawable.adimg03);
		imagev.add(R.drawable.adimg04);	//벡터에 이미지 넣기
		
		this.context = context;
		super.onUpdate(context, appWidgetManager, appWidgetIds);

		for (int i = 0; i < appWidgetIds.length; i++) {
			int appWidgetId = appWidgetIds[i];
			RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_main);
			appWidgetManager.updateAppWidget(appWidgetId, views);
		}
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
		//Toast.makeText(context, "initUI", Toast.LENGTH_SHORT).show();
		
		RemoteViews views;		
		
		SharedPreferences pref = context.getSharedPreferences("ADWIDGET", 0); 	//계정등록이 되었는지 확인하자.
		if(pref != null){
			ADING = pref.getBoolean("ADING", false);
		}
		
		if(isChecked){
			views = new RemoteViews(context.getPackageName(), R.layout.widget_main_r);	//메뉴 누를 때
			isChecked = false;
		}else{
			views = new RemoteViews(context.getPackageName(), R.layout.widget_main);
			isChecked = true;
		}
						
		if(ADING){		//계정등록 한다면.... 광고 넣어준다.
			idx++;
			point++;	//이미지 바뀔때마다 포인트를 줘볼까?
						
			views.setImageViewResource(R.id.addImage, (Integer) imagev.get(idx % 4));	//이미지 4개니까 4모듈러로 5초에 한번씩 뿌려준다.			
			Toast.makeText(context, "Point > " + point , Toast.LENGTH_SHORT).show();
		}else{
			idx = 0;
			views.setImageViewResource(R.id.addImage, R.drawable.initimg);
			 			
			//ImageLoader bitm = new ImageLoader(this.context);
			//Bitmap bitmap = bitm.getBitmap("");
			//views.setImageViewBitmap(R.id.addImage, bitmap);
		}

		Intent mintent = new Intent(Const.ACTION_MENU);
		mintent.putExtra("CHECKED", isChecked);
		
		Intent sintent = new Intent(Const.ACTION_MENUAL);
		Intent aintent = new Intent(Const.ACTION_ACCOUNT);
		Intent pintent = new Intent(Const.ACTION_POINT);
		pintent.putExtra("Point", point + "1234"); //포인트화면에 포인트 전달
		
		PendingIntent mPIntent = PendingIntent.getBroadcast(context, 0, mintent, PendingIntent.FLAG_ONE_SHOT);	//메뉴이동
		PendingIntent sPIntent = PendingIntent.getBroadcast(context, 0, sintent, 0);	//메뉴얼 이동
		PendingIntent aPintent = PendingIntent.getBroadcast(context, 0, aintent, 0);	//계정등록 이동
		PendingIntent pPintent = PendingIntent.getBroadcast(context, 0, pintent, 0);	//계정등록 이동

		views.setOnClickPendingIntent(R.id.menuBtn, mPIntent);	//메뉴
		views.setOnClickPendingIntent(R.id.menu1, sPIntent);	//이용안내
		views.setOnClickPendingIntent(R.id.menu2, aPintent);	//계정등록
		views.setOnClickPendingIntent(R.id.menu3, pPintent);	//내포인트	

		for (int appWidgetId : appWidgetIds) {
			appWidgetManager.updateAppWidget(appWidgetId, views);
		}
	}

	/**
	 * Receiver 수신
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		
		String action = intent.getAction();
		Log.d(TAG, "onReceive() action = " + action);
		
		//Toast.makeText(context, "onReceive action = " + action, Toast.LENGTH_SHORT).show();

		// Default Recevier
		if (AppWidgetManager.ACTION_APPWIDGET_ENABLED.equals(action)) {

		} else if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(action)) {			
			removePreviousAlarm();

			isChecked = intent.getBooleanExtra("CHECKED", false);
			
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
			removePreviousAlarm();		
			
			isChecked = intent.getBooleanExtra("CHECKED", false);
			
			AppWidgetManager manager = AppWidgetManager.getInstance(context);
			initUI(context, manager, manager.getAppWidgetIds(new ComponentName(context, getClass())));

			// 알람 등록 : 현제 시간에서 지정한 시간 후에 이벤트 발생
			
			long firstTime = System.currentTimeMillis() + WIDGET_UPDATE_INTERVAL;
			mSender = PendingIntent.getBroadcast(context, 0, intent, 0);
			alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
			alarmManager.set(AlarmManager.RTC, firstTime, mSender);
				
			
		} else if (Const.ACTION_MENUAL.equals(action)) {
			callActivity(context, CUSTOMER_MENUAL);
		} else if (Const.ACTION_ACCOUNT.equals(action)) {
			callActivity(context, CUSTOMER_ACCOUNT);
		}else if (Const.ACTION_POINT.equals(action)) {
			callActivity(context, CUSTOMER_POINT);
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
