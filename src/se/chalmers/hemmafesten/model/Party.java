package se.chalmers.hemmafesten.model;

import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;

public class Party extends Model {
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
	
	// The access code is the Parse object ID, which is garantued to be unique.
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
