package se.chalmers.hemmafesten.service;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import se.chalmers.hemmafesten.APIKeys;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
	
	private Bitmap qrCode = null;

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
     
	public PartyController getParty() {
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
    
    
    public void loadQR(){
        /*URL imageURL = null;
        try {
        	 String temp = pc.getAccessCode();
        	 imageURL = new URL("http://api.qrserver.com/v1/create-qr-code/?size=150x150&data=" + temp);
	         HttpURLConnection connection= (HttpURLConnection)imageURL.openConnection();
	         connection.setDoInput(true);
	         connection.connect();
	         InputStream inputStream = connection.getInputStream();
	         qrCode = BitmapFactory.decodeStream(inputStream);//Convert to bitmap
        }catch (IOException e) {
        	Log.e("getQr()", e.getMessage());
        }*/
    	
    	
    	try {
    		String temp = pc.getAccessCode();
    		URL url = new URL("http://api.qrserver.com/v1/create-qr-code/?size=150x150&data=" + temp);
			qrCode = BitmapFactory.decodeStream(url.openConnection().getInputStream());
		} catch (IOException e) {
			Log.e("loadQr()", e.getMessage());
		}
    }
    
    public Bitmap getQr(){
    	if(qrCode == null){
    		loadQR();
    	}
    	return qrCode;
    }

}

