// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.input;

import java.io.IOException;

final class RawMouse extends Mouse
{
    private static final int EVENT_DONE = 1;
    private static final int EVENT_X = 2;
    private static final int EVENT_Y = 3;
    private static final int EVENT_Z = 4;
    private static final int EVENT_BUTTON_0 = 5;
    private static final int EVENT_BUTTON_1 = 6;
    private static final int EVENT_BUTTON_2 = 7;
    private static final int EVENT_BUTTON_3 = 8;
    private static final int EVENT_BUTTON_4 = 9;
    private final RawDevice device;
    private final RawMouseEvent current_event;
    private int event_state;
    
    protected RawMouse(final String name, final RawDevice device, final Component[] components, final Controller[] children, final Rumbler[] rumblers) throws IOException {
        super(name, components, children, rumblers);
        this.current_event = new RawMouseEvent();
        this.event_state = 1;
        this.device = device;
    }
    
    public final void pollDevice() throws IOException {
        this.device.pollMouse();
    }
    
    private static final boolean makeButtonEvent(final RawMouseEvent mouse_event, final Event event, final Component button_component, final int down_flag, final int up_flag) {
        if ((mouse_event.getButtonFlags() & down_flag) != 0x0) {
            event.set(button_component, 1.0f, mouse_event.getNanos());
            return true;
        }
        if ((mouse_event.getButtonFlags() & up_flag) != 0x0) {
            event.set(button_component, 0.0f, mouse_event.getNanos());
            return true;
        }
        return false;
    }
    
    protected final synchronized boolean getNextDeviceEvent(final Event event) throws IOException {
        while (true) {
            switch (this.event_state) {
                case 1: {
                    if (!this.device.getNextMouseEvent(this.current_event)) {
                        return false;
                    }
                    this.event_state = 2;
                    continue;
                }
                case 2: {
                    final int rel_x = this.device.getEventRelativeX();
                    this.event_state = 3;
                    if (rel_x != 0) {
                        event.set(this.getX(), (float)rel_x, this.current_event.getNanos());
                        return true;
                    }
                    continue;
                }
                case 3: {
                    final int rel_y = this.device.getEventRelativeY();
                    this.event_state = 4;
                    if (rel_y != 0) {
                        event.set(this.getY(), (float)rel_y, this.current_event.getNanos());
                        return true;
                    }
                    continue;
                }
                case 4: {
                    final int wheel = this.current_event.getWheelDelta();
                    this.event_state = 5;
                    if (wheel != 0) {
                        event.set(this.getWheel(), (float)wheel, this.current_event.getNanos());
                        return true;
                    }
                    continue;
                }
                case 5: {
                    this.event_state = 6;
                    if (makeButtonEvent(this.current_event, event, this.getPrimaryButton(), 1, 2)) {
                        return true;
                    }
                    continue;
                }
                case 6: {
                    this.event_state = 7;
                    if (makeButtonEvent(this.current_event, event, this.getSecondaryButton(), 4, 8)) {
                        return true;
                    }
                    continue;
                }
                case 7: {
                    this.event_state = 8;
                    if (makeButtonEvent(this.current_event, event, this.getTertiaryButton(), 16, 32)) {
                        return true;
                    }
                    continue;
                }
                case 8: {
                    this.event_state = 9;
                    if (makeButtonEvent(this.current_event, event, this.getButton3(), 64, 128)) {
                        return true;
                    }
                    continue;
                }
                case 9: {
                    this.event_state = 1;
                    if (makeButtonEvent(this.current_event, event, this.getButton4(), 256, 512)) {
                        return true;
                    }
                    continue;
                }
                default: {
                    throw new RuntimeException("Unknown event state: " + this.event_state);
                }
            }
        }
    }
    
    protected final void setDeviceEventQueueSize(final int size) throws IOException {
        this.device.setBufferSize(size);
    }
    
    static final class Axis extends AbstractComponent
    {
        private final RawDevice device;
        
        public Axis(final RawDevice device, final Component.Identifier.Axis axis) {
            super(axis.getName(), axis);
            this.device = device;
        }
        
        public final boolean isRelative() {
            return true;
        }
        
        public final boolean isAnalog() {
            return true;
        }
        
        protected final float poll() throws IOException {
            if (this.getIdentifier() == Component.Identifier.Axis.X) {
                return (float)this.device.getRelativeX();
            }
            if (this.getIdentifier() == Component.Identifier.Axis.Y) {
                return (float)this.device.getRelativeY();
            }
            if (this.getIdentifier() == Component.Identifier.Axis.Z) {
                return (float)this.device.getWheel();
            }
            throw new RuntimeException("Unknown raw axis: " + this.getIdentifier());
        }
    }
    
    static final class Button extends AbstractComponent
    {
        private final RawDevice device;
        private final int button_id;
        
        public Button(final RawDevice device, final Component.Identifier.Button id, final int button_id) {
            super(id.getName(), id);
            this.device = device;
            this.button_id = button_id;
        }
        
        protected final float poll() throws IOException {
            return this.device.getButtonState(this.button_id) ? 1.0f : 0.0f;
        }
        
        public final boolean isAnalog() {
            return false;
        }
        
        public final boolean isRelative() {
            return false;
        }
    }
}
