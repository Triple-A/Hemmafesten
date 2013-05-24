package se.chalmers.hemmafesten.activity;


import se.chalmers.hemmafesten.R;
import se.chalmers.hemmafesten.task.RetreiveMusicTask;
import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
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
	            
	            ListView  listView = (ListView) findViewById(R.id.results_list);
	           
	            new RetreiveMusicTask(listView, query, this).execute();
					
	        }	 
	 }
}
