package se.chalmers.hemmafesten.activity;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import se.chalmers.hemmafesten.R;
import se.chalmers.hemmafesten.adapter.PartySongAdapter;
import se.chalmers.hemmafesten.item.SavePartyItem;
import se.chalmers.hemmafesten.model.Song;
import se.chalmers.hemmafesten.service.PartyService;
import se.chalmers.hemmafesten.service.PartyService.Status;
import se.chalmers.hemmafesten.task.RetreiveQrTask;
import se.chalmers.hemmafesten.task.updatePlaylistTask;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class PartyActivity extends ActionBarActivity {

	private ListView listView;
	private List<Song> songz;
	private ArrayList<SavePartyItem> items;
	private PartySongAdapter adapter = null;
	private ImageButton playButton;
	

	/**
	 * loads connection qrcode via ASyncTask and restfull qr generator
	 */
	private void loadQR(){
		if(psIsBound){
			new RetreiveQrTask(this, partyService).execute();
		}
	}
	
	/**
	 * loads playlist via ASyncTask
	 */
	private void loadList(){
		long delay = 5000;
		long period = 1000;
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask(){
			public void run(){
		        new updatePlaylistTask(songz, PartyService.getPartyController().getParty(), adapter).execute();
			}
		},delay,period);

	}
	
	/** 
	 * button listener to change play state
	 * 
	 * @param sender
	 */
	public void onClickPlay(View sender){
		if(psIsBound) {
			if(partyService.getPlay()){
				partyService.stopLoop();
			}else{
				partyService.startLoop();
			}
		}
		updatePartyButton();
	}
	
	public void updatePartyButton(){
		if(psIsBound) {
			if(partyService.getStatus() == Status.HOST){
				playButton.setVisibility(0);
				if(partyService.getPlay()){
					playButton.setImageResource(R.drawable.stop_button);
				}else{
					playButton.setImageResource(R.drawable.play_button);
				}
			}else{
				playButton.setVisibility(8);
			}
		}
	}


//////////////DIALOG METHODS ////////////////////////////////////////////////////////
	
	/**
	 * save a party
	 * @param sender
	 */
	public void savePartyClicked(View sender){
	
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Save party");
		alert.setMessage("Enter a name to save the party");

		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		alert.setView(input);
		
		items = new ArrayList<SavePartyItem>();
		
		alert.setPositiveButton(R.string.save_party_button, new DialogInterface.OnClickListener() {
		@SuppressWarnings("unchecked")
		public void onClick(DialogInterface dialog, int whichButton) {
		  SavePartyItem saveParty = new SavePartyItem(input.getText().toString(),partyService.getPartyController().getAccessCode());
		  
		  try {
			FileInputStream fileInput = openFileInput("savedParties.txt");
			ObjectInputStream objectInput = new ObjectInputStream(fileInput);
			items.addAll((ArrayList<SavePartyItem>)objectInput.readObject());
			
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
		  Log.i("beforesavethisParty", Integer.toString(items.size()));
		  
		  items.add(saveParty);
		  
		  try {
			  
			FileOutputStream fileOutput = openFileOutput("savedParties.txt", Context.MODE_PRIVATE);
			ObjectOutputStream saveObject = new ObjectOutputStream(fileOutput);
			saveObject.writeUnshared(items);
			saveObject.close();
			fileOutput.close();
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		  
		  for(SavePartyItem item: items){
			  Log.i("partyItem", item.getPartyName());
		  }
		  
		  Log.i("beforeMakeToastItemsSize",Integer.toString(items.size()));
		  
		  Toast.makeText(PartyActivity.this, "Party saved", Toast.LENGTH_SHORT).show();}
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int whichButton) {
		    // Canceled.
		  }
		});

		alert.show();
	}

	

///////////////////////////////////////////////////////////////////////////////////////
    
	private PartyService partyService;
	private boolean psIsBound;


	private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			partyService = ((PartyService.LocalBinder)service).getService();
			loadQR();
			updatePartyButton();
			loadList();
		}

		public void onServiceDisconnected(ComponentName className) {
			partyService = null;
		}
	};

	/**
	 * bind to the party service
	 */
	void doBindService() {
		bindService(new Intent(this, PartyService.class), mConnection, Context.BIND_AUTO_CREATE);
		psIsBound = true;
	}

	/**
	 * unbind from the party service
	 */
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
		adapter = new PartySongAdapter(this,
	            R.layout.party_song_list_item, songz);
		listView.setAdapter(adapter);
		playButton = (ImageButton) findViewById(R.id.playButton);
		
		
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

			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}