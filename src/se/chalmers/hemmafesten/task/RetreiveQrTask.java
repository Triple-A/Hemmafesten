package se.chalmers.hemmafesten.task;

import java.io.IOException;
import java.net.URL;

import se.chalmers.hemmafesten.R;
import se.chalmers.hemmafesten.service.PartyService;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class RetreiveQrTask extends AsyncTask<Void, Void, Bitmap>{
	
	private ImageView qr_view;
	private Activity activity;
	private String accessCode;
	
	public RetreiveQrTask(Activity activity, PartyService partyService){
		this.activity = activity;
		this.accessCode = partyService.getParty().getAccessCode();
	}
	
	
	@Override
    protected void onPreExecute() {
        super.onPreExecute();
        qr_view = (ImageView) activity.findViewById(R.id.qr_icon);
    }

    protected Bitmap doInBackground(Void... arg0) {
    	try {
    		URL url = new URL("http://api.qrserver.com/v1/create-qr-code/?size=80x80&data=" + accessCode);
    		return BitmapFactory.decodeStream(url.openConnection().getInputStream());
		} catch (IOException e) {
			Log.e("RetriveQrTask", e.getMessage());
		}
    	return null;
    }
    protected void onPostExecute(Bitmap qrCode) {
    	qr_view.setImageBitmap(qrCode);
    }
}
