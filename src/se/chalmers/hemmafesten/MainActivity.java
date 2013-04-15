package se.chalmers.hemmafesten;

import com.parse.Parse;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Parse.initialize(this, APIKeys.ParseApplicationID(), APIKeys.ParseClientID());
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void createParty(View sender) {
    	Intent createPartyIntent = new Intent(MainActivity.this, null);
    	MainActivity.this.startActivity(createPartyIntent);
    }
    
    public void joinParty(View sender) {
    	Intent joinPartyIntent = new Intent(MainActivity.this, null);
    	MainActivity.this.startActivity(joinPartyIntent);
    }
}
