package se.chalmers.hemmafesten.service;



import java.util.ArrayList;
import java.util.List;

import se.chalmers.hemmafesten.model.Party;
import se.chalmers.hemmafesten.model.Song;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class PartyController {
	
	private Party party = null;
	private ParseUser user;

	

	/**
	 * Construct for creating a new party
	 */
	public PartyController() {  // creating a new party
		 this("", true);
	}
	
	/**
	 * construct for creating a partyController connected to an existing party
	 * @param accessCode
	 */
	public PartyController(String accessCode) {  // joining an existing party
		this(accessCode, false);
	}
	
	/**
	 * Construct for creating a PartyController for a new party of an existing.
	 * @param accessCode
	 * @param isHost
	 */
	public PartyController(final String accessCode, Boolean isHost) {
		if (accessCode != null && accessCode.length() > 0) {
			try {
				if (isHost == false) {
					party = Party.getParty(accessCode);
				} else {
					party = Party.getPartsForHostAccessCode(accessCode);
				}
			} catch (ParseException e) {
				Log.e("PartyController", "PartyController(String accessCode, Boolean isHost): failed to get party for access code: " + accessCode + "; is host: " + isHost + ". With error: " + e.getMessage());
			}
		} else {
			party = new Party();
		}
		
		if(party != null) {
			user = ParseUser.getCurrentUser();
			Log.i("PartyController", "User: " + user.getObjectId());
			if (user.getObjectId() == null) {
				try {
					user.save();
				} catch (ParseException e) {
					Log.e("PartyController","PartyController(String, Boolean): failed to save user: " + user + ". With error: " + e.getMessage());
				}
			}
			
			try {
				if (isHost) {
					party.setHost(user);
					party.save();
				}
				
				party.addAttendee(user);
				party.refresh();
	    		Log.i("PartyController","PartyController(String accessCode): joined party: "+ party.toString());
			} catch (ParseException e) {
				Log.e("PartyController","PartyController(String accessCode): failed to save party: " + party + ". With error: " + e.getMessage());
			}
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


	public Party getParty() {
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
