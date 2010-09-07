package com.whtr.browser;

import android.content.Context;

/**
 * Manages settings state for a WebView. When a WebView is first created, it
 * obtains a set of default settings. These default settings will be returned
 * from any getter call. A WebSettings object obtained from
 * WebView.getSettings() is tied to the life of the WebView. If a WebView has
 * been destroyed, any method call on WebSettings will throw an
 * IllegalStateException.
 */
public class WebSettings {
    
    /**
     * Package constructor to prevent clients from creating a new settings
     * instance.
     */
    WebSettings(Context context, WebView webview) {
        
    }
}
