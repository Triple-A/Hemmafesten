package se.chalmers.hemmafesten.model.callback;

import java.util.List;

import com.parse.ParseException;

public abstract class FindCallback<E extends se.chalmers.hemmafesten.model.Model> {
	public abstract void done(List<E> objects, ParseException e);
}
