package se.chalmers.hemmafesten.model;

import com.parse.ParseException;
import com.parse.ParseObject;

public abstract class Model {
	private ParseObject parseObject;
	
	public Model() {
		this((ParseObject)null);
	}
	
	public Model(ParseObject parseObject) {
		String parseClassName = this.getParseObjectNameInternal();
		if (parseObject == null) {
			parseObject = new ParseObject(parseClassName);
		}
		
		if (parseObject.getClassName().equals(parseClassName)) {
			this.parseObject = parseObject;
		} else {
			throw new IllegalArgumentException("Parse object must be of type " + this.getParseObjectNameInternal() + ", got: " + parseObject.getClassName());
		}
	}
	
	/**
	 * Get the Parse model class name.
	 * 
	 * @discussion Subclasses must override this method.
	 * @return The Parse model class name.
	 */
	protected abstract String getParseObjectNameInternal();
	
	public ParseObject getParseObject() {
		return this.parseObject;
	}
	
	protected void setParseObject(ParseObject parseObject) {
		this.parseObject = parseObject;
	}
	
	// Saving
	public void save() throws ParseException {
		this.parseObject.save();
	}
	
	public void saveInBackground() {
		this.parseObject.saveInBackground();
	}
	
	public void saveEventually() {
		this.parseObject.saveEventually();
	}
	
	// Equality and hashing
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Model)) return false;
	
		 Model lhs = (Model) o;
		 return this.getParseObject().equals(lhs.getParseObject());
	}
	
	@Override
	public int hashCode() {
		int hash = 23;
		hash = 37 * hash + this.getParseObject().hashCode();
		return hash;
	}
}
