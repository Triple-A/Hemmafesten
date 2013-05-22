package se.chalmers.hemmafesten.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import se.chalmers.hemmafesten.R;
import se.chalmers.hemmafesten.activity.SearchableActivity;
import se.chalmers.hemmafesten.adapter.SearchSongAdapter;
import se.chalmers.hemmafesten.item.SearchSongItem;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

public class RetreiveMusicTask extends AsyncTask<String, Void, ArrayList<SearchSongItem>> {

	
	private ListView listView;
	private String query;
	private SearchableActivity searchableActivity;
	
	public RetreiveMusicTask(ListView listView, String query, SearchableActivity searchableActivity){
		super();
		this.listView = listView;
		this.query = query;
		this.searchableActivity = searchableActivity;
	}
	
	
	
	
	/**
	 * makes an HTTP request to spotify for making a search.
	 * Saves the JSON response in String
	 */
	@Override
	protected ArrayList<SearchSongItem> doInBackground(String... params) {
		StringBuilder response  = new StringBuilder();
	     try{
	     //String temp=params[0];
	     query.replaceAll(" ", "%20");
	     URL url = new URL("http://ws.spotify.com/search/1/track.json?q="+query);
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
	         
	         if(response != null){
	        	 JSONObject object;
				try {
					object = new JSONObject(response.toString());
					JSONArray arr = object.getJSONArray("tracks");
					
					ArrayList<SearchSongItem> songz = new ArrayList<SearchSongItem>();
					
					for(int i=0; i<arr.length(); i++){
						SearchSongItem song = new SearchSongItem(arr.getJSONObject(i));
						songz.add(song);
					}
					
					return songz;
				} catch (JSONException e) {
					Log.e("RetriveMusicTask", e.getMessage());
				}
	         }
	     }
	     }catch(IOException e){
	    	 e.printStackTrace();
	     }
	     return null;
	 }
	 
	/**
	 * updatera listViewn
	 */
	 @Override
	 protected void onPostExecute(ArrayList<SearchSongItem> songz){
		 
		 SearchSongAdapter adapter = new SearchSongAdapter(searchableActivity,
	                R.layout.search_song_list_item, songz);
	     listView.setAdapter(adapter);
	 }

}
