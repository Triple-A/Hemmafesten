package se.chalmers.hemmafesten.model.callback;

import java.util.List;

import com.parse.ParseException;

public abstract class FindCallback {
	public abstract void done(List<se.chalmers.hemmafesten.model.Model> objects, ParseException e) ;
}
