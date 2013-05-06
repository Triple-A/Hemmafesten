package se.chalmers.hemmafesten.activity;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import se.chalmers.hemmafesten.R;
import se.chalmers.hemmafesten.task.RetreiveMusicTask;


import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


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
												
						ArrayList<String> songz = new ArrayList<String>();
						
						for(int i=0; i<arr.length(); i++){
							songz.add(arr.getJSONObject(i).getString("name"));
						}
						
						
				    //Present data in list view
					ListView resultsListView = (ListView) findViewById(R.id.results_list);
					        resultsListView.setAdapter(new ArrayAdapter<String>(this,
					                android.R.layout.simple_list_item_1, 
					                songz));
						
						
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
