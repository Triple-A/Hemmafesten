package se.chalmers.hemmafesten;

import com.parse.Parse;

public class Application extends android.app.Application {

	@Override
	public void onCreate() {
		super.onCreate();
		Parse.initialize(this, APIKeys.ParseApplicationID(), APIKeys.ParseClientKey());
	}
}
