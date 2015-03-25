package com.fydp.shelfe;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.text.ParseException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class GraphicalView extends ActionBarActivity{

    private WebView mWebView;
    private String username;
    private String password;
    
    @SuppressLint("NewApi")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
	    Bundle myIntent = getIntent().getExtras();
	    username = myIntent.getString("username");
	    password = myIntent.getString("password");
	    
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        mWebView = new WebView(this);
        mWebView.loadUrl("http://shelfe.host22.com/graphicalView.php?" +
        		"username=" + username + 
        		"&password=" + password);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //view.loadUrl(url);
                return true;
            }
        });
        mWebView.setOnClickListener(new WebView.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				refresh();
			}});
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        this.setContentView(mWebView);
    }
 
    public void refresh(){
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //view.loadUrl(url);
                return true;
            }
        });   	
    }
    
    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        return false;
    }
    
    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        if ((keyCode == KeyEvent.KEYCODE_MENU) ){
			this.finish();
			this.startActivity(getIntent());
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }	
    /*@Override
	public boolean onCreateOptionsMenu(Menu menu) {

    		super.onCreateOptionsMenu(menu);
			//getMenuInflater().inflate(R.menu.main, menu);
			
			menu.add(0, 8, 0, R.string.action_refresh)
	        .setShortcut('8', 's');
			restoreActionBar();
			
			
			return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();


		if (id == 8) {
			this.finish();
			this.startActivity(getIntent());
			
		}
		return super.onOptionsItemSelected(item);
	}
    
	public void restoreActionBar() {
		//ActionBar actionBar = getSupportActionBar();
		//actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		//actionBar.setDisplayShowTitleEnabled(true);
			//actionBar.setTitle(mTitle);
	}*/
    
}
