package com.slsatl.aac2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
	

public class IconAndLabel {
	Drawable pic;
	String word;
	int variation = 0;
	public IconAndLabel(String picSelected, String wordSelect){
	
/*		if (wordSelect.equals("My favourites")){
			Bitmap bm = BitmapFactory.decodeResource(ttsUI.context.getResources(), R.drawable.fav);
			picPath = new BitmapDrawable(bm);
		}
		else{*/
		pic = new BitmapDrawable(decodeFile(new File(picSelected)));

		word = wordSelect;	
		
	}
	public IconAndLabel(String picSelected, String wordSelect, int variation){
		
		/*		if (wordSelect.equals("My favourites")){
					Bitmap bm = BitmapFactory.decodeResource(ttsUI.context.getResources(), R.drawable.fav);
					picPath = new BitmapDrawable(bm);
				}
				else{*/
				pic = new BitmapDrawable(decodeFile(new File(picSelected)));
				word = wordSelect;
				this.variation = variation;
				
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
}
