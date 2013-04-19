package se.chalmers.hemmafesten;


import com.parse.Parse;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
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
			status = Status.HOST;
		}else{                                // if service started by guest
			String accessCode = bundle.getString("accessCode");
			pc = new PartyController(accessCode);
			status = Status.GUEST;
		}
		
		if(pc == null) status = Status.FAILED;
		else{
			addSong("testar " + status + "la till en låt");
		}

	}
	
	public void addSong(String spotifyURI){
		pc.addSong(spotifyURI);
	}
	
	public static Status getStatus(){
		return status;
	}
	
	public static PartyController getParty() {
		Log.d("debugg","steg ett" + pc);
		return pc;
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}



}
