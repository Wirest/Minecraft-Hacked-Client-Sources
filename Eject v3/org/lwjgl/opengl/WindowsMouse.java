package org.lwjgl.opengl;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

final class WindowsMouse {
    private final long hwnd;
    private final int mouse_button_count;
    private final boolean has_wheel;
    private final EventQueue event_queue = new EventQueue(22);
    private final ByteBuffer mouse_event = ByteBuffer.allocate(22);
    private final Object blank_cursor;
    private boolean mouse_grabbed;
    private byte[] button_states;
    private int accum_dx;
    private int accum_dy;
    private int accum_dwheel;
    private int last_x;
    private int last_y;

    WindowsMouse(long paramLong)
            throws LWJGLException {
        this.hwnd = paramLong;
        this.mouse_button_count = Math.min(5, WindowsDisplay.getSystemMetrics(43));
        this.has_wheel = (WindowsDisplay.getSystemMetrics(75) != 0);
        this.blank_cursor = createBlankCursor();
        this.button_states = new byte[this.mouse_button_count];
    }

    private Object createBlankCursor()
            throws LWJGLException {
        int i = WindowsDisplay.getSystemMetrics(13);
        int j = WindowsDisplay.getSystemMetrics(14);
        IntBuffer localIntBuffer = BufferUtils.createIntBuffer(i * j);
        return WindowsDisplay.doCreateCursor(i, j, 0, 0, 1, localIntBuffer, null);
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

    public void poll(IntBuffer paramIntBuffer, ByteBuffer paramByteBuffer, WindowsDisplay paramWindowsDisplay) {
        for (int i = 0; i < paramIntBuffer.remaining(); i++) {
            paramIntBuffer.put(paramIntBuffer.position() | i, 0);
        }
        i = this.mouse_button_count;
        paramIntBuffer.put(paramIntBuffer.position() | 0x2, this.accum_dwheel);
        if (i > this.button_states.length) {
            i = this.button_states.length;
        }
        for (int j = 0; j < i; j++) {
            paramByteBuffer.put(paramByteBuffer.position() | j, this.button_states[j]);
        }
        if (isGrabbed()) {
            paramIntBuffer.put(paramIntBuffer.position() | 0x0, this.accum_dx);
            paramIntBuffer.put(paramIntBuffer.position() | 0x1, this.accum_dy);
            if ((paramWindowsDisplay.isActive()) && (paramWindowsDisplay.isVisible()) && ((this.accum_dx != 0) || (this.accum_dy != 0))) {
                WindowsDisplay.centerCursor(this.hwnd);
            }
        } else {
            paramIntBuffer.put(paramIntBuffer.position() | 0x0, this.last_x);
            paramIntBuffer.put(paramIntBuffer.position() | 0x1, this.last_y);
        }
        this.accum_dx = (this.accum_dy = this.accum_dwheel = 0);
    }

    private void putMouseEventWithCoords(byte paramByte1, byte paramByte2, int paramInt1, int paramInt2, int paramInt3, long paramLong) {
        this.mouse_event.clear();
        this.mouse_event.put(paramByte1).put(paramByte2).putInt(paramInt1).putInt(paramInt2).putInt(paramInt3).putLong(paramLong);
        this.mouse_event.flip();
        this.event_queue.putEvent(this.mouse_event);
    }

    private void putMouseEvent(byte paramByte1, byte paramByte2, int paramInt, long paramLong) {
        if (this.mouse_grabbed) {
            putMouseEventWithCoords(paramByte1, paramByte2, 0, 0, paramInt, paramLong);
        } else {
            putMouseEventWithCoords(paramByte1, paramByte2, this.last_x, this.last_y, paramInt, paramLong);
        }
    }

    public void read(ByteBuffer paramByteBuffer) {
        this.event_queue.copyEvents(paramByteBuffer);
    }

    public Object getBlankCursor() {
        return this.blank_cursor;
    }

    public void grab(boolean paramBoolean) {
        this.mouse_grabbed = paramBoolean;
        this.event_queue.clearEvents();
    }

    public void handleMouseScrolled(int paramInt, long paramLong) {
        this.accum_dwheel |= paramInt;
        putMouseEvent((byte) -1, (byte) 0, paramInt, paramLong * 1000000L);
    }

    public void setPosition(int paramInt1, int paramInt2) {
        this.last_x = paramInt1;
        this.last_y = paramInt2;
    }

    public void destroy() {
        WindowsDisplay.doDestroyCursor(this.blank_cursor);
    }

    public void handleMouseMoved(int paramInt1, int paramInt2, long paramLong) {
        int i = paramInt1 - this.last_x;
        int j = paramInt2 - this.last_y;
        if ((i != 0) || (j != 0)) {
            this.accum_dx |= i;
            this.accum_dy |= j;
            this.last_x = paramInt1;
            this.last_y = paramInt2;
            long l = paramLong * 1000000L;
            if (this.mouse_grabbed) {
                putMouseEventWithCoords((byte) -1, (byte) 0, i, j, 0, l);
            } else {
                putMouseEventWithCoords((byte) -1, (byte) 0, paramInt1, paramInt2, 0, l);
            }
        }
    }

    public void handleMouseButton(byte paramByte1, byte paramByte2, long paramLong) {
        putMouseEvent(paramByte1, paramByte2, 0, paramLong * 1000000L);
        if (paramByte1 < this.button_states.length) {
            this.button_states[paramByte1] = (paramByte2 != 0 ? 1 : 0);
        }
    }
}




