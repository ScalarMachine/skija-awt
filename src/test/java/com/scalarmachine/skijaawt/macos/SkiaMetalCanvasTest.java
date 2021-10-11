package com.scalarmachine.skijaawt;

import java.awt.*;
import java.awt.event.*;

// build: javac com/scalarmachine/**/macos/*.java
// run: java -Djava.library.path=../../../ com.scalarmachine.skijaawt.SkiaMetalCanvasTest

public class SkiaMetalCanvasTest {
    public static void main(String[] args) {
        System.out.println("Hello World");

        Frame frame = new Frame();
        frame.setPreferredSize(new Dimension(400, 300));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent ev) {
                System.exit(0);
            }
        });

        SkiaMetalCanvas canvas = new SkiaMetalCanvas();
        frame.add(canvas);

        EventQueue.invokeLater(() -> {
            canvas.nInitialize();
            frame.setVisible(true);
            frame.transferFocus();

            // canvas.oldTime = System.nanoTime();

            new Thread(() -> {
                while (true) {
                    if (!canvas.isValid()) return;
                    canvas.nRender();
                }
            }).start();
        });
    }
}
