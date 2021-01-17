// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.awt.Point;
import java.awt.Rectangle;
import org.lwjgl.BufferUtils;
import java.awt.Component;
import java.nio.IntBuffer;

final class MacOSXMouseEventQueue extends MouseEventQueue
{
    private final IntBuffer delta_buffer;
    private boolean skip_event;
    private static boolean is_grabbed;
    
    MacOSXMouseEventQueue(final Component component) {
        super(component);
        this.delta_buffer = BufferUtils.createIntBuffer(2);
    }
    
    @Override
    public void setGrabbed(final boolean grab) {
        if (MacOSXMouseEventQueue.is_grabbed != grab) {
            super.setGrabbed(grab);
            this.warpCursor();
            grabMouse(grab);
        }
    }
    
    private static synchronized void grabMouse(final boolean grab) {
        if (!(MacOSXMouseEventQueue.is_grabbed = grab)) {
            nGrabMouse(grab);
        }
    }
    
    @Override
    protected void resetCursorToCenter() {
        super.resetCursorToCenter();
        getMouseDeltas(this.delta_buffer);
    }
    
    @Override
    protected void updateDeltas(final long nanos) {
        super.updateDeltas(nanos);
        synchronized (this) {
            getMouseDeltas(this.delta_buffer);
            final int dx = this.delta_buffer.get(0);
            final int dy = -this.delta_buffer.get(1);
            if (this.skip_event) {
                this.skip_event = false;
                nGrabMouse(this.isGrabbed());
                return;
            }
            if (dx != 0 || dy != 0) {
                this.putMouseEventWithCoords((byte)(-1), (byte)0, dx, dy, 0, nanos);
                this.addDelta(dx, dy);
            }
        }
    }
    
    void warpCursor() {
        synchronized (this) {
            this.skip_event = this.isGrabbed();
        }
        if (this.isGrabbed()) {
            final Rectangle bounds = this.getComponent().getBounds();
            final Point location_on_screen = this.getComponent().getLocationOnScreen();
            final int x = location_on_screen.x + bounds.width / 2;
            final int y = location_on_screen.y + bounds.height / 2;
            nWarpCursor(x, y);
        }
    }
    
    private static native void getMouseDeltas(final IntBuffer p0);
    
    private static native void nWarpCursor(final int p0, final int p1);
    
    static native void nGrabMouse(final boolean p0);
}
