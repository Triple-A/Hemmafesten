package se.chalmers.hemmafesten.activity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import se.chalmers.hemmafesten.PartySongAdapter;
import se.chalmers.hemmafesten.R;
import se.chalmers.hemmafesten.SavePartyItem;
import se.chalmers.hemmafesten.SavedPartiesAdapter;
import se.chalmers.hemmafesten.model.Song;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;
import android.widget.TextView;

public class SavedPartiesActivity extends ActionBarActivity {

	private ListView listView;
	private List<SavePartyItem> parties;
	private SavedPartiesAdapter adapter = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_saved_parties);
		// Show the Up button in the action bar.
		
		//listView = (ListView) findViewById(R.id.saved_parties);
		parties = new ArrayList<SavePartyItem>();
		
		try {
			FileInputStream fis = openFileInput("savedParties.dat");
			ObjectInputStream is = new ObjectInputStream(fis);
			//SavePartyItem partyItem = (SavePartyItem) is.readObject();
			//parties.add(partyItem);
				SavePartyItem item = (SavePartyItem) is.readObject();
				parties.add(item);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		
	
		if(parties.size()!=0){
			listView = (ListView) findViewById(R.id.saved_parties);
			Log.i("listContains", parties.get(0).getPartyName());
		adapter = new SavedPartiesAdapter(this,
	            R.layout.saved_party_list_item, parties);
		listView.setAdapter(adapter);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}

}
