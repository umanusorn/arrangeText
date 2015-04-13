package com.slsatl.aac;

/**
 * Created by um2013 on 2/14/2014.
 */

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;

public class ConfirmDialog {

private ConfirmListener listener;

public static void show(Context context,String title,final ConfirmListener listener,final String key ){
//context using getActivity or this

	Builder builder = new AlertDialog.Builder(context);
	builder.setIcon ( R.drawable.launcher_icon );
	builder.setTitle ( title );
	//builder.setMessage(title);
	builder.setPositiveButton(context.getString( R.string.yes), new DialogInterface.OnClickListener() {

		public void onClick(DialogInterface dialog, int id) {
			// HomeFragmentActivity.this.finish();
			if(listener != null){
				listener.onConfirm(key);
			}
		}
	});
	builder.setNegativeButton(context.getString( R.string.no), null);
	builder.show();
}


public interface ConfirmListener {
	public void onConfirm( String key );
}

}
