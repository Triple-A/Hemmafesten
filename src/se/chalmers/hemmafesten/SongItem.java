package se.chalmers.hemmafesten;

import android.graphics.Bitmap;
import android.widget.ImageView;

/*
 * This class saves some stuff for presenting search result on the screen
 * (spotify songs)
 * @author ante
 */
public class SongItem {
	
	private String name;
	private String artist;
	private String uri;
	private String album;
	//kanske längre fram
	private ImageView image;
	private double length;
	
	public SongItem(String name, String artist, String uri, String album, double length){
		this.name = name;
		this.artist = artist;
		this.uri = uri;
		this.album = album;
		this.length = length;
	}
	
	public double getLength() {
		return length;
	}
	public void setLength(double length) {
		this.length = length;
	}
	public ImageView getImage() {
		return image;
	}
	public void setImage(ImageView image) {
		this.image = image;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

}
