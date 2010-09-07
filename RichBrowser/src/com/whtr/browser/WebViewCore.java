package com.whtr.browser;

import java.util.ArrayList;
import java.util.Map;

import junit.framework.Assert;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.webkit.CacheManager;

final public class WebViewCore
{
    private static final String LOGTAG = "webcore";
    
    /*
     * WebViewCore always executes in the same thread as the native webkit.
     */

    // The WebView that corresponds to this WebViewCore.
    private WebView mWebView;
    // Proxy for handling callbacks from native code
    private final CallbackProxy mCallbackProxy;
    // Settings object for maintaining all settings
    private final WebSettings mSettings;
    // Context for initializing the BrowserFrame with the proper assets.
    private final Context mContext;
    // The pointer to a native view object.
    private int mNativeClass;
    // The BrowserFrame is an interface to the native Frame component.
    private BrowserFrame mBrowserFrame;
    // Custom JS interfaces to add during the initialization.
    private Map<String, Object> mJavascriptInterfaces;
    
    // EventHub for processing messages
    private final EventHub mEventHub;
    // WebCore thread handler
    private static Handler sWebCoreHandler;
    // Class for providing Handler creation inside the WebCore thread.
    private static class WebCoreThread implements Runnable {
        // Message id for initializing a new WebViewCore.
        private static final int INITIALIZE = 0;
        private static final int REDUCE_PRIORITY = 1;
        private static final int RESUME_PRIORITY = 2;
        private static final int CACHE_TICKER = 3;
        private static final int BLOCK_CACHE_TICKER = 4;
        private static final int RESUME_CACHE_TICKER = 5;

        private static final int CACHE_TICKER_INTERVAL = 60 * 1000; // 1 minute

        private static boolean mCacheTickersBlocked = true;

