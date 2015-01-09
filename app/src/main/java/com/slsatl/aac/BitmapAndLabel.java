package com.slsatl.aac;




import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
	

public class BitmapAndLabel {
	Drawable showPic;
	String showWord;
	public  BitmapAndLabel(Bitmap picSelected, String wordSelect){
	
		
		showPic = new BitmapDrawable(picSelected);
		showWord = wordSelect;
		
		
	}

}
