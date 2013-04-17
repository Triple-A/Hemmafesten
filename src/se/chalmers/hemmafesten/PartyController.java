package se.chalmers.hemmafesten;

import java.math.BigInteger;
import java.security.SecureRandom;

import com.parse.ParseObject;

public class PartyController {
	
	private String partyCode;
	private boolean isCreator;

	public PartyController() {  // creating a new party
		super();
		this.isCreator = true;
		partyCode = partySessionCode();
		ParseObject party = new ParseObject("Party");
		party.put("code", partyCode);
		party.saveInBackground();
	}
	
	public PartyController(String pc) {  // joining an existing party    Have to add check that party exists!!!!!!
		super();
		this.isCreator = false;
		this.partyCode = pc;
	}
	
	public void addSong(String songId){  // add song to party
		if(partyCode != null){
			ParseObject song = new ParseObject("Song");
			song.put("party", partyCode);
			song.put("songId", songId);
			song.saveInBackground();
		}
	}
	
	  
	private String partySessionCode() {
		SecureRandom random = new SecureRandom();
	    return new BigInteger(130, random).toString(32);
	}


	
	


}
