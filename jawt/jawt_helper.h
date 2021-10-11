#pragma once

#include <jni.h>
#include <jawt_md.h>

class JawtHelper {
  JNIEnv *env;
  jclass canvas;

public:
  JAWT awt;
  JAWT_DrawingSurface *ds;
  JAWT_DrawingSurfaceInfo *dsi;

  jint lock;

  int error;

  JawtHelper(JNIEnv *env, jclass canvas): env(env), canvas(canvas) {
    error = init();
  }

  int init();

  ~JawtHelper();
};
