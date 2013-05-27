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


/**
 * A model representing a party backed by a ParseObject instance and saved to
 * the Parse cloud service.
 * 
 * A _guest_ can add new tracks (or songs) to a party while a _host_ has the
 * ability to play the 
 */
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
	
	/**
	 * Synchronously fetches a specific party from the backend based on its host access code.
	 * 
	 * @param hostAccessCode The party host access code.
	 * @return A Party instance for the party associated with the host party access code.
	 * @throws ParseException If we could not communicate with Parse.
	 */
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
	 * Asynchronously creates a new party and saves it to Parse.
	 * 
	 * @param callback The callback which handles the newly created, and saved, Party instance.
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
	/**
	 * Initializes a new Party instance with an empty ParseObject instance.
	 */
	public Party() {
		this(new ParseObject(getParseObjectName()));
	}
	
	/**
	 * Initializes a new Party instance with the given ParseObject instance.
	 * @param parseObject The ParseObject instance which should be used a backing store.
	 */
	public Party(ParseObject parseObject) {
		super(parseObject);
		
		String hostAccessCode = generateHostAccessCode().toString().substring(0, 10);
		parseObject.put("hostAccessCode", hostAccessCode);
	}
	
	/**
	 * Generate a new, more or less garantued to be unique, host access code.
	 * @return A newly generated host access code.
	 */
	private static String generateHostAccessCode()
	{
		SecureRandom random = new SecureRandom();
		return new BigInteger(130, random).toString(32);
	}
	
	
	// Getters and setters
	/**
	 * Get the name of the party.
	 * @return The name of the party.
	 */
	public String getName() {
		return this.getParseObject().getString("name");
	}
	
	/**
	 * Set the name of the party.
	 * @param name The new name of the party.
	 */
	public void setName(String name) {
		this.getParseObject().put("name", name);
	}
	
	/**
	 * The guest access code of the party.
	 * 
	 * The access code is the Parse object ID, which is garantued to be unique.
	 * 
	 * @warning This is not available until the object has been saved.
	 * @return The guest access code of the party.
	 */
	public String getAccessCode() {
		return this.getParseObject().getObjectId();
	}
	
	/**
	 * Get the host access code.
	 * @return The host access code for the party.
	 */
	public String getHostAccessCode() {
		return this.getParseObject().getString("hostAccessCode");
	}
	
	/**
	 * Get the parse user which is the host of the party.
	 * @return The ParseUser instance representing the user hosting the party.
	 */
	public ParseUser getHost() {
		return this.getParseObject().getParseUser("host");
	}
	
	/**
	 * Set the parse user representing the user hosting the party.
	 * @param host The ParseUser instance representing the user hosting the party.
	 */
	public void setHost(ParseUser host) {
		this.getParseObject().put("host", host);
	}
	
	/**
	 * Get the Parse relation to for all attendees of the party.
	 * @return A ParseRelation instance which represent all attendees of the party.
	 */
	public ParseRelation getAttendeesRelation() {
		return this.getParseObject().getRelation("attendees");
	}
	
	/**
	 * Add an attendee to the party.
	 * 
	 * Also, eventually, saves the party and user.
	 * 
	 * @param user The ParseUser instance representing the user which should be added to the list of attendees.
	 */
	public void addAttendee(ParseUser user) {
		this.getAttendeesRelation().add(user);
		user.saveEventually();
		this.saveEventually();
	}
	
	/**
	 * Remove an attendee from the party.
	 * 
	 * Also, eventually, saves the party and user.
	 * 
	 * @param user The ParseUser instance representing the user which should be removed from the list of attendees.
	 */
	public void removeAttendee(ParseUser user) {
		this.getAttendeesRelation().remove(user);
		user.saveEventually();
		this.saveEventually();
	}
	
	/**
	 * Get the Parse relation to all songs which have been added to the party.
	 * @return The ParseRelation instance representing all songs which have been added to the party.
	 */
	public ParseRelation getSongsRelation() {
		return this.getParseObject().getRelation("songs");
	}
	
	/**
	 * Add a song to the party.
	 * 
	 * Also, eventually, saves the party and song.
	 * 
	 * @param song The Song instance which should be added to the party.
	 */
	public void addSong(Song song) {
		ParseObject parseSongObject = song.getParseObject();
		this.getSongsRelation().add(parseSongObject);
		parseSongObject.put("party", this.getParseObject());
		this.saveEventually();
		song.saveEventually();
	}
	
	/**
	 * Remove a song from the party.
	 * 
	 * Also, eventually, saves the party and, eventually, deletes the song.
	 * 
	 * @param song The Song instance representing the song which should be removed from the party.
	 */
	public void removeSong(Song song) {
		ParseObject parseSongObject = song.getParseObject();
		this.getSongsRelation().remove(parseSongObject);
		song.deleteEventually();
		this.saveEventually();
	}
	
	/**
	 * Get a list of all songs in the party.
	 * @return A List instance of all songs in the party.
	 * @throws ParseException If we could not get all songs from the backend. 
	 */
	public List<Song> getSongs() throws ParseException {
		List<ParseObject> parseObjects = this.getSongsRelation().getQuery().orderByAscending("createdAt").find();
		List<Song> songs = Song.songsFromParseObjectSongs(parseObjects);
		return songs;
	}
	
	/**
	 * Get a list of all songs in the party asynchronously.
	 * @param callback The callback which handles the list of songs when it arrives, or the ParseException if we were not able to get the songs.
	 */
	public void getSongsAsync(final se.chalmers.hemmafesten.model.callback.FindCallback<Song> callback){
		this.getSongsRelation().getQuery().findInBackground(new FindCallback() {
		    public void done(List<ParseObject> results, ParseException e) {
		    	List<Song> songs = Song.songsFromParseObjectSongs(results);
		    	callback.done(songs, e);
		    }
		});
	}
	
	/**
	 * Mark all songs in the party as played or not.
	 * @param isPlayed Whether the songs should be marked as played (true) or not (false).
	 */
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
	 * The next song which should be played. Might be null if there are no more songs in the list.
	 * @return A Song instance representing the next song which should be played.
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
	
	/**
	 * Delete all songs associated with the party, eventually.
	 * 
	 * Will happen at some point when the device have access to the network. 
	 */
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
	
	/**
	 * Delete both the party synchronously and all associated songs eventually.
	 * 
	 * @see Model.delete()
	 */
	@Override
	public void delete() throws ParseException {
		this.deleteSongsInBackgroundEventually();
		super.delete();
	}
	
	/**
	 * Delete both the party in the background and all associated songs eventually.
	 * 
	 * @see Model.deleteInBackground()
	 */
	@Override
	public void deleteInBackground() {
		this.deleteSongsInBackgroundEventually();
		super.deleteInBackground();
	}
	
	@Override
	/**
	 * Delete both the party and all associated songs eventually.
	 * 
	 * @see Model.deleteEventually()
	 */
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
