package com.scalarmachine.skijaawt;

import org.jetbrains.skija.*;
import org.jetbrains.skija.impl.*;

import java.awt.*;
import java.awt.event.*;

public class SkiaTest {
    public static void main(String[] args) {
        System.out.println(SkiaMetalCanvas.nPing());

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
                    canvas.nBeginRender();

                    long texture = canvas.nGetMetalTexture();

                    System.out.println(texture);

                    BackendRenderTarget renderTarget = BackendRenderTarget.makeMetal(
                        (int) 200,
                        (int) 200,
                        texture
                    );

                    // canvas.nRender();
                    canvas.nSwapBuffers();
                }
            }).start();
        });
    }
}
