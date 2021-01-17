// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.input;

import java.io.IOException;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.AWTEventListener;

final class AWTMouse extends Mouse implements AWTEventListener
{
    private static final int EVENT_X = 1;
    private static final int EVENT_Y = 2;
    private static final int EVENT_BUTTON = 4;
    private final List awt_events;
    private final List processed_awt_events;
    private int event_state;
    
    protected AWTMouse() {
        super("AWTMouse", createComponents(), new Controller[0], new Rumbler[0]);
        this.awt_events = new ArrayList();
        this.processed_awt_events = new ArrayList();
        this.event_state = 1;
        Toolkit.getDefaultToolkit().addAWTEventListener(this, 131120L);
    }
    
    private static final Component[] createComponents() {
        return new Component[] { new Axis(Component.Identifier.Axis.X), new Axis(Component.Identifier.Axis.Y), new Axis(Component.Identifier.Axis.Z), new Button(Component.Identifier.Button.LEFT), new Button(Component.Identifier.Button.MIDDLE), new Button(Component.Identifier.Button.RIGHT) };
    }
    
    private final void processButtons(final int button_enum, final float value) {
        final Button button = this.getButton(button_enum);
        if (button != null) {
            button.setValue(value);
        }
    }
    
    private final Button getButton(final int button_enum) {
        switch (button_enum) {
            case 1: {
                return (Button)this.getLeft();
            }
            case 2: {
                return (Button)this.getMiddle();
            }
            case 3: {
                return (Button)this.getRight();
            }
            default: {
                return null;
            }
        }
    }
    
    private final void processEvent(final AWTEvent event) throws IOException {
        if (event instanceof MouseWheelEvent) {
            final MouseWheelEvent mwe = (MouseWheelEvent)event;
            final Axis wheel = (Axis)this.getWheel();
            wheel.setValue(wheel.poll() + mwe.getWheelRotation());
        }
        else if (event instanceof MouseEvent) {
            final MouseEvent me = (MouseEvent)event;
            final Axis x = (Axis)this.getX();
            final Axis y = (Axis)this.getY();
            x.setValue((float)me.getX());
            y.setValue((float)me.getY());
            switch (me.getID()) {
                case 501: {
                    this.processButtons(me.getButton(), 1.0f);
                    break;
                }
                case 502: {
                    this.processButtons(me.getButton(), 0.0f);
                    break;
                }
            }
        }
    }
    
    public final synchronized void pollDevice() throws IOException {
        final Axis wheel = (Axis)this.getWheel();
        wheel.setValue(0.0f);
        for (int i = 0; i < this.awt_events.size(); ++i) {
            final AWTEvent event = this.awt_events.get(i);
            this.processEvent(event);
            this.processed_awt_events.add(event);
        }
        this.awt_events.clear();
    }
    
    protected final synchronized boolean getNextDeviceEvent(final Event event) throws IOException {
        while (!this.processed_awt_events.isEmpty()) {
            final AWTEvent awt_event = this.processed_awt_events.get(0);
            if (awt_event instanceof MouseWheelEvent) {
                final MouseWheelEvent awt_wheel_event = (MouseWheelEvent)awt_event;
                final long nanos = awt_wheel_event.getWhen() * 1000000L;
                event.set(this.getWheel(), (float)awt_wheel_event.getWheelRotation(), nanos);
                this.processed_awt_events.remove(0);
            }
            else {
                if (!(awt_event instanceof MouseEvent)) {
                    continue;
                }
                final MouseEvent mouse_event = (MouseEvent)awt_event;
                final long nanos = mouse_event.getWhen() * 1000000L;
                switch (this.event_state) {
                    case 1: {
                        this.event_state = 2;
                        event.set(this.getX(), (float)mouse_event.getX(), nanos);
                        return true;
                    }
                    case 2: {
                        this.event_state = 4;
                        event.set(this.getY(), (float)mouse_event.getY(), nanos);
                        return true;
                    }
                    case 4: {
                        this.processed_awt_events.remove(0);
                        this.event_state = 1;
                        final Button button = this.getButton(mouse_event.getButton());
                        if (button == null) {
                            continue;
                        }
                        switch (mouse_event.getID()) {
                            case 501: {
                                event.set(button, 1.0f, nanos);
                                return true;
                            }
                            case 502: {
                                event.set(button, 0.0f, nanos);
                                return true;
                            }
                            default: {
                                continue;
                            }
                        }
                        break;
                    }
                    default: {
                        throw new RuntimeException("Unknown event state: " + this.event_state);
                    }
                }
            }
        }
        return false;
    }
    
    public final synchronized void eventDispatched(final AWTEvent event) {
        this.awt_events.add(event);
    }
    
    static final class Axis extends AbstractComponent
    {
        private float value;
        
        public Axis(final Component.Identifier.Axis axis_id) {
            super(axis_id.getName(), axis_id);
        }
        
        public final boolean isRelative() {
            return false;
        }
        
        public final boolean isAnalog() {
            return true;
        }
        
        protected final void setValue(final float value) {
            this.value = value;
        }
        
        protected final float poll() throws IOException {
            return this.value;
        }
    }
    
    static final class Button extends AbstractComponent
    {
        private float value;
        
        public Button(final Component.Identifier.Button button_id) {
            super(button_id.getName(), button_id);
        }
        
        protected final void setValue(final float value) {
            this.value = value;
        }
        
        protected final float poll() throws IOException {
            return this.value;
        }
        
        public final boolean isAnalog() {
            return false;
        }
        
        public final boolean isRelative() {
            return false;
        }
    }
}
