LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

MY_ANDROID_SOURCE=$(HOME)/android/source/eclair

LOCAL_MODULE := libtimerdemo
LOCAL_CPP_EXTENSION := .cpp
LOCAL_CXXFLAGS := -DHAVE_PTHREADS -D__ANDROID__

LOCAL_C_INCLUDES := $(MY_ANDROID_SOURCE)/frameworks/base/core/jni/android/graphics \
					$(MY_ANDROID_SOURCE)/external/skia/include/images \
					$(MY_ANDROID_SOURCE)/external/skia/include/core	\
					$(MY_ANDROID_SOURCE)/frameworks/base/include \
					$(MY_ANDROID_SOURCE)/system/core/include \
					$(MY_ANDROID_SOURCE)/external/icu4c/common

LOCAL_SRC_FILES := timer.c timer_jni.cpp
LOCAL_LDLIBS := -llog -landroid_runtime -lgcc -lcutils \
	-L$(MY_ANDROID_SOURCE)/out/target/product/generic/system/lib

include $(BUILD_SHARED_LIBRARY)

