
package com.slsatl.aac;

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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Vector;

public class CategorySettingPage extends ListActivity {

	private ArrayList<CatIconAndLabel> m_orders = null;
	private CateOrderAdapter m_adapter;
	static Vector<CatIconAndLabel> categoryShow;
	Resources r;
	private ListView lv;
	String newCoveredPicName;
	static Locale THAI_locale = new Locale("th_TH");
	MenuItem hideMItm, reorderMItm, helpMItm, addCateMItm;
	String inputValue;
	static String currCategory;
	static int currCid;
	static int delCid;
	static String newCate;
	static String activityMode = "hide";
	static LinearLayout topPanel;
	static Button saveOrderButton;
	static boolean orderChanged = false;
	int selectedPosition;
	AdapterView <?> parentView;
	
	
	FileOutputStream fos;
	
	// For addVocab
	AlertDialog alertDialog;
	ImageView newImage;
	EditText getNewCate;
	private static final int SELECT_PICTURE = 1;
	String newCatePicName;
	Drawable shownImageCate;
	String newVocab;
	Uri currImageURI;
	
	// For AsyncTask
	LinearLayout lockPane;

	public static boolean checkImageFileExist(String filename){

	    File folder = new File("sdcard/AAConAndroid");
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
          if (listOfFiles[i].isFile()&&listOfFiles[i].getName().equals(filename)) {
        	return true;
          }
        }
        return false;

	}

	public void createCateRow(String savedPicPath, String newTag) {
		String currLocale = Keeper.locale.toString();
		String subtitle = getString(R.string.CUSTOM_SUBTITLE);
		if(currLocale.equals("th_th")){
    	      currLocale = "th_TH";

    	}
		ContentValues initialValues = new ContentValues();
		initialValues.put("cid", getMinAvailableCustomCid());
		initialValues.put("core",0);
		initialValues.put("subtitle",subtitle);
		initialValues.put("enable",1);
		initialValues.put("variation",0);
		initialValues.put("version",0);
		initialValues.put("weight",0);
		initialValues.put("title", newTag);
		initialValues.put("coverPath", savedPicPath);
		initialValues.put("lang", currLocale);
		initialValues.put("nextCid",0);

		Keeper.myDB.insert(Constant.TABLE_NEW_CATE, null, initialValues);
	}

	public int getMinAvailableCustomCid(){
		String[] column = {"cid"};
		boolean [] cidCheck = new boolean[2000];
		Cursor c = Keeper.myDB.query(Constant.TABLE_NEW_CATE, column, "cid >= 10001 AND cid <= 12000", null, null,null, null);
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			int cid = c.getInt(c.getColumnIndex("cid"));
			cidCheck[cid-10001] = true;
		}
		c.close();
		int i;
		for(i=0;i<cidCheck.length;i++){
			if(!cidCheck[i]) break;
		}
		return (10001+i);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cateview);
		setTitle(R.string.ACT_TITLE_CATEGORY_SETTING);

		saveOrderButton = (Button) findViewById(R.id.saveOrderButton);
		topPanel = (LinearLayout) findViewById(R.id.topPanel);

		lockPane  = (LinearLayout) findViewById(R.id.list);

		saveOrderButton.setOnClickListener(new SaveCatOrderHandler(this));

		manageTopPanel();

		m_orders = new ArrayList<CatIconAndLabel>(categoryShow);
		if (lv == null) {
			this.m_adapter = new CateOrderAdapter(this, R.layout.caterow,
					m_orders);
			configUI();
			r = getResources();
			registerForContextMenu(lv);
		}
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,int position, long id) {
				selectedPosition = position;
				parentView = parent;
				if(orderChanged){
					orderDiscardedConfirm("launchVocab");
				}else{
					launchVocabSettingPage();
				}
			}
		});
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
	public void onBackPressed(){
		if(orderChanged){
			orderDiscardedConfirm("backPressed");
		}else{
			super.onBackPressed();
		}
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

		addCateMItm = menu.add(Menu.NONE, addBtnId, addBtnId, getString(R.string.ADD_NEW_CATE));
		addCateMItm.setIcon(android.R.drawable.ic_menu_add);

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
		case Menu.FIRST+2: // add
			launchAddCategory();
			break;
		case Menu.FIRST + 3: // help
			launchHelpPage();
			break;
		}
		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		if (v == lv) {
			//AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			// (Countries[info.position]);
			Resources res = getResources();
			String[] menuItems = res.getStringArray(R.array.CATE_MENU);

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
		delCid = Integer.parseInt(cidTextView.getText().toString());

		if (menuItemIndex == 0) {
			String Yes = getString(R.string.YES);
			String No = getString(R.string.NO);
			String delCateQues = getString(R.string.DEL_CATE_QUES);
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
									Keeper.myDB.delete("NewLexicalItem",
											"cid = " + delCid, null);
									Keeper.myDB.delete("category",
											"cid = " + delCid, null);
									finish();
									categoryShow = null;
									Keeper.selected = new Vector <VocabSelected>();
									try {
										categoryShow = ComposePage.queryCategory(2);
									} catch (IOException e) {
										e.printStackTrace();
									}
									launchCategorySetting();
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
	
	public void launchCategorySetting() {
        Intent i = new Intent(this, CategorySettingPage.class);
        startActivity(i);
    }
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == RESULT_OK) {
			if (requestCode == SELECT_PICTURE) {
				currImageURI = data.getData();
				String picPath = AACUtil.getRealPathFromURI(currImageURI,this);
				Bitmap image = BitmapFactory.decodeFile(picPath);
				shownImageCate = new BitmapDrawable(image);
				newImage.setBackgroundDrawable(shownImageCate);
			}
		}

	}
	
	public void launchAddCategory(){
		AlertDialog.Builder builder;
		Context mContext = getApplicationContext();
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(LAYOUT_INFLATER_SERVICE);

		View layout = inflater.inflate(R.layout.editcatview,
				(ViewGroup) findViewById(R.id.addCateRoot));
		Button selPhotoText = (Button) layout
				.findViewById(R.id.addphoto_button);
		selPhotoText.setText(R.string.SEL_PHOTO);
		TextView vocabText = (TextView) layout.findViewById(R.id.vocabTag);
		vocabText.setText(R.string.CAT_NAME);
		Button saveText = (Button) layout.findViewById(R.id.saveadd_button);
		saveText.setText(R.string.SAVE);
		newImage = (ImageView) layout.findViewById(R.id.newphoto_view);
		getNewCate = (EditText) layout.findViewById(R.id.EditText04);

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
				newVocab = getNewCate.getText().toString();
				// get pic file path
				if (!newVocab.equals("") && newImage.getBackground() !=null ){
					if(orderChanged){
						orderDiscardedConfirm("saveNewCate");
					}else{
						new AddingCateTask().execute();
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
		alertDialog.setTitle(R.string.ADD_NEW_CATE);
		alertDialog.show();
	}

	public void launchHelpPage(){
		 Intent i = new Intent(this, HelpPage.class);
		 HelpPage.currHelpTabId = "Settings";
	     startActivity(i);
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
									CategorySettingPage.orderChanged = false;
									CategorySettingPage.this.doBackPressed();
								}
						});
		}else if(mode.equals("launchVocab")){
			builder.setPositiveButton(yes,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int id) {
								CategorySettingPage.orderChanged = false;
								CategorySettingPage.this.launchVocabSettingPage();
							}
					});
		}else if(mode.equals("saveNewCate")){
			builder.setPositiveButton(yes,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int id) {
								CategorySettingPage.orderChanged = false;
								new AddingCateTask().execute();
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
	
	public void launchVocabSettingPage() {
		CatIconAndLabel x  = (CatIconAndLabel) parentView.getItemAtPosition(selectedPosition);
		currCategory = x.word;
		currCid = x.cid;
		try {
			VocabSettingPage.vocabShow = ComposePage.queryVocabs(currCid,2);
			VocabSettingPage.currCid = currCid;
			Intent i = new Intent(this, VocabSettingPage.class);
			startActivity(i);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void doBackPressed(){
		super.onBackPressed();
	}
	
	class SaveCatOrderHandler implements View.OnClickListener{
		CategorySettingPage parent;
		public SaveCatOrderHandler(CategorySettingPage parent){
			this.parent = parent;
		}
		@Override
		public void onClick(View v) {
			saveCatOrder();
			orderChanged = false;
			manageTopPanel();
		}
		private void saveCatOrder(){
			CatIconAndLabel thisItem;
			for(int i=0;i<parent.m_adapter.getCount();i++){
				ContentValues cv = new ContentValues();
				cv.put("weight",(i+1));
				thisItem = (CatIconAndLabel)parent.m_adapter.getItem(i);
				Keeper.myDB.update("category",cv, "cid = "+thisItem.cid, null);
			}
			Toast.makeText(parent,R.string.TOAST_ORDER_SAVED, Toast.LENGTH_SHORT).show();
		}
	}
	
	class AddingCateTask extends AsyncTask<String, Void, Void> {
	    private final ProgressDialog dialog = new ProgressDialog(CategorySettingPage.this);

	    // automatically done on worker thread (separate from UI thread)
	    protected Void doInBackground(final String... args) {
	    	String newCoveredPicPath = AACUtil.getRealPathFromURI(currImageURI,CategorySettingPage.this);
			//String newCoveredPicFullName = new File(newCoveredPicPath).getName();
			Locale locale = Keeper.locale;
			String currLocale = locale.toString();
			if(currLocale.equals("th_th")){
	    	      currLocale = "th_TH";
	    	}

			//String picExtension = newCoveredPicFullName.substring(newCoveredPicFullName.lastIndexOf("."));
			String newPicPath = String.valueOf(System.currentTimeMillis())+".jpg";

			createCateRow(newPicPath, newVocab);

			try {
				AACUtil.saveImageToAAC(newCoveredPicPath,
					"sdcard/AAConAndroid/" + newPicPath);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
	    }

	    // can use UI thread here
	    protected void onPreExecute() {
	       this.dialog.setMessage(getString(R.string.PROGRESS_ADD_CATE));
	       this.dialog.show();
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
				CategorySettingPage.categoryShow = ComposePage.queryCategory(2);
				startActivity(i);
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
	 }
}
