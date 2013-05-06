package se.chalmers.hemmafesten.activity;

import se.chalmers.hemmafesten.R;
import se.chalmers.hemmafesten.R.id;
import se.chalmers.hemmafesten.R.layout;
import se.chalmers.hemmafesten.service.PartyService;
import se.chalmers.hemmafesten.service.PartyService.LocalBinder;
import se.chalmers.hemmafesten.service.PartyService.Status;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {


    public void clickCreateParty(View sender) {
    	createPartyService(true);
    }
    
    
	public void clickJoinParty(View sender) {
    	View joinFrame = findViewById(R.id.joinFrame);
    	int visible = joinFrame.getVisibility();
    	if(visible == 0){ //visible
    		joinFrame.setVisibility(8);
    	}else if(visible == 4){  //invisible
    		joinFrame.setVisibility(0);
    	}else{// 8 = gone
    		joinFrame.setVisibility(0);
    	}
    }
    
    
    public void clickConnect(View sender){
    	createPartyService(false);
    }
    
    
    /**
     * Start partyService, 
     * @param isCreator true=with new party. false = connected to existing party.
     */
    private void createPartyService(boolean isCreator){
    	if(PartyService.getStatus() == Status.FREE){
			Intent partyIntent = new Intent(this, PartyService.class); //                          
			partyIntent.putExtra("isCreator", isCreator);                  //
			if(!isCreator){
				partyIntent.putExtra("accessCode", "MqF3jjzsw0"); //getCodeInput()
			}
			startService(partyIntent);                                 // Starting partyService

			Intent intent = new Intent(this, PartyActivity.class);
			startActivity(intent);
			
		}else{
			Toast.makeText(MainActivity.this,
					"PartServicen är upptagen",
					Toast.LENGTH_SHORT).show();
		}
    	doBindService();
    }
    
    
    private String getCodeInput(){
		EditText et = (EditText)findViewById(R.id.accessInput);  // accessCode input
		return et.getText().toString();             			// get accessCode
	}
    

    ///////////////////////////////////////////////////////////////////////////////////////

    
    private PartyService partyService;
    private boolean psIsBound;
	
	
	private ServiceConnection mConnection = new ServiceConnection() {
	    public void onServiceConnected(ComponentName className, IBinder service) {
	        partyService = ((PartyService.LocalBinder)service).getService();
	        Toast.makeText(MainActivity.this, "testetaijföaosijrföaoijrföaoiwjregöoaijwre",
	                Toast.LENGTH_SHORT).show();
	    }

	    public void onServiceDisconnected(ComponentName className) {
	        partyService = null;
	        Toast.makeText(MainActivity.this, "test",
	                Toast.LENGTH_SHORT).show();
	    }
	};
    
    void doBindService() {
        bindService(new Intent(this, PartyService.class), mConnection, Context.BIND_AUTO_CREATE);
        psIsBound = true;
    }

    void doUnbindService() {
        if (psIsBound) {
            unbindService(mConnection);
            psIsBound = false;
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    
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
		    actionBar.setDisplayHomeAsUpEnabled(false);
	    
	    return true;
	}

 
}
