package com.scalarmachine.skijaawt;

import java.awt.*;
import java.awt.event.*;

// build: javac -h ../../../macos com/scalarmachine/**/macos/*.java
public class SkiaMetalCanvas extends Canvas {
    static {
        System.loadLibrary("skija_awt_x64");
    }

    protected native int nInitialize();

    protected native int nRender();
}
