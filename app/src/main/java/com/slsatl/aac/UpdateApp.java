package com.slsatl.aac;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Build;
import android.os.Environment;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;


public class UpdateApp {
	
	String url_version = "http://www.sls-atl.com/?q=aac%2Fwebservice&action=latest&client_version=";
	String url_app = "";
	String app_path = Environment.getExternalStorageDirectory() + "/download/";  
	String eng_reply = "";
	String th_reply = "";
	
	Context mContext;
	
	public UpdateApp(Context context){
		this.mContext = context;
	}
	
	public String checkVersion(){
		
		// check current version of installed software
		
		int strVersionCode = 0;
		String platformVersion = "";
		String os = "android";
		PackageInfo packageInfo;
		try {
			packageInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(),0);
			strVersionCode = packageInfo.versionCode;
			platformVersion  = Build.VERSION.RELEASE;
			
		} catch (NameNotFoundException e1) {
			Log.i("info","error version");
			e1.printStackTrace();
		}
				
		
		// check for the newest software
		url_version = url_version + "strVersionCode";
		url_version = url_version + "&platform=android&platform_version" + platformVersion;
		
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(url_version);
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String response_str = "";
        boolean cannot_connect = false;
        try {
			response_str = client.execute(request, responseHandler);
		} catch (Exception e) {
			cannot_connect = true;
			e.printStackTrace();
		}
		if (cannot_connect == false){
			response_str = "["+ response_str + "]";
			JSONArray data;
			try {
				data = new JSONArray(response_str);			
				JSONObject c = data.getJSONObject(0);
			
				String status = c.getString("status");
				String message = c.getString("message");
				// line below is to be used when server is ok.
				//url_app = c.getString("url");
				message = "[" + message + "]";
				data = new JSONArray(message);
				c = data.getJSONObject(0);
				eng_reply = c.getString("en_US");
				th_reply = c.getString("th_TH");
				return status;
			}
			catch(Exception e){
				e.printStackTrace();
				Log.i("info","error jason");
			}
			return "no update";
		}
		else return "netError";
	}
	
	public String getThaiReply(){
		return th_reply;
	}
	
	public String getEngReply(){
		return eng_reply;
	}
	
	public String downloadApp(){
		
		String app_name = "ChulaAAC";
		
		//the below line is to be removed when the server operates properly.
		url_app = "http://www.whatseat.net/HelloWorld.apk";
		
		try{
			
	        URL url = new URL(url_app);
	        HttpURLConnection c = (HttpURLConnection) url.openConnection();
	        c.setRequestMethod("GET");
	        c.setDoOutput(true);
	        c.connect();
	        File file = new File(app_path);
	        file.mkdirs();
	        File outputFile = new File(file, app_name);
	        FileOutputStream fos = new FileOutputStream(outputFile);
	        InputStream is = c.getInputStream();
	        byte[] buffer = new byte[1024];
	        int len1 = 0;
	        while ((len1 = is.read(buffer)) != -1) {
	            fos.write(buffer, 0, len1);
	        }
	        fos.close();
	        is.close();
		}
		catch(Exception e){
			Log.i("info",e.getMessage());
			return "error";
		}
	        return "finish";
	}
}
