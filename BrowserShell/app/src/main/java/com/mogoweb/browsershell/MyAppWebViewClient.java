package com.mogoweb.browsershell;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by chenzhengyong on 15/7/3.
 */
public class MyAppWebViewClient extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return false;
    }
}
