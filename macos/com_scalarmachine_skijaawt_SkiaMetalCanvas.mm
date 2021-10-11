#import "com_scalarmachine_skijaawt_SkiaMetalCanvas.h"
#import "jawt/jawt_helper.h"

#import <Metal/Metal.h>
#import <Cocoa/Cocoa.h>
#import <QuartzCore/QuartzCore.h>
#import <simd/simd.h>

bool inited = false;
id <MTLDevice> device;
id <MTLCommandQueue> queue;
CAMetalLayer *layer;

id <MTLRenderPipelineState> rps;

id <CAMetalDrawable> currentDrawable;
id <MTLCommandBuffer> currentCb;

bool initialize();

/*
 * Class:     com_scalarmachine_skijaawt_SkiaMetalCanvas
 * Method:    nPing
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_scalarmachine_skijaawt_SkiaMetalCanvas_nPing
  (JNIEnv *, jclass) {
    return 123;
  }

/*
 * Class:     com_scalarmachine_skijaawt_SkiaMetalCanvas
 * Method:    nInitialize
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_scalarmachine_skijaawt_SkiaMetalCanvas_nInitialize
  (JNIEnv *env, jobject canvas) {
    JawtHelper helper(env, canvas);

    [CATransaction begin];

    if (!initialize()) {
      [CATransaction commit];
      return -1;
    }

    id <JAWT_SurfaceLayers> surfaceLayers = (id) helper.dsi->platformInfo;
    surfaceLayers.layer = layer;

    [CATransaction commit];

    return 0;
  }

/*
 * Class:     com_scalarmachine_skijaawt_SkiaMetalCanvas
 * Method:    nBeginRender
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_scalarmachine_skijaawt_SkiaMetalCanvas_nBeginRender
  (JNIEnv *, jobject) {
    currentDrawable = [layer nextDrawable];
    CFRetain(currentDrawable);
    currentCb = [queue commandBuffer];
    CFRetain(currentCb);
    currentCb.label = @"Present";
    return 0;
  }

/*
 * Class:     com_scalarmachine_skijaawt_SkiaMetalCanvas
 * Method:    nRender
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_scalarmachine_skijaawt_SkiaMetalCanvas_nRender
  (JNIEnv *, jobject) {
    return 0;
  }

/*
 * Class:     com_scalarmachine_skijaawt_SkiaMetalCanvas
 * Method:    nSwapBuffers
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_scalarmachine_skijaawt_SkiaMetalCanvas_nSwapBuffers
  (JNIEnv *, jobject) {
    id <CAMetalDrawable> drawable = currentDrawable;
    assert(drawable);

    id <MTLCommandBuffer> cb = currentCb;
    assert(cb);

    [cb presentDrawable:drawable];
    [cb commit];

    CFRelease(cb);
    currentCb = nil;
    CFRelease(drawable);
    currentDrawable = nil;
    return 0;
  }

bool initialize() {
  if (inited) return true;

  device = MTLCreateSystemDefaultDevice();
  queue = [device newCommandQueue];
  layer = [CAMetalLayer layer];
  layer.device = device;
  layer.pixelFormat = MTLPixelFormatBGRA8Unorm;

  CGColorRef color = CGColorCreateGenericRGB(0.5f, 1.0f, 0.5f, 1.0f);
  layer.backgroundColor = color;


  // layer.displaySyncEnabled = YES;

  // layer.layoutManager = [CAConstraintLayoutManager layoutManager];
  // layer.autoresizingMask = kCALayerHeightSizable | kCALayerWidthSizable;
  // layer.contentsGravity = kCAGravityTopLeft;
  // layer.magnificationFilter = kCAFilterNearest;
  // NSColorSpace *cs = surfaceLayers.windowLayer.colorspace;
  // layer.colorspace = cs.CGColorSpace;

  // CGRect frame = CGRectMake(0, 0, backingSize.width, backingSize.height);
  // NSView *view = [[NSView alloc] initWithFrame:frame];
  // view.layer = layer;
  // view.wantsLayer = YES;


  // MTLCompileOptions *compileOptions = [MTLCompileOptions new];
  // compileOptions.languageVersion = MTLLanguageVersion1_1;
  // NSError *compileError;
  // id <MTLLibrary> lib = [device newLibraryWithSource:
  //                 @"#include <metal_stdlib>\n"
  //                 "using namespace metal;\n"
  //                 "vertex float4 v_simple(\n"
  //                 "    constant float4* in  [[buffer(0)]],\n"
  //                 "    uint             vid [[vertex_id]])\n"
  //                 "{\n"
  //                 "    return in[vid];\n"
  //                 "}\n"
  //                 "fragment float4 f_simple(\n"
  //                 "    float4 in [[stage_in]])\n"
  //                 "{\n"
  //                 "    return float4(1, 0, 0, 1);\n"
  //                 "}\n"
  //                                             options:compileOptions error:&compileError];
  // if (!lib) {
  //   NSLog(@"can't create library: %@", compileError);
  //   return false;
  // }

  // id <MTLFunction> vs = [lib newFunctionWithName:@"v_simple"];
  // assert(vs);
  // id <MTLFunction> fs = [lib newFunctionWithName:@"f_simple"];
  // assert(fs);

  // id <MTLCommandQueue> cq = [device newCommandQueue];
  // assert(cq);

  // MTLRenderPipelineDescriptor *rpd = [MTLRenderPipelineDescriptor new];
  // rpd.vertexFunction = vs;
  // rpd.fragmentFunction = fs;
  // rpd.colorAttachments[0].pixelFormat = layer.pixelFormat;
  // rps = [device newRenderPipelineStateWithDescriptor:rpd error:nullptr];
  // assert(rps);

  // CFRelease(cq);
  // CFRelease(lib);

  inited = true;

  printf("initialized\n");

  return true;
}
