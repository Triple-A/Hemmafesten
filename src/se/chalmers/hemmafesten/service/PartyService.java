package se.chalmers.hemmafesten.service;


import se.chalmers.hemmafesten.APIKeys;
import se.chalmers.hemmafesten.model.Song;
import se.chalmers.hemmafesten.model.SongList;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

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
	private Bitmap qrCode = null;
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
    
    
    private void startLoop(){
    	handler.post(playback);
    }
    
    private void stopLoop(){
    	handler.removeCallbacks(playback);    /// doesnt work!!!!!
    }
    
    private Song getNext(){
    	//TODO add code for retreiving the next song
    	return new Song();
    }
    
    private void playSong(Song song){
    	String uri = song.getSpotifyURI();
    	Log.d("playSong", song.toString());
    	Intent launcher = new Intent( Intent.ACTION_VIEW, Uri.parse(uri));
    	startActivity(launcher);
    }

    private Handler handler = new Handler();
    private Runnable playback = new Runnable(){

    	private Long time = 2000L;
    	private Song song = null;
    	  @Override
    	public void run() {
    		  
    		  song = getNext();
    		  time = Double.valueOf(song.getLength()).longValue() * 1000;
    		  handler.postDelayed(playback, time);
    		  playSong(song);
    	}};
}

