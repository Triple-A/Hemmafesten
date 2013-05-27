package se.chalmers.hemmafesten.model;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * The Song class represent a single song, or track, in the Spotify library.
 * 
 * @author Aron
 */
public class Song extends se.chalmers.hemmafesten.model.Model {
	// Spotify metadata API JSON keys
	private static final String SPOTIFY_JSON_SONG_URI_KEY = "href";
	private static final String SPOTIFY_JSON_SONG_NAME_KEY = "name";
	private static final String SPOTIFY_JSON_SONG_LENGTH_KEY = "length";
	private static final String SPOTIFY_JSON_SONG_POPULARITY_KEY = "popularity";
	
	private static final String SPOTIFY_JSON_ALBUM_KEY = "album";
	private static final String SPOTIFY_JSON_ALBUM_NAME_KEY = "name";
	private static final String SPOTIFY_JSON_ALBUM_URI_KEY = "href";
	
	private static final String SPOTIFY_JSON_ARTISTS_KEY = "artists";
	private static final String SPOTIFY_JSON_ARTISTS_ARTIST_NAME_KEY = "name";
	//private static final String SPOTIFY_JSON_ARTISTS_ARTIST_URI_KEY = "href";
	
	private double popularity;
	
	/**
	 * Create a Song instance from a Spotify metadata API JSON object and saves it to Parse.
	 * 
	 * @param jsonObject The JSON returned by Spotify’s metadata API, as an 
	 * @return A newly created Song instance initialized with the values in the JSON dictionary or `null`. 
	 * @throws ParseException If the the save to Parse failed.
	 * @throws JSONException If the given JSON was malformed.
	 */
	public static Song createSongWithSpotifyJSON(JSONObject jsonObject) throws ParseException, JSONException {
		Song song = new Song(jsonObject);
		song.setIsPlayed(false);
		song.save();
		return song;
	}
	
	/**
	 * Create a Song instance from a Spotify song URI and saves it to Parse.
	 * 
	 * It will be empty except for the Spotify song URI and its Parse identifier. 
	 * 
	 * @param spotifyUri The Spotify URI.
	 * @return A newly created Song instance initialized with the given Spotify song URI.
	 * @throws ParseException If the save to Parse failed.
	 */
	public static Song createSongWithSpotifyURI(String spotifyUri) throws ParseException {
		Song song = new Song();
		song.setSpotifyURI(spotifyUri);
		song.setIsPlayed(false);
		song.save();
		return song;
	}
	
	/**
	 * Create a Song instance from a Spotify song URI and saves it asynchronously to Parse.
	 * 
	 * It will be empty except for the Spotify song URI and its Parse identifier. 
	 * 
	 * @param spotifyUri The Spotify URI.
	 * @param callback The callback which should handle the object once it has been created and saved to Parse.
	 * @return A newly created Song instance initialized with the given Spotify song URI.
	 */
	public static void createSongWithSpotifyURIAsync(String spotifyUri, final se.chalmers.hemmafesten.model.callback.GetCallback callback) {
		final Song song = new Song();
		song.setSpotifyURI(spotifyUri);
		song.setIsPlayed(false);
		song.getParseObject().saveInBackground(new com.parse.SaveCallback() {
			@Override
			public void done(ParseException e) {
				callback.done(song, e);
			}
		});
	}
	
	/**
	 * Asynchronously fetches a specific song from the backend.
	 * @param songId The Parse identifier associated with the song object.
	 * @param callback The callback which handles the song.
	 */
	public static void getSongAsync(String songId, final se.chalmers.hemmafesten.model.callback.GetCallback callback) {
		ParseQuery query = new ParseQuery(getParseObjectName());
		query.getInBackground(songId, new com.parse.GetCallback() {
			@Override
			public void done(ParseObject parseSongObject, ParseException e) {
				Song song = new Song(parseSongObject);
				callback.done(song, e);
			}
		});
	}
	
