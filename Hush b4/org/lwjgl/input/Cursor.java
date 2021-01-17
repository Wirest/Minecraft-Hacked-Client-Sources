// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.input;

import org.lwjgl.LWJGLUtil;
import org.lwjgl.BufferUtils;
import org.lwjgl.Sys;
import java.nio.Buffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.LWJGLException;
import java.nio.IntBuffer;

public class Cursor
{
    public static final int CURSOR_ONE_BIT_TRANSPARENCY = 1;
    public static final int CURSOR_8_BIT_ALPHA = 2;
    public static final int CURSOR_ANIMATION = 4;
    private final CursorElement[] cursors;
    private int index;
    private boolean destroyed;
    
    public Cursor(final int width, final int height, final int xHotspot, int yHotspot, final int numImages, final IntBuffer images, final IntBuffer delays) throws LWJGLException {
        synchronized (OpenGLPackageAccess.global_lock) {
            if ((getCapabilities() & 0x1) == 0x0) {
                throw new LWJGLException("Native cursors not supported");
            }
            BufferChecks.checkBufferSize(images, width * height * numImages);
            if (delays != null) {
                BufferChecks.checkBufferSize(delays, numImages);
            }
            if (!Mouse.isCreated()) {
                throw new IllegalStateException("Mouse must be created before creating cursor objects");
            }
            if (width * height * numImages > images.remaining()) {
                throw new IllegalArgumentException("width*height*numImages > images.remaining()");
            }
            if (xHotspot >= width || xHotspot < 0) {
                throw new IllegalArgumentException("xHotspot > width || xHotspot < 0");
            }
            if (yHotspot >= height || yHotspot < 0) {
                throw new IllegalArgumentException("yHotspot > height || yHotspot < 0");
            }
            Sys.initialize();
            yHotspot = height - 1 - yHotspot;
            this.cursors = createCursors(width, height, xHotspot, yHotspot, numImages, images, delays);
        }
    }
    
    public static int getMinCursorSize() {
        synchronized (OpenGLPackageAccess.global_lock) {
            if (!Mouse.isCreated()) {
                throw new IllegalStateException("Mouse must be created.");
            }
            return Mouse.getImplementation().getMinCursorSize();
        }
    }
    
    public static int getMaxCursorSize() {
        synchronized (OpenGLPackageAccess.global_lock) {
            if (!Mouse.isCreated()) {
                throw new IllegalStateException("Mouse must be created.");
            }
            return Mouse.getImplementation().getMaxCursorSize();
        }
    }
    
    public static int getCapabilities() {
        synchronized (OpenGLPackageAccess.global_lock) {
            if (Mouse.getImplementation() != null) {
                return Mouse.getImplementation().getNativeCursorCapabilities();
            }
            return OpenGLPackageAccess.createImplementation().getNativeCursorCapabilities();
        }
    }
    
    private static CursorElement[] createCursors(final int width, final int height, final int xHotspot, final int yHotspot, final int numImages, final IntBuffer images, final IntBuffer delays) throws LWJGLException {
        final IntBuffer images_copy = BufferUtils.createIntBuffer(images.remaining());
        flipImages(width, height, numImages, images, images_copy);
        CursorElement[] cursors = null;
        switch (LWJGLUtil.getPlatform()) {
            case 2: {
                convertARGBtoABGR(images_copy);
                cursors = new CursorElement[numImages];
                for (int i = 0; i < numImages; ++i) {
                    final Object handle = Mouse.getImplementation().createCursor(width, height, xHotspot, yHotspot, 1, images_copy, null);
                    final long delay = (delays != null) ? delays.get(i) : 0L;
                    final long timeout = System.currentTimeMillis();
                    cursors[i] = new CursorElement(handle, delay, timeout);
                    images_copy.position(width * height * (i + 1));
                }
                break;
            }
            case 3: {
                cursors = new CursorElement[numImages];
                for (int i = 0; i < numImages; ++i) {
                    for (int size = width * height, j = 0; j < size; ++j) {
                        final int index = j + i * size;
                        final int alpha = images_copy.get(index) >> 24 & 0xFF;
                        if (alpha != 255) {
                            images_copy.put(index, 0);
                        }
                    }
                    final Object handle2 = Mouse.getImplementation().createCursor(width, height, xHotspot, yHotspot, 1, images_copy, null);
                    final long delay2 = (delays != null) ? delays.get(i) : 0L;
                    final long timeout2 = System.currentTimeMillis();
                    cursors[i] = new CursorElement(handle2, delay2, timeout2);
                    images_copy.position(width * height * (i + 1));
                }
                break;
            }
            case 1: {
                final Object handle3 = Mouse.getImplementation().createCursor(width, height, xHotspot, yHotspot, numImages, images_copy, delays);
                final CursorElement cursor_element = new CursorElement(handle3, -1L, -1L);
                cursors = new CursorElement[] { cursor_element };
                break;
            }
            default: {
                throw new RuntimeException("Unknown OS");
            }
        }
        return cursors;
    }
    
