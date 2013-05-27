package se.chalmers.hemmafesten.task;

import java.util.LinkedList;
import java.util.List;

import se.chalmers.hemmafesten.adapter.PartySongAdapter;
import se.chalmers.hemmafesten.model.Party;
import se.chalmers.hemmafesten.model.Song;
import android.os.AsyncTask;
import android.util.Log;

import com.parse.ParseException;


public class updatePlaylistTask extends AsyncTask<String, Boolean, List<Song>> {

	private PartySongAdapter adapter;
	private List<Song> songz;
	private Party party;
	
	
	public updatePlaylistTask(List<Song> songz, Party party, PartySongAdapter adapter) {
		super();
		this.party = party; 
		this.songz = songz;
		this.adapter = adapter;
	}

	protected void onPreExecute() {
		try {
			party.refresh();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * gets all the songs from parse
	 */
	@Override
	protected List<Song> doInBackground(String... arg0) {
		try {
			List<Song> tmp = party.getSongs();
			return tmp;
		} catch (ParseException e) {
			Log.e(this.getClass().getName(), e.getMessage());
		}
		return new LinkedList<Song>();
	}
	
	/**
	 * upadates the partySongAdapter
	 */
	protected void onPostExecute(List<Song> list) {
		songz.clear();
		songz.addAll(list);
		adapter.notifyDataSetChanged();
	}

}
