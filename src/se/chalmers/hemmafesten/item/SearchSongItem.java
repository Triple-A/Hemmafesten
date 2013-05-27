package se.chalmers.hemmafesten.item;

import org.json.JSONException;
import org.json.JSONObject;

/*
 * This class saves some stuff for presenting search result on the screen
 * (spotify songs)
 * @author ante
 */
public class SearchSongItem {
	
	/*
	private String name;
	private String artist;
	private String uri;
	private String album;
	private ImageView image;
	private double length;
	*/
	private JSONObject json;
	
	/**
	 * create a new item for a search result item
	 * @param song
	 */
	public SearchSongItem(JSONObject song){
		this.json = song;
	}
	
	/**
	 * get length of song
	 * @return double length of song
	 * @throws JSONException
	 */
	public double getLength() throws JSONException {
			return json.getDouble("length");
	}

	/**
	 * get spotify uri of a song
	 * @return spotify uri as string
	 * @throws JSONException
	 */
	public String getUri() throws JSONException {
		return json.getString("href");
	}
	
	/**
	 * get the name of the artist
	 * @return artist name as string
	 * @throws JSONException
	 */
	public String getArtist() throws JSONException {
		return json.getJSONArray("artists").getJSONObject(0).getString("name");
	}

	/**
	 * get name of song
	 * @return name of song as string
	 * @throws JSONException
	 */
	public String getName() throws JSONException {
		return json.getString("name");
	}

	/**
	 * get name of album
	 * @return album name as string
	 * @throws JSONException
	 */
	public String getAlbum() throws JSONException {
		return json.getJSONObject("album").getString("name").toString();
	}

	/**
	 * get the jsonobject for the search result
	 * @return json as jsonobject
	 */
	public JSONObject getJson() {
		return json;
	}

	/**
	 * save the search result as JSON
	 * @param json
	 */
	public void setJson(JSONObject json) {
		this.json = json;
	}

}
