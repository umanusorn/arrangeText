package com.slsatl.aac2;


import android.content.ContentValues;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class LexOrderAdapter extends ArrayAdapter<LexIconAndLabel> {

static ArrayList<LexIconAndLabel> items;
LayoutInflater vi;

public LexOrderAdapter(Context context, int textViewResourceId, ArrayList<LexIconAndLabel> items) {
	super(context, textViewResourceId, items);
	LexOrderAdapter.items = items;
}

@Override
public LexIconAndLabel getItem(int arg0) {
	// TODO Auto-generated method stub
	return items.get(arg0);
}

//@Override
public View getView(int position, View convertView, ViewGroup parent) {
	View v;
	if (convertView == null) {
		vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v = vi.inflate( R.layout.lexrow, null);

	}
	else {
		v = convertView;
	}
	LexIconAndLabel o = (LexIconAndLabel) items.toArray()[position];
	TextView tt = (TextView) v.findViewById(R.id.toptext);
	TextView subTitle = (TextView) v.findViewById(R.id.subTitleText);
	TextView cidText = (TextView) v.findViewById(R.id.hiddenCid);
	TextView lidText = (TextView) v.findViewById(R.id.hiddenLid);
	ImageView vv = (ImageView) v.findViewById(R.id.icon);
	ToggleButton hideToggle = (ToggleButton) v.findViewById(R.id.hideButton);
	View reorderWrapper = v.findViewById(R.id.reorderWrapper);
	ImageButton reorderUp = (ImageButton) v.findViewById(R.id.reorderUp);
	ImageButton reorderDown = (ImageButton) v.findViewById(R.id.reorderDown);
	tt.setText(o.word);
	subTitle.setText("");
	vv.setBackgroundDrawable(o.pic);
	cidText.setText(o.cid + "");
	lidText.setText(o.lid + "");
	if (o.enable == 1) {
		hideToggle.setChecked(true);
	}
	else {
		hideToggle.setChecked(false);
	}
	hideToggle.setOnClickListener(new HideLexToggleClickHandler(hideToggle, o));
	ReorderOnClickHandler reorderHandler = new ReorderOnClickHandler(position, this);
	reorderUp.setOnClickListener(reorderHandler);
	reorderDown.setOnClickListener(reorderHandler);
	if (VocabSettingPage.activityMode.equals("hide")) {
		hideToggle.setVisibility(View.VISIBLE);
		reorderWrapper.setVisibility(View.GONE);
	}
	else if (VocabSettingPage.activityMode.equals("reorder")) {
		hideToggle.setVisibility(View.GONE);
		reorderWrapper.setVisibility(View.VISIBLE);
	}
	else {
		hideToggle.setVisibility(View.GONE);
		reorderWrapper.setVisibility(View.GONE);
	}
	return v;


}

class HideLexToggleClickHandler implements View.OnClickListener {
	ToggleButton    source;
	LexIconAndLabel o;

	public HideLexToggleClickHandler(ToggleButton source, LexIconAndLabel o) {
		this.source = source;
		this.o = o;
	}

	@Override
	public void onClick(View arg0) {
		ContentValues cv = new ContentValues();
		if (source.isChecked()) {
			cv.put("enable", 1);
			o.enable = 1;
		}
		else {
			cv.put("enable", 0);
			o.enable = 0;
		}
		int numAffected = Keeper.myDB.update("NewLexicalItem", cv, "lid =" + o.lid, null);
	}

}

class ReorderOnClickHandler implements View.OnClickListener {
	int                           position;
	ArrayAdapter<LexIconAndLabel> adapter;

	private ReorderOnClickHandler(int position, ArrayAdapter<LexIconAndLabel> adapter) {
		this.position = position;
		this.adapter = adapter;
	}

	@Override
	public void onClick(View v) {
		LexIconAndLabel thisItem = (LexIconAndLabel) adapter.getItem(position);
		LexIconAndLabel otherItem;

		// TODO Auto-generated method stub
		if (v.getId() == R.id.reorderUp) {
			if (position == 0) {
				Toast.makeText(getContext(), R.string.TOAST_TOP_OF_LIST, Toast.LENGTH_SHORT).show();
			}
			else {
				otherItem = (LexIconAndLabel) adapter.getItem(position - 1);
				adapter.remove(thisItem);
				adapter.remove(otherItem);
				adapter.insert(thisItem, position - 1);
				adapter.insert(otherItem, position);
				VocabSettingPage.orderChanged = true;
				VocabSettingPage.manageTopPanel();
			}
		}
		else if (v.getId() == R.id.reorderDown) {
			if (position == (adapter.getCount() - 1)) {
				Toast.makeText(getContext(), R.string.TOAST_END_OF_LIST, Toast.LENGTH_SHORT).show();
			}
			else {
				otherItem = (LexIconAndLabel) adapter.getItem(position + 1);
				adapter.remove(thisItem);
				adapter.remove(otherItem);
				adapter.insert(otherItem, position);
				adapter.insert(thisItem, position + 1);
				VocabSettingPage.orderChanged = true;
				VocabSettingPage.manageTopPanel();
			}
		}
	}
}

}

