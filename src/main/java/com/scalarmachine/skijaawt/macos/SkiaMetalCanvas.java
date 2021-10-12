package com.scalarmachine.skijaawt.macos;

import java.awt.*;
import java.awt.event.*;

// build: javac -h ../../../macos com/scalarmachine/**/macos/*.java -d ../../../out
public class SkiaMetalCanvas extends Canvas {
    static {
        System.loadLibrary("skija_awt_x64");
    }

    protected static native int nPing();

    protected native int nInitialize();

    protected native long nGetDevicePtr();

    protected native long nGetQueuePtr();

    protected native int nResize();

    protected native float nGetBackWidth();

    protected native float nGetBackHeight();

    protected native int nBeginRender();

    protected native long nGetDrawableTexturePtr();

    protected native int nRender();

    protected native int nSwapBuffers();

    @Override
    public Graphics getGraphics() {
        return new NoOpGraphics2D(super.getGraphics());
    }

    @Override
    public void paint(Graphics g) {
        // System.out.println("Paint on SkiaMetalCanvas ignored");
    }
}
