package se.chalmers.hemmafesten.model;

import com.parse.ParseException;
import com.parse.ParseObject;

/**
 * Represent an abstract model class which wraps a ParseObject and uses that
 * as its backend store.
 * 
 * By default two model objects are considered equal if their backing
 * ParseObjects are equal. 
 */
public abstract class Model {
	private ParseObject parseObject;
	
	/**
	 * Creates a new model with an empty ParseObject with the name returned by
	 * `getParseObjectNameInternal()`.
	 * 
	 * @see Model(ParseObject)
	 * @see getParseObjectNameInternal()
	 */
	public Model() {
		this((ParseObject)null);
	}
	
	/**
	 * Create a new model backed by the given ParseObject instance.
	 * 
	 * The ParseObject instance must be of the type associated with the model.
	 * 
	 * @param parseObject The ParseObject instance which should back the model. May be `null` in which case a new ParseObject instance will be created for the class with the name returned by `getParseObjectNameInternal()`.
	 * 
	 * @see getParseObjectNameInternal()
	 */
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
	
	/**
	 * The ParseObject instance which backs the model instance.
	 * @return The ParseObject instance which back the model instance.
	 */
	public ParseObject getParseObject() {
		return this.parseObject;
	}
	
	/**
	 * Set the ParseObject instance used for this model.
	 * @param parseObject The new ParseObject instance. 
	 * @protected
	 */
	protected void setParseObject(ParseObject parseObject) {
		this.parseObject = parseObject;
	}
	
	// Saving
	/**
	 * Save the model to the Parse backend.
	 * 
	 * Runs synchronously on the calling thread.
	 * 
	 * @throws ParseException If the save was unsuccessful (e.g. no network connection).
	 * 
	 * @see com.parse.ParseObject.save()
	 */
	public void save() throws ParseException {
		this.parseObject.save();
	}
	
	/**
	 * Save the model to the Parse backend in the background. 
	 * 
	 * @see com.parse.ParseObject.saveInBackground()
	 */
	public void saveInBackground() {
		this.parseObject.saveInBackground();
	}
	
	/**
	 * Eventually (i.e. when the device has a network connection) save the model to the Parse backend in the background.
	 * 
	 * @see com.parse.ParseObject.saveEventually()
	 */
	public void saveEventually() {
		this.parseObject.saveEventually();
	}
	
	// Deleting
	/**
	 * Delete the model from the Parse backend.
	 * 
	 * Runs synchronously on the calling thread.
	 * 
	 * @throws ParseException If the delete was unsuccessful (e.g no network connection).
	 * 
	 * @see com.parse.ParseObject.delete()
	 */
	public void delete() throws ParseException {
		this.parseObject.delete();
	}
	
	/**
	 * Delete the model from the Parse backend in the background. 
	 * 
	 * @see com.parse.ParseObject.deleteInBackground()
	 */
	public void deleteInBackground() {
		this.parseObject.deleteInBackground();
	}
	
	/**
	 * Eventually (i.e. when the device has a network connection) delete the model from the Parse backend in the background.
	 * 
	 * @see com.parse.ParseObject.deleteEventually()
	 */
	public void deleteEventually() {
		this.parseObject.deleteEventually();
	}
	
	// Refreshing
	/**
	 * Refresh the model from the Parse backend.
	 * 
	 * Runs synchronously on the calling thread.
	 * 
	 * @throws ParseException If the refresh was unsuccessful (e.g no network connection).
	 * 
	 * @see com.parse.ParseObject.refresh()
	 */
	public void refresh() throws ParseException {
		this.parseObject.refresh();
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
