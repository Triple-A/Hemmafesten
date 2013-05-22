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

import se.chalmers.hemmafesten.R;
import se.chalmers.hemmafesten.adapter.PartySongAdapter;
import se.chalmers.hemmafesten.adapter.SavedPartiesAdapter;
import se.chalmers.hemmafesten.item.SavePartyItem;
import se.chalmers.hemmafesten.model.Song;
import android.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class SavedPartiesActivity extends ActionBarActivity {

	private ListView listView;
	private SavedPartiesAdapter adapter = null;
	private ArrayList<SavePartyItem> items;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		ActionBar bar = getActionBar();
		bar.setTitle("Saved parties");
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}

}
