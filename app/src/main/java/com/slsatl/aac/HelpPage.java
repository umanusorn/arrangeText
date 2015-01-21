package com.slsatl.aac;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;


public class HelpPage extends Activity {
static WebView webView;
static String currHelpTabId = "About";
TabHost tabs;

@Override
public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setTitle(R.string.ACT_TITLE_HELP);

	setContentView(R.layout.helpview);

	webView = (WebView) findViewById(R.id.helpWebView);

	tabs = (TabHost) this.findViewById(R.id.helpTabHost);
	tabs.setup();

	TabSpec tspecAbout = tabs.newTabSpec("About");
	tspecAbout.setIndicator(getString(R.string.TAB_TAG_HELP_ABOUT));
	tspecAbout.setContent(R.id.helpWebView);
	tabs.addTab(tspecAbout);
	TabSpec tspecSetting = tabs.newTabSpec("Settings");
	tspecSetting.setIndicator(getString(R.string.TAB_TAG_HELP_SETTING));
	tspecSetting.setContent(R.id.helpWebView);
	tabs.addTab(tspecSetting);
	TabSpec tspecCompose = tabs.newTabSpec("Compose");
	tspecCompose.setIndicator(getString(R.string.TAB_TAG_HELP_COMPOSE));
	tspecCompose.setContent(R.id.helpWebView);
	tabs.addTab(tspecCompose);

	tabs.setOnTabChangedListener(new OnTabChangeListener() {
		@Override
		public void onTabChanged(String tabId) {
			showHTML(tabId);
		}
	});

	tabs.setCurrentTab(1); // A hack pulled in order to get the "first" tab content to show / need better solution
	tabs.setCurrentTabByTag(currHelpTabId);
	showHTML(currHelpTabId);

}

public static void showHTML(String tabId) {
	String data = "";
	data = AACUtil.getHelpHtml(tabId);
	webView.loadDataWithBaseURL("file:///android_asset/", data, "text/html", "utf-8", null);
	webView.postInvalidate();
}

}
