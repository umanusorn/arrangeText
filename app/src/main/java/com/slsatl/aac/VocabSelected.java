package com.slsatl.aac;

import android.graphics.drawable.Drawable;
	

public class VocabSelected {
	Drawable picPath;
	String word;
	String wavFile;
	public VocabSelected(Drawable picSelect, String wordSelect,String wavSelect){
		picPath = picSelect;
		word = wordSelect;
		wavFile = wavSelect;	
		
	}
}
