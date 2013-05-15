package se.chalmers.hemmafesten.activity;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.parse.ParseException;

import se.chalmers.hemmafesten.PartySongAdapter;
import se.chalmers.hemmafesten.R;
import se.chalmers.hemmafesten.SearchSongItem;
import se.chalmers.hemmafesten.model.Party;
import se.chalmers.hemmafesten.model.Song;
import se.chalmers.hemmafesten.service.PartyService;
import se.chalmers.hemmafesten.task.RetreiveMusicTask;
import se.chalmers.hemmafesten.task.RetreiveQrTask;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class PartyActivity extends ActionBarActivity {



	public void loadQR(){
		new RetreiveQrTask(this, partyService).execute();
	}

///////////////////////////////////////////////////////////////////////////////////////
    
	private PartyService partyService;
	private boolean psIsBound;


	private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			partyService = ((PartyService.LocalBinder)service).getService();
			loadQR();
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
		
		try {
			Party party = Party.getParty(PartyService.getParty().getAccessCode());
			party.refresh();
			List<Song> songz = new ArrayList<Song>();
			songz = party.getSongs();
			
			ListView  listView = (ListView) findViewById(R.id.queue); 
		    PartySongAdapter adapter = new PartySongAdapter(this,
		                R.layout.party_song_list_item, songz);
		     listView.setAdapter(adapter);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		


	}

	protected void onPause(){
		super.onPause();
		doUnbindService();
	}
	
	protected void onResume(){
		super.onResume();
		try {
			Party party = Party.getParty(PartyService.getParty().getAccessCode());
			party.refresh();
			List<Song> songz = new ArrayList<Song>();
			songz = party.getSongs();
			
			ListView  listView = (ListView) findViewById(R.id.queue); 
		    PartySongAdapter adapter = new PartySongAdapter(this,
		                R.layout.party_song_list_item, songz);
		     listView.setAdapter(adapter);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
		//return super.onCreateOptionsMenu(menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu){
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