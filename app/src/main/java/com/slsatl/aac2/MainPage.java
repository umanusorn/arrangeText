package com.slsatl.aac2;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

//import com.test.R;

public class MainPage extends Activity {
PackageInfo packageInfo = null;
LinearLayout ttsView;
AlertDialog  alertDialog;
static File tempZip;
LinearLayout lockPane;
static String myTable = "vocaburaly";
static Context   context;
static Resources r;
ImageButton downloadButton, settingButton, helpButton, ttsButton;
ImageButton cateButton;
TextView    mainGuideText;
//test commit git

/*@Override
public void onDestroy(){
	Keeper.myDB.close();
}*/
public static boolean DownloadFile(String fileURL, String fileName) {
	Boolean fin = false;
	try {

		URL u = new URL(fileURL);
		HttpURLConnection c = (HttpURLConnection) u.openConnection();
		c.setRequestMethod("GET");
		c.setDoOutput(true);
		c.connect();
		File destinationFile = new File("/sdcard/AAConAndroid/" + fileName);
		InputStream in = c.getInputStream();
		BufferedOutputStream buffer = new BufferedOutputStream(new FileOutputStream(destinationFile));
		byte byt[] = new byte[1024];
		int i;

		for (long l = 0L; (i = in.read(byt)) != -1; l += i) {
			buffer.write(byt, 0, i);
		}
		   /*     for (long l = 0L; in.read(byt) != -1; l += 1 ) {
                buffer.write(byt, l, 1);
            }*/

		buffer.flush();
		in.close();
		buffer.close();
		fin = true;

	}
	catch (Exception e) {
		Log.d("Downloader", e.getMessage());
	}
	return fin;
}

/*
public void launchCategory() {
			Intent i = new Intent(this, CategoryPage.class);
			startActivity(i);

	}*/
public void launchCompose() {
	Intent i = new Intent(getApplicationContext(), ComposePage.class);
	startActivity(i);

}
    /*
    public void onDestroy(){
        super.onPause();
        cateButton.setImageResource(0);
        cateButton.setBackgroundResource(0);
        downloadButton.setImageResource(0);
        downloadButton.setBackgroundResource(0);
        helpButton.setImageResource(0);
        helpButton.setBackgroundResource(0);
        settingButton.setImageResource(0);
        settingButton.setBackgroundResource(0);
        ttsButton.setImageResource(0);
        ttsButton.setBackgroundResource(0);
        System.gc();
    }
    */

    /*
    public void onBackPressed(){
        super.onBackPressed();
        cateButton.setImageResource(0);
        cateButton.setBackgroundResource(0);
        downloadButton.setImageResource(0);
        downloadButton.setBackgroundResource(0);
        helpButton.setImageResource(0);
        helpButton.setBackgroundResource(0);
        settingButton.setImageResource(0);
        settingButton.setBackgroundResource(0);
        ttsButton.setImageResource(0);
        ttsButton.setBackgroundResource(0);
        Keeper.mainPage = null;
        System.gc();
    }
    */

public void launchValidatePage() {
	Intent i = new Intent(getApplicationContext(), ValidatePage.class);
	startActivity(i);

}

/**
 * Called when the activity is first created.
 */
@Override
public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	// get resource , context of application
	r = getResources();
	context = getApplicationContext();

	Keeper.myDbHelper = new DataBaseHelper(getBaseContext());

	try {
		Keeper.myDbHelper.createDataBase();
	}
	catch (IOException ioe) {
		throw new Error("Unable to create database");

	}
	try {
		Keeper.myDbHelper.openDataBase();
		Keeper.myDB = DataBaseHelper.myDataBase;
	}
	catch (SQLException sqle) {
		throw sqle;
	}

        /*if(!AACUtil.serialIsValidated()){
           	finish();
           	launchValidatePage();
        }*/

	Keeper.mainPage = this;
	//set default lang
	if (Keeper.locale == null) {
		Keeper.locale = Locale.US;
		//Keeper.locale = new Locale("th_TH");
	}
	setContentView( R.layout.main);

	try {
		packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
	}
	catch (Exception e) {
		e.printStackTrace();
	}

	TextView versionText = (TextView) findViewById(R.id.VersionText);
	if (packageInfo != null) {
		versionText.setText(packageInfo.versionName);
	}
	else {
		versionText.setText("Unknown Version");
	}

	// set button and text ui
	cateButton = (ImageButton) findViewById(R.id.cate_button);
	downloadButton = (ImageButton) findViewById(R.id.download_button);
	//downloadButton.setVisibility(View.GONE);
	settingButton = (ImageButton) findViewById(R.id.setting_button);
	helpButton = (ImageButton) findViewById(R.id.help_button);
	ttsButton = (ImageButton) findViewById(R.id.tts_button);
	lockPane = (LinearLayout) findViewById(R.id.mainpageAncestor);
	mainGuideText = (TextView) findViewById(R.id.main_guide_text);


	// check resources for first time installation
	File dir = new File(Environment.getExternalStorageDirectory() + "/AAConAndroid");

	if (dir.exists() && dir.isDirectory()) {
	}
	else {
		new DownloadResourceTask().execute();
		lockPane.setClickable(false);
	}


	cateButton.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v) {
			new PrepareComposePage().execute();
			lockPane.setClickable(false);
		}
	});
	downloadButton.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v) {
			launchDownload();
		}
	});

	settingButton.setOnClickListener(new OnClickListener() {

		public void onClick(View v) {
			launchSetting();

		}
	});

	helpButton.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v) {
			launchHelpPage();
		}
	});

	ttsButton.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v) {
			launchTTSPage();
		}
	});


}

