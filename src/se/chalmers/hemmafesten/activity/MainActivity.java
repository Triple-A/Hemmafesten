package se.chalmers.hemmafesten.activity;

import se.chalmers.hemmafesten.R;
import se.chalmers.hemmafesten.service.PartyService;
import se.chalmers.hemmafesten.service.PartyService.Status;
import se.chalmers.zxing.IntentIntegrator;
import se.chalmers.zxing.IntentResult;
import android.app.ActionBar;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {



    public void clickCreateParty(View sender) {
    	if(PartyService.getStatus() == Status.GUEST || PartyService.getStatus() == Status.HOST){
			Toast.makeText(MainActivity.this,
					"Kill old party before creating a new one!",
					Toast.LENGTH_SHORT).show();
		}else{
			createPartyService(true);
		}
    }
    
    public void scanQRParty(View sender) {
    	IntentIntegrator integrator = new IntentIntegrator(this);
    	integrator.initiateScan();
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	  IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
    	  if (scanResult != null) {
    	    String qr = (String) scanResult.getContents();
    	    EditText et = (EditText)findViewById(R.id.accessInput);
    	    et.setText(qr);
    	    
    	    createPartyService(false);
    	  }
    	  // else continue with any other code you need in the method
    }
    
    
	public void clickJoinParty(View sender) {
		if(PartyService.getStatus() == Status.GUEST || PartyService.getStatus() == Status.HOST){
			Toast.makeText(MainActivity.this,
					"Kill old party before connecting to a new one!",
					Toast.LENGTH_SHORT).show();
		}else{
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
    }
    
    
    public void clickConnect(View sender){
    	createPartyService(false);
    }
    
    public void clickActiveParty(View sender){
    	if(PartyService.getStatus() == Status.GUEST || PartyService.getStatus() == Status.HOST){
    		Intent intent = new Intent(this, PartyActivity.class);
			startActivity(intent);
    	}
    }
    
    public void clickKillParty(View sender){
    	if(psIsBound){
    		partyService.killService();
    		doUnbindService();
    		activePartyVisibility();
    		Toast.makeText(MainActivity.this,
					"Party: Disconnected",
					Toast.LENGTH_SHORT).show();
    		
    	}
    }
    
    
    /**
     * Start partyService, 
     * @param isCreator true=with new party. false = connected to existing party.
     */
    private void createPartyService(boolean isCreator){
		Intent partyIntent = new Intent(this, PartyService.class); //                          
		partyIntent.putExtra("isCreator", isCreator);                  //
		if(!isCreator){
			partyIntent.putExtra("accessCode", getCodeInput()); //
		}
		
		partyService.initiateParty(partyIntent);
		
		Intent intent = new Intent(this, PartyActivity.class);
		startActivity(intent);
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
	        Log.i("MainActivity", "onServiceConnected");
	    }

	    public void onServiceDisconnected(ComponentName className) {
	        partyService = null;
	        Log.i("MainActivity", "onServiceDisconnected");
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
        startService(new Intent(this, PartyService.class)); 
        doBindService();
        activePartyVisibility();
    }
    
    protected void onResume(){
    	super.onResume();
    	doBindService();
    	activePartyVisibility();
    }
    
    protected void onPause(){
    	super.onPause();
    	doUnbindService();
    }
    
    protected void onStop(){
    	super.onStop();
    	doUnbindService();
    }
    
    
    
    private void activePartyVisibility(){
    	if(PartyService.getStatus() == Status.GUEST || PartyService.getStatus() == Status.HOST){
    		findViewById(R.id.activePartyFrame).setVisibility(0);
    	}else{
    		findViewById(R.id.activePartyFrame).setVisibility(8);
    	}
    }

    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(false);
	    actionBar.setHomeButtonEnabled(false);
	    return true;
	}

	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu){
		return super.onPrepareOptionsMenu(menu);
	}

 
}