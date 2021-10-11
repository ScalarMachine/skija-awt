package com.scalarmachine.skijaawt;

// build: javac -h ../../../macos com/scalarmachine/**/macos/*.java
public class SkiaMetalCanvas {
    static {
        System.loadLibrary("skija_awt_x64");
    }

    protected native int nInitialize();

    protected native int nRender();
}
