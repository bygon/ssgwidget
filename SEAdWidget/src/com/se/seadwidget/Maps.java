package com.se.seadwidget;

import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.se.seadwidget.R;

public class Maps extends MapActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		
		MapView mapView = (MapView) findViewById(R.id.mapview);
	    mapView.setBuiltInZoomControls(true);
	    
	    List<Overlay> mapOverlays = mapView.getOverlays();
	    Drawable drawable = this.getResources().getDrawable(R.drawable.logo);
	    MapItemizedOverlay itemizedoverlay = new MapItemizedOverlay(drawable, this);
	    
	    GeoPoint point1;
	    GeoPoint point2;
	    OverlayItem overlayitem1;
	    OverlayItem overlayitem2;
	    
	    point1 = new GeoPoint(37485871,126891530);
	    point2 = new GeoPoint(35689488,139691706);
	  
	    overlayitem1 = new OverlayItem(point1, "안녕", "나 남구로야");
	    overlayitem2 = new OverlayItem(point2, "니뽄", "도쿄데스");
	    
	    itemizedoverlay.addOverlay(overlayitem1);
	    itemizedoverlay.addOverlay(overlayitem2);
	    mapOverlays.add(itemizedoverlay);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.widget_main, menu);
		return true;
	}
	
	@Override
	protected boolean isRouteDisplayed() {
	    return false;
	}

}
