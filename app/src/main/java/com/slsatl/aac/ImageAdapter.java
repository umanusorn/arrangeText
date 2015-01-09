package com.slsatl.aac;

import java.util.Vector;

//import com.test.R;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class ImageAdapter extends BaseAdapter{
	Context mContext;

/*    private Integer[] mThumbIds = {
    		R.drawable.apple, R.drawable.bread,
            R.drawable.chicken, R.drawable.coffee,
            R.drawable.egg, R.drawable.icecream,
            R.drawable.milk, R.drawable.orange,
            R.drawable.pizza,R.drawable.pork,
            R.drawable.water
           
    };*/

    
//		private Integer[] mThumbIds  ={
//				R.drawable.icecream50,R.drawable.icecream80,R.drawable.icecream100
//		};
	public static final int ACTIVITY_CREATE = 10;
	public ImageAdapter(Context c){
		mContext = c;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return VocabSettingPage.vocabShow.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v;
		ImageView iv;
		if(convertView==null){
			LayoutInflater li =  (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = li.inflate(R.layout.vocabview, null);
		

		
		}
		else
		{
			v = convertView;
		}
		IconAndLabel a = (IconAndLabel) VocabSettingPage.vocabShow.toArray()[position];
		iv = (ImageView)v.findViewById(R.id.icon_image);
		iv.setBackgroundDrawable(a.pic);
		TextView tv = (TextView)v.findViewById(R.id.icon_text);
		tv.setText(a.word);
		
		
	    
		return v;
	}
	@Override
	public IconAndLabel getItem(int arg0) {
		// TODO Auto-generated method stub
		return VocabSettingPage.vocabShow.get(arg0);
	}
	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
