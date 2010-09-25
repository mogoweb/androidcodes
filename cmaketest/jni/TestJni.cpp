#include <jni.h>
#include "TestJni.h"
#include "mylog.h"

void Java_com_whtr_example_cmaketest_MainActivity_test(JNIEnv *env, jobject thizz)
{
	LOGD("entering test");
}
