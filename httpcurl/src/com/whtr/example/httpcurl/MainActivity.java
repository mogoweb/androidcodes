package com.whtr.example.httpcurl;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	private String[] mUrls = {"www.sina.com",
			"www.baidu.com", "www.souhu.com",
			"www.google.com", "www.qq.com"
	};
	int mUrlIndex = 0;
	
    static {
        System.loadLibrary("httpcurl");
    }
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //~loadUrl("http://kong.net");
        //~multiLoadUrl("http://kong.net");
        
        Button addButton = (Button)findViewById(R.id.btn_add);
        addButton.setOnClickListener(new OnClickListener()
        {
        	public void onClick(View view)
        	{
        		addUrl(mUrls[mUrlIndex % 6]);
        		mUrlIndex++;
        	}
        });
    }
    
    private native void loadUrl(String url);
    private native void multiLoadUrl(String url);
    private native void addUrl(String url);
}