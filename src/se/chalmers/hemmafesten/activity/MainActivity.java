package se.chalmers.hemmafesten.activity;

import se.chalmers.hemmafesten.R;

import se.chalmers.hemmafesten.service.PartyService;
import se.chalmers.hemmafesten.service.PartyService.Status;
import se.chalmers.zxing.IntentIntegrator;
import se.chalmers.zxing.IntentResult;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	
	private View joinFrame;
	private View activePartyFrame;

	/**
	 * create a new party
	 * @param sender
	 */
    public void clickCreateParty(View sender) {
    	if(PartyService.getStatus() == Status.GUEST || PartyService.getStatus() == Status.HOST){
			Toast.makeText(MainActivity.this,
					"Kill old party before creating a new one!",
					Toast.LENGTH_SHORT).show();
		}else{
	      	createPartyService(true);
		}
    }
    
    /**
     * open the qr scanner
     * @param sender
     */
    public void scanQRParty(View sender) {
    	IntentIntegrator integrator = new IntentIntegrator(this);
    	integrator.initiateScan();
    }
    
    /**
     * handle the result from qr scanner
     */
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
    
    
    /**
     * open the Join Party frame
     * @param sender
     */
	public void clickJoinParty(View sender) {
		if(PartyService.getStatus() == Status.GUEST || PartyService.getStatus() == Status.HOST){
			Toast.makeText(MainActivity.this,
					"Kill old party before connecting to a new one!",
					Toast.LENGTH_SHORT).show();
		}else{
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
    
    
	/**
	 * connect to existing party
	 * @param sender
	 */
    public void clickConnect(View sender){
    	//WwD4JWNrdi
        createPartyService(false);
    }
    
    
    /**
     * open party you are already connected to
     * @param sender
     */
    public void clickActiveParty(View sender){
    	if(PartyService.getStatus() == Status.GUEST || PartyService.getStatus() == Status.HOST){
    		Intent intent = new Intent(this, PartyActivity.class);
			startActivity(intent);
    	}
    }
    
    /**
     * disconnect from the party you are connected to
     * @param sender
     */
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
		final Intent partyIntent = new Intent(this, PartyService.class); //                          
		partyIntent.putExtra("isCreator", isCreator);                  //
		
		if(!isCreator){
			partyIntent.putExtra("accessCode", getCodeInput()); //
			EditText et = (EditText)findViewById(R.id.accessInput);
    	    et.setText("");
		}
		final ProgressDialog loader = ProgressDialog.show(this, "", "Connecting to party, please wait...", true);
        new Thread(new Runnable(){
            public void run() {
            	partyService.initiateParty(partyIntent);
        		Intent intent = new Intent(MainActivity.this, PartyActivity.class);
        		startActivity(intent);
              if(loader!=null){
                loader.dismiss();}}
        }).start();
    }
    
    public void reJoinParty(boolean isCreator, String accessCode){
    	final Intent partyIntent = new Intent(this, PartyService.class); //                          
		partyIntent.putExtra("isCreator", isCreator);                  //
		
		if(!isCreator){
			partyIntent.putExtra("accessCode", accessCode); //
			EditText et = (EditText)findViewById(R.id.accessInput);
    	    et.setText("");
		}
		final ProgressDialog loader = ProgressDialog.show(this, "", "Connecting to party, please wait...", true);
        new Thread(new Runnable(){
            public void run() {
            	partyService.initiateParty(partyIntent);
        		Intent intent = new Intent(MainActivity.this, PartyActivity.class);
        		startActivity(intent);
              if(loader!=null){
                loader.dismiss();}}
        }).start();
    }
  
    
    /**
     * get the inputed accesscode
     * @return accesscode as string
     */
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
    
    @SuppressLint("ResourceAsColor")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Log.d("skit", "skit");
        startService(new Intent(this, PartyService.class)); 
        doBindService();
        
        joinFrame = findViewById(R.id.joinFrame);
        activePartyFrame = findViewById(R.id.activePartyFrame);
        
        activePartyVisibility();
        
        EditText et = (EditText) findViewById(R.id.accessInput);
        final Button joinButton = (Button) findViewById(R.id.connect);
        joinButton.setBackgroundColor(Color.GRAY);
        joinButton.setEnabled(false);
        et.addTextChangedListener(new TextWatcher(){
			 
	        @Override
	        public void afterTextChanged(Editable s) {
	        	if(s.length()!=10){
	                joinButton.setBackgroundColor(Color.GRAY);
	        		joinButton.setEnabled(false);
	        	}
	        }
	        @Override
	        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
	        @Override
	        public void onTextChanged(CharSequence s, int start, int before,int count) {
	        	if(s.length()==10){
	        		int spGreen = getApplicationContext().getResources().getColor(R.color.sp_green);
	        		joinButton.setBackgroundColor(spGreen);
	        		joinButton.setEnabled(true);
	        	}else{
	                joinButton.setBackgroundColor(Color.GRAY);
	        		joinButton.setEnabled(false);
	        	}
	        }
	    });
    }
    
    protected void onResume(){
    	super.onResume();
    	doBindService();
    	joinFrame.setVisibility(View.GONE);
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
    		activePartyFrame.setVisibility(View.VISIBLE);
    	}else{
    		activePartyFrame.setVisibility(View.GONE);
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