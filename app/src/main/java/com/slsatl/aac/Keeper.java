package com.slsatl.aac;


import java.util.Locale;
import java.util.Vector;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;

public class Keeper {
	static Vector<VocabSelected> selected = new Vector <VocabSelected>();
	static SQLiteDatabase myDB = DataBaseHelper.myDataBase;
	static DataBaseHelper myDbHelper = null;
	static Locale locale = null;
	static MainPage mainPage;

	public static boolean checkInternet(){
		ConnectivityManager con=(ConnectivityManager)mainPage.getSystemService(Activity.CONNECTIVITY_SERVICE);
	    boolean wifi=con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
	    boolean internet=con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
		return (wifi||internet);
	}
	
}
