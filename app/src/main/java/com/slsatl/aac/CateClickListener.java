package com.slsatl.aac;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.TextView;

public class CateClickListener implements OnClickListener {
String      word;
ComposePage composePage;
GridView    vocabView;
int         cid;
TextView sc1;
int position;

public CateClickListener(String word, int cid, ComposePage composePage, GridView vocabView,TextView sc1,int position) {
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
		ComposePage.vocabShow = ComposePage.queryVocabs(ComposePage.currCid, 1);
		vocabView.setAdapter(new VocabGridAdapter(composePage));

		Log.d("errrrror1","");
	sc1.setText("               SC"+position+"               ");
	}
	catch (Exception e) {
		Log.d("errrrror2","");
		e.printStackTrace();
	}
}

}
