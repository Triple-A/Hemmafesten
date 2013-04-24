package se.chalmers.hemmafesten;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.parse.Parse;


public class PartyService extends Service {
	
	private static PartyController pc;
	private static Status status = Status.FREE;
	public enum Status {
	    FREE,
	    HOST,
	    GUEST,
	    FAILED
	}

	private final IBinder mBinder = new LocalBinder();


	
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
		pc = null;
		status = Status.FREE;
		stopSelf();
	}
	
	
	@Override
	public void onCreate() {
	        super.onCreate();
	        Parse.initialize(this, APIKeys.ParseApplicationID(), APIKeys.ParseClientKey());
	        Log.i("PartyService", "Service Started.");
	}

	public IBinder onBind(Intent arg0) {
		return mBinder;
	}

    public class LocalBinder extends Binder {
        PartyService getService() {
            return PartyService.this;
        }
    }

}

