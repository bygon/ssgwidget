package com.se.img;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;

import com.se.seadwidget.Const;
import com.se.seadwidget.R;

public class ImageDownloaderAsynkTask extends AsyncTask<String, Void, Bitmap>
{
	public String url;
	public String targetUrl;
	public int[] widgetIds;
	public AppWidgetManager widgetManager;
	public RemoteViews rViews;
	public Context context;

	public ImageDownloaderAsynkTask(String url, RemoteViews imageView, int[] appWidgetIds, AppWidgetManager appWidgetManager, Context context)
	{
		Log.e("ImageDownloaderAsynkTask", "url = " +url);
		this.targetUrl = url;
		this.rViews = imageView;
		this.widgetIds = appWidgetIds;
		this.widgetManager = appWidgetManager;
		this.context = context;
	}
	
	@Override
	protected Bitmap doInBackground(String... params) {	//백단에서 이미지를 가져온다
		Bitmap imgBitmap = null;
		try	{
			URL url = new URL(params[0]);
			URLConnection conn = url.openConnection();
			conn.connect();
		
			int nSize = conn.getContentLength();
			BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(), nSize);
			imgBitmap = BitmapFactory.decodeStream(bis);
			bis.close();
		}
		catch (Exception e){
			e.printStackTrace();
		}
		Log.e("ImageDownloaderAsynkTask", "imgBitmap = " +imgBitmap);
		return imgBitmap;
	}
	
	protected void onPostExecute(Bitmap bitmap){
		if(bitmap == null){
			Log.e("ImageDownloaderAsynkTask", "onPostExecute = null null null");
		}
		Log.e("ImageDownloaderAsynkTask", "onPostExecute = " +bitmap);
		
		rViews.setImageViewBitmap(R.id.addImage, bitmap);	//이제 이미지를 올려보자...
		for (int appWidgetId : widgetIds) {	//이미지를 올렸으면 위젯 갱신
			widgetManager.updateAppWidget(appWidgetId, rViews);
		}
	}
}
