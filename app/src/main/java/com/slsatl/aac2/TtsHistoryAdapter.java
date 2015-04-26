package com.slsatl.aac2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class TtsHistoryAdapter extends ArrayAdapter<String> {

        static ArrayList<String> items;
        LayoutInflater vi;
        public TtsHistoryAdapter(Context context, int textViewResourceId, ArrayList <String> items) {
                super(context, textViewResourceId, items);
                TtsHistoryAdapter.items = items;
        }
        //@Override
        public View getView(int position, View convertView, ViewGroup parent) {
                View v;
                if (convertView == null) {
                    vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate( R.layout.tts_history_item, null);

                }else{
                	v = convertView;
                }
                
                String text = (String)items.toArray()[position];
                TextView tv = (TextView) v.findViewById(R.id.tv);
                tv.setText(text);
                return v;
                
                
        }
        @Override
    	public String getItem(int arg0) {
    		// TODO Auto-generated method stub
    		return items.get(arg0);
    	}
        

}

