package com.scalarmachine.skijaawt;

import java.awt.*;
import java.awt.event.*;

// build: javac -h ../../../macos com/scalarmachine/**/macos/*.java -d ../../../out
public class SkiaMetalCanvas extends Canvas {
    static {
        System.loadLibrary("skija_awt_x64");
    }

    protected static native int nPing();

    protected native int nInitialize();

    protected native int nBeginRender();

    protected native int nRender();

    protected native int nSwapBuffers();
}
