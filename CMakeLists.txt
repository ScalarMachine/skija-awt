cmake_minimum_required(VERSION 3.16)
project(skija_awt)
set(NAME skija_awt)

set(ARCH x64)

set(CMAKE_CXX_STANDARD 17)

add_library(
    ${NAME} SHARED macos/com_scalarmachine_skijaawt_SkiaMetalCanvas.mm
)

# set_source_files_properties(
#         PROPERTIES
#         MACOSX_PACKAGE_LOCATION
#         Resources
# )

if(APPLE)
    target_include_directories(${NAME} PRIVATE $ENV{JAVA_HOME}/include $ENV{JAVA_HOME}/include/darwin)
    set_target_properties(${NAME} PROPERTIES OUTPUT_NAME "${NAME}_${ARCH}")

elseif(UNIX)
    target_include_directories(${NAME} PRIVATE $ENV{JAVA_HOME}/include $ENV{JAVA_HOME}/include/linux)

elseif(WIN32)
    set(CMAKE_C_COMPILER cl)
    set(CMAKE_CXX_COMPILER cl)

    target_include_directories(${NAME} PRIVATE $ENV{JAVA_HOME}/include $ENV{JAVA_HOME}/include/win32)
    target_link_libraries(${NAME} d3d12 dxgi d3dcompiler)

    set(CMAKE_CXX_FLAGS_RELEASE "/MT")
    set(CMAKE_CXX_FLAGS_DEBUG "/MTd")
    target_link_options(${NAME} PRIVATE /INCREMENTAL:NO /NODEFAULTLIB:MSVCRT /machine:X64)
endif()

find_library(JAWT_LIBRARY jawt HINTS "$ENV{JAVA_HOME}/lib")

target_link_libraries(
        ${NAME}
        ${JAWT_LIBRARY}
)

if (APPLE)
    # find_library(COCOA_LIBRARY Cocoa)
    # find_library(METAL_LIBRARY Metal)
    target_link_libraries(${NAME}
            # ${COCOA_LIBRARY}
            # ${METAL_LIBRARY}
            "-framework CoreFoundation"
            "-framework AppKit"
            "-framework MetalKit"
            "-framework Metal"
            "-framework QuartzCore")
endif()
