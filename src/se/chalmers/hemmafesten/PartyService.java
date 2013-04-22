package se.chalmers.hemmafesten;


import java.util.ArrayList;

import com.parse.Parse;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;


public class PartyService extends Service {
	
	private static PartyController pc;
	private static Status status = Status.FREE;
	public enum Status {
	    FREE,
	    HOST,
	    GUEST,
	    FAILED
	}
	static final int MSG_REGISTER_CLIENT = 1;
    static final int MSG_UNREGISTER_CLIENT = 2;
	
	private ArrayList<Messenger> mClients = new ArrayList<Messenger>(); // Keeps track of all current registered clients.
	final Messenger mMessenger = new Messenger(new IncomingHandler()); // Target we publish for clients to send messages to IncomingHandler.
	/** For showing and hiding our notification. */
    private NotificationManager mNM;

	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		if(status == Status.FREE){
			initiateParty(intent);
		}
	    return Service.START_NOT_STICKY;
	}
	
	private void initiateParty(Intent intent){
		
		Bundle bundle = intent.getExtras();      // get passed parameters
		
		if(bundle.getBoolean("isCreator")){  // if service started by creator
			pc = new PartyController();
			status = ((pc != null) ? Status.HOST : Status.FAILED);
		}else{                                // if service started by guest
			String accessCode = bundle.getString("accessCode");
			pc = new PartyController(accessCode);
			status = ((pc != null) ? Status.GUEST : Status.FAILED);
		}
		sendMessageToUI();
	}
	
	private void sendMessageToUI() {
		for (int i=0; i < mClients.size(); i++) {
            try {
                mClients.get(i).send(null);
            } catch (RemoteException e) {
                mClients.remove(i);
            }
        }
    }
	
	class IncomingHandler extends Handler { // Handler of incoming messages from clients.
        @Override
        public void handleMessage(Message msg) {
        	switch (msg.what) {
            case MSG_REGISTER_CLIENT:
                mClients.add(msg.replyTo);
                break;
            case MSG_UNREGISTER_CLIENT:
                mClients.remove(msg.replyTo);
                break;
            
        	}
        }
	}
	
	
	public void addSong(String spotifyURI){
		pc.addSong(spotifyURI);
	}
	
	public static Status getStatus(){
		return status;
	}
	
	public static PartyController getParty() {
		return pc;
	}
	
	public void killService(){
		pc.killParty(status == Status.HOST);
		status = Status.FREE;
		stopSelf();
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return mMessenger.getBinder();
	}
	
	@Override
	public void onCreate() {
	        super.onCreate();
	        Parse.initialize(this, APIKeys.ParseApplicationID(), APIKeys.ParseClientKey());
	        Log.i("PartyService", "Service Started.");
	}
}

