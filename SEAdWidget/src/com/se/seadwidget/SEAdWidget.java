package com.se.seadwidget;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.se.img.AdBoxDownloader;
import com.se.img.ImageDownloaderAsynkTask;

public class SEAdWidget extends AppWidgetProvider {

	private static AlarmManager alarmManager; 	//주기적으로 이미지 변경하기 위해 타이머 둬서 호출한다.
	private static PendingIntent mSender;

	private static final String TAG = "SEAdWidget";
	private static Context context;
	private static int idx = 0;
	private static int Pidx = 0;	
	private static Boolean isOpen = false;

	private static final int CUSTOMER_MENUAL  = 1;	//매뉴얼
	private static final int CUSTOMER_ACCOUNT = 2;	//계정등록
	private static final int CUSTOMER_POINT   = 3;	//내포인트	
	private static final int CUSTOMER_LINK    = 4;	//링크
	
	private static boolean ADING = false;			//광고 시작 변수
	private static List imageL = new ArrayList();	//정보 넣을 벡터
	private static HashMap imageMap = new HashMap();//정보 넣을 해쉬맵	
	private static String IMG_URL = "";					//현재이미지
	private static String LINK_URL = "";				//현재광고
	private static String NOTICE = "";					//공지사항
	
	private static final int WIDGET_UPDATE_INTERVAL = 3000; // 7초마다 갱신	
	private static int rid = R.layout.widget_main;	//위젯 메인 레이아웃
	
	private static Calendar now;
	private static String Year = "";
	private static String Month = "";
	private static String Day = "";
	private static boolean PoinPlus = false;
	private static SharedPreferences pref = null;
	private static RemoteViews views;
			
	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		//Toast.makeText(context, "onUpdate > " + imageL.size() + " MENU " + isOpen, Toast.LENGTH_SHORT).show();	
		Log.e("onUpdate", "onUpdate");
		
		pref = context.getSharedPreferences("ADWIDGET", 0); 	//계정등록이 되었는지 확인하자.
		
		if(pref != null){
			ADING = pref.getBoolean("ADING", false);
		}
		
		if(!ADING){
			SetPoint("0");	//로그아웃할 경우 포인트 초기화
		}
		
		now =  Calendar.getInstance();		
		if(now.get(Calendar.HOUR_OF_DAY) == 0 && ADING){	//24시에 전송
			SetPoint("3");
		}
		
		//서버에서 광고정보를 가져오자..  로긴 안했어도 일단 가져온다. 뿌리는건 로긴 후
		if(imageL.size() <= 0 || imageL == null ){	//광고를 다보았다면 서버에서 다시 내려받자
			DownLoadUrlAsynkTask urlData = new DownLoadUrlAsynkTask();
			urlData.execute("MANI");
		}

