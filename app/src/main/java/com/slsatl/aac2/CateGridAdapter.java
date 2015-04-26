package com.slsatl.aac2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

//import com.test.R;


public class CateGridAdapter extends BaseAdapter{
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
	public CateGridAdapter(Context c){
		mContext = c;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return ComposePage.cateShow.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v;
		ImageView iv;
		if(convertView==null){
			LayoutInflater li =  (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = li.inflate( R.layout.categridview, null);
		}
		else
		{
			v = convertView;
		}
		IconAndLabel a = (IconAndLabel) ComposePage.cateShow.toArray()[position];
		iv = (ImageView)v.findViewById(R.id.cate_icon_image);
		iv.setBackgroundDrawable(a.pic);
		TextView tv = (TextView)v.findViewById(R.id.cate_icon_text);
		tv.setText(a.word);
		
		
	    
		return v;
	}
	@Override
	public IconAndLabel getItem(int arg0) {
		// TODO Auto-generated method stub
		return ComposePage.cateShow.get(arg0);
	}
	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
