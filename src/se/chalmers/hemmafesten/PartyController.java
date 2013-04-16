package se.chalmers.hemmafesten;

import java.math.BigInteger;
import java.security.SecureRandom;

import com.parse.ParseObject;

public class PartyController {
	
	private String accessCode;
	private boolean isCreator;
	private ParseObject party;

	public PartyController(String name, String pass) {  // creating a new party
		//super();
		this.isCreator = true;
		accessCode = partySessionCode();
		party = new ParseObject("Party");
		party.put("name", name);
		party.put("accessCode", accessCode);
		party.saveInBackground();
	}
	
	public PartyController(String pc) {  // joining an existing party    Have to add check that party exists!!!!!!
		super();
		this.isCreator = false;
		this.accessCode = pc;
	}
	
	public void addSong(String songId){  // add song to party
		if(party != null){
			ParseObject song = new ParseObject("Song");
			song.put("party", party);
			song.put("spotifyURI", songId);
			song.saveInBackground();
		}
	}
	
	
	

	  
	public String getAccessCode() {
		return accessCode;
	}

	public boolean isCreator() {
		return isCreator;
	}

	public ParseObject getParty() {
		return party;
	}

	private String partySessionCode() {
		SecureRandom random = new SecureRandom();
	    return new BigInteger(50, random).toString(32);
	}


	
	


}
