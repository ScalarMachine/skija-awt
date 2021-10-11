package com.scalarmachine.skijaawt;

import org.jetbrains.skija.*;
import org.jetbrains.skija.impl.*;

import java.awt.Frame;
import java.awt.Dimension;
import java.awt.EventQueue;
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

        SkiaMetalCanvas panel = new SkiaMetalCanvas();
        frame.add(panel);

        EventQueue.invokeLater(() -> {
            panel.nInitialize();
            frame.setVisible(true);
            frame.transferFocus();

            panel.nResize();

            // panel.oldTime = System.nanoTime();

            DirectContext directContext = DirectContext.makeMetal(
                panel.nGetDevicePtr(), panel.nGetQueuePtr());            

            new Thread(() -> {
                var fRotationAngle = 0f;
                while (true) {
                    if (!panel.isValid()) return;
                    panel.nBeginRender();

                    BackendRenderTarget renderTarget = BackendRenderTarget.makeMetal(
                        (int) panel.nGetBackWidth(),
                        (int) panel.nGetBackHeight(),
                        panel.nGetDrawableTexturePtr()
                    );
                    Surface surface = Surface.makeFromBackendRenderTarget(
                        directContext,
                        renderTarget,
                        SurfaceOrigin.TOP_LEFT,
                        SurfaceColorFormat.BGRA_8888,
                        ColorSpace.getDisplayP3(),
                        new SurfaceProps(PixelGeometry.RGB_H));

                    // panel.nRender();

                    var canvas = surface.getCanvas();
                    canvas.clear(0);

                    fRotationAngle += 0.2f;
                    if (fRotationAngle > 360.0f) {
                        fRotationAngle -= 360.0f;
                    }
                    // canvas.translate(256, 256);
                    canvas.rotate(fRotationAngle);

                    Paint paint = new Paint();
                    paint.setStrokeWidth(3f);
                    paint.setColor(0xFFFFFFFF);
                    canvas.drawLine(10, 10, 100, 100, paint);

                    surface.flushAndSubmit();

                    panel.nSwapBuffers();

                    surface.close();
                    renderTarget.close();
                }
            }).start();
        });
    }
}
