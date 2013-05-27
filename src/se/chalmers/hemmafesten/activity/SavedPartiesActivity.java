package se.chalmers.hemmafesten.activity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

import se.chalmers.hemmafesten.R;
import se.chalmers.hemmafesten.adapter.SavedPartiesAdapter;
import se.chalmers.hemmafesten.item.SavePartyItem;
import android.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;

public class SavedPartiesActivity extends ActionBarActivity {

	private ListView listView;
	private SavedPartiesAdapter adapter = null;
	private ArrayList<SavePartyItem> items;
	
	/**
	 * loads all saved parties
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_saved_parties);
		
		items = new ArrayList<SavePartyItem>();
		
		try {
			FileInputStream fis = openFileInput("savedParties.txt");
			ObjectInputStream is = new ObjectInputStream(fis);
			ArrayList<SavePartyItem> partyItems = (ArrayList<SavePartyItem>) is.readObject();
			items.addAll(partyItems);

			is.close();
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
		
		Log.i("itemsSize", Integer.toString(items.size()));
	
		if(items.size()!=0){
			listView = (ListView) findViewById(R.id.saved_parties);
		adapter = new SavedPartiesAdapter(this,
	            R.layout.saved_party_list_item, items);
		listView.setAdapter(adapter);
		}
		
		
	}
	
	/**
	 * get actionbar settings from super class
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Saved Parties");
	    return true;
	}

}
