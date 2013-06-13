package com.se.img;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.content.Context;
import android.util.Log;

import com.se.seadwidget.R;
import com.se.seadwidget.SEAdWidget;
import com.util.JSONParser;

public class AdBoxDownloader {
	private static List imageL = new ArrayList();	//정보 넣을 벡터
	private static JSONArray ADBOX = null;
	private static Context context = null;
	private static final String TAG_CONTACTS = "AD";
	
	public AdBoxDownloader(Context _context){
		this.context = _context;
    }
	
	public static List GetAdBox(){
		
		try {		
			// XML을 가져온다.
		    URL url;
	        url = new URL(context.getString(R.string.serverip) + context.getString(R.string.addroot));
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
	            NodeList nl = docEle.getElementsByTagName("campaign");	            
	            
	            if (nl != null && nl.getLength() > 0) {
	                for (int i = 0 ; i < nl.getLength(); i++) {
	                    Element entry = (Element)nl.item(i);
	                    Element IMG_URL = (Element)entry.getElementsByTagName("image").item(0);
	                    Element LINK_URL = (Element)entry.getElementsByTagName("url").item(0);
	                    
	                    String IMG_URLS = "";
	                    String LINK_URLS = "";
	                    
	                    
	                    if(IMG_URL.hasChildNodes()){
	                    	IMG_URLS = IMG_URL.getFirstChild().getNodeValue();
	                    }
	                    
	                    if(LINK_URL.hasChildNodes()){
	                    	LINK_URLS = LINK_URL.getFirstChild().getNodeValue();
	                    }
	                    HashMap map = new HashMap();
	    				map.put("IMG_URL", context.getString(R.string.serverip) + IMG_URLS);
	    				map.put("LINK_URL", LINK_URLS);
	    				
	    				NodeList n2 = docEle.getElementsByTagName("announce");	//걍 하나만 가져와서 광고많큼 담아버리자....
	    				if (n2 != null && n2.getLength() > 0) {
	    					Element entry2 = (Element)n2.item(0);
		                    Element NOTICE = (Element)entry2.getElementsByTagName("message").item(0);
		                    if(NOTICE.hasChildNodes()){
		                    	String NOTICES = NOTICE.getFirstChild().getNodeValue();
		                    	map.put("NOTICE", NOTICES);
		                    }
	    				}
	    						    				
	    				imageL.add(map);
	                }
	            }
	        }
	    } catch (MalformedURLException e) {
	    	 Log.e("AdBoxDownloader", "예외발생 :"+e.getMessage());
	    } catch (IOException e) {
	    	 Log.e("AdBoxDownloader", "예외발생 :"+e.getMessage());
	    } catch (ParserConfigurationException e) {
	    	 Log.e("AdBoxDownloader", "예외발생 :"+e.getMessage());
	    } catch (SAXException e) {
	    	 Log.e("AdBoxDownloader", "예외발생 :"+e.getMessage());
	    } finally {
	    }
		
		Log.e("ADBOX SIZE : ", imageL.size() + "");
		    
		return imageL;
	}
	
	public static List GetAdBox2(){
		
		try { 
			JSONParser jParser = new JSONParser();
			String page = jParser.getJSONFromTxt(context);	
			JSONObject json = new JSONObject(page);				
			
			ADBOX = json.getJSONArray(TAG_CONTACTS);
			
			for(int i = 0; i < ADBOX.length(); i++){ 
				json = ADBOX.getJSONObject(i); 

				HashMap map = new HashMap();
				map.put("LINK_URL", json.getString("LINK_URL"));
				map.put("IMG_URL", json.getString("IMG_URL"));
				map.put("NOTICE", "신세계 애드 서비스가 오픈되었습니다.");
				
				imageL.add(map);
			}         
	    
         } catch (Exception e) {
             Log.e("NewsApp", "예외발생 :"+e.getMessage());
         }		
		
		return imageL;
	}
	
	public static List GetAdBox3(){	
		
		try {
	    	GetMethod method = new GetMethod(context.getString(R.string.serverip) + context.getString(R.string.addroot));
	        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(2, false));
	        
	        HttpClient client = new HttpClient();
	        int statusCode = client.executeMethod(method);
	        
	        Log.e("statusCode", statusCode + "<>" + HttpStatus.SC_OK);
	        
	        if (statusCode == HttpStatus.SC_OK) {			        		        
	        			        
		        InputStream in = method.getResponseBodyAsStream();			            
	            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	            DocumentBuilder db = dbf.newDocumentBuilder();

	            Document dom = db.parse(in);
	            Element docEle = dom.getDocumentElement();
	            
	            //정보로 구성된 리스트를 얻어온다.
	            NodeList nl = docEle.getElementsByTagName("campaign");
	            if (nl != null && nl.getLength() > 0) {
	                for (int i = 0 ; i < nl.getLength(); i++) {
	                    Element entry = (Element)nl.item(i);
	                    Element IMG_URL = (Element)entry.getElementsByTagName("image").item(0);
	                    Element LINK_URL = (Element)entry.getElementsByTagName("url").item(0);

	                    String IMG_URLS = IMG_URL.getFirstChild().getNodeValue();
	                    String LINK_URLS = LINK_URL.getFirstChild().getNodeValue();
	                    
	                    HashMap map = new HashMap();
	    				map.put("IMG_URL", context.getString(R.string.serverip) + IMG_URLS);
	    				map.put("LINK_URL", LINK_URLS);
	    						    				
	    				imageL.add(map);                    
	                }
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
	    }		
		
		return imageL;
	}
	
	public static List GetAdBox4(){
		
		HashMap map1 = new HashMap();
		map1.put("IMG_URL", R.drawable.img01);
		map1.put("LINK_URL", "http://m.emart.com/event/eventDetail.emart?eventId=7380&emid=em_ma_06_02");
		map1.put("NOTICE", "신세계 애드위젯 서비스가 오픈되었습니다~*");
		
		HashMap map2 = new HashMap();
		map2.put("IMG_URL", R.drawable.img05);
		map2.put("LINK_URL", "http://www.shinsegae.com/culture/academylecture/lecture.jsp");
		map2.put("NOTICE", "신세계 애드위젯 서비스가 오픈되었습니다~*");
		
		HashMap map3 = new HashMap();
		map3.put("IMG_URL", R.drawable.img02);
		map3.put("LINK_URL", "http://m.emart.com/event/eventDetail.emart?eventId=8262&emid=em_ec_00_02");
		map3.put("NOTICE", "신세계 애드위젯 서비스가 오픈되었습니다~*");	
		
		HashMap map4 = new HashMap();
		map4.put("IMG_URL", R.drawable.img03);
		map4.put("LINK_URL", "http://m.emart.com/event/eventDetail.emart?eventId=8262&emid=em_ec_00_02");
		map4.put("NOTICE", "신세계 애드위젯 서비스가 오픈되었습니다~*");
		
		HashMap map5 = new HashMap();
		map5.put("IMG_URL", R.drawable.img04);
		map5.put("LINK_URL", "http://m.emart.com/event/eventDetail.emart?eventId=8262&emid=em_ec_00_02");
		map5.put("NOTICE", "신세계 애드위젯 서비스가 오픈되었습니다~*");
		
		imageL.add(map1);
		imageL.add(map2);
		imageL.add(map3);
		imageL.add(map4);
		imageL.add(map5);
		
		Log.e("ADBOX SIZE : ", imageL.size() + "");
		    
		return imageL;
	}
}
