package se.chalmers.hemmafesten.service;



import se.chalmers.hemmafesten.model.Party;
import se.chalmers.hemmafesten.model.Song;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseUser;

public class PartyController {
	
	private Party party = null;
	private ParseUser user;

	

	/**
	 * Construct for creating a new party
	 */
	 public PartyController() {  // creating a new party
		party = new Party();
		user = new ParseUser();
		party.setHost(user);
		Log.i("PartyController","PartyController(): party created");
	}
	
	
	/**
	 * construct for creating a partyController connected to an existing party
	 * @param accessCode
	 */
	public PartyController(String accessCode) {  // joining an existing party
		try {
			party = Party.getParty(accessCode);
			if(party != null){
				user = new ParseUser();
				party.addAttendee(user);
		    	Log.i("PartyController","PartyController(String accessCode): joined party: "+ party.toString());
			}
		} catch (ParseException e) {
			Log.e("PartyController","PartyController(String accessCode): failed: " + e.getMessage());
	      // something went wrong
		}
	}
	
	public void addSong(Song song){
		party.addSong(song);
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
			party.deleteInBackground();
			// TODO delete all songs connected to party
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
