package com.slsatl.aac;

import java.io.IOException;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;

public class CateClickListener implements OnClickListener{
	String word;
	ComposePage composePage;
	GridView vocabView;
	int cid;
	
	public CateClickListener(String word, int cid, ComposePage composePage, GridView vocabView){
		this.word = word;
		this.cid = cid;
		this.composePage = composePage;
		this.vocabView = vocabView;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		ComposePage.currCid = cid;
		try {
			ComposePage.vocabShow = ComposePage.queryVocabs(ComposePage.currCid,1);
			vocabView.setAdapter(new VocabGridAdapter(composePage));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}