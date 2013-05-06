package se.chalmers.hemmafesten.model.callback;

import com.parse.ParseException;

public abstract class GetCallback {
	public abstract void done(se.chalmers.hemmafesten.model.Model object, ParseException e);
}
