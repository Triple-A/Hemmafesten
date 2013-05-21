package se.chalmers.hemmafesten.item;

import java.io.Serializable;
import java.util.Date;

public class SavePartyItem implements Serializable{

	private String partyName;
	private String accessCode;
	private String date;
	
	public SavePartyItem(String partyName, String accessCode){
		this.setPartyName(partyName);
		this.setAccessCode(accessCode);
		this.setDate(new Date().toString());
	}

	public String getPartyName() {
		return partyName;
	}

	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}

	public String getAccessCode() {
		return accessCode;
	}

	public void setAccessCode(String accessCode) {
		this.accessCode = accessCode;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}
