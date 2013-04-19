package se.chalmers.hemmafesten;

import se.chalmers.hemmafesten.PartyService.Status;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class CreatePartyActivity extends Activity {
	
	public void onClickCreate(View sender){
			
		if(PartyService.getStatus() == Status.FREE){
				
			Intent partyIntent = new Intent(this, PartyService.class); //                          
			partyIntent.putExtra("isCreator", true);                  // 
			startService(partyIntent);                                 // Starting partyService
				
			if(PartyService.getStatus() == Status.GUEST){              // success!!
				Log.i("createPartyActivity","onClickCreate: connected to: " + PartyService.getParty());
					
				/// todo: switch screen to PartyActivity
					
			}else if(PartyService.getStatus() == Status.FAILED){       //fail
				Log.e("createPartyActivity","onClickCreate: could not create new party");
					
				//todo:  reset service and ask user to try again???
					
			}
				
		}else{
				//PartyService busy
		}
	}
	
	
	
	
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_party);
		}	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_party, menu);
		return true;
	}

}
