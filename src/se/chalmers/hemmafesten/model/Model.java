package se.chalmers.hemmafesten.model;

import com.parse.ParseException;
import com.parse.ParseObject;

public abstract class Model {
	private ParseObject parseObject;
	
	public Model(ParseObject parseObject) {
		if (parseObject.getClassName().equals(this.getParseObjectNameInternal())) {
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
}
