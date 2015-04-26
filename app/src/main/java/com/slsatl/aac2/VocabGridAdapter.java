package com.slsatl.aac2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

//import com.test.R;

public class VocabGridAdapter extends BaseAdapter {
Context mContext;

public static final int ACTIVITY_CREATE = 10;

public VocabGridAdapter(Context c) {
	mContext = c;
}

private Bitmap decodeFile(File f){
	try {
		//Decode image size
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(new FileInputStream(f),null,o);

		//The new size we want to scale to
		final int REQUIRED_SIZE=100;

		//Find the correct scale value. It should be the power of 2.
		int width_tmp=o.outWidth, height_tmp=o.outHeight;
		int scale=1;
		while(true){
			if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
				break;
			width_tmp/=2;
			height_tmp/=2;
			scale*=2;
		}

		//Decode with inSampleSize
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize=scale;
		return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
	} catch (FileNotFoundException e) {}
	return null;
}

@Override
public int getCount() {
	// TODO Auto-generated method stub
	return ComposePage.vocabShow.size();
}

@Override
public IconAndLabel getItem(int arg0) {
	// TODO Auto-generated method stub
	return ComposePage.vocabShow.get(arg0);
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
	ImageView imageView;
	if (convertView == null) {
		LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v = li.inflate( R.layout.vocabview, null);
	}
	else {
		v = convertView;
	}

	IconAndLabel a = (IconAndLabel) ComposePage.vocabShow.toArray()[position];
	imageView = (ImageView) v.findViewById(R.id.icon_image);

//todo set static img here
	imageView.setBackgroundDrawable ( a.pic );

//	imageView.setId ( position );
	Log.d("setSubCate",a.toString());
	TextView tv = (TextView) v.findViewById(R.id.icon_text);
	tv.setText(a.word);

	return v;
}

}
