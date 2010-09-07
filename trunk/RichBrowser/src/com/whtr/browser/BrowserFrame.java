package com.whtr.browser;

import java.util.Map;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Handler;
import android.webkit.WebBackForwardList;

public class BrowserFrame extends Handler {
    
    /**
     * Create a new BrowserFrame to be used in an application.
     * @param context An application context to use when retrieving assets.
     * @param w A WebViewCore used as the view for this frame.
     * @param proxy A CallbackProxy for posting messages to the UI thread and
     *              querying a client for information.
     * @param settings A WebSettings object that holds all settings.
     * XXX: Called by WebCore thread.
     */
    public BrowserFrame(Context context, WebViewCore w, CallbackProxy proxy,
            WebSettings settings, Map<String, Object> javascriptInterfaces) {
        
    }

    /**
     * Load a url from the network or the filesystem into the main frame.
     * Following the same behaviour as Safari, javascript: URLs are not
     * passed to the main frame, instead they are evaluated immediately.
     * @param url The url to load.
     */
    public void loadUrl(String url) {
//        mLoadInitFromJava = true;
//        if (URLUtil.isJavaScriptUrl(url)) {
//            // strip off the scheme and evaluate the string
//            stringByEvaluatingJavaScriptFromString(
//                    url.substring("javascript:".length()));
//        } else {
//            nativeLoadUrl(url);
//        }
//        mLoadInitFromJava = false;
    }
    
    //==========================================================================
    // native functions
    //==========================================================================

    /**
     * Create a new native frame for a given WebView
     * @param w     A WebView that the frame draws into.
     * @param am    AssetManager to use to get assets.
     * @param list  The native side will add and remove items from this list as
     *              the native list changes.
     */
    private native void nativeCreateFrame(WebViewCore w, AssetManager am,
            WebBackForwardList list);
    
    /**
     * Returns false if the url is bad.
     */
    private native void nativeLoadUrl(String url);
}
