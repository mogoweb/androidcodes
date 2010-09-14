SRCROOT=..
ANDROID_SOURCE=$(HOME)/android/source/eclair

INCLUDES=-I$(ANDROID_SOURCE)/external/icu4c/common/ -I$(ANDROID_SOURCE)/external/skia/include/core/ \
	-I$(ANDROID_SOURCE)/bionic/libc/include -I$(ANDROID_SOURCE)/bionic/libstdc++/include \
	-I$(ANDROID_SOURCE)/bionic/libc/arch-arm/include -I$(ANDROID_SOURCE)/bionic/libc/kernel/common \
	-I$(ANDROID_SOURCE)/bionic/libc/kernel/arch-arm -I$(ANDROID_SOURCE)/bionic/libm/include \
	-I$(ANDROID_SOURCE)/system/core/include -I$(ANDROID_SOURCE)/hardware/libhardware/include \
	-I$(ANDROID_SOURCE)/frameworks/base/include -I$(ANDROID_SOURCE)/external/skia/include/images \
	-I$(ANDROID_SOURCE)/external/webkit/WebKit/android/stl

DEFINES=-D__ANDROID__ -DANDROID 

LDFLAGS=-nostdlib -Bdynamic -Wl,-T,$(ANDROID_SOURCE)/build/core/armelf.x -Wl,-dynamic-linker,/system/bin/linker -Wl,--gc-sections -Wl,-z,nocopyreloc -Wl,-rpath-link=$(ANDROID_SOURCE)/out/target/product/generic/obj/lib -Wl,--no-undefined $(ANDROID_SOURCE)/out/target/product/generic/obj/lib/crtbegin_dynamic.o $(ANDROID_SOURCE)/out/target/product/generic/obj/lib/crtend_android.o

CC=$(ANDROID_SOURCE)/prebuilt/linux-x86/toolchain/arm-eabi-4.4.0/bin/arm-eabi-gcc
CCFLAGS = -pipe -O2   $(INCLUDES) $(DEFINES)

all : test.c
	$(CC) $(CCFLAGS) $(LDFLAGS) test.c
