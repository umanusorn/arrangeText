
package com.slsatl.aac;

//import IconAndLabel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import java.util.Locale;
import java.util.Vector;


import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.slsatl.aac.CategorySettingPage.SaveCatOrderHandler;
import com.slsatl.aac.MainPage.DownloadResourceTask;

//import com.test.R;

//import VocabPage.fetchThaiWavTask;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class VocabSettingPage extends ListActivity {

	Resources r;
	static int currCid;
	int delLid;
	private ArrayList<LexIconAndLabel> m_orders = null;
	private LexOrderAdapter m_adapter;
	static Vector<LexIconAndLabel> vocabShow;
	private ListView lv;
	MenuItem hideMItm, reorderMItm, helpMItm, addLexMItm;
	static String activityMode = "hide";
	static LinearLayout topPanel;
	static Button saveOrderButton;
	static boolean orderChanged = false;
	int selectedPosition;
	AdapterView <?> parentView;
	
	// For addVocab
	AlertDialog alertDialog;
	ImageView imageAdd;
	EditText getNewVocab;
	private static final int SELECT_PICTURE = 1;
	String newVocabPicName;
	Drawable shownImageVocab;
	String newVocab;
	Uri currImageURI;
	
	// For AsyncTask
	LinearLayout lockPane;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lexview);
		setTitle(R.string.ACT_TITLE_VOCAB_SETTING);
		
		lockPane  = (LinearLayout) findViewById(R.id.list);
		
		saveOrderButton = (Button) findViewById(R.id.saveOrderButton);
		topPanel = (LinearLayout) findViewById(R.id.topPanel);
		
		saveOrderButton.setOnClickListener(new SaveLexOrderHandler(this));
		
		manageTopPanel();
		
		m_orders = new ArrayList<LexIconAndLabel>(vocabShow);
		if (lv == null) {
			this.m_adapter = new LexOrderAdapter(this, R.layout.lexrow,
					m_orders);
			configUI();
			r = getResources();
			registerForContextMenu(lv);
		}
	}

	public void configUI() {
		setListAdapter(this.m_adapter);
		lv = this.getListView();
	}
	
	static void manageTopPanel(){
		if(!activityMode.equals("reorder")){
			topPanel.setVisibility(View.GONE);
		}else{
			topPanel.setVisibility(View.VISIBLE);
		}
		if(!orderChanged){
			saveOrderButton.setEnabled(false);
		}else{
			saveOrderButton.setEnabled(true);
		}
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		if (v == lv) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			Resources res = getResources();
			String[] menuItems = res.getStringArray(R.array.VOCAB_MENU);

			for (int i = 0; i < menuItems.length; i++) {
				menu.add(Menu.NONE, i, i, menuItems[i]);
			}
		}
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		int menuItemIndex = item.getItemId();
		View current = info.targetView;
		TextView cidTextView = (TextView) current.findViewById(R.id.hiddenCid);
		TextView lidTextView = (TextView) current.findViewById(R.id.hiddenLid);
		delLid = Integer.parseInt(lidTextView.getText().toString());
		currCid = Integer.parseInt(cidTextView.getText().toString());
		
		if (menuItemIndex == 0) {
			String Yes = getString(R.string.YES);
			String No = getString(R.string.NO);
			String delCateQues = getString(R.string.DEL_VOCAB_QUES);
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setIcon(android.R.drawable.ic_dialog_alert);
			builder.setMessage(delCateQues);
			builder.setTitle(R.string.DEL_CATE)
					.setCancelable(false)
					.setPositiveButton(Yes,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// delete category and vocab in that cat
									Keeper.myDB.delete("lexicalItem",
											"lid = " + delLid, null);
									finish();
									vocabShow = null;
									Keeper.selected = new Vector <VocabSelected>();
									try {
										vocabShow = ComposePage.queryVocabs(currCid,2);
									} catch (IOException e) {
										e.printStackTrace();
									}
									launchVocabSettingPage();
								}
							})
					.setNegativeButton(No,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});
			AlertDialog alert = builder.create();
			alert.show();
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		int hideBtnId = Menu.FIRST;
		int reorderBtnId = Menu.FIRST+1;
		int addBtnId = Menu.FIRST+2;
		int helpBtnId = Menu.FIRST+3;
		
		hideMItm = menu.add(Menu.NONE, hideBtnId, hideBtnId, getString(R.string.MENU_ITEM_HIDE));
		hideMItm.setIcon(android.R.drawable.ic_menu_view);
		
		reorderMItm = menu.add(Menu.NONE, reorderBtnId, reorderBtnId, getString(R.string.MENU_ITEM_REORDER));
		reorderMItm.setIcon(android.R.drawable.ic_menu_sort_by_size);
		
		addLexMItm = menu.add(Menu.NONE, addBtnId, addBtnId, getString(R.string.ADD_NEW_VOCAB));
		addLexMItm.setIcon(android.R.drawable.ic_menu_add);

		helpMItm = menu.add(Menu.NONE, helpBtnId, helpBtnId, getString(R.string.HELP));
		helpMItm.setIcon(android.R.drawable.ic_menu_help);

		return super.onCreateOptionsMenu(menu);

	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu){
		if(activityMode.equals("hide")){
			hideMItm.setEnabled(false).setVisible(false);
			reorderMItm.setEnabled(true).setVisible(true);
		}else if(activityMode.equals("reorder")){
			hideMItm.setEnabled(true).setVisible(true);
			reorderMItm.setEnabled(false).setVisible(false);	
		}else{
			return false;
		}
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case Menu.FIRST: // hide
			activityMode = "hide";
			manageTopPanel();
			m_adapter.notifyDataSetChanged();
			break;
		case Menu.FIRST+1: // reorder
			activityMode = "reorder";
			manageTopPanel();
			m_adapter.notifyDataSetChanged();
			break;
			
		case Menu.FIRST+2: // add vocab
			launchAddVocab();
			break;

		case Menu.FIRST + 3: // help
			launchHelpPage();
			break;
		}
		return true;
	}
	
	@Override
	public void onBackPressed(){
		if(orderChanged){
			orderDiscardedConfirm("backPressed");
		}else{
			super.onBackPressed();
		}
	}
	public void doBackPressed(){
		super.onBackPressed();
	}
	
	public void orderDiscardedConfirm(String mode){
		String yes = getString(R.string.YES);
		String no = getString(R.string.NO);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setMessage(R.string.TOAST_ORDER_DISCARDED);
		builder.setTitle(R.string.DIALOG_TITLE_CONFIRM);
		builder.setCancelable(false);
		if(mode.equals("backPressed")){
			builder.setPositiveButton(yes,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int id) {
									VocabSettingPage.orderChanged = false;
									VocabSettingPage.this.doBackPressed();
								}
						});
		}else if(mode.equals("saveNewVocab")){
			builder.setPositiveButton(yes,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int id) {
								VocabSettingPage.orderChanged = false;
								new AddingVocabTask().execute();
								lockPane.setClickable(false);
							}
					});	
		}	
		builder.setNegativeButton(no,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int id) {
								dialog.cancel();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	class SaveLexOrderHandler implements View.OnClickListener{
		VocabSettingPage parent;
		public SaveLexOrderHandler(VocabSettingPage parent){
			this.parent = parent;
		}
		@Override
		public void onClick(View v) {
			saveLexOrder();
			orderChanged = false;
			manageTopPanel();
		}
		private void saveLexOrder(){
			LexIconAndLabel thisItem;
			for(int i=0;i<parent.m_adapter.getCount();i++){
				ContentValues cv = new ContentValues();
				cv.put("weight",(i+1));
				thisItem = (LexIconAndLabel)parent.m_adapter.getItem(i);
				Keeper.myDB.update("lexicalItem",cv, "lid = "+thisItem.lid, null);
			}
			Toast.makeText(parent,R.string.TOAST_ORDER_SAVED, Toast.LENGTH_SHORT).show();
		}
	}
	
	public void launchVocabSettingPage() {
		Intent i = new Intent(this, VocabSettingPage.class);
		startActivity(i);
	}
	
	public void launchAddVocab(){
			AlertDialog.Builder builder;
			Context mContext = getApplicationContext();
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(R.layout.addvocabview,
					(ViewGroup) findViewById(R.id.widget77));

			Button selPhotoText = (Button) layout
					.findViewById(R.id.addphoto_button);
			selPhotoText.setText(R.string.SEL_PHOTO);
			TextView vocabText = (TextView) layout.findViewById(R.id.vocabTag);
			vocabText.setText(R.string.VOCAB);
			Button saveText = (Button) layout.findViewById(R.id.saveadd_button);
			saveText.setText(R.string.SAVE);
			imageAdd = (ImageView) layout.findViewById(R.id.newphoto_view);
			getNewVocab = (EditText) layout.findViewById(R.id.EditText04);

			Button selPhotoButton = (Button) layout
					.findViewById(R.id.addphoto_button);
			selPhotoButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setType("image/*");
					intent.setAction(Intent.ACTION_GET_CONTENT);
					startActivityForResult(
							Intent.createChooser(intent, "Select Picture"),
							SELECT_PICTURE);
				}

			});
			saveText.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					// get vocab string
					newVocab = getNewVocab.getText().toString();
					// get pic file path
					if (!newVocab.equals("") && imageAdd.getBackground() !=null ){
						if(orderChanged){
							orderDiscardedConfirm("saveNewVocab");	
						}else{
							new AddingVocabTask().execute();
							lockPane.setClickable(false);
						}
					}else{
						Toast.makeText(getApplicationContext(),
								R.string.PLS_COMPLETE_ADD, Toast.LENGTH_SHORT).show();
					}			
				}
			});

			builder = new AlertDialog.Builder(this);
			builder.setView(layout);
			alertDialog = builder.create();
			alertDialog.setTitle(R.string.ADD_NEW_VOCAB);
			alertDialog.show();
	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == RESULT_OK) {
			if (requestCode == SELECT_PICTURE) {
				currImageURI = data.getData();
				String picPath = AACUtil.getRealPathFromURI(currImageURI,this);
				Bitmap image = BitmapFactory.decodeFile(picPath);
				shownImageVocab = new BitmapDrawable(image);
				imageAdd.setBackgroundDrawable(shownImageVocab);
			}
		}

	}
	
	public void launchHelpPage(){
		 Intent i = new Intent(this, HelpPage.class);
		 HelpPage.currHelpTabId = "Settings";
	     startActivity(i);
	}
	
	public void createRow(String savedPicPath, String newVocab) {
		ContentValues initialValues = new ContentValues();		
		initialValues.put("cid", VocabSettingPage.currCid);
		initialValues.put("lid", getMinAvailableCustomLid());
		initialValues.put("core",0);
		initialValues.put("enable",1);
		initialValues.put("tag", newVocab);
		initialValues.put("picPath", savedPicPath);
		initialValues.put("nextCid",0);
		initialValues.put("voicePath","");
		initialValues.put("weight",0);
		Keeper.myDB.insert("lexicalItem", null, initialValues);
	}
	
	public int getMinAvailableCustomLid(){
		String[] column = {"lid"};
		boolean [] lidCheck = new boolean[2000];
		Cursor c = Keeper.myDB.query("lexicalItem", column, "lid >= 10001 AND lid <= 12000", null, null,null, null);
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			int lid = c.getInt(c.getColumnIndex("lid"));
			lidCheck[lid-10001] = true;
		}
		c.close();
		int i;
		for(i=0;i<lidCheck.length;i++){
			if(!lidCheck[i]) break;
		}
		return (10001+i);
	}
	
	
	
	class AddingVocabTask extends AsyncTask<String, Void, Void> {
	    private final ProgressDialog dialog = new ProgressDialog(VocabSettingPage.this);

	    // can use UI thread here
	    protected void onPreExecute() {
	       this.dialog.setMessage(getString(R.string.PROGRESS_ADD_VOCAB));
	       this.dialog.show();     
	    }

	    // automatically done on worker thread (separate from UI thread)
	    protected Void doInBackground(final String... args) {
	    	String newCoveredPicPath = AACUtil.getRealPathFromURI(currImageURI,VocabSettingPage.this);
			//String newCoveredPicFullName = new File(newCoveredPicPath).getName();		
			Locale locale = Keeper.locale;
			String currLocale = locale.toString();
			if(currLocale.equals("th_th")){
	    	      currLocale = "th_TH";
	    	}
			
			//String picExtension = newCoveredPicFullName.substring(newCoveredPicFullName.lastIndexOf("."));
			String newPicPath = String.valueOf(System.currentTimeMillis())+".jpg";
			
			createRow(newPicPath, newVocab);
			
			try {
				AACUtil.saveImageToAAC(newCoveredPicPath,
					"sdcard/AAConAndroid/" + newPicPath);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
	    }

	    // can use UI thread here
	    protected void onPostExecute(final Void unused) {
	       if (this.dialog.isShowing()) {
	          this.dialog.dismiss();
	       }
	       alertDialog.dismiss();
	       lockPane.setClickable(true);
	       Intent i = getIntent();
	       finish();
	       
	       try {
				VocabSettingPage.vocabShow = ComposePage.queryVocabs(currCid,2);
				startActivity(i);
			} catch (IOException e) {
				e.printStackTrace();
			}	
	    }
	 }
	
}