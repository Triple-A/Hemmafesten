package se.chalmers.hemmafesten.service;


import se.chalmers.hemmafesten.APIKeys;
import se.chalmers.hemmafesten.model.Song;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;


public class PartyService extends Service {
	
	private static PartyController pc;
	private Boolean play;

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
	    return Service.START_NOT_STICKY;
	}
	
	public void initiateParty(Intent intent){
		

		Bundle bundle = intent.getExtras();      // get passed parameters
		
		if(bundle.getBoolean("isCreator")){  // if service started by creator
			pc = new PartyController();
			status = ((pc != null) ? Status.HOST : Status.FAILED);
		}else{                                // if service started by guest
			String accessCode = bundle.getString("accessCode");
			pc = new PartyController(accessCode);
			status = ((pc != null) ? Status.GUEST : Status.FAILED);
		}
		play = false;
		
	}
	
	public void addSong(Song song){
		pc.addSong(song);
	}
	
	public static Status getStatus(){
		return status;
	}
     
	public static PartyController getParty() {
		return pc;
	}
	
	public void killService(){
		stopLoop();
		pc.killParty(status == Status.HOST);
		pc = null;
		status = Status.FREE;
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
        public PartyService getService() {
            return PartyService.this;
        }
    }
    
    
////////////////////////////////////////spotify timer/player
    
    public Boolean getPlay(){
    	return play;
    }
    
    public void startLoop(){
    	play = true;
    	handler.post(playback);
    }
    
    public void stopLoop(){
    	play = false;
    }
    
    private Song getNext(){
    	return pc.getParty().getNext();
    }
    
    private void playSong(Song song){
    	String uri = song.getSpotifyURI();
    	Log.d("playSong", song.toString());
    	Intent launcher = new Intent( Intent.ACTION_VIEW, Uri.parse(uri));
    	startActivity(launcher);
    }

    private Handler handler = new Handler();
    private Runnable playback = new Runnable(){

    	private Long time = 0L;
    	private Song song = null;
    	  @Override
    	public void run() {
    		  
    		  if((song = getNext()) != null && play){
        		  time = Double.valueOf(song.getLength()).longValue() * 1000;
        		  handler.postDelayed(playback, time);
        		  playSong(song);
    		  }
    	}};
}

