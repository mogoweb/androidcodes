LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

MY_ANDROID_SOURCE=$(HOME)/android/source/cupcake

LOCAL_MODULE := libSkiaDemo
LOCAL_CPP_EXTENSION := .cpp
LOCAL_CXXFLAGS :=

LOCAL_C_INCLUDES :=

LOCAL_SRC_FILES := SkiaDemoJni.cpp

LOCAL_LDLIBS := -llog -lsgl \
				-L$(MY_ANDROID_SOURCE)/out/target/product/generic/system/lib/

include $(BUILD_SHARED_LIBRARY)
