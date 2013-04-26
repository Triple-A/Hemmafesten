package se.chalmers.hemmafesten;

import se.chalmers.hemmafesten.R;
import se.chalmers.hemmafesten.R.layout;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class SavedPartiesActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_saved_parties);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return super.onCreateOptionsMenu(menu);
	}

}