		this.context = context;
		super.onUpdate(context, appWidgetManager, appWidgetIds);	
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
		imageL.clear();		
	}

	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
	}

	/**
	 * UI 설정 이벤트 설정
	 */
	public void initUI(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		Log.e("initUI", "initUI");
		
		if(isOpen){	//메뉴 레이아웃 분개
			rid = R.layout.widget_main_r;
		}else{
			rid = R.layout.widget_main;
		}
		RemoteViews views;
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
			
			if(imageL.size() > 0){
				HashMap m = new HashMap();	//가져온광고에서 첫번째꺼를 빼오고 지운다.... 다지우면 다시 가져오기
				m = (HashMap)imageL.get(0);
				IMG_URL = m.get("IMG_URL").toString();
				LINK_URL = m.get("LINK_URL").toString();
				
				if(m.get("NOTICE") != null){
					NOTICE = m.get("NOTICE").toString();
				}
				
				imageL.remove(0);	//맨첫번째꺼를 계속 빼먹는다.
				
//				if(imageL.size() == 0){
//									    
//				    SetPoint("1");	// 포인트 적립				    
//					PoinPlus = true;
//				}else{
//					PoinPlus = false;
//				}
				Pidx++;
				
				if(Pidx%2 == 0){
					PoinPlus = true;
					SetPoint("1");	// 포인트 적립
				}else{
					PoinPlus = false;
				}				
				
				//광고이미지를 비동기로 가져온다 		
				ImageDownloaderAsynkTask imageDownTask = new ImageDownloaderAsynkTask(IMG_URL, views,appWidgetIds, appWidgetManager, this.context, PoinPlus, NOTICE);
				imageDownTask.execute(IMG_URL);
			}		
			
		}else{
			idx = 0;	//로그아웃시 광고 클리어
			LINK_URL = "";
			IMG_URL = "";
			
			views.setImageViewResource(R.id.addImage, R.drawable.sinc5_ui_wid_regbtn);
			views.setImageViewResource(R.id.addImage, R.drawable.sinc5_ui_wid_regbtn);
			views.setViewVisibility(R.id.NoticeImg, View.INVISIBLE);
			views.setViewVisibility(R.id.Notice, View.INVISIBLE);
			views.setViewVisibility(R.id.PoinPlus, View.INVISIBLE);			
			views.setTextViewText(R.id.Notice, "");
			
			for (int appWidgetId : appWidgetIds) {
				appWidgetManager.updateAppWidget(appWidgetId, views);
			}
		}
	}
	
	/**
	 * 메뉴 설정
	 */
	public void initMenu(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {	//메뉴만 누를때 레이아웃만 변경하기
		
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
		
		if(ADING){	//계정등록 한다면.... 광고 넣어준다.
			
			//현재 셋팅된 광고이미지를 비동기로 가져온다 		
			ImageDownloaderAsynkTask imageDownTask = new ImageDownloaderAsynkTask(IMG_URL, views, appWidgetIds, appWidgetManager, this.context, PoinPlus, NOTICE);
			imageDownTask.execute(IMG_URL);
			
		}else{
			
			views.setImageViewResource(R.id.addImage, R.drawable.sinc5_ui_wid_regbtn);
			views.setViewVisibility(R.id.NoticeImg, View.INVISIBLE);
			views.setViewVisibility(R.id.Notice, View.INVISIBLE);
			views.setViewVisibility(R.id.PoinPlus, View.INVISIBLE);
			views.setTextViewText(R.id.Notice, "");
			
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
			if(LINK_URL.length() > 0){
				callActivity(context, CUSTOMER_LINK);
			}else{
				callActivity(context, CUSTOMER_ACCOUNT);
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
				intent.putExtra("src", "sinc5_ui_abar_title_new1");
				break;
				
			case CUSTOMER_ACCOUNT:
				intent = new Intent("com.se.seadwidget.ACTION_ACCOUNT");				
				intent.putExtra("src", "sinc5_ui_02_abar1");
				break;
				
			case CUSTOMER_POINT:
				intent = new Intent("com.se.seadwidget.ACTION_POINT");				
				intent.putExtra("src", "sinc5_ui_02_abar2");
				break;
				
			case CUSTOMER_LINK:
				intent = new Intent(Intent.ACTION_VIEW);
				intent.putExtra("src", "sinc5_ui_02_abar4");
			    Uri u = Uri.parse(LINK_URL);	//광고클릭 시 해당 광고 사이트 호출
			    intent.setData(u);
				break;
				
			default:
				break;
		}
		
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
	
	public void SetPoint(String ptype){
		
		if(pref != null){
		
			SharedPreferences.Editor editor = pref.edit();
			
			if(ptype.equals("0")){	//로그아웃
				editor.putInt("POINT", 0);
				editor.putInt("TOTPOINT", 0);
			    editor.commit();			
				
			}else if(ptype.equals("1")){	//당일포인트
				int point = pref.getInt("POINT", 0);			
			    editor.putInt("POINT", ++point);
			    editor.commit();
			    
			}else if(ptype.equals("2")){	//24시 누적포인트
				
				now =  Calendar.getInstance();
				Year = Integer.toString(now.get(Calendar.YEAR));		
				Month = Integer.toString(now.get(Calendar.MONTH));
				Day = Integer.toString(now.get(Calendar.DATE));
				
				if(pref.getString("PUPDAY", (Year + Month + Day)) != (Year + Month + Day)){	//24시에 전송
					int point = pref.getInt("POINT", 0);
					int totpoint = pref.getInt("TOTPOINT", 0);
					
				    editor.putInt("POINT", 0);	//당일포인트는 초기화
				    editor.putInt("TOTPOINT", point + totpoint);
				    editor.putString("PUPDAY", (Year + Month + Day));
				    editor.commit();
					
				}
			}
		}
	}
	
	public void removePreviousAlarm() {
		if (alarmManager != null && mSender != null) {
			mSender.cancel();
			alarmManager.cancel(mSender);
		}
	}
		
	public class DownLoadUrlAsynkTask extends AsyncTask<String, Void, List<HashMap<String, String>>>{		

		  @Override
		 protected List<HashMap<String, String>> doInBackground(String... params) {		  
			    			  
			 try {
				  AdBoxDownloader adbox = new AdBoxDownloader(context);
				  imageL = adbox.GetAdBox();
		    
	         } catch (Exception e) {
	             Log.e("NewsApp", "예외발생 :"+e.getMessage());
	         }
			    
			 return null;
		  }

		  @Override
		  protected void onPostExecute(List<HashMap<String, String>> result) {
			  super.onPostExecute(result);
			  //Toast.makeText(context, "onPostExecute", Toast.LENGTH_SHORT).show();
			  //여기서 서버로 포인트를 보내볼까
		 } 
	}
	
}
