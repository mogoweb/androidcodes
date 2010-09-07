package com.whtr.browser;

import java.util.Map;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.AbsoluteLayout;

import com.whtr.browser.WebViewCore.EventHub;

@SuppressWarnings("deprecation")
public class WebView extends AbsoluteLayout
{
    // This would be final but it needs to be set to null when the WebView is
    // destroyed.
    private WebViewCore mWebViewCore;
    // Handler for dispatching UI messages.
    /* package */ final Handler mPrivateHandler = new PrivateHandler();
    
    // A final CallbackProxy shared by WebViewCore and BrowserFrame.
    private final CallbackProxy mCallbackProxy;
    
    /**
     * Private message ids
     */
    private static final int REMEMBER_PASSWORD          = 1;
    private static final int NEVER_REMEMBER_PASSWORD    = 2;
    private static final int SWITCH_TO_SHORTPRESS       = 3;
    private static final int SWITCH_TO_LONGPRESS        = 4;
    private static final int RELEASE_SINGLE_TAP         = 5;
    private static final int REQUEST_FORM_DATA          = 6;
    private static final int RESUME_WEBCORE_UPDATE      = 7;

    //! arg1=x, arg2=y
    static final int SCROLL_TO_MSG_ID                   = 10;
    static final int SCROLL_BY_MSG_ID                   = 11;
    //! arg1=x, arg2=y
    static final int SPAWN_SCROLL_TO_MSG_ID             = 12;
    //! arg1=x, arg2=y
    static final int SYNC_SCROLL_TO_MSG_ID              = 13;
    static final int NEW_PICTURE_MSG_ID                 = 14;
    static final int UPDATE_TEXT_ENTRY_MSG_ID           = 15;
    static final int WEBCORE_INITIALIZED_MSG_ID         = 16;
    static final int UPDATE_TEXTFIELD_TEXT_MSG_ID       = 17;
    static final int UPDATE_ZOOM_RANGE                  = 18;
    static final int MOVE_OUT_OF_PLUGIN                 = 19;
    static final int CLEAR_TEXT_ENTRY                   = 20;
    static final int UPDATE_TEXT_SELECTION_MSG_ID       = 21;
    static final int UPDATE_CLIPBOARD                   = 22;
    static final int LONG_PRESS_CENTER                  = 23;
    static final int PREVENT_TOUCH_ID                   = 24;
    static final int WEBCORE_NEED_TOUCH_EVENTS          = 25;
    // obj=Rect in doc coordinates
    static final int INVAL_RECT_MSG_ID                  = 26;
    static final int REQUEST_KEYBOARD                   = 27;
    static final int SHOW_RECT_MSG_ID                   = 28;
    
    /**
     * Construct a new WebView with a Context object.
     * @param context A Context object used to access application assets.
     */
    public WebView(Context context) {
        this(context, null);
    }

    /**
     * Construct a new WebView with layout parameters.
     * @param context A Context object used to access application assets.
     * @param attrs An AttributeSet passed to our parent.
     */
    public WebView(Context context, AttributeSet attrs) {
        //~this(context, attrs, com.android.internal.R.attr.webViewStyle);
        this(context, attrs, 0);
    }
    
    /**
     * Construct a new WebView with layout parameters and a default style.
     * @param context A Context object used to access application assets.
     * @param attrs An AttributeSet passed to our parent.
     * @param defStyle The default style resource ID.
     */
    public WebView(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs, defStyle, null);
    }
    
    /**
     * Construct a new WebView with layout parameters, a default style and a set
     * of custom Javscript interfaces to be added to the WebView at initialization
     * time. This guraratees that these interfaces will be available when the JS
     * context is initialized.
     * @param context A Context object used to access application assets.
     * @param attrs An AttributeSet passed to our parent.
     * @param defStyle The default style resource ID.
     * @param javascriptInterfaces is a Map of intareface names, as keys, and
     * object implementing those interfaces, as values.
     * @hide pending API council approval.
     */
    protected WebView(Context context, AttributeSet attrs, int defStyle,
            Map<String, Object> javascriptInterfaces) {
        super(context, attrs, defStyle);
        
        mCallbackProxy = new CallbackProxy(context, this);
        mWebViewCore = new WebViewCore(context, this, mCallbackProxy, javascriptInterfaces);
    }
    
    /**
     * Load the given url.
     * @param url The url of the resource to load.
     */
    public void loadUrl(String url) {
        mWebViewCore.sendMessage(EventHub.LOAD_URL, url);
    }
    
    //-------------------------------------------------------------------------
    // Methods can be called from a separate thread, like WebViewCore
    // If it needs to call the View system, it has to send message.
    //-------------------------------------------------------------------------

    /**
     * General handler to receive message coming from webkit thread
     */
    class PrivateHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
        
        }
    }
}
