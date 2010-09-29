#ifndef __TIMER_JNI_H__
#define __TIMER_JNI_H__

#ifdef __cplusplus
extern "C" {
#endif

#include <jni.h>

JNIEXPORT void JNICALL Java_com_whtr_example_mtimer_MainActivity_startTimer(JNIEnv *env, jobject thizz);


#ifdef __cplusplus
}
#endif

#endif

