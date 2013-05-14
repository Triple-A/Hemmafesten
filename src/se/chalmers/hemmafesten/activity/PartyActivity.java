package se.chalmers.hemmafesten.activity;


import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import se.chalmers.hemmafesten.R;
import se.chalmers.hemmafesten.SongAdapter;
import se.chalmers.hemmafesten.SongItem;
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
		
		
		RetreiveMusicTask task = new RetreiveMusicTask();
		String result;
		try {
			result = task.execute("get%20lucky").get();
			JSONObject object = new JSONObject(result);
			JSONArray arr = object.getJSONArray("tracks");
									
			ArrayList<SongItem> songz = new ArrayList<SongItem>();
			
			for(int i=0; i<arr.length(); i++){
				SongItem song = new SongItem(arr.getJSONObject(i).getString("name"),
						arr.getJSONObject(i).getJSONArray("artists").getJSONObject(0).getString("name"),
						arr.getJSONObject(i).getString("href"),
						arr.getJSONObject(i).getJSONObject("album").getString("name"),
						arr.getJSONObject(i).getDouble("length"));
				songz.add(song);
			}

		
		ListView  listView = (ListView) findViewById(R.id.queue);
	    SongAdapter adapter = new SongAdapter(this,
	                R.layout.song_list_item, songz);
	     listView.setAdapter(adapter);
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		


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