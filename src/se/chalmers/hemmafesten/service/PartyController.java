package se.chalmers.hemmafesten.service;



import java.util.List;

import se.chalmers.hemmafesten.model.Party;
import se.chalmers.hemmafesten.model.Song;

import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

public class PartyController {
	
	private Party party = null;

	

	/**
	 * Construct for creating a new party
	 */
	 public PartyController() {  // creating a new party
		try {
			party = new Party();
			party.save();
			Log.i("PartyController","PartyController(): party created");
		} catch (ParseException e) {
			Log.e("PartyController","PartyController(): failed: " + e.getMessage());
		}
	}
	
	
	/**
	 * construct for creating a partyController connected to an existing party
	 * @param accessCode
	 */
	public PartyController(String accessCode) {  // joining an existing party
		try {
			party = Party.getParty(accessCode);
			if(party != null){
		    	Log.i("PartyController","PartyController(String accessCode): joined party: "+ party.toString());
			}
		} catch (ParseException e) {
			Log.e("PartyController","PartyController(String accessCode): failed: " + e.getMessage());
	      // something went wrong
		}
	}
	
	
	/**
	 * Adds a "song"(spotify uri) to the Song table in parse and a relation between current party and the new song
	 * 
	 * @param spotifyURI Spotify uri as a string
	 */
	public void addSong(String spotifyURI){  // add song to party
		if(party != null){
			try {
				Song song = Song.getOrCreateSongBySpotifyURI(spotifyURI);
				party.addSong(song);
				party.saveEventually();
			} catch (ParseException e) {
				Log.e("PartyController","addSong: SpotifyURI: "+ spotifyURI + " could not be added to party: "+party);
			}
		}
	}
	
	public void removeSong(Song song){
		party.removeSong(song);
		party.saveEventually();
	}

	
	public List<ParseObject> getList(){
		try {
			return party.songs().getQuery().find();
		} catch (ParseException e) {
			Log.e("PartyController", "getList: " + e.getMessage());
		}
		return null;
	}
	
	
	/**
	 * sets the name of current party
	 * @param name New name as a string
	 */
	public void setName(String name){
		if(party != null){
			party.setName(name);
			party.saveInBackground();
		}
	}

	
	/**
	 * 
	 * @return the accesCode of the party (string)
	 */
	public String getAccessCode() {
		return party.getAccessCode();
	}


	public Party getParty() {   /// is this needed??????????
		return party;
	}
	
	public void killParty(boolean isCreator){
		if(isCreator){
			party.deleteEventually();
			Log.i("PartyController","killParty: Creator is killing the party");
		}else{
			Log.i("PartyController","killParty: joiner is leaving the party");
		}
		party = null;
	}
	
	

	@Override
	public String toString() {
		return "PartyController [party=" + party
				+ "]";
	}
}
