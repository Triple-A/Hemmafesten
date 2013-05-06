package se.chalmers.hemmafesten.model;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * The Song class represent a single song, or track, in the Spotify library.
 * 
 * @author aron
 */
public class Song extends ParseObject {	
	private static final String SPOTIFY_JSON_SONG_URI_KEY = "href";
	private static final String SPOTIFY_JSON_SONG_NAME_KEY = "name";
	private static final String SPOTIFY_JSON_SONG_LENGTH_KEY = "length";
	private static final String SPOTIFY_JSON_SONG_POPULARITY_KEY = "popularity";
	
	private static final String SPOTIFY_JSON_ALBUM_KEY = "album";
	private static final String SPOTIFY_JSON_ALBUM_NAME_KEY = "name";
	private static final String SPOTIFY_JSON_ALBUM_URI_KEY = "href";
	
	private static final String SPOTIFY_JSON_ARTISTS_KEY = "artists";
	private static final String SPOTIFY_JSON_ARTISTS_ARTIST_NAME_KEY = "name";
	private static final String SPOTIFY_JSON_ARTISTS_ARTIST_URI_KEY = "href";
	
	private double length;
	private double popularity;
	private String albumSpotifyUri;
	
	
	/**
	 * Asynchronously fetches a song from the backend.
	 * @param songId The Parse identifier associated with the song object.
	 * @param callback The callback which handles the song.
	 */
	public static void getSongAsync(String songId, GetCallback callback) {
		ParseQuery query = new ParseQuery(className());
		query.getInBackground(songId, callback);
	}
	
	/**
	 * Asynchronously fetches a song from the backend.
	 * @param spotifyUri The Spotify URI used to identify the song.
	 * @param callback The callback which handles the song(s) fetched.
	 */
	public static void getSongBySpotifyAsync(String spotifyUri, FindCallback callback) {
		songQueryBySpotifyURI(spotifyUri).findInBackground(callback);
	}
	
	/**
	 * Synchronously gets a song from the backend.
	 * @param spotifyUri The Spotify URI used to identify the song.
	 * @return The song associated with the Spotify URI if found, otherwise `null`.
	 */
	public static Song getSongBySpotifyURI(String spotifyUri) {
		Song song = null;
		List<ParseObject> songs = null;
		try { songs = songQueryBySpotifyURI(spotifyUri).find();	}
		catch (ParseException e) { e.printStackTrace(); }
		
		if (songs != null && songs.size() > 0) {
			song = (Song) songs.get(0);
		}
		
		return song;
	}
	
	/**
	 * Synchronously tries to get a song for the given JSON object and if it could not be found creates it.
	 * @param jsonObject A Spotify JSON object.
	 * @return
	 * @throws JSONException
	 */
	public static Song getOrCreateSong(JSONObject jsonObject) throws JSONException {
		String spotifyUri = jsonObject.getString(SPOTIFY_JSON_SONG_URI_KEY);
		
		Song song = getSongBySpotifyURI(spotifyUri);
		if (song == null) {
			song = new Song(jsonObject);
		} else {
			song.updateWithSpotifyJSONObject(jsonObject);
		}
		
		return song;
	} 
	
	private static ParseQuery songQueryBySpotifyURI(String spotifyUri) {
		ParseQuery query = new ParseQuery(className());
		query.whereEqualTo("spotifyURI", spotifyUri);
		return query;
	}
	
	private static String className() {
		return "Song";
	}
	
	public Song() {
		super(className());
	}
	
	public Song(JSONObject jsonObject) throws JSONException {
		this();
		this.updateWithSpotifyJSONObject(jsonObject);
	}

	public void updateWithSpotifyJSONObject(JSONObject jsonObject) throws JSONException {
		this.setName(jsonObject.getString(SPOTIFY_JSON_SONG_NAME_KEY));
		this.setSpotifyURI(jsonObject.getString(SPOTIFY_JSON_SONG_URI_KEY));
		this.setLength(jsonObject.getDouble(SPOTIFY_JSON_SONG_LENGTH_KEY));
		this.setPopularity(jsonObject.getDouble(SPOTIFY_JSON_SONG_POPULARITY_KEY));
		
		JSONObject album = jsonObject.getJSONObject(SPOTIFY_JSON_ALBUM_KEY);
		if (album != null) {
			this.setAlbumName(album.getString(SPOTIFY_JSON_ALBUM_NAME_KEY));
			this.setAlbumURI(album.getString(SPOTIFY_JSON_ALBUM_URI_KEY));
		}
		
		StringBuilder artistStringBuilder = new StringBuilder();
		JSONArray artists = jsonObject.getJSONArray(SPOTIFY_JSON_ARTISTS_KEY);
		for (int i = 0; i < artists.length(); ++i) {
			JSONObject artist = artists.getJSONObject(i);
			if (artist != null) {
				String artistName = artist.getString(SPOTIFY_JSON_ARTISTS_ARTIST_NAME_KEY);
				if (artistName != null) {
					String prefix = artistStringBuilder.length() > 0 ? ", " : "";
					artistStringBuilder.append(prefix).append(artistName);
				}
			}
		}
		this.setArtistName(artistStringBuilder.toString());
	}

	// Getters and setters
	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}
	
	public double getPopularity() {
		return popularity;
	}

	public void setPopularity(double popularity) {
		this.popularity = popularity;
	}
	
	public String getName() {
		return this.getString("name");
	}
	
	public void setName(String name) {
		this.put("name", name);
	}
	
	public String getAlbumName() {
		return this.getString("album");
	}
	
	public void setAlbumName(String albumName) {
		this.put("album", albumName);
	}
	
	public String getAlbumURI() {
		return this.albumSpotifyUri;
	}
	
	public void setAlbumURI(String albumUri) {
		this.albumSpotifyUri = albumUri;
	}
	
	public String getArtistName() {
		return this.getString("artist");
	}
	
	public void setArtistName(String artistName) {
		this.put("artist", artistName);
	}
	
	public String getSpotifyURI() {
		return this.getString("spotifyURI");
	}
	
	public void setSpotifyURI(String spotifyUri) {
		this.put("spotifyURI", spotifyUri);
	}
}
