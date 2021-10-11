#include "jawt_helper.h"

int JawtHelper::init() {
  /* Get the AWT */
  awt.version = JAWT_VERSION_9;
  if (JAWT_GetAWT(env, &awt) == JNI_FALSE) {
    printf("AWT Not found\n");
    return -1;
  }

  /* Get the drawing surface */
  ds = awt.GetDrawingSurface(env, canvas);
  if (ds == nullptr) {
    printf("NULL drawing surface\n");
    return -1;
  }

  /* Lock the drawing surface */
  lock = ds->Lock(ds);
  if ((lock & JAWT_LOCK_ERROR) != 0) {
    printf("Error locking surface\n");
    awt.FreeDrawingSurface(ds);
    return -1;
  }

  /* Get the drawing surface info */
  dsi = ds->GetDrawingSurfaceInfo(ds);
  if (dsi == nullptr) {
    printf("Error getting surface info\n");
    ds->Unlock(ds);
    awt.FreeDrawingSurface(ds);
    return -1;
  }

  return 0;
}

JawtHelper::~JawtHelper() {
  if (error == 0) {
    error = 1;

    /* Free the drawing surface info */
    ds->FreeDrawingSurfaceInfo(dsi);

    /* Unlock the drawing surface */
    ds->Unlock(ds);

    /* Free the drawing surface */
    awt.FreeDrawingSurface(ds);
  }
}
