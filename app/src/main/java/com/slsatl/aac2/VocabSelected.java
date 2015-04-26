package com.slsatl.aac2;

import android.graphics.drawable.Drawable;
	

public class VocabSelected {
	Drawable picPath;
	String word;
	String wavFile;
int pos;
	public VocabSelected(Drawable picSelect, String wordSelect,String wavSelect){
		picPath = picSelect;
		word = wordSelect;
		wavFile = wavSelect;	
		
	}
}
