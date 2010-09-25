#ifndef __MY_LOG_H__
#define __MY_LOG_H__

#include <android/log.h>

#define LOG_TAG		"skiademo"
#define LOGD(...)   __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGI(...)	__android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...)	__android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

#endif //__MY_LOG_H__
