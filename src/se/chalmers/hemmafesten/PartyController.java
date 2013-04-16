package se.chalmers.hemmafesten;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.security.SecureRandom;
import java.util.List;
import java.math.BigInteger;

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
	
	public PartyController(String pc) {  // joining an existing party
		super();
		this.isCreator = false;


		ParseQuery query = new ParseQuery("Party");
		query.whereEqualTo("code", pc);
		query.findInBackground(new FindCallback() {
		    public void done(List<ParseObject> partyList, ParseException e) {
		        if (e == null || partyList.size() < 1) {
		        	
		        } else {
		            Log.d("party", "Error: " + e.getMessage());
		        }
		    }

		});
	}
	

	  
	private String partySessionCode() {
		SecureRandom random = new SecureRandom();
	    return new BigInteger(130, random).toString(32);
	}


	
	


}
