package com.scalarmachine.skijaawt;

import org.jetbrains.skija.*;
import org.jetbrains.skija.impl.*;

import java.awt.Frame;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.*;

public class SkiaTest extends Frame {
    SkiaMetalCanvas panel;
    
    public SkiaTest() {
        setPreferredSize(new Dimension(400, 300));
        pack();
        setLocationRelativeTo(null);

        panel = new SkiaMetalCanvas();
        add(panel);

        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent ev) {
                System.exit(0);
            }
        });

        addComponentListener(new ComponentAdapter() {
            @Override public void componentResized(ComponentEvent ev) {
                panel.nResize();
            }
        });
    }

    public void myShow() {
        EventQueue.invokeLater(() -> {
            panel.nInitialize();
            setVisible(true);
            transferFocus();

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

                    float dpi = 2.0f;

                    canvas.scale(dpi, dpi);

                    fRotationAngle += 0.2f;
                    if (fRotationAngle > 360.0f) {
                        fRotationAngle -= 360.0f;
                    }
                    canvas.translate(256, 256);
                    canvas.rotate(fRotationAngle);

                    Paint paint = new Paint();
                    paint.setStrokeWidth(3f);
                    paint.setColor(0xFFFFFFFF);
                    canvas.drawLine(-50, -50, 100, 100, paint);

                    surface.flushAndSubmit();

                    panel.nSwapBuffers();

                    surface.close();
                    renderTarget.close();
                }
            }).start();
        });
    }

    public static void main(String[] args) {
        System.out.println(SkiaMetalCanvas.nPing());

        SkiaTest frame = new SkiaTest();
        frame.myShow();
    }
}
