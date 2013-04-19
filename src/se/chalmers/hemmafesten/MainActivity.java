package se.chalmers.hemmafesten;

import com.parse.Parse;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Parse.initialize(this, APIKeys.ParseApplicationID(), APIKeys.ParseClientKey());
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    
    public void createParty(View sender) {
    	Intent createIntent = new Intent(this, CreatePartyActivity.class);
    	startActivity(createIntent);
    }
    
    
    public void joinParty(View sender) {
    	Intent joinIntent = new Intent(this, JoinPartyActivity.class);
    	startActivity(joinIntent);
    }
}
