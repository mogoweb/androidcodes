cmake_minimum_required (VERSION 2.6)
project (CMakeTest)

set (CMAKE_VERBOSE_MAKEFILE "true")

# add the executable
add_library(test SHARED TestJni.cpp)

#include ("../script/android.cmake")

