package se.chalmers.hemmafesten.task;

import java.io.InputStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.renderscript.Element;
import android.util.Log;
import android.widget.ImageView;

public class GetAlbumImageTask extends AsyncTask<String,Void,String>{

	/**
	 * get source for album and save it as a string
	 */
	@Override
	protected String doInBackground(String... urls) {
        String url = urls[0];
        try {
        	Document doc = Jsoup.connect(url).get();
        	
        	org.jsoup.nodes.Element img = doc.select("img").first();
        	
        	String imageUrl = img.absUrl("src");
        	        	
        	return imageUrl;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "No image found";
    }

	protected void onPostExecute(String url) {
    }
}
