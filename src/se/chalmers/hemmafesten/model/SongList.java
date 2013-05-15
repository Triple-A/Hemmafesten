package se.chalmers.hemmafesten.model;

import java.util.LinkedList;

public class SongList {
	
	private LinkedList<Song> list;
	private String accessCode;
	private Party party;
	
	
	
	
	public SongList(String accessCode) {
		super();
		this.accessCode = accessCode;
		list = new LinkedList<Song>();
	}
	
	public LinkedList<Song> getList() {
		return list;
	}
	public String getAccessCode() {
		return accessCode;
	}
	
	public void addSong(Song song){
		list.add(song);
	}

	public void update() {
		for(Song s : list){
			
		}
		
	}
	
	
	
	

}
