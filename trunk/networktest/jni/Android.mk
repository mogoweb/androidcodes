LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

MY_ANDROID_SOURCE=$(HOME)/android/source/cupcake

LOCAL_MODULE := libnt
LOCAL_CPP_EXTENSION := .cpp
LOCAL_C_INCLUDES := 
LOCAL_SRC_FILES := NetworkTestJni.cpp
LOCAL_LDLIBS := -llog

include $(BUILD_SHARED_LIBRARY)
