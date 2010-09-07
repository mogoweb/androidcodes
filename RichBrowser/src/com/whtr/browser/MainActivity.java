package com.whtr.browser;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //~setContentView(R.layout.main);
        WebView webview = new WebView(this);
        webview.loadUrl("");
        setContentView(webview);
    }
}