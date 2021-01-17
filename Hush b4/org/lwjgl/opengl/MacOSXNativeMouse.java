// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.LWJGLException;
import org.lwjgl.BufferUtils;
import java.nio.IntBuffer;
import java.nio.ByteBuffer;

final class MacOSXNativeMouse extends EventQueue
{
    private static final int WHEEL_SCALE = 120;
    private static final int NUM_BUTTONS = 3;
    private ByteBuffer window_handle;
    private MacOSXDisplay display;
    private boolean grabbed;
    private float accum_dx;
    private float accum_dy;
    private int accum_dz;
    private float last_x;
    private float last_y;
    private boolean saved_control_state;
    private final ByteBuffer event;
    private IntBuffer delta_buffer;
    private int skip_event;
    private final byte[] buttons;
    
    MacOSXNativeMouse(final MacOSXDisplay display, final ByteBuffer window_handle) {
        super(22);
        this.event = ByteBuffer.allocate(22);
        this.delta_buffer = BufferUtils.createIntBuffer(2);
        this.buttons = new byte[3];
        this.display = display;
        this.window_handle = window_handle;
    }
    
    private native void nSetCursorPosition(final ByteBuffer p0, final int p1, final int p2);
    
    public static native void nGrabMouse(final boolean p0);
    
    private native void nRegisterMouseListener(final ByteBuffer p0);
    
    private native void nUnregisterMouseListener(final ByteBuffer p0);
    
    private static native long nCreateCursor(final int p0, final int p1, final int p2, final int p3, final int p4, final IntBuffer p5, final int p6, final IntBuffer p7, final int p8) throws LWJGLException;
    
    private static native void nDestroyCursor(final long p0);
    
    private static native void nSetCursor(final long p0) throws LWJGLException;
    
    public synchronized void register() {
        this.nRegisterMouseListener(this.window_handle);
    }
    
    public static long createCursor(final int width, final int height, final int xHotspot, final int yHotspot, final int numImages, final IntBuffer images, final IntBuffer delays) throws LWJGLException {
        try {
            return nCreateCursor(width, height, xHotspot, yHotspot, numImages, images, images.position(), delays, (delays != null) ? delays.position() : -1);
        }
        catch (LWJGLException e) {
            throw e;
        }
    }
    
    public static void destroyCursor(final long cursor_handle) {
        nDestroyCursor(cursor_handle);
    }
    
    public static void setCursor(final long cursor_handle) throws LWJGLException {
        try {
            nSetCursor(cursor_handle);
        }
        catch (LWJGLException e) {
            throw e;
        }
    }
    
    public synchronized void setCursorPosition(final int x, final int y) {
        this.nSetCursorPosition(this.window_handle, x, y);
    }
    
    public synchronized void unregister() {
        this.nUnregisterMouseListener(this.window_handle);
    }
    
    public synchronized void setGrabbed(final boolean grabbed) {
        nGrabMouse(this.grabbed = grabbed);
        this.skip_event = 1;
        final float n = 0.0f;
        this.accum_dy = n;
        this.accum_dx = n;
    }
    
    public synchronized boolean isGrabbed() {
        return this.grabbed;
    }
    
    protected void resetCursorToCenter() {
        this.clearEvents();
        final float n = 0.0f;
        this.accum_dy = n;
        this.accum_dx = n;
        if (this.display != null) {
            this.last_x = (float)(this.display.getWidth() / 2);
            this.last_y = (float)(this.display.getHeight() / 2);
        }
    }
    
    private void putMouseEvent(final byte button, final byte state, final int dz, final long nanos) {
        if (this.grabbed) {
            this.putMouseEventWithCoords(button, state, 0, 0, dz, nanos);
        }
        else {
            this.putMouseEventWithCoords(button, state, (int)this.last_x, (int)this.last_y, dz, nanos);
        }
    }
    
    protected void putMouseEventWithCoords(final byte button, final byte state, final int coord1, final int coord2, final int dz, final long nanos) {
        this.event.clear();
        this.event.put(button).put(state).putInt(coord1).putInt(coord2).putInt(dz).putLong(nanos);
        this.event.flip();
        this.putEvent(this.event);
    }
    
    public synchronized void poll(final IntBuffer coord_buffer, final ByteBuffer buttons_buffer) {
        if (this.grabbed) {
            coord_buffer.put(0, (int)this.accum_dx);
            coord_buffer.put(1, (int)this.accum_dy);
        }
        else {
            coord_buffer.put(0, (int)this.last_x);
            coord_buffer.put(1, (int)this.last_y);
        }
        coord_buffer.put(2, this.accum_dz);
        final int accum_dz = 0;
        this.accum_dz = accum_dz;
        final float n = (float)accum_dz;
        this.accum_dy = n;
        this.accum_dx = n;
        final int old_position = buttons_buffer.position();
        buttons_buffer.put(this.buttons, 0, this.buttons.length);
        buttons_buffer.position(old_position);
    }
    
    private void setCursorPos(final float x, final float y, final long nanos) {
        if (this.grabbed) {
            return;
        }
        final float dx = x - this.last_x;
        final float dy = y - this.last_y;
        this.addDelta(dx, dy);
        this.last_x = x;
        this.last_y = y;
        this.putMouseEventWithCoords((byte)(-1), (byte)0, (int)x, (int)y, 0, nanos);
    }
    
    protected void addDelta(final float dx, final float dy) {
        this.accum_dx += dx;
        this.accum_dy += -dy;
    }
    
    public synchronized void setButton(final int button, final int state, final long nanos) {
        this.buttons[button] = (byte)state;
        this.putMouseEvent((byte)button, (byte)state, 0, nanos);
    }
    
    public synchronized void mouseMoved(final float x, final float y, final float dx, float dy, final float dz, final long nanos) {
        if (this.skip_event > 0) {
            --this.skip_event;
            if (this.skip_event == 0) {
                this.last_x = x;
                this.last_y = y;
            }
            return;
        }
        if (dz != 0.0f) {
            if (dy == 0.0f) {
                dy = dx;
            }
            final int wheel_amount = (int)(dy * 120.0f);
            this.accum_dz += wheel_amount;
            this.putMouseEvent((byte)(-1), (byte)0, wheel_amount, nanos);
        }
        else if (this.grabbed) {
            if (dx != 0.0f || dy != 0.0f) {
                this.putMouseEventWithCoords((byte)(-1), (byte)0, (int)dx, (int)(-dy), 0, nanos);
                this.addDelta(dx, dy);
            }
        }
        else {
            this.setCursorPos(x, y, nanos);
        }
    }
}