	/**
	 * Asynchronously fetches a song from the backend.
	 * @param spotifyUri The Spotify URI used to identify the song.
	 * @param callback The callback which handles the song(s) fetched.
	 */
	public static void getSongBySpotifyURIAsync(String spotifyUri, final se.chalmers.hemmafesten.model.callback.FindCallback<Song> callback) {
		songQueryBySpotifyURI(spotifyUri).findInBackground(new com.parse.FindCallback() {
			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				List<Song> songs = new LinkedList<Song>();
				for (ParseObject parseObject : objects) {
					Song song = new Song(parseObject);
					songs.add(song);
				}
				callback.done(songs, e);
			}			
		});
	}
	
	/**
	 * Synchronously gets a song from the backend.
	 * @param spotifyUri The Spotify URI used to identify the song.
	 * @return The song associated with the Spotify URI if found, otherwise `null`.
	 */
	public static Song getSongBySpotifyURI(String spotifyUri) throws ParseException {
		Song song = null;
		List<ParseObject> songs = null;
		songs = songQueryBySpotifyURI(spotifyUri).find();
		
		if (songs != null && songs.size() > 0) {
			song = new Song(songs.get(0));
		}
		
		return song;
	}
	
	// Parse queries
	// Query for finding a all songs equal to a given Spotify URI.
	private static ParseQuery songQueryBySpotifyURI(String spotifyUri) {
		ParseQuery query = new ParseQuery(getParseObjectName());
		query.whereEqualTo("spotifyURI", spotifyUri);
		return query;
	}
	
	/**
	 * Create Song model instances from a list of song ParseObjects. 
	 * @param parseObjectSongs The ParseObjects which represent the songs.
	 * @return A list of Song instances.
	 */
	public static List<Song> songsFromParseObjectSongs(List<ParseObject> parseObjectSongs) {
		List<Song> songs = new LinkedList<Song>();
    	for(ParseObject po : parseObjectSongs){
    		songs.add(new Song(po));
    	}
    	return songs;
	}
	
	
	// Constructors
	/**
	 * Initialize a Song instance with the given Spotify metadata API JSON object.
	 * @param jsonObject The Spotify metadata API JSON object used to initialize the instance.
	 * @throws JSONException If the JSON object was malformed.
	 */
	public Song(JSONObject jsonObject) throws JSONException {
		this();
		this.setIsPlayed(false);
		this.updateWithSpotifyJSONObject(jsonObject);
	}
	
	/**
	 * Initialize a Song instance and associate it with a new ParseObject instance.
	 */
	public Song() {
		this(new ParseObject(getParseObjectName()));
		this.setIsPlayed(false);
	}
	
	/**
	 * Initialize a Song instance with the given ParseObject instance.
	 * @param parseObject The ParseObject instance which will back the Song instance.
	 */
	public Song(ParseObject parseObject) {
		super(parseObject);
	}

	
	// Updating a Song
	/**
	 * Update the song object with the data in the Spotify JSON object.
	 * @param jsonObject The JSON object gotten from Spotify.
	 * @throws JSONException If the JSON was malformed.
	 */
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
	// Parse backed
	/**
	 * Get the name of the song.
	 * @return
	 */
	public String getName() {
		return this.getParseObject().getString("name");
	}
	
	/**
	 * Set the name of the song.
	 * @param name The new name.
	 * @private
	 */
	private void setName(String name) {
		this.getParseObject().put("name", name);
	}
	
	/**
	 * Get whether the song has been played.
	 * @return
	 */
	public Boolean getIsPlayed() {
		return this.getParseObject().getBoolean("isPlayed");
	}
	
	/**
	 * Set whether the song has been played.
	 * @param hasBeenPlayed A boolean indicating whether it has been played (true) or not (false).
	 */
	public void setIsPlayed(Boolean hasBeenPlayed) {
		this.getParseObject().put("isPlayed", hasBeenPlayed);
	}
	
	/**
	 * Get the name of the album the song belongs to.
	 * @return The name of the album which the song belongs to.
	 */
	public String getAlbumName() {
		return this.getParseObject().getString("album");
	}
	
	/**
	 * Set the name of the album the song belongs to.
	 * @param albumName The name of the album which the song belongs to.
	 */
	private void setAlbumName(String albumName) {
		this.getParseObject().put("album", albumName);
	}
	
	/**
	 * Get the name(s) of the artist(s) which created the song. 
	 * 
	 * Comma-separated list if multiple artists; e.g. "Jane Doe, John Doe".
	 * 
	 * @return The name(s) of the artist(s) (comma-separated if multiple) which created the song.
	 */
	public String getArtistName() {
		return this.getParseObject().getString("artist");
	}
	
	/**
	 * Set the name(s) of the artist(s) which created the song.
	 * 
	 * Comma-separated list if multiple artists; e.g. "Jane Doe, John Doe".
	 * 
	 * @param artistName The name(s) of the artist(s) (comma-separated if multiple)
	 */
	private void setArtistName(String artistName) {
		this.getParseObject().put("artist", artistName);
	}
	
	/**
	 * Get the song’s Spotify URI.
	 * @return The song’s Spotify URI.
	 */
	public String getSpotifyURI() {
		return this.getParseObject().getString("spotifyURI");
	}
	
	/**
	 * Set the song’s Spotify URI.
	 * @param spotifyUri The Spotify URI of the song.
	 */
	private void setSpotifyURI(String spotifyUri) {
		this.getParseObject().put("spotifyURI", spotifyUri);
	}
	
	/**
	 * The playback length of the song, in seconds.
	 * @return The playback length of the song, in seconds.
	 */
	public double getLength() {
		return this.getParseObject().getDouble("length");
	}

	/**
	 * Set the playback length of the song, in seconds.
	 * @param length The playback length of the song, in seconds.
	 */
	private void setLength(double length) {
		this.getParseObject().put("length", length);
	}
	
	/**
	 * The Spotify URI for the album which the song belongs to.
	 * @return The Spotify URI for the album which the song belongs to.
	 */
	public String getAlbumURI() {
		return this.getParseObject().getString("albumSpotifyURI");
	}
	
	/**
	 * Set the Spotify URI for the album which the song belongs to.
	 * @param albumUri The Spotify URI which the album belongs to.
	 */
	private void setAlbumURI(String albumUri) {
		this.getParseObject().put("albumSpotifyURI", albumUri);
	}
	
	/**
	 * The popularity of the song.
	 * 
	 * Only available when the song has been created from Spotify JSON metadata and not persisted to Parse.
	 * 
	 * @return The popularity of the song. 
	 */
	public double getPopularity() {
		return popularity;
	}

	/**
	 * Set the popularity of the song.
	 * Only available when the song has been created from Spotify JSON metadata and not persisted to Parse.
	 * 
	 * @param popularity The popularity of the song.
	 */
	private void setPopularity(double popularity) {
		this.popularity = popularity;
	}
	
	
	// Equality, hashing and such
	@Override
	public String toString() {
		return "" + this.getClass().getCanonicalName() + " { identifier = '"+ this.getParseObject().getObjectId() +"'; name = '" + this.getName() + "'; Spotify URI = '"+ this.getSpotifyURI() +"'; }"; 
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Song)) return false;
		
		 Song lhs = (Song) o;
		 return (this.getSpotifyURI() != null &&
				 lhs.getSpotifyURI() != null &&
				 this.getSpotifyURI().equals(lhs.getSpotifyURI()));
	}
	
	@Override
	public int hashCode() {
		int hash = 23;
		hash = 37 * hash + this.getSpotifyURI().hashCode();
		return hash;
	}
	
	// Private helpers
	/**
	 * The ParseObject for the Parse type which backs the Song model.
	 * @return The ParseObject for the Parse type which backs the Song model. 
	 */
	private static String getParseObjectName() {
		return "Song";
	}

	@Override
	protected String getParseObjectNameInternal() {
		return getParseObjectName();
	}

}
