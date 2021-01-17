// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.input;

import java.io.IOException;

final class DIControllers
{
    private static final DIDeviceObjectData di_event;
    
    public static final synchronized boolean getNextDeviceEvent(final Event event, final IDirectInputDevice device) throws IOException {
        if (!device.getNextEvent(DIControllers.di_event)) {
            return false;
        }
        final DIDeviceObject object = device.mapEvent(DIControllers.di_event);
        final DIComponent component = device.mapObject(object);
        if (component == null) {
            return false;
        }
        int event_value;
        if (object.isRelative()) {
            event_value = object.getRelativeEventValue(DIControllers.di_event.getData());
        }
        else {
            event_value = DIControllers.di_event.getData();
        }
        event.set(component, component.getDeviceObject().convertValue((float)event_value), DIControllers.di_event.getNanos());
        return true;
    }
    
    public static final float poll(final Component component, final DIDeviceObject object) throws IOException {
        final int poll_data = object.getDevice().getPollData(object);
        float result;
        if (object.isRelative()) {
            result = (float)object.getRelativePollValue(poll_data);
        }
        else {
            result = (float)poll_data;
        }
        return object.convertValue(result);
    }
    
    static {
        di_event = new DIDeviceObjectData();
    }
}
