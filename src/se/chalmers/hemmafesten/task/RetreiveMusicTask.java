package se.chalmers.hemmafesten.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import android.os.AsyncTask;

public class RetreiveMusicTask extends AsyncTask<String, Void, String> {

	@Override
	protected String doInBackground(String... params) {
		StringBuilder response  = new StringBuilder();
	     try{
	     URL url = new URL("http://ws.spotify.com/search/1/track.json?q="+params);
	     HttpURLConnection httpconn = (HttpURLConnection)url.openConnection();
	     if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK)
	     {
	         BufferedReader input = new BufferedReader(new InputStreamReader(httpconn.getInputStream()),8192);
	         String strLine = null;
	         while ((strLine = input.readLine()) != null)
	         {
	        	 response.append(strLine);		        	 
	         }
	         input.close();
	         
	     }
	     }catch(IOException e){
	    	 
	     }
	     return response.toString();
	 }
	 
	 @Override
	 protected void onPostExecute(String result){
		super.onPostExecute(result);
	 }

}
