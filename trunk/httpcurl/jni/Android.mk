LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

MY_ANDROID_SOURCE=$(HOME)/android/source/eclair
MY_MO_SOURCE=$(HOME)/projects/freebrowser

LOCAL_MODULE := libhttpcurl
LOCAL_CPP_EXTENSION := .cpp
LOCAL_C_INCLUDES := $(MY_ANDROID_SOURCE)/system/core/include \
					$(MY_MO_SOURCE)/third/curl/include 
LOCAL_SRC_FILES := httpcurl.cpp fopen.c
LOCAL_LDLIBS := -lcutils \
				-L$(MY_ANDROID_SOURCE)/out/target/product/generic/system/lib \
				$(MY_MO_SOURCE)/group/libcurl.a

include $(BUILD_SHARED_LIBRARY)

