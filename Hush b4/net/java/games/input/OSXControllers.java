// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.input;

import java.io.IOException;

final class OSXControllers
{
    private static final OSXEvent osx_event;
    
    public static final synchronized float poll(final OSXHIDElement element) throws IOException {
        element.getElementValue(OSXControllers.osx_event);
        return element.convertValue((float)OSXControllers.osx_event.getValue());
    }
    
    public static final synchronized boolean getNextDeviceEvent(final Event event, final OSXHIDQueue queue) throws IOException {
        if (queue.getNextEvent(OSXControllers.osx_event)) {
            final OSXComponent component = queue.mapEvent(OSXControllers.osx_event);
            event.set(component, component.getElement().convertValue((float)OSXControllers.osx_event.getValue()), OSXControllers.osx_event.getNanos());
            return true;
        }
        return false;
    }
    
    static {
        osx_event = new OSXEvent();
    }
}
