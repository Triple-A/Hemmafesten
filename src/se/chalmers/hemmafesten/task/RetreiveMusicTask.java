package se.chalmers.hemmafesten.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;

public class RetreiveMusicTask extends AsyncTask<String, Void, String> {

	/**
	 * makes an HTTP request to spotify for making a search.
	 * Saves the JSON response in String
	 */
	@Override
	protected String doInBackground(String... params) {
		StringBuilder response  = new StringBuilder();
	     try{
	     String temp=params[0];
	     String result = temp.replaceAll(" ", "%20");
	     URL url = new URL("http://ws.spotify.com/search/1/track.json?q="+result);
	     HttpURLConnection httpconn = (HttpURLConnection)url.openConnection();
	     if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK)
	     {
	         BufferedReader input = new BufferedReader(new InputStreamReader(httpconn.getInputStream(), "UTF-8"),8192);
	         String strLine = null;
	         while ((strLine = input.readLine()) != null)
	         {
	        	 response.append(strLine);		        	 
	         }
	         input.close();
	         return response.toString();
	         
	     }
	     }catch(IOException e){
	    	 e.printStackTrace();
	     }
	     return null;
	 }
	 
	/**
	 * nothing yao
	 */
	 @Override
	 protected void onPostExecute(String result){
	 }

}
