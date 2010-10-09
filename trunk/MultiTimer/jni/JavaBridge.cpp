#include <jni.h>

// ----------------------------------------------------------------------------

static jfieldID gJavaBridge_ObjectID;

// ----------------------------------------------------------------------------

class JavaBridge : public TimerClient
{
public:
    JavaBridge(JNIEnv* env, jobject obj);
    virtual ~JavaBridge();

    /*
     * WebCore -> Java API
     */
    virtual void setSharedTimer(long long timemillis);
    virtual void stopSharedTimer();

    ////////////////////////////////////////////

    virtual void setSharedTimerCallback(void (*f)());

    ////////////////////////////////////////////

    // jni functions
    static void Constructor(JNIEnv* env, jobject obj);
    static void Finalize(JNIEnv* env, jobject obj);
    static void SharedTimerFired(JNIEnv* env, jobject);
    static void SetDeferringTimers(JNIEnv* env, jobject obj, jboolean defer);

private:
    jobject     mJavaObject;
    jmethodID   mSetSharedTimer;
    jmethodID   mStopSharedTimer;
};

static void (*sSharedTimerFiredCallback)();

JavaBridge::JavaBridge(JNIEnv* env, jobject obj)
{
    mJavaObject = adoptGlobalRef(env, obj);
    jclass clazz = env->GetObjectClass(obj);

    mSetSharedTimer = env->GetMethodID(clazz, "setSharedTimer", "(J)V");
    mStopSharedTimer = env->GetMethodID(clazz, "stopSharedTimer", "()V");
    
    LOG_ASSERT(mSetSharedTimer, "Could not find method setSharedTimer");
    LOG_ASSERT(mStopSharedTimer, "Could not find method stopSharedTimer");
    
    JavaSharedClient::SetTimerClient(this);
}

JavaBridge::~JavaBridge()
{
    if (mJavaObject) {
        JNIEnv* env = JSC::Bindings::getJNIEnv();
        env->DeleteGlobalRef(mJavaObject);
        mJavaObject = 0;
    }
    
    JavaSharedClient::SetTimerClient(NULL);
}

void
JavaBridge::setSharedTimer(long long timemillis)
{
    JNIEnv* env = JSC::Bindings::getJNIEnv();
    AutoJObject obj = getRealObject(env, mJavaObject);
    if (!obj.get())
        return;
    env->CallVoidMethod(obj.get(), mSetSharedTimer, timemillis);
}

void
JavaBridge::stopSharedTimer()
{    
    JNIEnv* env = JSC::Bindings::getJNIEnv();
    AutoJObject obj = getRealObject(env, mJavaObject);
    if (!obj.get())
        return;
    env->CallVoidMethod(obj.get(), mStopSharedTimer);
}

void
JavaBridge::setSharedTimerCallback(void (*f)())
{
    LOG_ASSERT(!sSharedTimerFiredCallback || sSharedTimerFiredCallback==f,
               "Shared timer callback may already be set or null!");

    sSharedTimerFiredCallback = f;
}

// ----------------------------------------------------------------------------

void JavaBridge::Constructor(JNIEnv* env, jobject obj)
{
    JavaBridge* javaBridge = new JavaBridge(env, obj);
    env->SetIntField(obj, gJavaBridge_ObjectID, (jint)javaBridge);
}

void JavaBridge::Finalize(JNIEnv* env, jobject obj)
{
    JavaBridge* javaBridge = (JavaBridge*)
        (env->GetIntField(obj, gJavaBridge_ObjectID));    
    
    delete javaBridge;
    env->SetIntField(obj, gJavaBridge_ObjectID, 0);
}

// we don't use the java bridge object, as we're just looking at a global
void JavaBridge::SharedTimerFired(JNIEnv* env, jobject)
{
    if (sSharedTimerFiredCallback)
    {
        sSharedTimerFiredCallback();

    }
}


// ----------------------------------------------------------------------------

/*
 * JNI registration.
 */
static JNINativeMethod gWebCoreJavaBridgeMethods[] = {
    /* name, signature, funcPtr */
    { "nativeConstructor", "()V",
        (void*) JavaBridge::Constructor },
    { "nativeFinalize", "()V",
        (void*) JavaBridge::Finalize },
    { "sharedTimerFired", "()V",
        (void*) JavaBridge::SharedTimerFired }
};

int register_javabridge(JNIEnv* env)
{
    jclass javaBridge = env->FindClass("com/whtr/example/mtimer/JWebCoreJavaBridge");
    
    gJavaBridge_ObjectID = env->GetFieldID(javaBridge, "mNativeBridge", "I");
    
    return jniRegisterNativeMethods(env, "com/whtr/example/mtimer/JWebCoreJavaBridge", 
                                    gWebCoreJavaBridgeMethods, NELEM(gWebCoreJavaBridgeMethods));
}
