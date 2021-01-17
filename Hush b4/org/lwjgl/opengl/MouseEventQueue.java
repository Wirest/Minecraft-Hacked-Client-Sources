// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseEvent;
import java.nio.IntBuffer;
import java.awt.Point;
import java.nio.ByteBuffer;
import java.awt.Component;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;

class MouseEventQueue extends EventQueue implements MouseListener, MouseMotionListener, MouseWheelListener
{
    private static final int WHEEL_SCALE = 120;
    public static final int NUM_BUTTONS = 3;
    private final Component component;
    private boolean grabbed;
    private int accum_dx;
    private int accum_dy;
    private int accum_dz;
    private int last_x;
    private int last_y;
    private boolean saved_control_state;
    private final ByteBuffer event;
    private final byte[] buttons;
    
    MouseEventQueue(final Component component) {
        super(22);
        this.event = ByteBuffer.allocate(22);
        this.buttons = new byte[3];
        this.component = component;
    }
    
    public synchronized void register() {
        this.resetCursorToCenter();
        if (this.component != null) {
            this.component.addMouseListener(this);
            this.component.addMouseMotionListener(this);
            this.component.addMouseWheelListener(this);
        }
    }
    
    public synchronized void unregister() {
        if (this.component != null) {
            this.component.removeMouseListener(this);
            this.component.removeMouseMotionListener(this);
            this.component.removeMouseWheelListener(this);
        }
    }
    
    protected Component getComponent() {
        return this.component;
    }
    
    public synchronized void setGrabbed(final boolean grabbed) {
        this.grabbed = grabbed;
        this.resetCursorToCenter();
    }
    
    public synchronized boolean isGrabbed() {
        return this.grabbed;
    }
    
    protected int transformY(final int y) {
        if (this.component != null) {
            return this.component.getHeight() - 1 - y;
        }
        return y;
    }
    
    protected void resetCursorToCenter() {
        this.clearEvents();
        final int n = 0;
        this.accum_dy = n;
        this.accum_dx = n;
        if (this.component != null) {
            final Point cursor_location = AWTUtil.getCursorPosition(this.component);
            if (cursor_location != null) {
                this.last_x = cursor_location.x;
                this.last_y = cursor_location.y;
            }
        }
    }
    
    private void putMouseEvent(final byte button, final byte state, final int dz, final long nanos) {
        if (this.grabbed) {
            this.putMouseEventWithCoords(button, state, 0, 0, dz, nanos);
        }
        else {
            this.putMouseEventWithCoords(button, state, this.last_x, this.last_y, dz, nanos);
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
            coord_buffer.put(0, this.accum_dx);
            coord_buffer.put(1, this.accum_dy);
        }
        else {
            coord_buffer.put(0, this.last_x);
            coord_buffer.put(1, this.last_y);
        }
        coord_buffer.put(2, this.accum_dz);
        final int accum_dx = 0;
        this.accum_dz = accum_dx;
        this.accum_dy = accum_dx;
        this.accum_dx = accum_dx;
        final int old_position = buttons_buffer.position();
        buttons_buffer.put(this.buttons, 0, this.buttons.length);
        buttons_buffer.position(old_position);
    }
    
    private void setCursorPos(final int x, int y, final long nanos) {
        y = this.transformY(y);
        if (this.grabbed) {
            return;
        }
        final int dx = x - this.last_x;
        final int dy = y - this.last_y;
        this.addDelta(dx, dy);
        this.putMouseEventWithCoords((byte)(-1), (byte)0, this.last_x = x, this.last_y = y, 0, nanos);
    }
    
    protected void addDelta(final int dx, final int dy) {
        this.accum_dx += dx;
        this.accum_dy += dy;
    }
    
    public void mouseClicked(final MouseEvent e) {
    }
    
    public void mouseEntered(final MouseEvent e) {
    }
    
    public void mouseExited(final MouseEvent e) {
    }
    
    private void handleButton(final MouseEvent e) {
        byte state = 0;
        switch (e.getID()) {
            case 501: {
                state = 1;
                break;
            }
            case 502: {
                state = 0;
                break;
            }
            default: {
                throw new IllegalArgumentException("Not a valid event ID: " + e.getID());
            }
        }
        byte button = 0;
        switch (e.getButton()) {
            case 0: {
                return;
            }
            case 1: {
                if (state == 1) {
                    this.saved_control_state = e.isControlDown();
                }
                if (!this.saved_control_state) {
                    button = 0;
                    break;
                }
                if (this.buttons[1] == state) {
                    return;
                }
                button = 1;
                break;
            }
            case 2: {
                button = 2;
                break;
            }
            case 3: {
                if (this.buttons[1] == state) {
                    return;
                }
                button = 1;
                break;
            }
            default: {
                throw new IllegalArgumentException("Not a valid button: " + e.getButton());
            }
        }
        this.setButton(button, state, e.getWhen() * 1000000L);
    }
    
    public synchronized void mousePressed(final MouseEvent e) {
        this.handleButton(e);
    }
    
    private void setButton(final byte button, final byte state, final long nanos) {
        this.putMouseEvent(button, this.buttons[button] = state, 0, nanos);
    }
    
    public synchronized void mouseReleased(final MouseEvent e) {
        this.handleButton(e);
    }
    
    private void handleMotion(final MouseEvent e) {
        if (this.grabbed) {
            this.updateDeltas(e.getWhen() * 1000000L);
        }
        else {
            this.setCursorPos(e.getX(), e.getY(), e.getWhen() * 1000000L);
        }
    }
    
    public synchronized void mouseDragged(final MouseEvent e) {
        this.handleMotion(e);
    }
    
    public synchronized void mouseMoved(final MouseEvent e) {
        this.handleMotion(e);
    }
    
    private void handleWheel(final int amount, final long nanos) {
        this.accum_dz += amount;
        this.putMouseEvent((byte)(-1), (byte)0, amount, nanos);
    }
    
    protected void updateDeltas(final long nanos) {
    }
    
    public synchronized void mouseWheelMoved(final MouseWheelEvent e) {
        final int wheel_amount = -e.getWheelRotation() * 120;
        this.handleWheel(wheel_amount, e.getWhen() * 1000000L);
    }
}
