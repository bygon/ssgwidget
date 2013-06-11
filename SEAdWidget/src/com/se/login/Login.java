package com.se.login;

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

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.se.seadwidget.R;

import android.util.Log;

public class Login {
	private static String Root;
	private static String Login_CODE = "0";
	private static String MEMBER = "";
	private static HashMap MM = new HashMap();
	
	/*
	 *  public static Header SUCCESS              = new Header("100", "SUCCESS");
	    public static Header ALREADY_LOGINED      = new Header("110", "ALREADY LOGINED");
	    public static Header INVALID              = new Header("200", "INVALID");
	    public static Header FAIL                 = new Header("300", "FAIL");
	    public static Header MEMBER_NOT_FOUND     = new Header("310", "MEMBER NOT FOUND");
	    public static Header PASSWORD_DISMATCHING = new Header("311", "PASSWORD DISMATCHING");
	    public static Header PARAMETER_OMMITTED   = new Header("320", "ESSENTIAL PARAMETER OMMITTED");
	    public static Header ERROR                = new Header("900", "ERROR");
	 */
	
	public Login(String _Root){
		this.Root = _Root;
    }
	
	public static HashMap Check(String id, String pw){	
		
		try {
			URL url;			
	        url = new URL(Root+"?id=" + id + "&passwd=" + pw);
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
	            
	          //정보로 구성된 리스트를 얻어온다.
	            NodeList n2 = docEle.getElementsByTagName("member");
	            if (n2 != null && n2.getLength() > 0) {
	            	Element entry = (Element)n2.item(0);
                    Element name = (Element)entry.getElementsByTagName("name").item(0);

                    MEMBER = name.getFirstChild().getNodeValue();
	            }
	            
	            MM.put("Login_CODE", Login_CODE);
	            MM.put("MEMBER", MEMBER);
	        }
	    } catch (MalformedURLException e) {
	    	Log.e("Login", "예외발생 :"+e.getMessage());
	    } catch (IOException e) {
	    	Log.e("Login", "예외발생 :"+e.getMessage());
	    } catch (ParserConfigurationException e) {
	    	Log.e("Login", "예외발생 :"+e.getMessage());
	    } catch (SAXException e) {
	    	Log.e("Login", "예외발생 :"+e.getMessage());
	    } finally {
	    }
		
		Log.e("LOGIN_STATUS", "ID : " + id + " PW : " + pw + " CODE l " + Login_CODE);
		
		return MM;
	}
	
	
	public static HashMap Check2(String id, String pw){		
		MM.put("Login_CODE", "100");
        MM.put("MEMBER", "김진만");
        return MM;
	    
	}	
	
	public static String Check3(String id, String pw){	
		
		try {
			GetMethod method = new GetMethod(Root+"?id=" + id + "&passwd=" + pw);
	        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(2, false));
	        Log.e("idpw", id + "<>" + pw);
	        
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
	            NodeList nl = docEle.getElementsByTagName("header");
	            Log.e("11111>>>>>>>>", nl.getLength() + "");
	            
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
		
		Log.e("Login_CODE>>>>>>>>", Login_CODE);
		
		return Login_CODE;
	}

}
