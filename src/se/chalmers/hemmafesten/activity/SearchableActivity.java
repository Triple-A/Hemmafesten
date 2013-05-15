package se.chalmers.hemmafesten.activity;


import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import se.chalmers.hemmafesten.R;
import se.chalmers.hemmafesten.SongAdapter;
import se.chalmers.hemmafesten.SongItem;
import se.chalmers.hemmafesten.task.RetreiveMusicTask;


import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Intent;
import android.view.Menu;
import android.widget.ListView;



public class SearchableActivity extends ActionBarActivity {
		
	
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
	
	/**
	 * Gets search query which is typed and performs a search to the spotify
	 * metadata API. presents the result in a ListView
	 * @param intent
	 */
	 private void handleIntent(Intent intent) {

	        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	            String query = intent.getStringExtra(SearchManager.QUERY);
	            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
	                  SearchSuggestionProvider.AUTHORITY, SearchSuggestionProvider.MODE);
	            suggestions.saveRecentQuery(query, null);   
	            
	            ActionBar bar = getActionBar();
	            bar.setTitle("Result: " +  query);
	           
	            	RetreiveMusicTask task = new RetreiveMusicTask();
					String result;
					try {
						result = task.execute(query).get();
						JSONObject object = new JSONObject(result);
						JSONArray arr = object.getJSONArray("tracks");
												
						ArrayList<SongItem> songz = new ArrayList<SongItem>();
						
						for(int i=0; i<arr.length(); i++){
							SongItem song = new SongItem(arr.getJSONObject(i));
							songz.add(song);
						}

			
					
					ListView  listView = (ListView) findViewById(R.id.results_list);
				    SongAdapter adapter = new SongAdapter(this,
				                R.layout.song_list_item, songz);
				     listView.setAdapter(adapter);
						
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
	        }	 
	 }
}
