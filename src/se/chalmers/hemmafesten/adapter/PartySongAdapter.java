package se.chalmers.hemmafesten.adapter;

import java.util.List;

import se.chalmers.hemmafesten.R;
import se.chalmers.hemmafesten.model.Song;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PartySongAdapter extends ArrayAdapter<Song> {

private Context context;
	
   /**
    * creates a new partysongadapter for song in party
    * @param context
    * @param textViewResourceId
    * @param items
    */
	public PartySongAdapter(Context context, int textViewResourceId,List<Song> items) {
		super(context, textViewResourceId, items);
		this.context = context;
	}

	/**
	 * holder for the view
	 * @author AnteKioksy
	 *
	 */
	private class ViewHolder {
	     TextView song;
	     TextView artist;
	}
	

	/**
	 * returns a listview item with song for the party
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        final Song song = getItem(position);
 
        LayoutInflater myInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = myInflater.inflate(R.layout.party_song_list_item, null);
            holder = new ViewHolder();
            holder.song = (TextView) convertView.findViewById(R.id.partySong);
            holder.artist = (TextView) convertView.findViewById(R.id.partyArtist);            

            convertView.setTag(holder);
        } else
        
        holder = (ViewHolder) convertView.getTag();
 
        holder.song.setText(song.getName());
		holder.artist.setText(song.getArtistName());
		if(song.getIsPlayed()==true){
			convertView.setBackgroundColor(Color.parseColor("#E8E6E6"));
		}else{
			convertView.setBackgroundColor(Color.WHITE);
		}
		
        
        return convertView;
    }
}
	

