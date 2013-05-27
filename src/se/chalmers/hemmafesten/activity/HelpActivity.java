package se.chalmers.hemmafesten.activity;

import se.chalmers.hemmafesten.R;
import android.app.ActionBar;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.Menu;
import android.widget.TextView;

public class HelpActivity extends ActionBarActivity {

	/**
	 * settings for when creating an instance of HelpActivity
	 * generates html to the view for cdata in strings
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		TextView partyHelp = (TextView) findViewById(R.id.party_help_text_view);
		String partyHelpString = getString(R.string.create_party_help);
		Spanned result = Html.fromHtml(partyHelpString);
		partyHelp.setText(result);
		
		TextView searchHelp = (TextView) findViewById(R.id.search_help_text_view);
		String searchHelpString = getString(R.string.search_help_text_view);
		Spanned searchResult = Html.fromHtml(searchHelpString);
		searchHelp.setText(searchResult);
	}

	/**
	 * adds title to actionbar and also returns actionbarsettings from 
	 * super class
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		ActionBar bar = getActionBar();
		bar.setTitle("Help");
		return super.onCreateOptionsMenu(menu);
	}

}
