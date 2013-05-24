package se.chalmers.hemmafesten.service;


import java.util.Calendar;

import se.chalmers.hemmafesten.R;
import se.chalmers.hemmafesten.activity.MainActivity;
import se.chalmers.hemmafesten.activity.PartyActivity;
import se.chalmers.hemmafesten.model.Song;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.parse.ParseException;


public class PartyService extends Service {
	
	private static PartyController pc;
	private Boolean play;
	private Notification note; 
	
	private PendingIntent pi;
    private BroadcastReceiver br;
    private AlarmManager am;
    private PowerManager pm;
    private PowerManager.WakeLock wl;

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
		//wl.release();
		am.cancel(pi);
	    unregisterReceiver(br);
	    
		status = Status.FREE;
	}
	
	
	@Override
	public void onCreate() {
	        super.onCreate();
	        setup();
	        Log.i("PartyService", "Service Started.");
	}
	
	
	@Override
	public void onDestroy(){
		super.onDestroy();
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
        	
        note = new Notification(R.drawable.ic_launcher ,"Can you hear the music?", System.currentTimeMillis());
        Intent i = new Intent(this, PartyActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|
        			Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pi=PendingIntent.getActivity(this, 0, i, 0);
        note.setLatestEventInfo(this, "HemmaFesten",
        									"Paaaaaaarty!!!!", pi);
        note.flags|=Notification.FLAG_NO_CLEAR;
        startForeground(1337, note);
        	
        //////////////////////////////////////////////////////////
        next();
    	
    }
    
    public void stopLoop(){
    	play = false;
    	stopForeground(true);
    	//wl.release();
    }
    
    private Song getNext(){
    	return pc.getParty().getNext();
    }
    
    private void playSong(Song song){
    	song.setIsPlayed(true);
    	song.saveEventually();
		String uri = song.getSpotifyURI();
	    Log.d("playSong", song.toString() + "     " +Double.valueOf(song.getLength()).longValue());
	    	
	    Intent launcher = new Intent( Intent.ACTION_VIEW, Uri.parse(uri));
	    launcher.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    
	    startNext(Double.valueOf(song.getLength()).longValue());//Double.valueOf(song.getLength()).longValue()
	    
	    /*Intent launcher = new Intent( Intent.ACTION_VIEW, Uri.parse(uri));
	    launcher.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/
	    startActivity(launcher);
	    Toast.makeText(PartyService.this,
				"starter song:" + uri,
				Toast.LENGTH_SHORT).show();
    }
    
    public void next(){
    	Song song = null;
    	if((song = getNext()) != null && play){
  		  	playSong(song);
		}
    }

    
    private void setup() {
    	am = (AlarmManager)(this.getSystemService(Context.ALARM_SERVICE ));
        pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        br = new BroadcastReceiver() {
               @Override
               public void onReceive(Context c, Intent i) {
                   wl.acquire();
                   Log.d("broadcast re", "nu jävlar");
                   next();
                   
                   wl.release();
               }
        };
        registerReceiver(br, new IntentFilter("se.chalmers.hemmafesten.service.PartyService.br") );
    }
    
    private void startNext(long time){
    	Log.d("next", "nu jävlar");
    	pi = PendingIntent.getBroadcast( this, 0, new Intent("se.chalmers.hemmafesten.service.PartyService.br"),
        		0 );
    	Calendar cal = Calendar.getInstance();
    	cal.add(Calendar.SECOND, (int) time);
    	am.set( AlarmManager.ELAPSED_REALTIME_WAKEUP, cal.getTimeInMillis(), pi );
    }
}

