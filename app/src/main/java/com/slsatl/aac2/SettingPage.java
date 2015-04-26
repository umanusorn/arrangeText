package com.slsatl.aac2;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

//import com.test.R;

/**
 * A list view example where the data for the list comes from an array of
 * strings.
 */
public class SettingPage extends ListActivity {
	protected static final int RESET_ID = 0;
	protected static final int DELETE_ID = 1;
	protected static final String TAG = null;
	ListView lv;
	long selectID;

	AlertDialog alert;
	boolean checkLangchange = false;
	
	
	String noUpdate_titleDialog = "No update";
	String noUpdate_contentDialog = "No update";
	String Error_titleDialog = "Error occur";
	String Error_contentDialog = "Connection error";
	String update_titleDialog = "Do you want to update to the lastest version?";
	String update_contentDialog = "Software will be stored on SDCARD at \"download\" directory...";
	


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle( R.string.ACT_TITLE_SETTING);
		Resources res = getResources();
		String[] settingMenu = res.getStringArray(R.array.SETTING_MENU);
		// Use an existing ListAdapter that will map an array
		// of strings to TextViews
	
		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, settingMenu));
		getListView().setTextFilterEnabled(true);
		registerForContextMenu(getListView());

		lv = getListView();

		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				long select = lv.getItemIdAtPosition((position));
				if (select == 0){
					try {
						CategorySettingPage.categoryShow = ComposePage.queryCategory(2);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            	launchCategory();
	            	onPause();
				}
				if (select == 1) {
					launchLangSelect();
				}
				if (select == 2) {
					launchUpdateProcedure();
				}
			}
		});
	}

	public void launchCategory() {
        Intent i = new Intent(this, CategorySettingPage.class);
        startActivity(i);
    }
	
	public void launchUpdateProcedure() {
		final UpdateApp upp = new UpdateApp(SettingPage.this);
        final String status = upp.checkVersion();
        String TH_reply=upp.getThaiReply();
        String ENG_reply=upp.getEngReply();
        
        Log.d("check",status);
        
        if(status.equals("0")){
        	String OK = getString(R.string.OK);
			AlertDialog alertDialog = new AlertDialog.Builder(SettingPage.this).create();
			alertDialog.setTitle(noUpdate_titleDialog);
			alertDialog.setMessage(ENG_reply+":"+TH_reply);
			alertDialog.setButton(OK , new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	 
	          } });
			alertDialog.show();  	
        }else if(status.equals("1")){
			AlertDialog.Builder builder = new AlertDialog.Builder(SettingPage.this);
			builder.setMessage(update_contentDialog)
		       .setCancelable(false)
		       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   String result = upp.downloadApp();
		           }
		       })
		       .setNegativeButton("No", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		           }
		       });
			AlertDialog alert = builder.create();
			alert.setTitle(update_titleDialog);
			alert.show();   	
        }
        else{
        	String OK = getString(R.string.OK);
			AlertDialog alertDialog = new AlertDialog.Builder(SettingPage.this).create();
			alertDialog.setTitle(Error_titleDialog);
			alertDialog.setMessage(Error_contentDialog);
			alertDialog.setButton(OK , new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	 
	          } });
	    
			alertDialog.show();	
        }
	}

	
	public void launchLangSelect() {
		Resources res = getResources();
		final String[] langOption = res.getStringArray(R.array.LANG_OPTION);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.SET_LANG);/* UI caution */
		builder.setSingleChoiceItems(langOption, -1,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						checkLangchange = true;
						Toast.makeText(getApplicationContext(),
								langOption[item], Toast.LENGTH_SHORT).show();
						if (item == 0) {
							Keeper.locale = new Locale("th_TH");
							Locale.setDefault(Keeper.locale);
							Configuration config = new Configuration();
							config.locale = Keeper.locale;
							getBaseContext().getResources()
									.updateConfiguration(
											config,
											getBaseContext().getResources()
													.getDisplayMetrics());
							alert.dismiss();
							finish();
							Keeper.selected = new Vector <VocabSelected>();
							checkLangchange = true;
						}
						if (item == 1) {
							
							Keeper.locale = Locale.US; // check wheter uk
							Locale.setDefault(Keeper.locale);
							Configuration config = new Configuration();
							config.locale = Keeper.locale;
							getBaseContext().getResources()
									.updateConfiguration(
											config,
											getBaseContext().getResources()
													.getDisplayMetrics());
							alert.dismiss();
							finish();
							Keeper.selected = new Vector <VocabSelected>();
							checkLangchange = true;
						}

					}

				});

		alert = builder.create();
		alert.show();
		// method for changing language for interface

	}

	@Override
	public void onBackPressed() {
		finish();
		if (checkLangchange == true) {
			Intent i = new Intent(SettingPage.this, MainPage.class);
			startActivity(i);
		}

	}

	public void launchReset() {
		String Yes = getString(R.string.YES);
		String No = getString(R.string.NO);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setMessage(R.string.RESET_DATA_QUES);
		builder.setTitle(R.string.RESET_DATA).setCancelable(false)
				.setPositiveButton(Yes, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						new RestoreDefault().execute();
						
					}
				}).setNegativeButton(No, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();

	}
    private static void unZipFile(String zipname) throws IOException {
    	ZipFile zipFile;
    	zipFile = new ZipFile("/sdcard/AAConAndroid/"+ zipname);
    	Enumeration<?> entries = zipFile.entries();
    	while (entries.hasMoreElements()) {
    	ZipEntry entry = (ZipEntry) entries.nextElement();
    	// form individual files zipped
    	CopyInputStream(zipFile.getInputStream(entry), entry.getSize(), entry.getName());
    	}
    	zipFile.close();
    }
    private  static boolean CopyInputStream(InputStream in, long in_size, String outname) throws IOException {
    	byte[] buffer = new byte[1024];
    	int len;
    	DataOutputStream out = null;
    	 
    	try {
    	File f = new File("/sdcard/AAConAndroid/"+outname);
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
    	} catch (Exception ex) {
    	return false;
    	}
    	return true;
    	}

	public class RestoreDefault extends AsyncTask<String, Void, Void> {
		private final ProgressDialog dialog = new ProgressDialog(SettingPage.this);

		// can use UI thread here
		protected void onPreExecute() {

			this.dialog.setMessage(getString(R.string.RESTORING));
			this.dialog.show();
		}

		// automatically done on worker thread (separate from UI thread)
		protected Void doInBackground(final String... args) {
			//
			Keeper.myDB.delete("vocabulary",
					"isDefault = 'c' OR isDefault = 'l'", null);

		    File folder = new File("sdcard/AAConAndroid");
	        File[] listOfFiles = folder.listFiles();
	        for (int i = 0; i < listOfFiles.length; i++) {
	          if (listOfFiles[i].isFile()&&!listOfFiles[i].getName().equals("tester.zip")) {
	        	  listOfFiles[i].delete();
	          } 
	        }
	        
	     try {
			unZipFile("tester.zip");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Keeper.selected = new Vector <VocabSelected>();
			return null;
		}

		// can use UI thread here
		protected void onPostExecute(final Void unused) {

			if (this.dialog.isShowing()) {
				this.dialog.dismiss();
			}
		
		}
	}
}