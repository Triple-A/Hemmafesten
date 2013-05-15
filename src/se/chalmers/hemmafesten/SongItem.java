package se.chalmers.hemmafesten;

import org.json.JSONException;
import org.json.JSONObject;

/*
 * This class saves some stuff for presenting search result on the screen
 * (spotify songs)
 * @author ante
 */
public class SongItem {
	
	/*
	private String name;
	private String artist;
	private String uri;
	private String album;
	private ImageView image;
	private double length;
	*/
	private JSONObject json;
	
	public SongItem(JSONObject song){
		this.json = song;
	}
	
	public double getLength() throws JSONException {
			return json.getDouble("length");
	}

	public String getUri() throws JSONException {
		return json.getString("href");
	}
	
	public String getArtist() throws JSONException {
		return json.getJSONArray("artists").getJSONObject(0).getString("name");
	}

	
	public String getName() throws JSONException {
		return json.getString("name");
	}

	public String getAlbum() throws JSONException {
		return json.getJSONObject("album").getString("name").toString();
	}

	public JSONObject getJson() {
		return json;
	}

	public void setJson(JSONObject json) {
		this.json = json;
	}

}
