// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.ByteBuffer;

final class LinuxEvent
{
    public static final int FocusIn = 9;
    public static final int FocusOut = 10;
    public static final int KeyPress = 2;
    public static final int KeyRelease = 3;
    public static final int ButtonPress = 4;
    public static final int ButtonRelease = 5;
    public static final int MotionNotify = 6;
    public static final int EnterNotify = 7;
    public static final int LeaveNotify = 8;
    public static final int UnmapNotify = 18;
    public static final int MapNotify = 19;
    public static final int Expose = 12;
    public static final int ConfigureNotify = 22;
    public static final int ClientMessage = 33;
    private final ByteBuffer event_buffer;
    
    LinuxEvent() {
        this.event_buffer = createEventBuffer();
    }
    
    private static native ByteBuffer createEventBuffer();
    
    public void copyFrom(final LinuxEvent event) {
        final int pos = this.event_buffer.position();
        final int event_pos = event.event_buffer.position();
        this.event_buffer.put(event.event_buffer);
        this.event_buffer.position(pos);
        event.event_buffer.position(event_pos);
    }
    
    public static native int getPending(final long p0);
    
    public void sendEvent(final long display, final long window, final boolean propagate, final long event_mask) {
        nSendEvent(this.event_buffer, display, window, propagate, event_mask);
    }
    
    private static native void nSendEvent(final ByteBuffer p0, final long p1, final long p2, final boolean p3, final long p4);
    
    public boolean filterEvent(final long window) {
        return nFilterEvent(this.event_buffer, window);
    }
    
    private static native boolean nFilterEvent(final ByteBuffer p0, final long p1);
    
    public void nextEvent(final long display) {
        nNextEvent(display, this.event_buffer);
    }
    
    private static native void nNextEvent(final long p0, final ByteBuffer p1);
    
    public int getType() {
        return nGetType(this.event_buffer);
    }
    
    private static native int nGetType(final ByteBuffer p0);
    
    public long getWindow() {
        return nGetWindow(this.event_buffer);
    }
    
    private static native long nGetWindow(final ByteBuffer p0);
    
    public void setWindow(final long window) {
        nSetWindow(this.event_buffer, window);
    }
    
    private static native void nSetWindow(final ByteBuffer p0, final long p1);
    
    public int getFocusMode() {
        return nGetFocusMode(this.event_buffer);
    }
    
    private static native int nGetFocusMode(final ByteBuffer p0);
    
    public int getFocusDetail() {
        return nGetFocusDetail(this.event_buffer);
    }
    
    private static native int nGetFocusDetail(final ByteBuffer p0);
    
    public long getClientMessageType() {
        return nGetClientMessageType(this.event_buffer);
    }
    
    private static native long nGetClientMessageType(final ByteBuffer p0);
    
    public int getClientData(final int index) {
        return nGetClientData(this.event_buffer, index);
    }
    
    private static native int nGetClientData(final ByteBuffer p0, final int p1);
    
    public int getClientFormat() {
        return nGetClientFormat(this.event_buffer);
    }
    
    private static native int nGetClientFormat(final ByteBuffer p0);
    
    public long getButtonTime() {
        return nGetButtonTime(this.event_buffer);
    }
    
    private static native long nGetButtonTime(final ByteBuffer p0);
    
    public int getButtonState() {
        return nGetButtonState(this.event_buffer);
    }
    
    private static native int nGetButtonState(final ByteBuffer p0);
    
    public int getButtonType() {
        return nGetButtonType(this.event_buffer);
    }
    
    private static native int nGetButtonType(final ByteBuffer p0);
    
    public int getButtonButton() {
        return nGetButtonButton(this.event_buffer);
    }
    
    private static native int nGetButtonButton(final ByteBuffer p0);
    
    public long getButtonRoot() {
        return nGetButtonRoot(this.event_buffer);
    }
    
    private static native long nGetButtonRoot(final ByteBuffer p0);
    
    public int getButtonXRoot() {
        return nGetButtonXRoot(this.event_buffer);
    }
    
    private static native int nGetButtonXRoot(final ByteBuffer p0);
    
    public int getButtonYRoot() {
        return nGetButtonYRoot(this.event_buffer);
    }
    
    private static native int nGetButtonYRoot(final ByteBuffer p0);
    
    public int getButtonX() {
        return nGetButtonX(this.event_buffer);
    }
    
    private static native int nGetButtonX(final ByteBuffer p0);
    
    public int getButtonY() {
        return nGetButtonY(this.event_buffer);
    }
    
    private static native int nGetButtonY(final ByteBuffer p0);
    
    public long getKeyAddress() {
        return nGetKeyAddress(this.event_buffer);
    }
    
    private static native long nGetKeyAddress(final ByteBuffer p0);
    
    public long getKeyTime() {
        return nGetKeyTime(this.event_buffer);
    }
    
    private static native int nGetKeyTime(final ByteBuffer p0);
    
    public int getKeyType() {
        return nGetKeyType(this.event_buffer);
    }
    
    private static native int nGetKeyType(final ByteBuffer p0);
    
    public int getKeyKeyCode() {
        return nGetKeyKeyCode(this.event_buffer);
    }
    
    private static native int nGetKeyKeyCode(final ByteBuffer p0);
    
    public int getKeyState() {
        return nGetKeyState(this.event_buffer);
    }
    
    private static native int nGetKeyState(final ByteBuffer p0);
}
