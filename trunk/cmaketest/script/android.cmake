#android.cmake Chen zhengyong (chen.zhengyong@gmail.com) 2010

include (CMakeForceCompiler)

set (CMAKE_SYSTEM_NAME Linux)
set (CMAKE_SYSTEM_VERSION 1)
set (CMAKE_SYSTEM_PROCESSOR arm-eabi)

set (NDKROOT "$ENV{HOME}/android/android-ndk-r4")
set (SDKROOT "${NDKROOT}/build/platforms/android-5/arch-arm")
set (TOOLSROOT "${NDKROOT}/build/prebuilt/linux-x86/arm-eabi-4.4.0")

CMAKE_FORCE_C_COMPILER("${TOOLSROOT}/bin/arm-eabi-gcc" GNU)
CMAKE_FORCE_CXX_COMPILER("${TOOLSROOT}/bin/arm-eabi-g++" GNU)

set (CMAKE_C_FLAGS 
		"-fpic -mthumb-interwork -ffunction-sections -funwind-tables -fstack-protector -fno-short-enums -D__ARM_ARCH_5__ -D__ARM_ARCH_5T__ -D__ARM_ARCH_5E__ -D__ARM_ARCH_5TE__ -march=armv5te -mtune=xscale -msoft-float -mthumb -Os -fomit-frame-pointer -fno-strict-aliasing -finline-limit=64 -DANDROID -O2 -DNDEBUG -g -MMD -MP -MF")

set (CMAKE_CXX_FLAGS 
		"-fpic -mthumb-interwork -ffunction-sections -funwind-tables -fstack-protector -fno-short-enums -D__ARM_ARCH_5__ -D__ARM_ARCH_5T__ -D__ARM_ARCH_5E__ -D__ARM_ARCH_5TE__ -march=armv5te -mtune=xscale -msoft-float -mthumb -Os -fomit-frame-pointer -fno-strict-aliasing -finline-limit=64 -DANDROID -O2 -DNDEBUG -g -MMD -MP -MF")

set (CMAKE_SHARED_LINKER_FLAGS "-nostdlib -Wl,--no-undefined")

include_directories (SYSTEM "${SDKROOT}/usr/include")

link_directories ("${SDKROOT}/usr/lib")


set (CMAKE_FIND_ROOT_PATH "${SDKROOT}" "${TOOLSROOT}" )
set (CMAKE_FIND_ROOT_PATH_MODE_PROGRAM BOTH)
set (CMAKE_FIND_ROOT_PATH_MODE_LIBRARY ONLY)
set (CMAKE_FIND_ROOT_PATH_MODE_INCLUDE ONLY)
