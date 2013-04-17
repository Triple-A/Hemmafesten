package se.chalmers.hemmafesten;

import se.chalmers.hemmafesten.PartyController.Status;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.support.v4.app.NavUtils;

public class JoinPartyActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_join_party);
		// Show the Up button in the action bar.
		
		setupActionBar();
	}
	
	
	/**
	 * connect to party 
	 * @param sender
	 */
	public void onConnect(View sender){
		switch(PartyController.getStatus()){
			case FREE:
				EditText et = (EditText)findViewById(R.id.accessInput);
				String accessCode = et.getText().toString();
				PartyController.joinParty(accessCode);   ////  accessCode
				if(PartyController.getStatus() == Status.GUEST){
					Log.i("JoinPartyActivity","onConnect: connected to: " + PartyController.getInstance().getParty());
				}else{
					//could not connect!!!!!!
					Log.e("JoinPartyActivity","onConnect: could not connect");
				}
				break;
			case HOST:
			case GUEST:
			case FAILED:
			
		}
	}
	
	

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.join_party, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
