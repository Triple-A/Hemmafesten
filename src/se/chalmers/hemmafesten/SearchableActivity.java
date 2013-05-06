package se.chalmers.hemmafesten;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.parse.signpost.http.HttpResponse;

import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class SearchableActivity extends ActionBarActivity {
	
	private JSONArray array;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_searchable);
		handleIntent(getIntent());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }
	
	 private void handleIntent(Intent intent) {

	        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	            String query = intent.getStringExtra(SearchManager.QUERY);
	            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
	                    SearchSuggestionProvider.AUTHORITY, SearchSuggestionProvider.MODE);
	            suggestions.saveRecentQuery(query, null);
	            
	            String test = getWantedTracksJSON(query);
	            
	            //String test = getSearchResult(query);
	           // try {
					//JSONObject object = new JSONObject(test);
	            	
					TextView view = (TextView) findViewById(R.id.textView1);
					view.setText(test);
			//	} catch (JSONException e) {
					// TODO Auto-generated catch block
			//		e.printStackTrace();
			//	}
	            //Log.i("jsonmessage", getSearchResult(query));
	        }
	}	 
	 
	 public String getWantedTracksJSON(String input) {
		    StringBuilder jsonString = new StringBuilder();
		    HttpClient client = new DefaultHttpClient();
		    HttpGet httpGet = new HttpGet("http://ws.spotify.com/search/1/track.json?q="+input);
		    try {
		      HttpResponse response = (HttpResponse) client.execute(httpGet);
		      StatusLine statusLine = ((org.apache.http.HttpResponse) response).getStatusLine();
		      int statusCode = statusLine.getStatusCode();
		      if (statusCode == 200) {
		        HttpEntity entity = ((HttpEntityEnclosingRequestBase) response).getEntity();
		        InputStream content = entity.getContent();
		        BufferedReader reader = new BufferedReader(new InputStreamReader(content));
		        String line;
		        while ((line = reader.readLine()) != null) {
		          jsonString.append(line);
		        }
		      } else {
		        Log.e(SearchableActivity.class.toString(), "Failed to download file");
		      }
		    } catch (ClientProtocolException e) {
		      e.printStackTrace();
		    } catch (IOException e) {
		      e.printStackTrace();
		    }
		    return jsonString.toString();
		  }
	 
	 
}
