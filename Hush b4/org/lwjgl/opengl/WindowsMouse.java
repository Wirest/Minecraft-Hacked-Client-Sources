// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import java.nio.ByteBuffer;

final class WindowsMouse
{
    private final long hwnd;
    private final int mouse_button_count;
    private final boolean has_wheel;
    private final EventQueue event_queue;
    private final ByteBuffer mouse_event;
    private final Object blank_cursor;
    private boolean mouse_grabbed;
    private byte[] button_states;
    private int accum_dx;
    private int accum_dy;
    private int accum_dwheel;
    private int last_x;
    private int last_y;
    
    WindowsMouse(final long hwnd) throws LWJGLException {
        this.event_queue = new EventQueue(22);
        this.mouse_event = ByteBuffer.allocate(22);
        this.hwnd = hwnd;
        this.mouse_button_count = Math.min(5, WindowsDisplay.getSystemMetrics(43));
        this.has_wheel = (WindowsDisplay.getSystemMetrics(75) != 0);
        this.blank_cursor = this.createBlankCursor();
        this.button_states = new byte[this.mouse_button_count];
    }
    
    private Object createBlankCursor() throws LWJGLException {
        final int width = WindowsDisplay.getSystemMetrics(13);
        final int height = WindowsDisplay.getSystemMetrics(14);
        final IntBuffer pixels = BufferUtils.createIntBuffer(width * height);
        return WindowsDisplay.doCreateCursor(width, height, 0, 0, 1, pixels, null);
    }
    
    public boolean isGrabbed() {
        return this.mouse_grabbed;
    }
    
    public boolean hasWheel() {
        return this.has_wheel;
    }
    
    public int getButtonCount() {
        return this.mouse_button_count;
    }
    
    public void poll(final IntBuffer coord_buffer, final ByteBuffer buttons, final WindowsDisplay display) {
        for (int i = 0; i < coord_buffer.remaining(); ++i) {
            coord_buffer.put(coord_buffer.position() + i, 0);
        }
        int num_buttons = this.mouse_button_count;
        coord_buffer.put(coord_buffer.position() + 2, this.accum_dwheel);
        if (num_buttons > this.button_states.length) {
            num_buttons = this.button_states.length;
        }
        for (int j = 0; j < num_buttons; ++j) {
            buttons.put(buttons.position() + j, this.button_states[j]);
        }
        if (this.isGrabbed()) {
            coord_buffer.put(coord_buffer.position() + 0, this.accum_dx);
            coord_buffer.put(coord_buffer.position() + 1, this.accum_dy);
            if (display.isActive() && display.isVisible() && (this.accum_dx != 0 || this.accum_dy != 0)) {
                WindowsDisplay.centerCursor(this.hwnd);
            }
        }
        else {
            coord_buffer.put(coord_buffer.position() + 0, this.last_x);
            coord_buffer.put(coord_buffer.position() + 1, this.last_y);
        }
        final int accum_dx = 0;
        this.accum_dwheel = accum_dx;
        this.accum_dy = accum_dx;
        this.accum_dx = accum_dx;
    }
    
    private void putMouseEventWithCoords(final byte button, final byte state, final int coord1, final int coord2, final int dz, final long nanos) {
        this.mouse_event.clear();
        this.mouse_event.put(button).put(state).putInt(coord1).putInt(coord2).putInt(dz).putLong(nanos);
        this.mouse_event.flip();
        this.event_queue.putEvent(this.mouse_event);
    }
    
    private void putMouseEvent(final byte button, final byte state, final int dz, final long nanos) {
        if (this.mouse_grabbed) {
            this.putMouseEventWithCoords(button, state, 0, 0, dz, nanos);
        }
        else {
            this.putMouseEventWithCoords(button, state, this.last_x, this.last_y, dz, nanos);
        }
    }
    
    public void read(final ByteBuffer buffer) {
        this.event_queue.copyEvents(buffer);
    }
    
    public Object getBlankCursor() {
        return this.blank_cursor;
    }
    
    public void grab(final boolean grab) {
        this.mouse_grabbed = grab;
        this.event_queue.clearEvents();
    }
    
    public void handleMouseScrolled(final int event_dwheel, final long millis) {
        this.accum_dwheel += event_dwheel;
        this.putMouseEvent((byte)(-1), (byte)0, event_dwheel, millis * 1000000L);
    }
    
    public void setPosition(final int x, final int y) {
        this.last_x = x;
        this.last_y = y;
    }
    
    public void destroy() {
        WindowsDisplay.doDestroyCursor(this.blank_cursor);
    }
    
    public void handleMouseMoved(final int x, final int y, final long millis) {
        final int dx = x - this.last_x;
        final int dy = y - this.last_y;
        if (dx != 0 || dy != 0) {
            this.accum_dx += dx;
            this.accum_dy += dy;
            this.last_x = x;
            this.last_y = y;
            final long nanos = millis * 1000000L;
            if (this.mouse_grabbed) {
                this.putMouseEventWithCoords((byte)(-1), (byte)0, dx, dy, 0, nanos);
            }
            else {
                this.putMouseEventWithCoords((byte)(-1), (byte)0, x, y, 0, nanos);
            }
        }
    }
    
    public void handleMouseButton(final byte button, final byte state, final long millis) {
        this.putMouseEvent(button, state, 0, millis * 1000000L);
        if (button < this.button_states.length) {
            this.button_states[button] = (byte)((state != 0) ? 1 : 0);
        }
    }
}
