package se.chalmers.hemmafesten.adapter;

import java.util.List;

import se.chalmers.hemmafesten.R;
import se.chalmers.hemmafesten.R.id;
import se.chalmers.hemmafesten.R.layout;
import se.chalmers.hemmafesten.activity.MainActivity;
import se.chalmers.hemmafesten.activity.PartyActivity;
import se.chalmers.hemmafesten.item.SavePartyItem;
import se.chalmers.hemmafesten.service.PartyService;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SavedPartiesAdapter extends ArrayAdapter<SavePartyItem> {

	private Context context;
	
	public SavedPartiesAdapter(Context context, int textViewResourceId,List<SavePartyItem> items) {
		super(context, textViewResourceId, items);
		this.context = context;
	}

	private class ViewHolder {
		 Button joinPartyButton;
	     TextView partyName;
	     TextView partyDate;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        final SavePartyItem partyItem = getItem(position);
 
        LayoutInflater myInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = myInflater.inflate(R.layout.saved_party_list_item, null);
            holder = new ViewHolder();
            holder.partyName = (TextView) convertView.findViewById(R.id.party_name);
            holder.partyDate = (TextView) convertView.findViewById(R.id.party_date_saved);
            holder.joinPartyButton = (Button) convertView.findViewById(R.id.re_join_saved_party);

            convertView.setTag(holder);
        } else
        
        holder = (ViewHolder) convertView.getTag();
 
			holder.partyName.setText(partyItem.getPartyName());
	        holder.partyDate.setText(partyItem.getDate());
        
	        
	        holder.joinPartyButton.setOnClickListener(
        		new Button.OnClickListener() {  
        	        public void onClick(View v)
        	            {
        	        	/*
        	        	CharSequence text = "Soon you will be able to join saved parties";
        	        	int duration = Toast.LENGTH_SHORT;

        	        	Toast toast = Toast.makeText(context, text, duration);
        	        	toast.show();*/
        	        	
        	        	
        	           }
        	         });       

        
        
        return convertView;
    }
	
}
