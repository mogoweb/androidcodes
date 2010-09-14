package com.whtr.example.networktest;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {
	static {
		System.loadLibrary("nt");
	}
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        getHostName("www.baidu.com");
    }
    
    private native void getHostName(String url);
}