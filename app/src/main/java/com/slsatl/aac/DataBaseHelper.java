package com.slsatl.aac;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DataBaseHelper extends SQLiteOpenHelper {

//The Android's default system path of your application database.
private static String DB_PATH = "/data/data/com.slsatl.aac/databases/";

private static String DB_NAME = "aac_1_03.sqlite3";


public static SQLiteDatabase myDataBase;

private final Context myContext;

/**
 * Constructor
 * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
 *
 * @param context
 */
public DataBaseHelper(Context context) {

	super(context, DB_NAME, null, 1);
	this.myContext = context;
}

@Override
public synchronized void close() {

	if (myDataBase != null) {
		myDataBase.close();
	}

	super.close();

}

@Override
public void onCreate(SQLiteDatabase db) {

}

@Override
public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
}

/**
 * Creates a empty database on the system and rewrites it with your own database.
 */
public void createDataBase() throws IOException {


	boolean dbExist = checkDataBase();

	if (dbExist) {
		//do nothing - database already exist
	}
	else {

		//By calling this method and empty database will be created into the default system path
		//of your application so we are gonna be able to overwrite that database with our database.
		Log.e("","NoDB");
		this.getWritableDatabase();

		try {

			copyDataBase();

		}
		catch (IOException e) {

			throw new Error("Error copying database");

		}
	}

}

/**
 * Check if the database already exist to avoid re-copying the file each time you open the application.
 *
 * @return true if it exists, false if it doesn't
 */
private boolean checkDataBase() {

	SQLiteDatabase checkDB = null;

	try {
		String myPath = DB_PATH + DB_NAME;
		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
	}
	catch (SQLiteException e) {

		//database does't exist yet.

	}catch (Exception e) {
// Failed to open database '/data/data/com.slsatl.aac/databases/aac_1_03.sqlite3'.
		//	android.database.sqlite.SQLiteCantOpenDatabaseException: unknown error (code 14): Could not open database

		Log.e("","(2) open(/data/data/com.slsatl.aac/databases/aac_1_03.sqlite3)  \n\n E/SQLiteDatabase﹕ Failed to open database '/data/data/com.slsatl.aac/databases/aac_1_03.sqlite3'.");
		checkDB=null;
	}

	Log.d("chkDB data=","");

	//Log.d("chkDB data=", checkDB.toString() );
	if (checkDB != null) {
		checkDB.close();
	}

	return checkDB != null;
}

/**
 * Copies your database from your local assets-folder to the just created empty database in the
 * system folder, from where it can be accessed and handled.
 * This is done by transfering bytestream.
 */
private void copyDataBase() throws IOException {

	Log.d("cpyDB","");

	//Open your local db as the input stream
	InputStream myInput = myContext.getAssets().open(DB_NAME);

	// Path to the just created empty db
	String outFileName = DB_PATH + DB_NAME;

	//Open the empty db as the output stream
	OutputStream myOutput = new FileOutputStream(outFileName);

	//transfer bytes from the inputfile to the outputfile
	byte[] buffer = new byte[1024];
	int length;
	while ((length = myInput.read(buffer)) > 0) {
		myOutput.write(buffer, 0, length);
	}

	//Close the streams
	myOutput.flush();

	myOutput.close();
	myInput.close();

	Log.e("","done cptDB");

}

public void openDataBase() throws SQLException {

	//Open the database
	String myPath = DB_PATH + DB_NAME;
	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);

}
// Add your public helper methods to access and get content from the database.
// You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
// to you to create adapters for your views.

}
