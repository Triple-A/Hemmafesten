package se.chalmers.hemmafesten.activity;

import java.util.zip.Inflater;

import se.chalmers.hemmafesten.R;
import se.chalmers.hemmafesten.R.id;
import se.chalmers.hemmafesten.R.layout;
import se.chalmers.hemmafesten.R.menu;
import se.chalmers.hemmafesten.service.PartyService;

import android.os.Bundle;
import android.os.IBinder;
import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

public class ActionBarActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_action_bar);
	}

	/**
	 * Handler for when a menu button is clicked
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.action_bar, menu);
	    
	    SearchManager searchManager =
	            (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	     SearchView searchView =
	             (SearchView) menu.findItem(R.id.menu_search).getActionView();
	     searchView.setSearchableInfo(
	             searchManager.getSearchableInfo(getComponentName()));

		    ActionBar actionBar = getActionBar();
		    actionBar.setDisplayHomeAsUpEnabled(true);
	    return true;
	}
	
	
	@Override
	public boolean onMenuItemSelected(int featureId,MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.home:
	        	Intent homeIntent = new Intent(this, MainActivity.class);
	        	homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
	        	startActivity(homeIntent);
	        	return true;
	        case R.id.leaveParty:
	            return true;
	        case R.id.savedParties:
	            Intent savedPartiesIntent = new Intent(this, SavedPartiesActivity.class);
	            savedPartiesIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
	            startActivity(savedPartiesIntent);
	            return true;
	        case R.id.help:
	            Intent helpIntent = new Intent(this, HelpActivity.class);
	            helpIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
	            startActivity(helpIntent);
	            return true;
	        case R.id.settings:
	            Intent settingsIntent = new Intent(this, SettingsActivity.class);
	            settingsIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
	            startActivity(settingsIntent);
	        	return true;
	        case android.R.id.home:
	            onBackPressed();
	            return true;
	        default:
	            return false;
	    }
	}
}
