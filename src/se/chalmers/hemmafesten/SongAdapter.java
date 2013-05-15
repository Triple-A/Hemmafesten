package se.chalmers.hemmafesten;


import java.util.List;

import org.json.JSONException;

import com.parse.ParseException;

import se.chalmers.hemmafesten.model.Song;
import se.chalmers.hemmafesten.service.PartyController;
import se.chalmers.hemmafesten.service.PartyService;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SongAdapter extends ArrayAdapter<SongItem> {

	private Context context;
	
	public SongAdapter(Context context, int textViewResourceId,List<SongItem> items) {
		super(context, textViewResourceId, items);
		this.context = context;
	}

	private class ViewHolder {
		 Button button;
	     TextView song;
	     TextView artist;
	}
	

    @SuppressLint("SetJavaScriptEnabled")
	public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        final SongItem songItem = getItem(position);
 
        LayoutInflater myInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = myInflater.inflate(R.layout.song_list_item, null);
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
        	        	
        	        	try {
							Song song = new Song(songItem.getJson());
							song.save();
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
	
