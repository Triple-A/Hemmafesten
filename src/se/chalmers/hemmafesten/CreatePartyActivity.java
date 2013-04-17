package se.chalmers.hemmafesten;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class CreatePartyActivity extends Activity {
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_party);
		PartyController.createParty();
		PartyController pc = PartyController.getInstance();
		
		String string = getString(R.string.Connection_code_name, PartyController.getInstance().getAccessCode());
	}	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_party, menu);
		return true;
	}

}
