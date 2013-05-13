package se.chalmers.hemmafesten.model;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

public class Party extends Model {
	
	/**
	 * Asynchronously fetches a specific party from the backend.
	 * @param partyId The Parse object ID of the party.
	 * @param callback The callback which is called once the party has been fetched and converted to a Party object.
	 */
	public static void getPartyAsync(String partyId, final se.chalmers.hemmafesten.model.callback.GetCallback callback) {
		ParseQuery query = new ParseQuery(getParseObjectName());
		query.getInBackground(partyId, new com.parse.GetCallback() {
			@Override
			public void done(ParseObject parsePartyObject, ParseException e) {
				Party party = new Party(parsePartyObject);
				callback.done(party, e);
			}
		});
	}
	
	/**
	 * Asynchronously fetches a specific party from the backend based on its access code.
	 * @param accessCode The party access code.
	 * @param callback The callback which is called once the party has been fetched and converted to a Party object.
	 */
	public static void getPartyForAccessCodeAsync(String accessCode, final se.chalmers.hemmafesten.model.callback.GetCallback callback) {
		getPartyAsync(accessCode, callback);
	}
	
	/**
	 * 
	 * @param callback
	 */
	public static void createPartyAsync(final se.chalmers.hemmafesten.model.callback.GetCallback callback) {
		final Party party = new Party();
		party.getParseObject().saveInBackground(new com.parse.SaveCallback() {
			@Override
			public void done(ParseException e) {
				callback.done(party, e);
			}
		});
	}	
	
	// Constructors
	// To create a new party object you should really use the static
	// createPartyAsync method.
	public Party() {
		super(new ParseObject(getParseObjectName()));
	}
	
	public Party(ParseObject parseObject) {
		super(parseObject);
	}
	
	
	// Getters and setters
	public String getName() {
		return this.getParseObject().getString("name");
	}
	
	public void setName(String name) {
		this.getParseObject().put("name", name);
	}
	
	/**
	 * The access code of the party.
	 * 
	 * The access code is the Parse object ID, which is garantued to be unique.
	 * 
	 * @warning This is not available until the object has been saved.
	 * @return
	 */
	public String getAccessCode() {
		return this.getParseObject().getObjectId();
	}
	
	public ParseUser getHost() {
		return this.getParseObject().getParseUser("host");
	}
	
	public void setHost(ParseUser host) {
		this.getParseObject().put("host", host);
	}
	
	public ParseRelation attendees() {
		return this.getParseObject().getRelation("attendees");
	}
	
	public void addAttendee(ParseUser user) {
		this.attendees().add(user);
	}
	
	public void removeAttendee(ParseUser user) {
		this.attendees().remove(user);
	}
	
	public ParseRelation songs() {
		return this.getParseObject().getRelation("songs");
	}
	
	public void addSong(Song song) {
		ParseObject parseSongObject = song.getParseObject();
		this.songs().add(parseSongObject);
	}
	
	public void removeSong(Song song) {
		ParseObject parseSongObject = song.getParseObject();
		this.songs().remove(parseSongObject);
	}
	
	// Description
	@Override
	public String toString() {
		return "" + this.getClass().getCanonicalName() + " { identifier = '"+ this.getParseObject().getObjectId() +"'; }"; 
	}
	
	
	// Private helpers
	private static String getParseObjectName() {
		return "Song";
	}
	
	@Override
	protected String getParseObjectNameInternal() {
		return getParseObjectName();
	}

}
