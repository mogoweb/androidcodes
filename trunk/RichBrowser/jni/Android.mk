LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

MY_ANDROID_SOURCE=$(HOME)/android/source/cupcake

LOCAL_MODULE := librichbrowser
LOCAL_CPP_EXTENSION := .cpp
LOCAL_CXXFLAGS :=

LOCAL_C_INCLUDES := $(MY_ANDROID_SOURCE)/frameworks/base/core/jni/android/graphics \
					$(MY_ANDROID_SOURCE)/external/skia/include/core \
					$(MY_ANDROID_SOURCE)/external/skia/include/images \
					$(MY_ANDROID_SOURCE)/frameworks/base/include \
					$(MY_ANDROID_SOURCE)/system/core/include \
				    $(MY_ANDROID_SOURCE)/dalvik/libnativehelper/include/nativehelper 

LOCAL_SRC_FILES := BrowserJniOnLoad.cpp FrameBridge.cpp

LOCAL_LDLIBS := -llog -lsgl -landroid_runtime -lnativehelper \
				-L$(MY_ANDROID_SOURCE)/out/target/product/generic/system/lib/

include $(BUILD_SHARED_LIBRARY)
