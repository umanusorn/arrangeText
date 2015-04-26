package com.slsatl.aac2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class TTSPage extends Activity implements TextToSpeech.OnInitListener {
Button ttsSpeakButton, ttsClearButton;
EditText          ttsInputText;
ListView          ttsHistory;
ArrayList<String> history;
TextToSpeech      mTts;
int MAX_HISTORY = 20;
LinearLayout lockPane;

MenuItem toAacMItm, helpMItm;

protected void launchCompose() {
	Intent i = new Intent(this, ComposePage.class);
	startActivity(i);
}

/**
 * Called when the activity is first created.
 */
@Override
public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	//requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView( R.layout.ttspage);
	setTitle(R.string.ACT_TITLE_TTS);

	history = new ArrayList<String>();
	mTts = new TextToSpeech(this, this);
	ttsInputText = (EditText) findViewById(R.id.TTSInputText);
	ttsSpeakButton = (Button) findViewById(R.id.TTSSpeakButton);
	ttsClearButton = (Button) findViewById(R.id.TTSClearButton);
	ttsHistory = (ListView) findViewById(R.id.TTSHistory);
	lockPane = (LinearLayout) findViewById(R.id.ttsPageAncestor);

	ttsClearButton.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v) {
			ttsInputText.setText("");
		}
	});
	ttsSpeakButton.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v) {
			String textToSpeak = ttsInputText.getText().toString().trim();
			speakText(textToSpeak, true);
		}
	});
	ttsHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position,
		                        long id)
		{
			String selectedItem = (String) ttsHistory.getItemAtPosition(position);
			speakText(selectedItem.trim(), false);
		}
	});
}

public void speakText(String textToSpeak, boolean addToHistory) {
	if (!textToSpeak.equals("")) {
		mTts.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null);
		if (addToHistory) {
			updateHistory(textToSpeak);
		}
	}
}

public void updateHistory(String textToSpeak) {
	if (history.size() >= MAX_HISTORY) {
		history.remove(0);
	}
	history.add(textToSpeak);
	refreshHistory();
}

public void refreshHistory() {
	ArrayList<String> revHistory = new ArrayList<String>(history);
	Collections.reverse(revHistory);
	ttsHistory.setAdapter(new TtsHistoryAdapter(this, R.layout.tts_history_item, revHistory));
}

@Override
protected void onDestroy() {
	//Close the Text to Speech Library
	if (mTts != null) {
		mTts.stop();
		mTts.shutdown();
		Log.d("", "TTS Destroyed");
	}
	super.onDestroy();
}

@Override
public boolean onCreateOptionsMenu(Menu menu) {

	int toAacBtnId = Menu.FIRST;
	int helpBtnId = Menu.FIRST + 1;

	toAacMItm = menu.add(Menu.NONE, toAacBtnId, toAacBtnId, getString(R.string.AAC));
	toAacMItm.setIcon(android.R.drawable.ic_menu_gallery);

	helpMItm = menu.add(Menu.NONE, helpBtnId, helpBtnId, getString(R.string.HELP));
	helpMItm.setIcon(android.R.drawable.ic_menu_help);

	return super.onCreateOptionsMenu(menu);

}

@Override
public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
		case Menu.FIRST: // go to TTS page
			new PrepareComposePage().execute();
			lockPane.setClickable(false);
			//launchComposePage();
			break;
		case Menu.FIRST + 1: // help
			launchHelpPage();
			break;
	}
	return true;
}

protected void launchHelpPage() {
	Intent i = new Intent(this, HelpPage.class);
	HelpPage.currHelpTabId = "Compose";
	startActivity(i);
}

@Override
public void onInit(int status) {
	// status can be either TextToSpeech.SUCCESS or TextToSpeech.ERROR.
	if (status == TextToSpeech.SUCCESS) {
		// Set preferred language to US english.
		// Note that a language may not be available, and the result will
		// indicate this.
		// **************** write check method for EN or TH********//
		int result = mTts.setLanguage(Keeper.locale);

		// int result mTts.setLanguage(Locale.FRANCE);
		if (result == TextToSpeech.LANG_MISSING_DATA
		    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
			// Lanuage data is missing or the language is not supported.
			Log.e("TAG", "Language is not available.");
		}
	}
	else {
		// Initialization failed.
		Log.e("TAG", "Could not initialize TextToSpeech.");
	}

}

class PrepareComposePage extends AsyncTask<String, Void, Void> {
	private final ProgressDialog dialog = new ProgressDialog(TTSPage.this);

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
		String[] column = {"cid"};
		Cursor
				c =
				Keeper.myDB.query(Constant.TABLE_NEW_CATE, column, "lang ='" + currLocale + "' and enable=1 ", null, null, null, "weight");
		c.moveToFirst();
		ComposePage.currCid = c.getInt(c.getColumnIndex("cid"));

		try {
			ComposePage.vocabShow = ComposePage.queryVocabs(ComposePage.currCid, 1);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// can use UI thread here
	protected void onPreExecute() {
		this.dialog.setMessage(getString(R.string.PROGRESS_PREPARE_COMPOSE_PAGE));
		this.dialog.show();
	}

	// can use UI thread here
	protected void onPostExecute(final Void unused) {
		if (this.dialog.isShowing()) {
			this.dialog.dismiss();
		}
		lockPane.setClickable(true);
		TTSPage.this.launchCompose();
	}
}

}
