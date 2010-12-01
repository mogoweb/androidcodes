# ----------------------------------------------------------------------------
#  Root CMake file for Android CurlTest Build
#
#    To build with cmake
#        $ mkdir build
#        $ cd build
#        $ cmake ..
#        $ make
#
#   - initial version Nov. 2010 richsoft(richsoft2010@gmail.com)
#
# ----------------------------------------------------------------------------
project(android-curltest-shared)

cmake_minimum_required(VERSION 2.8)

set(curltest_root "$ENV{HOME}/android/workspace/curltest" CACHE PATH "curltest source root directory")
if(NOT EXISTS ${curltest_root})
message(FATAL_ERROR "Cannot find your curltest root directory!" )
endif()

set(android_curltest_mk "${curltest_root}/android/build/android-curltest.mk" cache FILE "the generated android-curltest.mk file")
if(NOT EXISTS ${android_curltest_mk})
message(FATAL_ERROR "please locate the cmake generated android-curltest.mk file, usually in the android/build directory...")
endif()

set(ANDROID true)

file(GLOB module_includes "${curltest_root}/modules/[a-zA-Z]*")

list(REMOVE_ITEM module_includes ${curltest_root}/modules/CMakeLists.txt)

set(module_includes ${module_includes} ${CMAKE_SOURCE_DIR}/../third $(CURLTEST_BUILD_ROOT) )
foreach(mdir ${module_includes})
    string(REPLACE "${curltest_root}" "$(CURLTEST_ROOT)" n_f ${mdir})
    set(android_module_include_dirs "${android_module_include_dirs} ${n_f}/include")
endforeach()

configure_file("${CMAKE_SOURCE_DIR}/Android.mk.master.in" "${CMAKE_BINARY_DIR}/Android.mk")
configure_file("${CMAKE_SOURCE_DIR}/Application.mk.in" "${CMAKE_BINARY_DIR}/Application.mk")
configure_file("${CMAKE_SOURCE_DIR}/AndroidManifest.xml.in" "${CMAKE_BINARY_DIR}/AndroidManifest.xml")
configure_file("${CMAKE_SOURCE_DIR}/default.properties.in" "${CMAKE_BINARY_DIR}/default.properties")

set(NDK_ROOT  "$ENV{HOME}/android/android-ndk-r4b" CACHE STRING "the ndk directory")
if(NOT EXISTS ${NDK_ROOT})
message(FATAL_ERROR "Cannot find your ndk root directory! please download and 
unzip the android ndk to the directory specified by NDK_ROOT")
endif()

set(J  "2" CACHE STRING "how many processes for make -j <J>")

ADD_CUSTOM_COMMAND(
OUTPUT    android-ndk
DEPENDS   ${CMAKE_BINARY_DIR}/Android.mk
COMMAND   "${NDK_ROOT}/ndk-build" 
ARGS      --directory=${CMAKE_BINARY_DIR} NDK_APPLICATION_MK=Application.mk -j${J}
)

ADD_CUSTOM_COMMAND(
OUTPUT    android-swig
DEPENDS   ${SWIG_MAIN}
COMMAND   "swig" 
ARGS      --directory=${CMAKE_BINARY_DIR} NDK_APPLICATION_MK=Application.mk -j${J}
)

configure_file("${CMAKE_SOURCE_DIR}/android-curltest.mk.in" "${CMAKE_BINARY_DIR}/android-curltest.mk")

ADD_CUSTOM_TARGET(ndk ALL echo
  DEPENDS android-ndk
)

message(STATUS "Make will use make -j${J} - for speeding up build - you may change this in the cache")
message(STATUS "The NDK directory is ${NDK_ROOT}")
message(STATUS "CurlTest source root is ${curltest_root}")
message(STATUS "just run make - and grab some coffee or tea ;)")
message(STATUS "The android curltest libs will be located in ${CMAKE_BINARY_DIR}/obj/local/armeabi*/")

