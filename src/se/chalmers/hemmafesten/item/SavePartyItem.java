package se.chalmers.hemmafesten.item;

import java.io.Serializable;
import java.util.Date;

public class SavePartyItem implements Serializable{

	private String partyName;
	private String accessCode;
	private String date;
	
	/**
	 * creates an item for saving a party
	 * @param partyName
	 * @param accessCode
	 */
	public SavePartyItem(String partyName, String accessCode){
		this.setPartyName(partyName);
		this.setAccessCode(accessCode);
		this.setDate(new Date().toString());
	}
	
	/**
	 * get party name
	 * @return party name
	 */
	public String getPartyName() {
		return partyName;
	}

	/**
	 * set name of the party 
	 * @param partyName 
	 */
	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}

	/**
	 * get the access code for the party
	 * @return access code
	 */
	public String getAccessCode() {
		return accessCode;
	}

	/**
	 * set accesscode for party
	 * @param accessCode
	 */
	public void setAccessCode(String accessCode) {
		this.accessCode = accessCode;
	}

	/**
	 * Date for when the party was saved
	 * @return date as string
	 */
	public String getDate() {
		return date;
	}

	/**
	 * set date for when the party was saved
	 * @param date
	 */
	public void setDate(String date) {
		this.date = date;
	}
}
