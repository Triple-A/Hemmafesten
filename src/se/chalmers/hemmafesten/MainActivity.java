package se.chalmers.hemmafesten;

import se.chalmers.hemmafesten.PartyService.Status;


import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends ActionBarActivity {

    
    
    public void createParty(View sender) {
    	createPartyService(true);
    }
    
    
    public void joinParty(View sender) {
    	View joinFrame = findViewById(R.id.joinFrame);
    	
    	int visible = joinFrame.getVisibility();
    	
    	if(visible == 0){ //visible
    		joinFrame.setVisibility(8);
    	}else if(visible == 4){ // invisble
    		joinFrame.setVisibility(0);
    	}else{// 2 = gone
    		joinFrame.setVisibility(0);
    	}
    	
    }
    
    
    public void clickConnect(View sender){
    	createPartyService(false);
    }
    
    
    /**
     * Start partyService, 
     * @param isCreator true=with new party. false = connected to existing party.
     */
    private void createPartyService(boolean isCreator){
    	if(PartyService.getStatus() == Status.FREE){
			Intent partyIntent = new Intent(this, PartyService.class); //                          
			partyIntent.putExtra("isCreator", isCreator);                  //
			if(!isCreator) partyIntent.putExtra("accessCode", getCodeInput());
			startService(partyIntent);                                 // Starting partyService
		}else{
				//PartyService busy
		}
    }
    
    private String getCodeInput(){
		EditText et = (EditText)findViewById(R.id.accessInput);  // accessCode input
		return et.getText().toString();             			// get accessCode
	}
    
    
    
    ///////////////////////////////////////////////////////////////////////////////////////
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

 
}
