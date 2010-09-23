#ifndef __SKIADEMOJNI_H__
#define __SKIADEMOJNI_H__

#include <jni.h>

#ifdef __cplusplus
extern "C" {
#endif

void Java_com_whtr_example_skiademo_SkiaView_renderHello(JNIEnv *env, jobject thizz, jobject canvas);
void Java_com_whtr_example_skiademo_SkiaView_renderText(JNIEnv *env, jobject thizz, jobject canvas);

#ifdef __cplusplus
}
#endif

#endif
