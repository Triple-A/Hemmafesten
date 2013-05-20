package se.chalmers.hemmafesten.activity;

import se.chalmers.hemmafesten.R;
import android.os.Bundle;
import android.view.Menu;

public class SavedPartiesActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_saved_parties);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}

}
