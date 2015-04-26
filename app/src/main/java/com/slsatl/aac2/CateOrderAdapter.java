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

//import com.test.R;

public class CateOrderAdapter extends ArrayAdapter<CatIconAndLabel> {

static ArrayList<CatIconAndLabel> items;
LayoutInflater vi;

public CateOrderAdapter(Context context, int textViewResourceId, ArrayList<CatIconAndLabel> items) {
	super(context, textViewResourceId, items);
	CateOrderAdapter.items = items;
}

@Override
public CatIconAndLabel getItem(int arg0) {
	// TODO Auto-generated method stub
	return items.get(arg0);
}

//@Override
public View getView(int position, View convertView, ViewGroup parent) {
	View v;
	if (convertView == null) {
		vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v = vi.inflate( R.layout.caterow, null);

	}
	else {
		v = convertView;
	}
	CatIconAndLabel o = (CatIconAndLabel) items.toArray()[position];
	TextView tt = (TextView) v.findViewById(R.id.toptext);
	TextView subTitle = (TextView) v.findViewById(R.id.subTitleText);
	TextView cidText = (TextView) v.findViewById(R.id.hiddenCid);
	ImageView vv = (ImageView) v.findViewById(R.id.icon);
	ToggleButton hideToggle = (ToggleButton) v.findViewById(R.id.hideButton);
	View reorderWrapper = v.findViewById(R.id.reorderWrapper);
	ImageButton reorderUp = (ImageButton) v.findViewById(R.id.reorderUp);
	ImageButton reorderDown = (ImageButton) v.findViewById(R.id.reorderDown);
	tt.setText(o.word);
	subTitle.setText(o.subtitle);
	vv.setBackgroundDrawable(o.pic);
	cidText.setText(o.cid + "");
	if (o.enabled == 1) {
		hideToggle.setChecked(true);
	}
	else {
		hideToggle.setChecked(false);
	}
	hideToggle.setOnClickListener(new HideToggleClickHandler(hideToggle, o));
	ReorderOnClickHandler reorderHandler = new ReorderOnClickHandler(position, this);
	reorderUp.setOnClickListener(reorderHandler);
	reorderDown.setOnClickListener(reorderHandler);
	if (CategorySettingPage.activityMode.equals("hide")) {
		hideToggle.setVisibility(View.VISIBLE);
		reorderWrapper.setVisibility(View.GONE);
	}
	else if (CategorySettingPage.activityMode.equals("reorder")) {
		hideToggle.setVisibility(View.GONE);
		reorderWrapper.setVisibility(View.VISIBLE);
	}
	else {
		hideToggle.setVisibility(View.GONE);
		reorderWrapper.setVisibility(View.GONE);
	}
	return v;


}

class HideToggleClickHandler implements View.OnClickListener {
	ToggleButton    source;
	CatIconAndLabel o;

	public HideToggleClickHandler(ToggleButton source, CatIconAndLabel o) {
		this.source = source;
		this.o = o;
	}

	@Override
	public void onClick(View arg0) {
		ContentValues cv = new ContentValues();
		if (source.isChecked()) {
			cv.put("enable", 1);
			o.enabled = 1;
		}
		else {
			cv.put("enable", 0);
			o.enabled = 0;
		}
		int numAffected = Keeper.myDB.update(Constant.TABLE_NEW_CATE, cv, "cid =" + o.cid, null);
	}

}

class ReorderOnClickHandler implements View.OnClickListener {
	int                           position;
	ArrayAdapter<CatIconAndLabel> adapter;

	private ReorderOnClickHandler(int position, ArrayAdapter<CatIconAndLabel> adapter) {
		this.position = position;
		this.adapter = adapter;
	}

	@Override
	public void onClick(View v) {
		CatIconAndLabel thisItem = (CatIconAndLabel) adapter.getItem(position);
		CatIconAndLabel otherItem;

		// TODO Auto-generated method stub
		if (v.getId() == R.id.reorderUp) {
			if (position == 0) {
				Toast.makeText(getContext(), R.string.TOAST_TOP_OF_LIST, Toast.LENGTH_SHORT).show();
			}
			else {
				otherItem = (CatIconAndLabel) adapter.getItem(position - 1);
				adapter.remove(thisItem);
				adapter.remove(otherItem);
				adapter.insert(thisItem, position - 1);
				adapter.insert(otherItem, position);
				CategorySettingPage.orderChanged = true;
				CategorySettingPage.manageTopPanel();
			}
		}
		else if (v.getId() == R.id.reorderDown) {
			if (position == (adapter.getCount() - 1)) {
				Toast.makeText(getContext(), R.string.TOAST_END_OF_LIST, Toast.LENGTH_SHORT).show();
			}
			else {
				otherItem = (CatIconAndLabel) adapter.getItem(position + 1);
				adapter.remove(thisItem);
				adapter.remove(otherItem);
				adapter.insert(otherItem, position);
				adapter.insert(thisItem, position + 1);
				CategorySettingPage.orderChanged = true;
				CategorySettingPage.manageTopPanel();
			}
		}
	}
}
}

