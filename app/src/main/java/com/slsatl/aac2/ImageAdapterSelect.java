package com.slsatl.aac2;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Vector;

//import com.test.R;


public class ImageAdapterSelect extends BaseAdapter {
Context mContext;

/*   private Integer[] mThumbIds = {
    		R.drawable.apple, R.drawable.bread,
            R.drawable.chicken
    };*/
	/*Vocab[] mThumbIds;
	static Vector<Vocab1> temp;
	public static  Vector<Vocab1> getVector(){
		temp = new Vector<Vocab1> ();
		temp.add(new Vocab1(R.drawable.bread, "Bread"));
		temp.add(new Vocab1(R.drawable.water, "Water"));
		temp.add(new Vocab1(R.drawable.apple, "Apple"));
		return temp;
	}*/

Vector<VocabSelected> temp = Keeper.selected;


public static final int ACTIVITY_CREATE = 10;

public ImageAdapterSelect(Context c) {
	mContext = c;
}

@Override
public int getCount() {
	// TODO Auto-generated method stub
	return temp.size();
}

@Override
public Object getItem(int arg0) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public long getItemId(int arg0) {
	// TODO Auto-generated method stub
	return 0;
}

@Override
public View getView(int position, View convertView, ViewGroup parent) {
	// TODO Auto-generated method stub
	View v;
	ImageView iv;
	if (convertView == null) {
		LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v = li.inflate( R.layout.vocabview, null);


	}
	else {
		v = convertView;
	}
	VocabSelected a = (VocabSelected) temp.toArray()[position];
	iv = (ImageView) v.findViewById(R.id.icon_image);
	iv.setBackgroundDrawable(a.picPath);
	TextView tv = (TextView) v.findViewById(R.id.icon_text);
	tv.setText(a.word);

	return v;
}

}
