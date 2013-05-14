package com.se.seadwidget;

import java.util.Vector;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class CopyOfSEAdWidget extends AppWidgetProvider {

	private static AlarmManager alarmManager; 	//주기적으로 이미지 변경하기 위해 타이머 둬서 호출한다.
	private static PendingIntent mSender;

	private static final String TAG = "SEAdWidget";
	private Context context;
	private static int idx = 0;

	private static final int CUSTOMER_MANUAL = 1;	//메뉴오픈
	private static final int SEARCH_AREA = 2;		//매장찾기
	private static boolean ADING = false;			//광고 시작 변수
	private static Vector imagev;					//이미지 넣을 벡터

	private static final int WIDGET_UPDATE_INTERVAL = 5000; // 5초마다 갱신 -- 시간은
															// 알아서 조정 할 것
															// millisecond 1000
															// = 1초
	@Override
	public void onEnabled(Context context) {
		Toast.makeText(context, "onEnabled", Toast.LENGTH_SHORT).show();
		
		super.onEnabled(context);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		Toast.makeText(context, "onUpdate", Toast.LENGTH_SHORT).show();
		
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
		Toast.makeText(context, "onDeleted", Toast.LENGTH_SHORT).show();
		
		super.onDeleted(context, appWidgetIds);
	}

	@Override
	public void onDisabled(Context context) {
		Toast.makeText(context, "onDisabled", Toast.LENGTH_SHORT).show();
		
		super.onDisabled(context);
	}

	/**
	 * UI 설정 이벤트 설정
	 */
	public void initUI(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		Toast.makeText(context, "initUI", Toast.LENGTH_SHORT).show();
		
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_main);
		
		if(ADING){	//계정등록 한다면.... 광고 넣어준다.
			idx++;
			
			views.setImageViewResource(R.id.addImage, (Integer) imagev.get(idx % 4));	//이미지 4개니까 4모듈러로 5초에 한번씩 뿌려준다.
		}

		Intent mintent = new Intent(Const.ACTION_MENU);
		Intent sintent = new Intent(Const.ACTION_SEARCH);
		Intent tintent = new Intent(Const.ACTION_START);
		
		PendingIntent mPIntent = PendingIntent.getBroadcast(context, 0, mintent, 0);	//메뉴이동		
		PendingIntent sPIntent = PendingIntent.getBroadcast(context, 0, sintent, 0);	//지점찾기 이동
		PendingIntent tPIntent = PendingIntent.getBroadcast(context, 0, tintent, 0);	//START 이동

		views.setOnClickPendingIntent(R.id.menuBtn, mPIntent);
//		views.setOnClickPendingIntent(R.id.srcBtn, sPIntent);
		views.setOnClickPendingIntent(R.id.strBtn, tPIntent);

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
		
		Toast.makeText(context, "onReceive action = " + action, Toast.LENGTH_SHORT).show();

		// Default Recevier
		if (AppWidgetManager.ACTION_APPWIDGET_ENABLED.equals(action)) {

		} else if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(action)) {			
			removePreviousAlarm();

			AppWidgetManager manager = AppWidgetManager.getInstance(context);
			initUI(context, manager, manager.getAppWidgetIds(new ComponentName(context, getClass())));

			// 알람 등록 : 현제 시간에서 지정한 시간 후에 이벤트 발생
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
			callActivity(context, CUSTOMER_MANUAL);
		} else if (Const.ACTION_SEARCH.equals(action)) {
			callActivity(context, SEARCH_AREA);
		} else if (Const.ACTION_START.equals(action)) {
			ADING = true;
			
			AppWidgetManager manager = AppWidgetManager.getInstance(context);
			initUI(context, manager, manager.getAppWidgetIds(new ComponentName(context, getClass())));
		}
	}

	/**
	 * Activity 호출 (Intent.FLAG_ACTIVITY_NEW_TASK)
	 */
	private void callActivity(Context context, int pageIdx) {
		Log.d(TAG, "callActivity()");

		Intent intent = null;

		switch (pageIdx) {
			case CUSTOMER_MANUAL:
				intent = new Intent("com.se.seadwidget.ACTION_MENU");
				break;
	
			case SEARCH_AREA:
				intent = new Intent("com.se.seadwidget.ACTION_SEARCH");
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
