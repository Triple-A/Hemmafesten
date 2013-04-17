package se.chalmers.hemmafesten;



import java.util.List;

import android.util.Log;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

public class PartyController {
	
	private static PartyController instance = null;
	private boolean isCreator;
	private ParseObject party = null;

	
	/**
	 * Party has to be crated or joined before instance can be returned 
	 * use static methods createParty() or joinParty(String accessCode) before.
	 * @return PartyController instance or null
	 */
	public static PartyController getInstance(){
		return instance;
	}
	
	/**
	 * initiates a party instance.
	 */
	public static void createParty(){
		if(instance == null){
			instance = new PartyController();
		}
	}
	
	
	public static void joinParty(String accessCode){
		if(instance == null){
			instance = new PartyController(accessCode);
		}else{
			// add code for telling you that you'r already connected to a party?
		}
		
	}
	
	/**
	 * Construct for creating a new party
	 */
	private PartyController() {  // creating a new party
		
		try {
			this.isCreator = true;
			party = new ParseObject("Party");
			party.save();
		} catch (ParseException e) {
			Log.d("PartyController","create construct: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	/**
	 * construct for creating a partyController connected to an existing party
	 * @param accessCode
	 */
	private PartyController(String accessCode) {  // joining an existing party
		ParseQuery query = new ParseQuery("party");
		query.getInBackground(accessCode, new GetCallback() {
		  public void done(ParseObject object, ParseException e) {
		    if (e == null) {
		    	party = object;
		    	isCreator = false;
		    	Log.d("PartyController","join construct: joined party: "+ party.toString());
		    } else {
		    	Log.d("PartyController","join construct: " + e.getMessage());
		      // something went wrong
		    }
		  }
		});
	}
	
	
	/**
	 * Adds a "song"(spotify uri) to the Song table in parse and a relation between current party and the new song
	 * 
	 * @param spotifyURI Spotify uri as a string
	 */
	public void addSong(String spotifyURI){  // add song to party
		if(party != null){
			try {
				ParseObject song = new ParseObject("Song");
				song.put("party", party);
				song.put("spotifyURI", spotifyURI);
				song.save();
				
				ParseRelation relation = party.getRelation("songs");
				relation.add(song);
				party.saveInBackground();
			} catch (ParseException e) {
				Log.d("addSong","SpotifyURI: "+ spotifyURI + " could not be added to party: "+party);
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * setts the name of current party
	 * @param name New name as a string
	 */
	public void setName(String name){
		if(party != null){
			party.put("name", name);
			party.saveInBackground();
		}
	}

	
	/**
	 * doesnt work at the moment?
	 * @return
	 */
	public String getAccessCode() {         //// diskutera både alternativ lösning och varför inte ddetta funkar!!!!
		return party.getObjectId().toString();
	}

	public boolean isCreator() {
		return isCreator;
	}

	public ParseObject getParty() {
		return party;
	}
	
	public void killParty(){
		
		if(isCreator){
			party.deleteInBackground();
			Log.d("PartyController","killParty: Creator is killing the party");
		}else{
			Log.d("PartyController","killParty: joiner is leaving the party");
		}
		party = null;
		instance = null;
	}
	
	public List<ParseObject> getList(){
		try {
			ParseQuery query = new ParseQuery("Song");
			query.whereEqualTo("party", party);
			return query.find();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
		
	}

}
