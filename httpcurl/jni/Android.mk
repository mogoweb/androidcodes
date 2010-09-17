LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

MY_ANDROID_SOURCE=$(HOME)/android/source/cupcake
MY_CURL_SOURCE=../third/curl

LOCAL_MODULE := libhttpcurl
LOCAL_CPP_EXTENSION := .cpp
LOCAL_C_INCLUDES := $(MY_ANDROID_SOURCE)/system/core/include \
		    $(MY_CURL_SOURCE)/include $(MY_CURL_SOURCE)/lib
LOCAL_SRC_FILES := httpcurl.cpp fopen.c lib507.c testutil.c
LOCAL_LDLIBS := -lcutils \
		-L$(MY_ANDROID_SOURCE)/out/target/product/generic/system/lib \
		$(MY_CURL_SOURCE)/libcurl.a

include $(BUILD_SHARED_LIBRARY)