        public void run() {
            Looper.prepare();
            Assert.assertNull(sWebCoreHandler);
            synchronized (WebViewCore.class) {
                sWebCoreHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        switch (msg.what) {
                            case INITIALIZE:
                                WebViewCore core = (WebViewCore) msg.obj;
                                core.initialize();
                                break;

                            case REDUCE_PRIORITY:
                                // 3 is an adjustable number.
                                /*Process.setThreadPriority(
                                        Process.THREAD_PRIORITY_DEFAULT + 3 *
                                        Process.THREAD_PRIORITY_LESS_FAVORABLE);*/
                                break;

                            case RESUME_PRIORITY:
                                /*Process.setThreadPriority(
                                        Process.THREAD_PRIORITY_DEFAULT);*/
                                break;

                            case CACHE_TICKER:
                                if (!mCacheTickersBlocked) {
                                    CacheManager.endCacheTransaction();
                                    CacheManager.startCacheTransaction();
                                    sendMessageDelayed(
                                            obtainMessage(CACHE_TICKER),
                                            CACHE_TICKER_INTERVAL);
                                }
                                break;

                            case BLOCK_CACHE_TICKER:
                                if (CacheManager.endCacheTransaction()) {
                                    mCacheTickersBlocked = true;
                                }
                                break;

                            case RESUME_CACHE_TICKER:
                                if (CacheManager.startCacheTransaction()) {
                                    mCacheTickersBlocked = false;
                                }
                                break;
                        }
                    }
                };
                WebViewCore.class.notify();
            }
            Looper.loop();
        }
    }

    // The thread name used to identify the WebCore thread and for use in
    // debugging other classes that require operation within the WebCore thread.
    /* package */ static final String THREAD_NAME = "WebViewCoreThread";
    
    public WebViewCore(Context context, WebView w, CallbackProxy proxy,
            Map<String, Object> javascriptInterfaces) {
        
        
        // No need to assign this in the WebCore thread.
        mCallbackProxy = proxy;
        mWebView = w;
        mJavascriptInterfaces = javascriptInterfaces;
        // This context object is used to initialize the WebViewCore during
        // subwindow creation.
        mContext = context;
        
        // We need to wait for the initial thread creation before sending
        // a message to the WebCore thread.
        // XXX: This is the only time the UI thread will wait for the WebCore
        // thread!
        synchronized (WebViewCore.class) {
            if (sWebCoreHandler == null) {
                // Create a global thread and start it.
                Thread t = new Thread(new WebCoreThread());
                t.setName(THREAD_NAME);
                t.start();
                try {
                    WebViewCore.class.wait();
                } catch (InterruptedException e) {
                    Log.e(LOGTAG, "Caught exception while waiting for thread " +
                           "creation.");
                    Log.e(LOGTAG, Log.getStackTraceString(e));
                }
            }
        }
        
        mEventHub = new EventHub();
        // Create a WebSettings object for maintaining all settings
        mSettings = new WebSettings(mContext, mWebView);
        
        // Send a message to initialize the WebViewCore.
        Message init = sWebCoreHandler.obtainMessage(
                WebCoreThread.INITIALIZE, this);
        sWebCoreHandler.sendMessage(init);
    }
    
    /* Initialize private data within the WebCore thread.
     */
    private void initialize() {
        /* Initialize our private BrowserFrame class to handle all
         * frame-related functions. We need to create a new view which
         * in turn creates a C level FrameView and attaches it to the frame.
         */
        mBrowserFrame = new BrowserFrame(mContext, this, mCallbackProxy,
                mSettings, mJavascriptInterfaces);
        
        // The transferMessages call will transfer all pending messages to the
        // WebCore thread handler.
        mEventHub.transferMessages();
        
        // Send a message back to WebView to tell it that we have set up the
        // WebCore thread.
        if (mWebView != null) {
            Message.obtain(mWebView.mPrivateHandler,
                    WebView.WEBCORE_INITIALIZED_MSG_ID,
                    mNativeClass, 0).sendToTarget();
        }
    }
    
    class EventHub {
        // Message Ids
        static final int UPDATE_FRAME_CACHE_IF_LOADING = 98;
        static final int SCROLL_TEXT_INPUT = 99;
        static final int LOAD_URL = 100;
        static final int STOP_LOADING = 101;
        static final int RELOAD = 102;
        static final int KEY_DOWN = 103;
        static final int KEY_UP = 104;
        static final int VIEW_SIZE_CHANGED = 105;
        static final int GO_BACK_FORWARD = 106;
        static final int SET_SCROLL_OFFSET = 107;
        static final int RESTORE_STATE = 108;
        static final int PAUSE_TIMERS = 109;
        static final int RESUME_TIMERS = 110;
        static final int CLEAR_CACHE = 111;
        static final int CLEAR_HISTORY = 112;
        static final int SET_SELECTION = 113;
        static final int REPLACE_TEXT = 114;
        static final int PASS_TO_JS = 115;
        static final int SET_GLOBAL_BOUNDS = 116;
        static final int UPDATE_CACHE_AND_TEXT_ENTRY = 117;
        static final int CLICK = 118;
        static final int SET_NETWORK_STATE = 119;
        static final int DOC_HAS_IMAGES = 120;
        static final int DELETE_SELECTION = 122;
        static final int LISTBOX_CHOICES = 123;
        static final int SINGLE_LISTBOX_CHOICE = 124;
        static final int MESSAGE_RELAY = 125;
        static final int SET_BACKGROUND_COLOR = 126;
        static final int PLUGIN_STATE = 127; // plugin notifications
        static final int SAVE_DOCUMENT_STATE = 128;
        static final int GET_SELECTION = 129;
        static final int WEBKIT_DRAW = 130;
        static final int SYNC_SCROLL = 131;
        static final int POST_URL = 132;
        static final int SPLIT_PICTURE_SET = 133;
        static final int CLEAR_CONTENT = 134;
        
        // Private handler for WebCore messages.
        private Handler mHandler;
        // Message queue for containing messages before the WebCore thread is
        // ready.
        private ArrayList<Message> mMessages = new ArrayList<Message>();
        // Flag for blocking messages. This is used during DESTROY to avoid
        // posting more messages to the EventHub or to WebView's event handler.
        private boolean mBlockMessages;
        
        private int mTid;
        private int mSavedPriority;
        
        private EventHub() {}
        
        /**
         * Send a message internally to the queue or to the handler
         */
        private synchronized void sendMessage(Message msg) {
            if (mBlockMessages) {
                return;
            }
            if (mMessages != null) {
                mMessages.add(msg);
            } else {
                mHandler.sendMessage(msg);
            }
        }
        
        private synchronized void sendMessageDelayed(Message msg, long delay) {
            if (mBlockMessages) {
                return;
            }
            mHandler.sendMessageDelayed(msg, delay);
        }
        
        /**
         * Transfer all messages to the newly created webcore thread handler.
         */
        private void transferMessages() {
            mTid = Process.myTid();
            mSavedPriority = Process.getThreadPriority(mTid);

            mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    /*if (DebugFlags.WEB_VIEW_CORE) {
                        Log.v(LOGTAG, (msg.what < UPDATE_FRAME_CACHE_IF_LOADING
                                || msg.what
                                > FREE_MEMORY ? Integer.toString(msg.what)
                                : HandlerDebugString[msg.what
                                        - UPDATE_FRAME_CACHE_IF_LOADING])
                                + " arg1=" + msg.arg1 + " arg2=" + msg.arg2
                                + " obj=" + msg.obj);
                    }*/
                    switch (msg.what) {
//                        case WEBKIT_DRAW:
//                            webkitDraw();
//                            break;
//
//                        case DESTROY:
//                            // Time to take down the world. Cancel all pending
//                            // loads and destroy the native view and frame.
//                            synchronized (WebViewCore.this) {
//                                mBrowserFrame.destroy();
//                                mBrowserFrame = null;
//                                mSettings.onDestroyed();
//                                mNativeClass = 0;
//                                mWebView = null;
//                            }
//                            break;
//
//                        case UPDATE_FRAME_CACHE_IF_LOADING:
//                            nativeUpdateFrameCacheIfLoading();
//                            break;
//
//                        case SCROLL_TEXT_INPUT:
//                            nativeScrollFocusedTextInput(
//                                    ((Float) msg.obj).floatValue(), msg.arg1);
//                            break;

                        case LOAD_URL:
                            loadUrl((String) msg.obj);
                            break;

//                        case POST_URL: {
//                            PostUrlData param = (PostUrlData) msg.obj;
//                            mBrowserFrame.postUrl(param.mUrl, param.mPostData);
//                            break;
//                        }
//                        case LOAD_DATA:
//                            BaseUrlData loadParams = (BaseUrlData) msg.obj;
//                            String baseUrl = loadParams.mBaseUrl;
//                            if (baseUrl != null) {
//                                int i = baseUrl.indexOf(':');
//                                if (i > 0) {
//                                    /*
//                                     * In 1.0, {@link
//                                     * WebView#loadDataWithBaseURL} can access
//                                     * local asset files as long as the data is
//                                     * valid. In the new WebKit, the restriction
//                                     * is tightened. To be compatible with 1.0,
//                                     * we automatically add the scheme of the
//                                     * baseUrl for local access as long as it is
//                                     * not http(s)/ftp(s)/about/javascript
//                                     */
//                                    String scheme = baseUrl.substring(0, i);
//                                    if (!scheme.startsWith("http") &&
//                                            !scheme.startsWith("ftp") &&
//                                            !scheme.startsWith("about") &&
//                                            !scheme.startsWith("javascript")) {
//                                        nativeRegisterURLSchemeAsLocal(scheme);
//                                    }
//                                }
//                            }
//                            mBrowserFrame.loadData(baseUrl,
//                                    loadParams.mData,
//                                    loadParams.mMimeType,
//                                    loadParams.mEncoding,
//                                    loadParams.mFailUrl);
//                            break;
//
//                        case STOP_LOADING:
//                            // If the WebCore has committed the load, but not
//                            // finished the first layout yet, we need to set
//                            // first layout done to trigger the interpreted side sync
//                            // up with native side
//                            if (mBrowserFrame.committed()
//                                    && !mBrowserFrame.firstLayoutDone()) {
//                                mBrowserFrame.didFirstLayout();
//                            }
//                            // Do this after syncing up the layout state.
//                            stopLoading();
//                            break;
//
//                        case RELOAD:
//                            mBrowserFrame.reload(false);
//                            break;
//
//                        case KEY_DOWN:
//                            key((KeyEvent) msg.obj, true);
//                            break;
//
//                        case KEY_UP:
//                            key((KeyEvent) msg.obj, false);
//                            break;
//
//                        case CLICK:
//                            nativeClick(msg.arg1, msg.arg2);
//                            break;
//
//                        case VIEW_SIZE_CHANGED: {
//                            WebView.ViewSizeData data =
//                                    (WebView.ViewSizeData) msg.obj;
//                            viewSizeChanged(data.mWidth, data.mHeight,
//                                    data.mTextWrapWidth, data.mScale,
//                                    data.mAnchorX, data.mAnchorY,
//                                    data.mIgnoreHeight);
//                            break;
//                        }
//                        case SET_SCROLL_OFFSET:
//                            // note: these are in document coordinates
//                            // (inv-zoom)
//                            Point pt = (Point) msg.obj;
//                            nativeSetScrollOffset(msg.arg1, pt.x, pt.y);
//                            break;
//
//                        case SET_GLOBAL_BOUNDS:
//                            Rect r = (Rect) msg.obj;
//                            nativeSetGlobalBounds(r.left, r.top, r.width(),
//                                r.height());
//                            break;
//
//                        case GO_BACK_FORWARD:
//                            // If it is a standard load and the load is not
//                            // committed yet, we interpret BACK as RELOAD
//                            if (!mBrowserFrame.committed() && msg.arg1 == -1 &&
//                                    (mBrowserFrame.loadType() ==
//                                    BrowserFrame.FRAME_LOADTYPE_STANDARD)) {
//                                mBrowserFrame.reload(true);
//                            } else {
//                                mBrowserFrame.goBackOrForward(msg.arg1);
//                            }
//                            break;
//
//                        case RESTORE_STATE:
//                            stopLoading();
//                            restoreState(msg.arg1);
//                            break;
//
//                        case PAUSE_TIMERS:
//                            mSavedPriority = Process.getThreadPriority(mTid);
//                            Process.setThreadPriority(mTid,
//                                    Process.THREAD_PRIORITY_BACKGROUND);
//                            pauseTimers();
//                            if (CacheManager.disableTransaction()) {
//                                WebCoreThread.mCacheTickersBlocked = true;
//                                sWebCoreHandler.removeMessages(
//                                        WebCoreThread.CACHE_TICKER);
//                            }
//                            break;
//
//                        case RESUME_TIMERS:
//                            Process.setThreadPriority(mTid, mSavedPriority);
//                            resumeTimers();
//                            if (CacheManager.enableTransaction()) {
//                                WebCoreThread.mCacheTickersBlocked = false;
//                                sWebCoreHandler.sendMessageDelayed(
//                                        sWebCoreHandler.obtainMessage(
//                                        WebCoreThread.CACHE_TICKER),
//                                        WebCoreThread.CACHE_TICKER_INTERVAL);
//                            }
//                            break;
//
//                        case ON_PAUSE:
//                            nativePause();
//                            break;
//
//                        case ON_RESUME:
//                            nativeResume();
//                            break;
//
//                        case FREE_MEMORY:
//                            clearCache(false);
//                            nativeFreeMemory();
//                            break;
//
//                        case PLUGIN_STATE:
//                            PluginStateData psd = (PluginStateData) msg.obj;
//                            nativeUpdatePluginState(psd.mFrame, psd.mNode, psd.mState);
//                            break;
//
//                        case SET_NETWORK_STATE:
//                            if (BrowserFrame.sJavaBridge == null) {
//                                throw new IllegalStateException("No WebView " +
//                                        "has been created in this process!");
//                            }
//                            BrowserFrame.sJavaBridge
//                                    .setNetworkOnLine(msg.arg1 == 1);
//                            break;
//
//                        case CLEAR_CACHE:
//                            clearCache(msg.arg1 == 1);
//                            break;
//
//                        case CLEAR_HISTORY:
//                            mCallbackProxy.getBackForwardList().
//                                    close(mBrowserFrame.mNativeFrame);
//                            break;
//
//                        case REPLACE_TEXT:
//                            ReplaceTextData rep = (ReplaceTextData) msg.obj;
//                            nativeReplaceTextfieldText(msg.arg1, msg.arg2,
//                                    rep.mReplace, rep.mNewStart, rep.mNewEnd,
//                                    rep.mTextGeneration);
//                            break;
//
//                        case PASS_TO_JS: {
//                            JSKeyData jsData = (JSKeyData) msg.obj;
//                            KeyEvent evt = jsData.mEvent;
//                            int keyCode = evt.getKeyCode();
//                            int keyValue = evt.getUnicodeChar();
//                            int generation = msg.arg1;
//                            passToJs(generation,
//                                    jsData.mCurrentText,
//                                    keyCode,
//                                    keyValue,
//                                    evt.isDown(),
//                                    evt.isShiftPressed(), evt.isAltPressed(),
//                                    evt.isSymPressed());
//                            break;
//                        }
//
//                        case SAVE_DOCUMENT_STATE: {
//                            CursorData cDat = (CursorData) msg.obj;
//                            nativeSaveDocumentState(cDat.mFrame);
//                            break;
//                        }
//
//                        case CLEAR_SSL_PREF_TABLE:
//                            Network.getInstance(mContext)
//                                    .clearUserSslPrefTable();
//                            break;
//
//                        case TOUCH_UP:
//                            TouchUpData touchUpData = (TouchUpData) msg.obj;
//                            nativeTouchUp(touchUpData.mMoveGeneration,
//                                    touchUpData.mFrame, touchUpData.mNode,
//                                    touchUpData.mX, touchUpData.mY);
//                            break;
//
//                        case TOUCH_EVENT: {
//                            TouchEventData ted = (TouchEventData) msg.obj;
//                            Message.obtain(
//                                    mWebView.mPrivateHandler,
//                                    WebView.PREVENT_TOUCH_ID, ted.mAction,
//                                    nativeHandleTouchEvent(ted.mAction, ted.mX,
//                                            ted.mY) ? 1 : 0).sendToTarget();
//                            break;
//                        }
//
//                        case SET_ACTIVE:
//                            nativeSetFocusControllerActive(msg.arg1 == 1);
//                            break;
//
//                        case ADD_JS_INTERFACE:
//                            JSInterfaceData jsData = (JSInterfaceData) msg.obj;
//                            mBrowserFrame.addJavascriptInterface(jsData.mObject,
//                                    jsData.mInterfaceName);
//                            break;
//
//                        case REQUEST_EXT_REPRESENTATION:
//                            mBrowserFrame.externalRepresentation(
//                                    (Message) msg.obj);
//                            break;
//
//                        case REQUEST_DOC_AS_TEXT:
//                            mBrowserFrame.documentAsText((Message) msg.obj);
//                            break;
//
//                        case SET_MOVE_MOUSE:
//                            CursorData cursorData = (CursorData) msg.obj;
//                            nativeMoveMouse(cursorData.mFrame,
//                                     cursorData.mX, cursorData.mY);
//                            break;
//
//                        case SET_MOVE_MOUSE_IF_LATEST:
//                            CursorData cData = (CursorData) msg.obj;
//                            nativeMoveMouseIfLatest(cData.mMoveGeneration,
//                                    cData.mFrame,
//                                    cData.mX, cData.mY);
//                            break;
//
//                        case REQUEST_CURSOR_HREF: {
//                            Message hrefMsg = (Message) msg.obj;
//                            String res = nativeRetrieveHref(msg.arg1, msg.arg2);
//                            hrefMsg.getData().putString("url", res);
//                            hrefMsg.sendToTarget();
//                            break;
//                        }
//
//                        case UPDATE_CACHE_AND_TEXT_ENTRY:
//                            nativeUpdateFrameCache();
//                            // FIXME: this should provide a minimal rectangle
//                            if (mWebView != null) {
//                                mWebView.postInvalidate();
//                            }
//                            sendUpdateTextEntry();
//                            break;
//
//                        case DOC_HAS_IMAGES:
//                            Message imageResult = (Message) msg.obj;
//                            imageResult.arg1 =
//                                    mBrowserFrame.documentHasImages() ? 1 : 0;
//                            imageResult.sendToTarget();
//                            break;
//
//                        case DELETE_SELECTION:
//                            TextSelectionData deleteSelectionData
//                                    = (TextSelectionData) msg.obj;
//                            nativeDeleteSelection(deleteSelectionData.mStart,
//                                    deleteSelectionData.mEnd, msg.arg1);
//                            break;
//
//                        case SET_SELECTION:
//                            nativeSetSelection(msg.arg1, msg.arg2);
//                            break;
//
//                        case LISTBOX_CHOICES:
//                            SparseBooleanArray choices = (SparseBooleanArray)
//                                    msg.obj;
//                            int choicesSize = msg.arg1;
//                            boolean[] choicesArray = new boolean[choicesSize];
//                            for (int c = 0; c < choicesSize; c++) {
//                                choicesArray[c] = choices.get(c);
//                            }
//                            nativeSendListBoxChoices(choicesArray,
//                                    choicesSize);
//                            break;
//
//                        case SINGLE_LISTBOX_CHOICE:
//                            nativeSendListBoxChoice(msg.arg1);
//                            break;
//
//                        case SET_BACKGROUND_COLOR:
//                            nativeSetBackgroundColor(msg.arg1);
//                            break;
//
//                        case GET_SELECTION:
//                            String str = nativeGetSelection((Region) msg.obj);
//                            Message.obtain(mWebView.mPrivateHandler
//                                    , WebView.UPDATE_CLIPBOARD, str)
//                                    .sendToTarget();
//                            break;
//
//                        case DUMP_DOMTREE:
//                            nativeDumpDomTree(msg.arg1 == 1);
//                            break;
//
//                        case DUMP_RENDERTREE:
//                            nativeDumpRenderTree(msg.arg1 == 1);
//                            break;
//
//                        case DUMP_NAVTREE:
//                            nativeDumpNavTree();
//                            break;
//
//                        case SET_JS_FLAGS:
//                            nativeSetJsFlags((String)msg.obj);
//                            break;
//
//                        case GEOLOCATION_PERMISSIONS_PROVIDE:
//                            GeolocationPermissionsData data =
//                                    (GeolocationPermissionsData) msg.obj;
//                            nativeGeolocationPermissionsProvide(data.mOrigin,
//                                    data.mAllow, data.mRemember);
//                            break;
//
//                        case SYNC_SCROLL:
//                            mWebkitScrollX = msg.arg1;
//                            mWebkitScrollY = msg.arg2;
//                            break;
//
//                        case SPLIT_PICTURE_SET:
//                            nativeSplitContent();
//                            mSplitPictureIsScheduled = false;
//                            break;
//
//                        case CLEAR_CONTENT:
//                            // Clear the view so that onDraw() will draw nothing
//                            // but white background
//                            // (See public method WebView.clearView)
//                            nativeClearContent();
//                            break;
//
//                        case MESSAGE_RELAY:
//                            if (msg.obj instanceof Message) {
//                                ((Message) msg.obj).sendToTarget();
//                            }
//                            break;
//
//                        case POPULATE_VISITED_LINKS:
//                            nativeProvideVisitedHistory((String[])msg.obj);
//                            break;
                    }
                }
            };
            // Take all queued messages and resend them to the new handler.
            synchronized (this) {
                int size = mMessages.size();
                for (int i = 0; i < size; i++) {
                    mHandler.sendMessage(mMessages.get(i));
                }
                mMessages = null;
            }
        }

    }
    
    //-------------------------------------------------------------------------
    // Methods called by WebView
    // If it refers to local variable, it needs synchronized().
    // If it needs WebCore, it has to send message.
    //-------------------------------------------------------------------------

    void sendMessage(Message msg) {
        mEventHub.sendMessage(msg);
    }

    void sendMessage(int what) {
        mEventHub.sendMessage(Message.obtain(null, what));
    }

    void sendMessage(int what, Object obj) {
        mEventHub.sendMessage(Message.obtain(null, what, obj));
    }

    void sendMessage(int what, int arg1) {
        // just ignore the second argument (make it 0)
        mEventHub.sendMessage(Message.obtain(null, what, arg1, 0));
    }

    void sendMessage(int what, int arg1, int arg2) {
        mEventHub.sendMessage(Message.obtain(null, what, arg1, arg2));
    }

    void sendMessage(int what, int arg1, Object obj) {
        // just ignore the second argument (make it 0)
        mEventHub.sendMessage(Message.obtain(null, what, arg1, 0, obj));
    }

    void sendMessage(int what, int arg1, int arg2, Object obj) {
        mEventHub.sendMessage(Message.obtain(null, what, arg1, arg2, obj));
    }

    void sendMessageDelayed(int what, Object obj, long delay) {
        mEventHub.sendMessageDelayed(Message.obtain(null, what, obj), delay);
    }
    
    private void loadUrl(String url) {
//        if (DebugFlags.WEB_VIEW_CORE) Log.v(LOGTAG, " CORE loadUrl " + url);
        mBrowserFrame.loadUrl(url);
    }
}