@Override
public void onResume() {
	configUI();
	super.onResume();

}

public void configUI() {
	// cateButton.setText(R.string.CATAGORIES);
	//   downloadButton.setText(R.string.DOWNLOADS);
	//   settingButton.setText(R.string.SETTINGS);
	//   helpButton.setText(R.string.HELP);
	mainGuideText.setText(R.string.MAIN_GUIDE_TEXT);


}

public void launchHelpPage() {
	Intent i = new Intent(getApplicationContext(), HelpPage.class);
	HelpPage.currHelpTabId = "About";
	startActivity(i);
}

protected void launchSetting() {
	Intent i = new Intent(getApplicationContext(), SettingPage.class);
	startActivity(i);

}

protected void launchDownload() {
	String OK = getString(R.string.OK);
	String internetMS = getString(R.string.CONNECT_INT);

	if (Keeper.checkInternet()) {
		Intent i = new Intent(getApplicationContext(), DownloadPage.class);
		startActivity(i);
	}
	else {
		alertDialog = new AlertDialog.Builder(MainPage.this).create();
		alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
		alertDialog.setTitle(R.string.CONN_FAILED);
		alertDialog.setMessage(internetMS);
		alertDialog.setButton(OK, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		alertDialog.show();
	}
}

protected void launchTTSPage() {
	Intent i = new Intent(getApplicationContext(), TTSPage.class);
	startActivity(i);

}

static void unZipFile(String zipname) throws IOException {
	ZipFile zipFile;
	zipFile = new ZipFile("/sdcard/AAConAndroid/" + zipname);
	Enumeration<?> entries = zipFile.entries();
	while (entries.hasMoreElements()) {
		ZipEntry entry = (ZipEntry) entries.nextElement();
		// form individual files zipped

		CopyInputStream(zipFile.getInputStream(entry), entry.getSize(), entry.getName());
	}
	zipFile.close();
}

static boolean CopyInputStream(InputStream in, long in_size, String outname) throws IOException {
	byte[] buffer = new byte[1024];
	int len;
	DataOutputStream out = null;

	try {
		File f = new File("/sdcard/AAConAndroid/" + outname);
		long fsz = f.length();

		// !! consider to remove this for clean reinstall at upload
		if (f != null && f.isFile() && fsz == in_size) {
			return true;
		}

		out = new DataOutputStream(new FileOutputStream("/sdcard/AAConAndroid/" + outname));

		while ((len = in.read(buffer)) >= 0) {
			out.write(buffer, 0, len);
		}

		in.close();
		out.close();
	}
	catch (Exception ex) {
		return false;
	}
	return true;
}

class DownloadResourceTask extends AsyncTask<String, Void, Void> {
	private final ProgressDialog dialog = new ProgressDialog(MainPage.this);

	// automatically done on worker thread (separate from UI thread)
	protected Void doInBackground(final String... args) {
		String newFolder = "/AAConAndroid";
		String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
		File myNewFolder = new File(extStorageDirectory + newFolder);
		myNewFolder.mkdir();
		DownloadFile("http://www.sls-atl.com/?q=aac2/webservice&action=starter_pack", "starterpack.zip");
		try {
			Thread.sleep(5000);
			unZipFile("starterpack.zip");
			Thread.sleep(5000);
		}
		catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}      // can use UI thread here

	protected void onPreExecute() {

		this.dialog.setMessage(getString(R.string.DOWNLOAD_RES));
		this.dialog.show();

	}


	// can use UI thread here
	protected void onPostExecute(final Void unused) {
	/*	if (this.dialog.isShowing()) {
			this.dialog.dismiss();

		}*/


		lockPane.setClickable(true);
		// reset the output view by retrieving the new data
		// (note, this is a naive example, in the real world it might make sense
		// to have a cache of the data and just append to what is already there, or such
		// in order to cut down on expensive database operations)

	}
}

class PrepareComposePage extends AsyncTask<String, Void, Void> {
	private final ProgressDialog dialog = new ProgressDialog(MainPage.this);

	// can use UI thread here
	protected void onPreExecute() {
		this.dialog.setMessage(getString(R.string.PROGRESS_PREPARE_COMPOSE_PAGE));
	//	this.dialog.show();
	}

	// automatically done on worker thread (separate from UI thread)
	protected Void doInBackground(final String... args) {

		try {
			ComposePage.cateShow = ComposePage.queryCategory(1);
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Locale locale = Keeper.locale;
		String currLocale = locale.toString();
		if (currLocale.equals("th_th")) {
			currLocale = "th_TH";
		}
		currLocale = "th_TH";
		String[] column = {"cid"};
		Cursor
				c =
				Keeper.myDB.query(Constant.TABLE_NEW_CATE, column, "lang ='" + currLocale + "' and enable=1 ", null, null, null, "weight");

	/*	Cursor
				c =
				Keeper.myDB.query(Constant.TABLE_NEW_CATE, column,null, null, null, null,null);*/
		c.moveToFirst();

		Log.e(Constant.TABLE_NEW_CATE,c.getColumnName(0));
		Log.e(Constant.TABLE_NEW_CATE,String.valueOf( c.getCount()));

		if(c==null){
			Log.e("","cursor == null!!!!!!!!!");
		}
		ComposePage.currCid = c.getInt(c.getColumnIndex(c.getColumnName(0)));

		try {
			ComposePage.vocabShow = ComposePage.queryVocabs(ComposePage.currCid, 1);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// can use UI thread here
	protected void onPostExecute(final Void unused) {
		if (this.dialog.isShowing()) {
			this.dialog.dismiss();
		}
		lockPane.setClickable(true);
		MainPage.this.launchCompose();
	}
}

}