    private static void convertARGBtoABGR(final IntBuffer imageBuffer) {
        for (int i = 0; i < imageBuffer.limit(); ++i) {
            final int argbColor = imageBuffer.get(i);
            final byte alpha = (byte)(argbColor >>> 24);
            final byte blue = (byte)(argbColor >>> 16);
            final byte green = (byte)(argbColor >>> 8);
            final byte red = (byte)argbColor;
            final int abgrColor = ((alpha & 0xFF) << 24) + ((red & 0xFF) << 16) + ((green & 0xFF) << 8) + (blue & 0xFF);
            imageBuffer.put(i, abgrColor);
        }
    }
    
    private static void flipImages(final int width, final int height, final int numImages, final IntBuffer images, final IntBuffer images_copy) {
        for (int i = 0; i < numImages; ++i) {
            final int start_index = i * width * height;
            flipImage(width, height, start_index, images, images_copy);
        }
    }
    
    private static void flipImage(final int width, final int height, final int start_index, final IntBuffer images, final IntBuffer images_copy) {
        for (int y = 0; y < height >> 1; ++y) {
            final int index_y_1 = y * width + start_index;
            final int index_y_2 = (height - y - 1) * width + start_index;
            for (int x = 0; x < width; ++x) {
                final int index1 = index_y_1 + x;
                final int index2 = index_y_2 + x;
                final int temp_pixel = images.get(index1 + images.position());
                images_copy.put(index1, images.get(index2 + images.position()));
                images_copy.put(index2, temp_pixel);
            }
        }
    }
    
    Object getHandle() {
        this.checkValid();
        return this.cursors[this.index].cursorHandle;
    }
    
    private void checkValid() {
        if (this.destroyed) {
            throw new IllegalStateException("The cursor is destroyed");
        }
    }
    
    public void destroy() {
        synchronized (OpenGLPackageAccess.global_lock) {
            if (this.destroyed) {
                return;
            }
            if (Mouse.getNativeCursor() == this) {
                try {
                    Mouse.setNativeCursor(null);
                }
                catch (LWJGLException ex) {}
            }
            for (final CursorElement cursor : this.cursors) {
                Mouse.getImplementation().destroyCursor(cursor.cursorHandle);
            }
            this.destroyed = true;
        }
    }
    
    protected void setTimeout() {
        this.checkValid();
        this.cursors[this.index].timeout = System.currentTimeMillis() + this.cursors[this.index].delay;
    }
    
    protected boolean hasTimedOut() {
        this.checkValid();
        return this.cursors.length > 1 && this.cursors[this.index].timeout < System.currentTimeMillis();
    }
    
    protected void nextCursor() {
        this.checkValid();
        this.index = ++this.index % this.cursors.length;
    }
    
    private static class CursorElement
    {
        final Object cursorHandle;
        final long delay;
        long timeout;
        
        CursorElement(final Object cursorHandle, final long delay, final long timeout) {
            this.cursorHandle = cursorHandle;
            this.delay = delay;
            this.timeout = timeout;
        }
    }
}
