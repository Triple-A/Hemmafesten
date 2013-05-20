package se.chalmers.hemmafesten.service;


import se.chalmers.hemmafesten.APIKeys;
import se.chalmers.hemmafesten.R;
import se.chalmers.hemmafesten.model.Party;
import se.chalmers.hemmafesten.model.Song;
import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
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
     
	public static PartyController getPartyController() {
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
    	
    	//////////////////////////////////////////////////////////
    	
    	Notification note=new Notification(R.drawable.ic_launcher ,"Can you hear the music?", System.currentTimeMillis());
    	Intent i = new Intent(this, PartyService.class);
    	i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|
    			Intent.FLAG_ACTIVITY_SINGLE_TOP);
    	PendingIntent pi=PendingIntent.getActivity(this, 0, i, 0);
    	note.setLatestEventInfo(this, "HemmaFesten",
    									"testar lite bara!", pi);
    	note.flags|=Notification.FLAG_NO_CLEAR;
    	startForeground(1337, note);
    	
    	//////////////////////////////////////////////////////////7
    	handler.post(playback);
    }
    
    public void stopLoop(){
    	play = false;
    	stopForeground(true);
    }
    
    private Song getNext(){
    	return pc.getParty().getNext();
    }
    
    private void playSong(Song song){
    	try {
    		song.getParseObject().put("isPlayed", true);
    		pc.getParty().refresh();
			String uri = song.getSpotifyURI();
	    	Log.d("playSong", song.toString());
	    	Intent launcher = new Intent( Intent.ACTION_VIEW, Uri.parse(uri));
	    	launcher.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    	startActivity(launcher);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    private Handler handler = new Handler();
    private Runnable playback = new Runnable(){

    	private Long time = 0L;
    	private Song song = null;
    	  @Override
    	public void run() {
    		  
    		  if((song = getNext()) != null && play){
        		  time = Double.valueOf(song.getLength()).longValue() * 1000;
    			  time = 20000L; /////////////////////////////////////////////////////ska bort (test) 20s
        		  handler.postDelayed(playback, time);
        		  playSong(song);
        		  
    		  }
    	}};
}

