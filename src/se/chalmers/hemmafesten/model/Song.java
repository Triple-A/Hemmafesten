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
	private String albumSpotifyUri;
	
	
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
	public static void getSongBySpotifyURIAsync(String spotifyUri, final se.chalmers.hemmafesten.model.callback.FindCallback callback) {
		songQueryBySpotifyURI(spotifyUri).findInBackground(new com.parse.FindCallback() {
			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				List<Model> songs = new LinkedList<Model>();
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
	public static Song getSongBySpotifyURI(String spotifyUri) {
		Song song = null;
		List<ParseObject> songs = null;
		try { songs = songQueryBySpotifyURI(spotifyUri).find();	}
		catch (ParseException e) { e.printStackTrace(); }
		
		if (songs != null && songs.size() > 0) {
			song = new Song(songs.get(0));
		}
		
		return song;
	}

	/**
	 * Synchronously tries to get a song for the given JSON object and if it could not be found a new song will be created.
	 * @param jsonObject A Spotify JSON object.
	 * @return Returns the found user object or the newly created.
	 * @throws JSONException
	 */
	public static Song getOrCreateSong(JSONObject jsonObject) throws JSONException {
		String spotifyUri = jsonObject.getString(SPOTIFY_JSON_SONG_URI_KEY);
		
		Song song = getSongBySpotifyURI(spotifyUri);
		if (song == null) {
			song = new Song();
		}
		
		song.updateWithSpotifyJSONObject(jsonObject);
		return song;
	}
	
	/**
	 * Asynchronously tries to get a song for the given JSON object and if it could not be found a new song will be created.
	 * @param jsonObject A Spotify JSON object.
	 * @param callback The callback which is called when the user object has been fetched or created.
	 * @throws JSONException
	 */
	public static void getOrCreateSongAsync(final JSONObject jsonObject, final se.chalmers.hemmafesten.model.callback.GetCallback callback) throws JSONException {
		String spotifyUri = jsonObject.getString(SPOTIFY_JSON_SONG_URI_KEY);
		ParseQuery query = songQueryBySpotifyURI(spotifyUri);
		query.findInBackground(new com.parse.FindCallback() {
			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				ParseObject parseObject = (objects.size() > 0 ? objects.get(0) : null);
				Song song = new Song(parseObject);
				
				try { song.updateWithSpotifyJSONObject(jsonObject); }
				catch (JSONException e1) { e1.printStackTrace(); }
				
				callback.done(song, e);
			}
		});
	}
	
	
	// Parse queries
	private static ParseQuery songQueryBySpotifyURI(String spotifyUri) {
		ParseQuery query = new ParseQuery(getParseObjectName());
		query.whereEqualTo("spotifyURI", spotifyUri);
		return query;
	}
	
	
	// Constructors
	public Song(JSONObject jsonObject) throws JSONException {
		this();
		this.updateWithSpotifyJSONObject(jsonObject);
	}
	
	public Song() {
		this(new ParseObject(getParseObjectName()));
	}
	
	public Song(ParseObject parseObject) {
		super(parseObject);
	}

	
	// Updating a Song
	/**
	 * Update the song object with the data in the Spotify JSON object.
	 * @param jsonObject The JSON object gotten from Spotify.
	 * @throws JSONException
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
	// Parse
	public String getName() {
		return this.getParseObject().getString("name");
	}
	
	public void setName(String name) {
		this.getParseObject().put("name", name);
	}
	
	public String getAlbumName() {
		return this.getParseObject().getString("album");
	}
	
	public void setAlbumName(String albumName) {
		this.getParseObject().put("album", albumName);
	}
	
	public String getArtistName() {
		return this.getParseObject().getString("artist");
	}
	
	public void setArtistName(String artistName) {
		this.getParseObject().put("artist", artistName);
	}
	
	public String getSpotifyURI() {
		return this.getParseObject().getString("spotifyURI");
	}
	
	public void setSpotifyURI(String spotifyUri) {
		this.getParseObject().put("spotifyURI", spotifyUri);
	}
	
	public double getLength() {
		return this.getParseObject().getDouble("length");
	}

	public void setLength(double length) {
		this.getParseObject().put("length", length);
	}
	
	// Local
	public String getAlbumURI() {
		return this.albumSpotifyUri;
	}
	
	public void setAlbumURI(String albumUri) {
		this.albumSpotifyUri = albumUri;
	}
	
	public double getPopularity() {
		return popularity;
	}

	public void setPopularity(double popularity) {
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
	private static String getParseObjectName() {
		return "Song";
	}

	@Override
	protected String getParseObjectNameInternal() {
		return getParseObjectName();
	}
}
