package se.chalmers.hemmafesten.adapter;


import java.util.List;

import org.json.JSONException;

import se.chalmers.hemmafesten.R;
import se.chalmers.hemmafesten.item.SearchSongItem;
import se.chalmers.hemmafesten.model.Party;
import se.chalmers.hemmafesten.model.Song;
import se.chalmers.hemmafesten.service.PartyController;
import se.chalmers.hemmafesten.service.PartyService;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;

public class SearchSongAdapter extends ArrayAdapter<SearchSongItem> {

	private Context context;
	
	/**
	 * creates a new searchsongadapter for a result in search
	 * @param context
	 * @param textViewResourceId
	 * @param items
	 */
	public SearchSongAdapter(Context context, int textViewResourceId,List<SearchSongItem> items) {
		super(context, textViewResourceId, items);
		this.context = context;
	}

	/**
	 * holder for the view
	 * @author AnteKioksy
	 *
	 */
	private class ViewHolder {
		 Button button;
	     TextView song;
	     TextView artist;
	}
	

	/**
	 * returns a searchresult item in listview
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        final SearchSongItem songItem = getItem(position);
 
        LayoutInflater myInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = myInflater.inflate(R.layout.search_song_list_item, null);
            holder = new ViewHolder();
            holder.song = (TextView) convertView.findViewById(R.id.song);
            holder.artist = (TextView) convertView.findViewById(R.id.artist);
            holder.button = (Button) convertView.findViewById(R.id.button1);
            

            convertView.setTag(holder);
        } else
        
        holder = (ViewHolder) convertView.getTag();
 
        try {
			holder.song.setText(songItem.getName());
	        holder.artist.setText(songItem.getArtist());
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        holder.button.setOnClickListener(
        		new Button.OnClickListener() {  
        	        public void onClick(View v)
        	            {
        	        	
        	        	PartyController pc = PartyService.getPartyController();
        	        	
        	        	try {
            	        	Party party = Party.getParty(pc.getAccessCode());
            	        	
        	        		Song song = Song.createSongWithSpotifyJSON(songItem.getJson());
        	        		
        	        		party.addSong(song);
            	        	party.save();

        	        		
        	        	} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
        	        	
        	        	CharSequence text = "Song added to party";
        	        	int duration = Toast.LENGTH_SHORT;

        	        	Toast toast = Toast.makeText(context, text, duration);
        	        	toast.show();
        	            }
        	         });
        
        
        
        return convertView;
    }
}
	
