package com.whtr.example.httpcurl;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {
    static {
        System.loadLibrary("httpcurl");
    }
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //~loadUrl("http://kong.net");
        multiLoadUrl("http://kong.net");
    }
    
    private native void loadUrl(String url);
    private native void multiLoadUrl(String url);
}