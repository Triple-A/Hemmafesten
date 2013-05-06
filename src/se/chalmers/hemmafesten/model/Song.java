package se.chalmers.hemmafesten.model;

import org.json.JSONException;
import org.json.JSONObject;
import com.parse.ParseObject;

public class Song extends ParseObject {
	public static final String SPOTIFY_JSON_SPOTIFY_URI_KEY = "href";
	
	public static Song getSong(String spotifyUri) {
		return null;
	}
	
	public static Song getOrCreateSong(JSONObject jsonObject) throws JSONException {
		String spotifyUri = jsonObject.getString(SPOTIFY_JSON_SPOTIFY_URI_KEY);
		
		Song song = getSong(spotifyUri);
		if (song == null) {
			song = new Song();
		}
		
		song.updateWithSpotifyJSONObject(jsonObject);
		
		return song;
	} 
	
	public Song() {
		super("Song");
	}
	
	public void updateWithSpotifyJSONObject(JSONObject jsonObject) {
		
	}
}
