if(ANDROID)
	configure_file("${CMAKE_SOURCE_DIR}/Android.mk.modules.in" "${CMAKE_CURRENT_BINARY_DIR}/Android.mk")
endif()

