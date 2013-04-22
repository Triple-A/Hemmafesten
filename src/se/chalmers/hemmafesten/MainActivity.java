package se.chalmers.hemmafesten;

import se.chalmers.hemmafesten.PartyService.Status;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	
    
    public void createParty(View sender) {
    	createPartyService(true);
    }
    
    
    public void joinParty(View sender) {
    	View joinFrame = findViewById(R.id.joinFrame);
    	
    	int visible = joinFrame.getVisibility();
    	
    	if(visible == 0){ //visible
    		joinFrame.setVisibility(8);
    	}else if(visible == 4){ // invisble
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
			if(!isCreator) partyIntent.putExtra("accessCode", getCodeInput());
			startService(partyIntent);                                 // Starting partyService
			
			Intent intent = new Intent(this, PartyActivity.class);
			startActivity(intent);
			
		}else{
				//PartyService busy
		}
    }
    
    private String getCodeInput(){
		EditText et = (EditText)findViewById(R.id.accessInput);  // accessCode input
		return et.getText().toString();             			// get accessCode
	}
    
    
    ///////////////////////////////////////////////////////////////////////////////////////
    
    private PartyService boundService;
    private boolean mIsBound;
	
	
	private ServiceConnection mConnection = new ServiceConnection() {
	    public void onServiceConnected(ComponentName className, IBinder service) {
	        boundService = ((PartyService.LocalBinder)service).getService();
	        Toast.makeText(MainActivity.this, "testetaijföaosijrföaoijrföaoiwjregöoaijwre",
	                Toast.LENGTH_SHORT).show();
	    }

	    public void onServiceDisconnected(ComponentName className) {
	        boundService = null;
	        Toast.makeText(MainActivity.this, "test",
	                Toast.LENGTH_SHORT).show();
	    }
	};
    
    void doBindService() {
        bindService(new Intent(this, PartyService.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService() {
        if (mIsBound) {
            unbindService(mConnection);
            mIsBound = false;
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
      /*  View activePartyButton = findViewById(R.id.active_party_button);
        if(PartyService.getStatus() == Status.GUEST || PartyService.getStatus() == Status.HOST){
        	activePartyButton.setVisibility(0);
        }else{
        	activePartyButton.setVisibility(8);
        }*/
        
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
