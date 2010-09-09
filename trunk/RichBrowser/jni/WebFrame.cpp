#include <JNIHelp.h> // For jniRegisterNativeMethods

#include "WebFrame.h"

namespace mo {

// ----------------------------------------------------------------------------

struct WebFrame::JavaBrowserFrame
{
    jobject     mObj;
    jobject     mHistoryList; // WebBackForwardList object
    jmethodID   mStartLoadingResource;
    jmethodID   mLoadStarted;
    jmethodID   mTransitionToCommitted;
    jmethodID   mLoadFinished;
    jmethodID   mReportError;
    jmethodID   mSetTitle;
    jmethodID   mWindowObjectCleared;
    jmethodID   mSetProgress;
    jmethodID   mDidReceiveIcon;
    jmethodID   mDidReceiveTouchIconUrl;
    jmethodID   mUpdateVisitedHistory;
    jmethodID   mHandleUrl;
    jmethodID   mCreateWindow;
    jmethodID   mCloseWindow;
    jmethodID   mDecidePolicyForFormResubmission;
    jmethodID   mRequestFocus;
    jmethodID   mGetRawResFilename;
    jmethodID   mDensity;
#if 0
    AutoJObject frame(JNIEnv* env) {
        return getRealObject(env, mObj);
    }
    AutoJObject history(JNIEnv* env) {
        return getRealObject(env, mHistoryList);
    }
#endif
};

static jfieldID gFrameField;
#define GET_NATIVE_FRAME(env, obj) ((WebCore::Frame*)env->GetIntField(obj, gFrameField))
#define SET_NATIVE_FRAME(env, obj, frame) (env->SetIntField(obj, gFrameField, frame))

// ----------------------------------------------------------------------------


// ----------------------------------------------------------------------------

/*
 * JNI registration.
 */
static JNINativeMethod gBrowserFrameNativeMethods[] = {
#if 0
    /* name, signature, funcPtr */
    { "nativeCallPolicyFunction", "(II)V",
        (void*) CallPolicyFunction },
    { "nativeCreateFrame", "(Landroid/webkit/WebViewCore;Landroid/content/res/AssetManager;Landroid/webkit/WebBackForwardList;)V",
        (void*) CreateFrame },
    { "nativeDestroyFrame", "()V",
        (void*) DestroyFrame },
    { "nativeStopLoading", "()V",
        (void*) StopLoading },
    { "nativeLoadUrl", "(Ljava/lang/String;)V",
        (void*) LoadUrl },
    { "nativePostUrl", "(Ljava/lang/String;[B)V",
        (void*) PostUrl },
    { "nativeLoadData", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V",
        (void*) LoadData },
    { "externalRepresentation", "()Ljava/lang/String;",
        (void*) ExternalRepresentation },
    { "documentAsText", "()Ljava/lang/String;",
        (void*) DocumentAsText },
    { "reload", "(Z)V",
        (void*) Reload },
    { "nativeGoBackOrForward", "(I)V",
        (void*) GoBackOrForward },
    { "nativeAddJavascriptInterface", "(ILjava/lang/Object;Ljava/lang/String;)V",
        (void*) AddJavascriptInterface },
    { "stringByEvaluatingJavaScriptFromString",
            "(Ljava/lang/String;)Ljava/lang/String;",
        (void*) StringByEvaluatingJavaScriptFromString },
    { "setCacheDisabled", "(Z)V",
        (void*) SetCacheDisabled },
    { "cacheDisabled", "()Z",
        (void*) CacheDisabled },
    { "clearCache", "()V",
        (void*) ClearCache },
    { "documentHasImages", "()Z",
        (void*) DocumentHasImages },
    { "hasPasswordField", "()Z",
        (void*) HasPasswordField },
    { "getUsernamePassword", "()[Ljava/lang/String;",
        (void*) GetUsernamePassword },
    { "setUsernamePassword", "(Ljava/lang/String;Ljava/lang/String;)V",
        (void*) SetUsernamePassword },
    { "getFormTextData", "()Ljava/util/HashMap;",
        (void*) GetFormTextData }
#endif
};

int register_webframe(JNIEnv* env)
{
    jclass clazz = env->FindClass("android/webkit/BrowserFrame");
    LOG_ASSERT(clazz, "Cannot find BrowserFrame");
    gFrameField = env->GetFieldID(clazz, "mNativeFrame", "I");
    LOG_ASSERT(gFrameField, "Cannot find mNativeFrame on BrowserFrame");

    return jniRegisterNativeMethods(env, "android/webkit/BrowserFrame",
            gBrowserFrameNativeMethods, NELEM(gBrowserFrameNativeMethods));
}
}
