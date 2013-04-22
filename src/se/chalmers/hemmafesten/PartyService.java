package se.chalmers.hemmafesten;


import com.parse.Parse;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;


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
			Parse.initialize(this, APIKeys.ParseApplicationID(), APIKeys.ParseClientKey());
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
	
	@Override
	public IBinder onBind(Intent arg0) {
		return mBinder;
	}

    public class LocalBinder extends Binder {
        PartyService getService() {
            return PartyService.this;
        }
    }



}
