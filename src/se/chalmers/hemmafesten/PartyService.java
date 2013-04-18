package se.chalmers.hemmafesten;


import android.app.Service;
import android.content.Intent;
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

	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		Bundle bundle = intent.getExtras();      // get passed parameters
		
		if(bundle.getBoolean("isCreator")){  // if service started by creator
			pc = new PartyController();
			status = Status.HOST;
		}else{                                // if service started by guest
			String accessCode = bundle.getString("accessCode");
			pc = new PartyController(accessCode);
			status = Status.GUEST;
		}
	    return Service.START_NOT_STICKY;
	}
	
	public static Status getStatus(){
		return status;
	}
	
	public static PartyController getParty() {
		return pc;
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}



}
