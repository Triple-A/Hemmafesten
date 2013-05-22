package se.chalmers.hemmafesten.model;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

public class Party extends Model {
	
	/**
	 * Synchronously fetches a specific party from the backend.
	 * @param partyId The Parse object ID of the party.
	 * @param callback The callback which is called once the party has been fetched and converted to a Party object.
	 * @throws ParseException Throws an exception when the network connection fails.
	 */
	public static Party getParty(String partyId) throws ParseException {
		ParseQuery query = new ParseQuery(getParseObjectName());
		ParseObject parsePartyObject = null;
		try {
			parsePartyObject = query.get(partyId);
		} catch (ParseException e) {
			if (e.getCode() != ParseException.OBJECT_NOT_FOUND) {
				Log.e("DATA_LAYER", "getParty(String): failed: " + e.getMessage());
				throw e;
			}
		}
		
		Party party = null;
		if (parsePartyObject != null) {
			party = new Party(parsePartyObject);
		}
		
		return party;
	}
	
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
				Party party = null;
				if (parsePartyObject != null) {
					party = new Party(parsePartyObject);
				}
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
	
	public static Party getPartsForHostAccessCode(String hostAccessCode) throws ParseException {
		ParseQuery query = new ParseQuery(getParseObjectName()).whereEqualTo("hostAccessCode", hostAccessCode);
		ParseObject parsePartyObject = null;
		try {
			parsePartyObject = query.getFirst();
		} catch (ParseException e) {
			if (e.getCode() != ParseException.OBJECT_NOT_FOUND) {
				Log.e("DATA_LAYER", "getParty(String): failed: " + e.getMessage());
				throw e;
			}
		}
		
		Party party = null;
		if (parsePartyObject != null) {
			party = new Party(parsePartyObject);
		}
		
		return party;
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
		this(new ParseObject(getParseObjectName()));
	}
	
	public Party(ParseObject parseObject) {
		super(parseObject);
		
		String hostAccessCode = generateHostAccessCode().toString().substring(0, 10);
		parseObject.put("hostAccessCode", hostAccessCode);
	}
	
	private static String generateHostAccessCode()
	{
		SecureRandom random = new SecureRandom();
		return new BigInteger(130, random).toString(32);
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
	
	public String getHostAccessCode() {
		return this.getParseObject().getString("hostAccessCode");
	}
	
	public ParseUser getHost() {
		return this.getParseObject().getParseUser("host");
	}
	
	public void setHost(ParseUser host) {
		this.getParseObject().put("host", host);
	}
	
	public ParseRelation getAttendeesRelation() {
		return this.getParseObject().getRelation("attendees");
	}
	
	public void addAttendee(ParseUser user) {
		this.getAttendeesRelation().add(user);
	}
	
	public void removeAttendee(ParseUser user) {
		this.getAttendeesRelation().remove(user);
	}
	
	public ParseRelation getSongsRelation() {
		return this.getParseObject().getRelation("songs");
	}
	
	public void addSong(Song song) {
		ParseObject parseSongObject = song.getParseObject();
		this.getSongsRelation().add(parseSongObject);
		parseSongObject.put("party", this.getParseObject());
		this.saveEventually();
		song.saveEventually();
	}
	
	public void removeSong(Song song) {
		ParseObject parseSongObject = song.getParseObject();
		this.getSongsRelation().remove(parseSongObject);
		song.deleteEventually();
		this.saveEventually();
	}
	
	public List<Song> getSongs() throws ParseException {
		List<ParseObject> parseObjects = this.getSongsRelation().getQuery().orderByAscending("createdAt").find();
		List<Song> songs = Song.songsFromParseObjectSongs(parseObjects);
		return songs;
	}
	
	public void getSongsAsync(final se.chalmers.hemmafesten.model.callback.FindCallback<Song> callback){
		this.getSongsRelation().getQuery().findInBackground(new FindCallback() {
		    public void done(List<ParseObject> results, ParseException e) {
		    	List<Song> songs = Song.songsFromParseObjectSongs(results);
		    	callback.done(songs, e);
		    }
		});
	}
	
	public void markAllSongsAsPlayed(final boolean isPlayed) {
		this.getSongsAsync(new se.chalmers.hemmafesten.model.callback.FindCallback<Song>() {
			@Override
			public void done(List<Song> songs, ParseException e) {
				for (Song song : songs) {
					song.setIsPlayed(isPlayed);
					song.saveEventually();
				}
			}
		});
	}

	/**
	 * The next song which should be played. Might be null if there are no more.
	 * @return
	 */
	public Song getNext() {
		ParseQuery pq = getSongsRelation().getQuery();
		pq.orderByAscending("createdAt");
		pq.whereEqualTo("isPlayed", false);
		ParseObject nextSongParseObject = null;
		try {
			nextSongParseObject = pq.getFirst();
		} catch (ParseException e) {
			if (e.getCode() != ParseException.OBJECT_NOT_FOUND) { 
				Log.e("DATA_LAYER", e.getMessage());
			}
		}
		
		Song nextSong = null;
		if (nextSongParseObject != null) {
			nextSong = new Song(nextSongParseObject);
		}
		
		return nextSong;
	}
	
	private void deleteSongsInBackgroundEventually() {
		this.getSongsAsync(new se.chalmers.hemmafesten.model.callback.FindCallback<Song>() {
			@Override
			public void done(List<Song> songs, ParseException e) {
				for (Song song : songs) {
					song.deleteEventually();
				}
			}
		});
	}
	
	public void delete() throws ParseException {
		this.deleteSongsInBackgroundEventually();
		super.delete();
	}
	
	public void deleteInBackground() {
		this.deleteSongsInBackgroundEventually();
		super.deleteInBackground();
	}
	
	public void deleteEventually() {
		this.deleteSongsInBackgroundEventually();
		super.deleteEventually();
	}
	
	// Description
	@Override
	public String toString() {
		return "" + this.getClass().getCanonicalName() + " { identifier = '"+ this.getParseObject().getObjectId() +"'; }"; 
	}
	
	
	// Private helpers
	private static String getParseObjectName() {
		return "Party";
	}
	
	@Override
	protected String getParseObjectNameInternal() {
		return getParseObjectName();
	}

}
