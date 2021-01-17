// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.input;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

final class LinuxJoystickDevice implements LinuxDevice
{
    public static final int JS_EVENT_BUTTON = 1;
    public static final int JS_EVENT_AXIS = 2;
    public static final int JS_EVENT_INIT = 128;
    public static final int AXIS_MAX_VALUE = 32767;
    private final long fd;
    private final String name;
    private final LinuxJoystickEvent joystick_event;
    private final Event event;
    private final LinuxJoystickButton[] buttons;
    private final LinuxJoystickAxis[] axes;
    private final Map povXs;
    private final Map povYs;
    private final byte[] axisMap;
    private final char[] buttonMap;
    private EventQueue event_queue;
    private boolean closed;
    
    public LinuxJoystickDevice(final String filename) throws IOException {
        this.joystick_event = new LinuxJoystickEvent();
        this.event = new Event();
        this.povXs = new HashMap();
        this.povYs = new HashMap();
        this.fd = nOpen(filename);
        try {
            this.name = this.getDeviceName();
            this.setBufferSize(32);
            this.buttons = new LinuxJoystickButton[this.getNumDeviceButtons()];
            this.axes = new LinuxJoystickAxis[this.getNumDeviceAxes()];
            this.axisMap = this.getDeviceAxisMap();
            this.buttonMap = this.getDeviceButtonMap();
        }
        catch (IOException e) {
            this.close();
            throw e;
        }
    }
    
    private static final native long nOpen(final String p0) throws IOException;
    
    public final synchronized void setBufferSize(final int size) {
        this.event_queue = new EventQueue(size);
    }
    
    private final void processEvent(final LinuxJoystickEvent joystick_event) {
        final int index = joystick_event.getNumber();
        final int type = joystick_event.getType() & 0xFFFFFF7F;
        switch (type) {
            case 1: {
                if (index < this.getNumButtons()) {
                    final LinuxJoystickButton button = this.buttons[index];
                    if (button != null) {
                        final float value = (float)joystick_event.getValue();
                        button.setValue(value);
                        this.event.set(button, value, joystick_event.getNanos());
                        break;
                    }
                }
                return;
            }
            case 2: {
                if (index < this.getNumAxes()) {
                    final LinuxJoystickAxis axis = this.axes[index];
                    if (axis != null) {
                        final float value = joystick_event.getValue() / 32767.0f;
                        axis.setValue(value);
                        if (this.povXs.containsKey(new Integer(index))) {
                            final LinuxJoystickPOV pov = this.povXs.get(new Integer(index));
                            pov.updateValue();
                            this.event.set(pov, pov.getPollData(), joystick_event.getNanos());
                            break;
                        }
                        if (this.povYs.containsKey(new Integer(index))) {
                            final LinuxJoystickPOV pov = this.povYs.get(new Integer(index));
                            pov.updateValue();
                            this.event.set(pov, pov.getPollData(), joystick_event.getNanos());
                            break;
                        }
                        this.event.set(axis, value, joystick_event.getNanos());
                        break;
                    }
                }
                return;
            }
            default: {
                return;
            }
        }
        if (!this.event_queue.isFull()) {
            this.event_queue.add(this.event);
        }
    }
    
    public final void registerAxis(final int index, final LinuxJoystickAxis axis) {
        this.axes[index] = axis;
    }
    
    public final void registerButton(final int index, final LinuxJoystickButton button) {
        this.buttons[index] = button;
    }
    
    public final void registerPOV(final LinuxJoystickPOV pov) {
        final LinuxJoystickAxis xAxis = pov.getYAxis();
        final LinuxJoystickAxis yAxis = pov.getXAxis();
        int xIndex;
        for (xIndex = 0; xIndex < this.axes.length && this.axes[xIndex] != xAxis; ++xIndex) {}
        int yIndex;
        for (yIndex = 0; yIndex < this.axes.length && this.axes[yIndex] != yAxis; ++yIndex) {}
        this.povXs.put(new Integer(xIndex), pov);
        this.povYs.put(new Integer(yIndex), pov);
    }
    
    public final synchronized boolean getNextEvent(final Event event) throws IOException {
        return this.event_queue.getNextEvent(event);
    }
    
    public final synchronized void poll() throws IOException {
        this.checkClosed();
        while (this.getNextDeviceEvent(this.joystick_event)) {
            this.processEvent(this.joystick_event);
        }
    }
    
    private final boolean getNextDeviceEvent(final LinuxJoystickEvent joystick_event) throws IOException {
        return nGetNextEvent(this.fd, joystick_event);
    }
    
    private static final native boolean nGetNextEvent(final long p0, final LinuxJoystickEvent p1) throws IOException;
    
    public final int getNumAxes() {
        return this.axes.length;
    }
    
    public final int getNumButtons() {
        return this.buttons.length;
    }
    
    public final byte[] getAxisMap() {
        return this.axisMap;
    }
    
    public final char[] getButtonMap() {
        return this.buttonMap;
    }
    
    private final int getNumDeviceButtons() throws IOException {
        return nGetNumButtons(this.fd);
    }
    
    private static final native int nGetNumButtons(final long p0) throws IOException;
    
    private final int getNumDeviceAxes() throws IOException {
        return nGetNumAxes(this.fd);
    }
    
    private static final native int nGetNumAxes(final long p0) throws IOException;
    
    private final byte[] getDeviceAxisMap() throws IOException {
        return nGetAxisMap(this.fd);
    }
    
    private static final native byte[] nGetAxisMap(final long p0) throws IOException;
    
    private final char[] getDeviceButtonMap() throws IOException {
        return nGetButtonMap(this.fd);
    }
    
    private static final native char[] nGetButtonMap(final long p0) throws IOException;
    
    private final int getVersion() throws IOException {
        return nGetVersion(this.fd);
    }
    
    private static final native int nGetVersion(final long p0) throws IOException;
    
    public final String getName() {
        return this.name;
    }
    
    private final String getDeviceName() throws IOException {
        return nGetName(this.fd);
    }
    
    private static final native String nGetName(final long p0) throws IOException;
    
    public final synchronized void close() throws IOException {
        if (!this.closed) {
            this.closed = true;
            nClose(this.fd);
        }
    }
    
    private static final native void nClose(final long p0) throws IOException;
    
    private final void checkClosed() throws IOException {
        if (this.closed) {
            throw new IOException("Device is closed");
        }
    }
    
    protected void finalize() throws IOException {
        this.close();
    }
}
