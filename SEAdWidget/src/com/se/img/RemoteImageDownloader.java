package com.se.img;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

public class RemoteImageDownloader extends Activity
{
	private static final String URL = "http://www.nacpress.com/files/attach/images/550/895/032/090719_IU_001.jpg";
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        
        //ImageView imageView = (ImageView) findViewById(R.id.image_view);
        //ImageDownloader.download(URL, imageView);
    }
}