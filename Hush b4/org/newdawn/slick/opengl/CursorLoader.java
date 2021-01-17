// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.opengl;

import java.nio.ByteOrder;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.lwjgl.LWJGLException;
import org.newdawn.slick.util.Log;
import java.nio.IntBuffer;
import org.newdawn.slick.util.ResourceLoader;
import org.lwjgl.input.Cursor;

public class CursorLoader
{
    private static CursorLoader single;
    
    static {
        CursorLoader.single = new CursorLoader();
    }
    
    public static CursorLoader get() {
        return CursorLoader.single;
    }
    
    private CursorLoader() {
    }
    
    public Cursor getCursor(final String ref, final int x, final int y) throws IOException, LWJGLException {
        LoadableImageData imageData = null;
        imageData = ImageDataFactory.getImageDataFor(ref);
        imageData.configureEdging(false);
        final ByteBuffer buf = imageData.loadImage(ResourceLoader.getResourceAsStream(ref), true, true, null);
        for (int i = 0; i < buf.limit(); i += 4) {
            final byte red = buf.get(i);
            final byte green = buf.get(i + 1);
            final byte blue = buf.get(i + 2);
            final byte alpha = buf.get(i + 3);
            buf.put(i + 2, red);
            buf.put(i + 1, green);
            buf.put(i, blue);
            buf.put(i + 3, alpha);
        }
        try {
            int yspot = imageData.getHeight() - y - 1;
            if (yspot < 0) {
                yspot = 0;
            }
            return new Cursor(imageData.getTexWidth(), imageData.getTexHeight(), x, yspot, 1, buf.asIntBuffer(), null);
        }
        catch (Throwable e) {
            Log.info("Chances are you cursor is too small for this platform");
            throw new LWJGLException(e);
        }
    }
    
    public Cursor getCursor(final ByteBuffer buf, final int x, final int y, final int width, final int height) throws IOException, LWJGLException {
        for (int i = 0; i < buf.limit(); i += 4) {
            final byte red = buf.get(i);
            final byte green = buf.get(i + 1);
            final byte blue = buf.get(i + 2);
            final byte alpha = buf.get(i + 3);
            buf.put(i + 2, red);
            buf.put(i + 1, green);
            buf.put(i, blue);
            buf.put(i + 3, alpha);
        }
        try {
            int yspot = height - y - 1;
            if (yspot < 0) {
                yspot = 0;
            }
            return new Cursor(width, height, x, yspot, 1, buf.asIntBuffer(), null);
        }
        catch (Throwable e) {
            Log.info("Chances are you cursor is too small for this platform");
            throw new LWJGLException(e);
        }
    }
    
    public Cursor getCursor(final ImageData imageData, final int x, final int y) throws IOException, LWJGLException {
        final ByteBuffer buf = imageData.getImageBufferData();
        for (int i = 0; i < buf.limit(); i += 4) {
            final byte red = buf.get(i);
            final byte green = buf.get(i + 1);
            final byte blue = buf.get(i + 2);
            final byte alpha = buf.get(i + 3);
            buf.put(i + 2, red);
            buf.put(i + 1, green);
            buf.put(i, blue);
            buf.put(i + 3, alpha);
        }
        try {
            int yspot = imageData.getHeight() - y - 1;
            if (yspot < 0) {
                yspot = 0;
            }
            return new Cursor(imageData.getTexWidth(), imageData.getTexHeight(), x, yspot, 1, buf.asIntBuffer(), null);
        }
        catch (Throwable e) {
            Log.info("Chances are you cursor is too small for this platform");
            throw new LWJGLException(e);
        }
    }
    
    public Cursor getAnimatedCursor(final String ref, final int x, final int y, final int width, final int height, final int[] cursorDelays) throws IOException, LWJGLException {
        final IntBuffer cursorDelaysBuffer = ByteBuffer.allocateDirect(cursorDelays.length * 4).order(ByteOrder.nativeOrder()).asIntBuffer();
        for (int i = 0; i < cursorDelays.length; ++i) {
            cursorDelaysBuffer.put(cursorDelays[i]);
        }
        cursorDelaysBuffer.flip();
        final LoadableImageData imageData = new TGAImageData();
        final ByteBuffer buf = imageData.loadImage(ResourceLoader.getResourceAsStream(ref), false, null);
        return new Cursor(width, height, x, y, cursorDelays.length, buf.asIntBuffer(), cursorDelaysBuffer);
    }
}
