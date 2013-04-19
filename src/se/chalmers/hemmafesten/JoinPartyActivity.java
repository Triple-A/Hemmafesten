package se.chalmers.hemmafesten;

import se.chalmers.hemmafesten.PartyService;
import se.chalmers.hemmafesten.PartyService.Status;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.support.v4.app.NavUtils;

public class JoinPartyActivity extends Activity {

	
	/**
	 * connect to party 
	 * @param sender
	 */
	public void onClickConnect(View sender){
		
		if(PartyService.getStatus() == Status.FREE){
			
			String accessCode = this.getCodeInput();                  // gets the accessCode from input
			
			Intent partyIntent = new Intent(this, PartyService.class); //                          
			partyIntent.putExtra("isCreator", false);                  //
			partyIntent.putExtra("accessCode", accessCode);            //
			startService(partyIntent);                                 // Starting partyService
			
			if(PartyService.getStatus() == Status.GUEST){              // success!!
				Log.i("JoinPartyActivity","onClickConnect: connected to: " + PartyService.getParty().getAccessCode());
				
				/// todo: switch screen to PartyActivity
				
			}else if(PartyService.getStatus() == Status.FAILED){       //fail
				Log.e("JoinPartyActivity","onClickConnect: unable to connect to party: "+ accessCode);
				
				//todo:  reset service and ask user to try again???
				
			}
			
		}else{
			//PartyService busy
		}
	}
	
	
	private String getCodeInput(){
		EditText et = (EditText)findViewById(R.id.accessInput);  // accessCode input
		return et.getText().toString();             			// get accessCode
	}
	
///////////////////////////////////////////////////////////////////////////////////////////////


	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_join_party);
		// Show the Up button in the action bar.
		setupActionBar();
	}
	
	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	//	// Inflate the menu; this adds items to the action bar if it is present.
	//	getMenuInflater().inflate(R.menu.join_party, menu);
	//	return true;
		return super.onCreateOptionsMenu(menu);
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
