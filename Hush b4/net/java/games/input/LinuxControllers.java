// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.input;

import java.io.IOException;

final class LinuxControllers
{
    private static final LinuxEvent linux_event;
    private static final LinuxAbsInfo abs_info;
    
    public static final synchronized boolean getNextDeviceEvent(final Event event, final LinuxEventDevice device) throws IOException {
        while (device.getNextEvent(LinuxControllers.linux_event)) {
            final LinuxAxisDescriptor descriptor = LinuxControllers.linux_event.getDescriptor();
            final LinuxComponent component = device.mapDescriptor(descriptor);
            if (component != null) {
                final float value = component.convertValue((float)LinuxControllers.linux_event.getValue(), descriptor);
                event.set(component, value, LinuxControllers.linux_event.getNanos());
                return true;
            }
        }
        return false;
    }
    
    public static final synchronized float poll(final LinuxEventComponent event_component) throws IOException {
        final int native_type = event_component.getDescriptor().getType();
        switch (native_type) {
            case 1: {
                final int native_code = event_component.getDescriptor().getCode();
                final float state = event_component.getDevice().isKeySet(native_code) ? 1.0f : 0.0f;
                return state;
            }
            case 3: {
                event_component.getAbsInfo(LinuxControllers.abs_info);
                return (float)LinuxControllers.abs_info.getValue();
            }
            default: {
                throw new RuntimeException("Unkown native_type: " + native_type);
            }
        }
    }
    
    static {
        linux_event = new LinuxEvent();
        abs_info = new LinuxAbsInfo();
    }
}
