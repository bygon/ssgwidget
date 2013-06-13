package com.se.img;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.view.View;
import android.widget.RemoteViews;
import com.se.seadwidget.R;

public class ImageLocalDownloader {
	public AppWidgetManager widgetManager;
	public int[] widgetIds;
	public RemoteViews rViews;
	public Context context;
	public boolean PoinPlus;
	public String Notice;
	public int imageid;
	
	public ImageLocalDownloader(int imageid, RemoteViews imageView, int[] appWidgetIds, AppWidgetManager appWidgetManager, Context context, boolean PoinPlus, String Notice){
		
		this.rViews = imageView;
		this.widgetIds = appWidgetIds;
		this.widgetManager = appWidgetManager;
		this.context = context;
		this.PoinPlus = PoinPlus;
		this.Notice = Notice;
		this.imageid = imageid;
    }
	
	public void execute(){
		
		rViews.setImageViewResource(R.id.addImage, imageid);
		
		if(PoinPlus){
			rViews.setViewVisibility(R.id.PoinPlus, View.VISIBLE);
		}else{
			rViews.setViewVisibility(R.id.PoinPlus, View.INVISIBLE);
		}
    	
		if(Notice.length() > 0){
			rViews.setViewVisibility(R.id.NoticeImg, View.VISIBLE);
			rViews.setViewVisibility(R.id.Notice, View.VISIBLE);
			rViews.setTextViewText(R.id.Notice, Notice);
		}else{
			rViews.setViewVisibility(R.id.NoticeImg, View.INVISIBLE);
			rViews.setViewVisibility(R.id.Notice, View.INVISIBLE);
			rViews.setTextViewText(R.id.Notice, "");
		}
		
		for (int appWidgetId : widgetIds) {					//이미지를 올렸으면 위젯 갱신
			widgetManager.updateAppWidget(appWidgetId, rViews);
		}
	}
	
}
