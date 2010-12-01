if(ANDROID)
	configure_file("${CMAKE_SOURCE_DIR}/Android.mk.modules.in" "${CMAKE_CURRENT_BINARY_DIR}/Android.mk")
endif()

add_subdirectory(curl)

if(NOT ANDROID)
add_subdirectory(gtest)
endif()

