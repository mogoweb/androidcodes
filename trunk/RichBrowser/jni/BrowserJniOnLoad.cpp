#include <jni.h>
#include <utils/Log.h>
#include <stdlib.h>

namespace mo {
	extern int register_webframe(JNIEnv*);
};

struct RegistrationMethod {
	const char* name;
	int (*func)(JNIEnv*);
};

static RegistrationMethod gWebCoreRegMethods[] = {
	{ "WebFrame", mo::register_webframe },
};

jint JNI_OnLoad(JavaVM* vm, void* reserved)
{
	JNIEnv* env = NULL;
	jint result = -1;

	if (vm->GetEnv((void**) &env, JNI_VERSION_1_4) != JNI_OK) {
		LOGE("GetEnv failed!");
		return result;
	}
	LOG_ASSERT(env, "Could not retrieve the env!");

	const RegistrationMethod* method = gWebCoreRegMethods;
    const RegistrationMethod* end = method + sizeof(gWebCoreRegMethods)/sizeof(RegistrationMethod);
    while (method != end) {
        if (method->func(env) < 0) {
            LOGE("%s registration failed!", method->name);
            return result;
        }
        method++;
    }

    // Initialize rand() function. The rand() function is used in
    // FileSystemAndroid to create a random temporary filename.
    srand(time(NULL));

    return JNI_VERSION_1_4;
}
