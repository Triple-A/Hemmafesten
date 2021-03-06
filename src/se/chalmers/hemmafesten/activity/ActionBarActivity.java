package se.chalmers.hemmafesten.activity;

import se.chalmers.hemmafesten.R;
import se.chalmers.hemmafesten.service.PartyService;
import se.chalmers.hemmafesten.service.PartyService.Status;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
/**
 * Activity for the actionbar which is extended in 
 * most of the activities
 * @author AnteKioksy
 *
 */
public class ActionBarActivity extends Activity {
	
	
	/**
	 * creates an instnace of an actionbar activity and sets
	 * the content view to the actionbar activity
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_action_bar);
	}

	/**
	 * sets up menu settings for the action bar menu
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
	    return super.onCreateOptionsMenu(menu);
	}
	
	/**
	 * Menu settings when returning to an actionbar activity
	 */
	@Override 
	public boolean onPrepareOptionsMenu(Menu menu){
		if(PartyService.getStatus() == Status.GUEST || PartyService.getStatus() == Status.HOST){
	    	menu.findItem(R.id.menu_search).setVisible(true);
	    	menu.findItem(R.id.leaveParty).setVisible(true);
	    	}else{
	    	menu.findItem(R.id.menu_search).setVisible(false);
	    	menu.findItem(R.id.leaveParty).setVisible(false);
			}
		return super.onPrepareOptionsMenu(menu);
	}
	
	/**
	 * When menu item is selected
	 */
	@Override
	public boolean onMenuItemSelected(int featureId,MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.home:
	        	Intent homeIntent = new Intent(this, MainActivity.class);
	        	homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
	        	startActivity(homeIntent);
	        	return true;
	        case R.id.leaveParty:
	        	PartyService.getPartyController().killParty();
	        	Intent backToHomeIntent = new Intent(this, MainActivity.class);
	        	backToHomeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
	        	startActivity(backToHomeIntent);
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
