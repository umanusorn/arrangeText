package com.slsatl.aac2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class ValidatePage extends Activity{
    Button submitButton;
    TextView serialBox;
    LinearLayout lockPane;
    String serial;
    String serverResponse = "";
    int responseCode = 0;
    String responseMsg = "";
    int validate = 0;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.validate_page);
        lockPane  = (LinearLayout) findViewById(R.id.validatePageRoot);
        serialBox = (TextView) findViewById(R.id.serialBox);
        submitButton = (Button)findViewById(R.id.submitButton);
        
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if(Keeper.checkInternet()){
            		new ValidateTask().execute();
            		lockPane.setClickable(false);
            	}else{
            		String OK = getString(R.string.OK);
            		String internetMS = getString(R.string.CONNECT_INT);
            		AlertDialog alertDialog = new AlertDialog.Builder(ValidatePage.this).create();
             		alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
             		alertDialog.setTitle(R.string.CONN_FAILED);
             		alertDialog.setMessage(internetMS);
             		alertDialog.setButton(OK, new DialogInterface.OnClickListener() {
             			public void onClick(DialogInterface dialog, int which) {
         
             			} });
             		alertDialog.show();
            	}
            }
        });
        
    } 
    
	public void launchMainPage() {
		if(!AACUtil.serialIsValidated()){
    		AlertDialog alertDialog = new AlertDialog.Builder(ValidatePage.this).create();
     		alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
     		alertDialog.setTitle("Unable to activate Chula AAC");
     		alertDialog.setMessage(responseMsg);
     		//alertDialog.setMessage(serverResponse);
     		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
     			public void onClick(DialogInterface dialog, int which) {
 
     			} });
     		alertDialog.show();
		}else{
			Intent i = new Intent(getApplicationContext(), MainPage.class);
			finish();
			startActivity(i);
		}
    }
    
	class ValidateTask extends AsyncTask<String, Void, Void> {
	    private final ProgressDialog dialog = new ProgressDialog(ValidatePage.this);

	    // can use UI thread here
	    protected void onPreExecute() {
	       this.dialog.setMessage(getString(R.string.CHECK_SERIAL));
	       this.dialog.show();     
	    }

	    // automatically done on worker thread (separate from UI thread)
	    protected Void doInBackground(final String... args) {
	    	serial = serialBox.getText().toString();
	    	final String android_id = Secure.getString(ValidatePage.this.getContentResolver(),Secure.ANDROID_ID);
	    	
	    	try{
	    		URL validateUrl = new URL("http://www.sls-atl.com/?q=aac2/webservice&action=validate&serial="+serial+"&id="+android_id);
	    		BufferedReader in = new BufferedReader(
	    	            new InputStreamReader(
	    	            		validateUrl.openStream()));

	    		serverResponse = in.readLine();
	    		int delimiterIdx = serverResponse.indexOf(":::");
	    		responseCode = Integer.parseInt(serverResponse.substring(1,delimiterIdx));
	    		responseMsg = serverResponse.substring(delimiterIdx+3,serverResponse.length()-1);
	    		in.close();
	    	}catch(Exception e){
	    		Log.e("",e.toString());
	    	};
	    	
	    	if(responseCode==1){
	    		validate = 1;
	    	}else{
	    		validate = 0;
	    	}
			return null;
	    }

	    // can use UI thread here
	    protected void onPostExecute(final Void unused) {
	       if (this.dialog.isShowing()) {
	          this.dialog.dismiss();
	       }  	 
	       lockPane.setClickable(true);
	       AACUtil.setValidate(validate);
	       ValidatePage.this.launchMainPage();	       
	    }
	 }
}