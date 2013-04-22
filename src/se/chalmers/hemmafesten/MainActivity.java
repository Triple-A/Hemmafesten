package se.chalmers.hemmafesten;

import se.chalmers.hemmafesten.PartyService.Status;

import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

    
    private boolean pcIsBound;
    Messenger mService = null;
    final Messenger mMessenger = new Messenger(new IncomingHandler());

	public void clickCreateParty(View sender) {
    	createPartyService(true);
    }
    
    
	public void clickJoinParty(View sender) {
    	View joinFrame = findViewById(R.id.joinFrame);
    	if(joinFrame.getVisibility() == 0){ //visible
    		joinFrame.setVisibility(8);
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
    	}else{
				//PartyService busy
		}
    	doBindService();
    }
    
    
    void doBindService() {
        bindService(new Intent(this, PartyService.class), mConnection, Context.BIND_AUTO_CREATE);
        pcIsBound = true;
    }
    void doUnbindService() {
        if (pcIsBound) {
            // If we have received the service, and hence registered with it, then now is the time to unregister.
            if (mService != null) {
                try {
                    Message msg = Message.obtain(null, PartyService.MSG_UNREGISTER_CLIENT);
                    msg.replyTo = mMessenger;
                    mService.send(msg);
                } catch (RemoteException e) {
                    // There is nothing special we need to do if the service has crashed.
                }
            }
            // Detach our existing connection.
            unbindService(mConnection);
            pcIsBound = false;
        }
    }
    
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mService = new Messenger(service);
            try {
                Message msg = Message.obtain(null, PartyService.MSG_REGISTER_CLIENT);
                msg.replyTo = mMessenger;
                mService.send(msg);
            } catch (RemoteException e) {
                Log.e("MainActivity", "onServiceConnecte: " + e.getMessage());
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been unexpectedly disconnected - process crashed.
            mService = null;
        }
    };
    
    private String getCodeInput(){
		EditText et = (EditText)findViewById(R.id.accessInput);  // accessCode input
		return et.getText().toString();             			// get accessCode
	}
    
    
    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
        	switch(PartyService.getStatus()){
        		case FREE:
        			break;
        		case HOST:
        		case GUEST:
        			Intent intent = new Intent(null, PartyActivity.class);
        			startActivity(intent);
        			break;
        		case FAILED:
        			break;
        	}
        }
    }
    
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
