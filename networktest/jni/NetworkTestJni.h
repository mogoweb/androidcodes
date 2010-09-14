#ifndef __NETWORK_TEST_JNI_H__
#define __NETWORK_TEST_JNI_H__

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT void JNICALL Java_com_whtr_example_networktest_MainActivity_getHostName(JNIEnv* env, jobject thizz, jstring urlstr);

#ifdef __cplusplus
}
#endif

#endif
