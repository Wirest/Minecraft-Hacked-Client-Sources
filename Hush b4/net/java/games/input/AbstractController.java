// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.input;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractController implements Controller
{
    static final int EVENT_QUEUE_DEPTH = 32;
    private static final Event event;
    private final String name;
    private final Component[] components;
    private final Controller[] children;
    private final Rumbler[] rumblers;
    private final Map id_to_components;
    private EventQueue event_queue;
    
    protected AbstractController(final String name, final Component[] components, final Controller[] children, final Rumbler[] rumblers) {
        this.id_to_components = new HashMap();
        this.event_queue = new EventQueue(32);
        this.name = name;
        this.components = components;
        this.children = children;
        this.rumblers = rumblers;
        for (int i = components.length - 1; i >= 0; --i) {
            this.id_to_components.put(components[i].getIdentifier(), components[i]);
        }
    }
    
    public final Controller[] getControllers() {
        return this.children;
    }
    
    public final Component[] getComponents() {
        return this.components;
    }
    
    public final Component getComponent(final Component.Identifier id) {
        return this.id_to_components.get(id);
    }
    
    public final Rumbler[] getRumblers() {
        return this.rumblers;
    }
    
    public PortType getPortType() {
        return PortType.UNKNOWN;
    }
    
    public int getPortNumber() {
        return 0;
    }
    
    public final String getName() {
        return this.name;
    }
    
    public String toString() {
        return this.name;
    }
    
    public Type getType() {
        return Type.UNKNOWN;
    }
    
    public final void setEventQueueSize(final int size) {
        try {
            this.setDeviceEventQueueSize(size);
            this.event_queue = new EventQueue(size);
        }
        catch (IOException e) {
            ControllerEnvironment.logln("Failed to create new event queue of size " + size + ": " + e);
        }
    }
    
    protected void setDeviceEventQueueSize(final int size) throws IOException {
    }
    
    public final EventQueue getEventQueue() {
        return this.event_queue;
    }
    
    protected abstract boolean getNextDeviceEvent(final Event p0) throws IOException;
    
    protected void pollDevice() throws IOException {
    }
    
    public synchronized boolean poll() {
        final Component[] components = this.getComponents();
        try {
            this.pollDevice();
            for (int i = 0; i < components.length; ++i) {
                final AbstractComponent component = (AbstractComponent)components[i];
                if (component.isRelative()) {
                    component.setPollData(0.0f);
                }
                else {
                    component.resetHasPolled();
                }
            }
            while (this.getNextDeviceEvent(AbstractController.event)) {
                final AbstractComponent component2 = (AbstractComponent)AbstractController.event.getComponent();
                final float value = AbstractController.event.getValue();
                if (component2.isRelative()) {
                    if (value == 0.0f) {
                        continue;
                    }
                    component2.setPollData(component2.getPollData() + value);
                }
                else {
                    if (value == component2.getEventValue()) {
                        continue;
                    }
                    component2.setEventValue(value);
                }
                if (!this.event_queue.isFull()) {
                    this.event_queue.add(AbstractController.event);
                }
            }
            return true;
        }
        catch (IOException e) {
            ControllerEnvironment.logln("Failed to poll device: " + e.getMessage());
            return false;
        }
    }
    
    static {
        event = new Event();
    }
}
