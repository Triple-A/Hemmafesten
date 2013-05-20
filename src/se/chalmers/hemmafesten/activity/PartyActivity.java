package se.chalmers.hemmafesten.activity;


import java.util.LinkedList;
import java.util.List;

import se.chalmers.hemmafesten.PartySongAdapter;
import se.chalmers.hemmafesten.R;
import se.chalmers.hemmafesten.model.Song;
import se.chalmers.hemmafesten.service.PartyService;
import se.chalmers.hemmafesten.task.RetreiveQrTask;
import se.chalmers.hemmafesten.task.updatePlaylistTask;
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
import android.view.View;
import android.widget.ListView;

public class PartyActivity extends ActionBarActivity {

	private ListView listView;
	private List<Song> songz;
	private PartySongAdapter adapter = null;
	

	private void loadQR(){
		if(psIsBound){
			new RetreiveQrTask(this, partyService).execute();
		}
	}
	
	private void loadList(){
		if(psIsBound){
			new updatePlaylistTask(songz, PartyService.getPartyController().getParty(), adapter).execute();
		}
	}
	
	public void onClickPlay(View sender){
		if(psIsBound) {
			if(partyService.getPlay()){
				partyService.stopLoop();
			}else{
				partyService.startLoop();
			}
		}
	}

///////////////////////////////////////////////////////////////////////////////////////
    
	private PartyService partyService;
	private boolean psIsBound;


	private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			partyService = ((PartyService.LocalBinder)service).getService();
			loadQR();
			loadList();
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
		
		listView = (ListView) findViewById(R.id.queue);
		songz = new LinkedList<Song>();
		Log.d("onCreate", "create song list");
		adapter = new PartySongAdapter(this,
	            R.layout.party_song_list_item, songz);
		listView.setAdapter(adapter);

		doBindService();
	}
	
	protected void onStart(){
		super.onStart();
		doBindService();
	}

	protected void onPause(){
		super.onPause();
		doUnbindService();
	}
	
	
	protected void onResume(){
		super.onResume();
		doBindService();
	}
	
	protected void onStop(){
		super.onStop();
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
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu){
		return super.onPrepareOptionsMenu(menu);
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