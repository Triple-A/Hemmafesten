package se.chalmers.hemmafesten;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

import se.chalmers.hemmafesten.activity.MainActivity;
import se.chalmers.hemmafesten.task.GetAlbumImageTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SongAdapter extends ArrayAdapter<SongItem> {

	private Context context;
	
	public SongAdapter(Context context, int textViewResourceId,List<SongItem> items) {
		super(context, textViewResourceId, items);
		this.context = context;
	}

	private class ViewHolder {
		 WebView view;
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
            
            holder.view = (WebView) convertView.findViewById(R.id.webview);

            convertView.setTag(holder);
        } else
        
        holder = (ViewHolder) convertView.getTag();
 
        holder.song.setText(songItem.getName());
        holder.artist.setText(songItem.getArtist());
        holder.button.setOnClickListener(
        		new Button.OnClickListener() {  
        	        public void onClick(View v)
        	            {
        	        	String uri = songItem.getUri();
        	           	Intent launcher = new Intent( Intent.ACTION_VIEW, Uri.parse(uri) );
        	           	v.getContext().startActivity(launcher);
        	            }
        	         });
        
        
        //holder.view.loadUrl("https://embed.spotify.com/?uri"+songItem.getUri());
        holder.view.getSettings().setJavaScriptEnabled(true);
        holder.view.setVerticalScrollBarEnabled(false);
        holder.view.setHorizontalScrollBarEnabled(false);
        holder.view.getSettings().setLoadWithOverviewMode(true);
        holder.view.getSettings().setUseWideViewPort(false);
        holder.view.setVisibility(View.VISIBLE);
        holder.view.setKeepScreenOn(true);
        holder.view.setSaveEnabled(true);
        holder.view.setPadding(0, 0, 0, 0);
        
        String customHtml = "<iframe src='https://embed.spotify.com/?uri="+songItem.getUri()+"' width='80' height='80' frameborder='0' allowtransparency='true'></iframe>";
        holder.view.loadData(customHtml, "text/html", "UTF-8");

        Log.i("spotifyuri", songItem.getUri());

        
        
        return convertView;
    }
}
	
