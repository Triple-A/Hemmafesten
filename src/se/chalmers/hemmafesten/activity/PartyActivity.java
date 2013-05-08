package se.chalmers.hemmafesten.activity;

import se.chalmers.hemmafesten.R;
import se.chalmers.hemmafesten.service.PartyService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;

public class PartyActivity extends ActionBarActivity {

///////////////////////////////////////////////////////////////////////////////////////
    
	private PartyService partyService;
	private boolean psIsBound;
	
	
	private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			partyService = ((PartyService.LocalBinder)service).getService();
		}
		
		public void onServiceDisconnected(ComponentName className) {
			partyService = null;
		}
	};
	
	void doBindService() {
		bindService(new Intent(this, PartyService.class), mConnection, Context.BIND_AUTO_CREATE);
		psIsBound = true;
	}
	
	void doUnbindService() {
		if (psIsBound) {
			unbindService(mConnection);
			psIsBound = false;
		}
	}	
	
///////////////////////////////////////////////////////////////////////////////////////////////7	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_party);
		// Show the Up button in the action bar.
		setupActionBar();
		
		doBindService();
		
	/*	
				
		InputStream URLcontent;
		try {
			ImageView qr = (ImageView) findViewById(R.drawable.qr_code);
			URLcontent = (InputStream) new URL("http://api.qrserver.com/v1/create-qr-code/?size=150x150&data="+partyService.getParty().getAccessCode()).getContent();
			Drawable image = Drawable.createFromStream(URLcontent, "your source link");
			qr.setImageDrawable(image);
		} catch (MalformedURLException e) {
			Log.e("qr-code", e.getMessage());
		} catch (IOException e) {
			Log.e("qr-code", e.getMessage());
		}*/
		
	}
	

	protected void onPause(){
		super.onPause();
		doUnbindService();
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
