// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.util.jinput;

import net.java.games.input.AbstractComponent;
import net.java.games.input.Event;
import java.io.IOException;
import net.java.games.input.Component;
import net.java.games.input.Rumbler;
import net.java.games.input.Controller;
import net.java.games.input.Mouse;

final class LWJGLMouse extends Mouse
{
    private static final int EVENT_X = 1;
    private static final int EVENT_Y = 2;
    private static final int EVENT_WHEEL = 3;
    private static final int EVENT_BUTTON = 4;
    private static final int EVENT_DONE = 5;
    private int event_state;
    
    LWJGLMouse() {
        super("LWJGLMouse", createComponents(), new Controller[0], new Rumbler[0]);
        this.event_state = 5;
    }
    
    private static Component[] createComponents() {
        return new Component[] { new Axis(Component.Identifier.Axis.X), new Axis(Component.Identifier.Axis.Y), new Axis(Component.Identifier.Axis.Z), new Button(Component.Identifier.Button.LEFT), new Button(Component.Identifier.Button.MIDDLE), new Button(Component.Identifier.Button.RIGHT) };
    }
    
    public synchronized void pollDevice() throws IOException {
        if (!org.lwjgl.input.Mouse.isCreated()) {
            return;
        }
        org.lwjgl.input.Mouse.poll();
        for (int i = 0; i < 3; ++i) {
            this.setButtonState(i);
        }
    }
    
    private Button map(final int lwjgl_button) {
        switch (lwjgl_button) {
            case 0: {
                return (Button)this.getLeft();
            }
            case 1: {
                return (Button)this.getRight();
            }
            case 2: {
                return (Button)this.getMiddle();
            }
            default: {
                return null;
            }
        }
    }
    
    private void setButtonState(final int lwjgl_button) {
        final Button button = this.map(lwjgl_button);
        if (button != null) {
            button.setValue(org.lwjgl.input.Mouse.isButtonDown(lwjgl_button) ? 1.0f : 0.0f);
        }
    }
    
    @Override
    protected synchronized boolean getNextDeviceEvent(final Event event) throws IOException {
        if (!org.lwjgl.input.Mouse.isCreated()) {
            return false;
        }
        while (true) {
            final long nanos = org.lwjgl.input.Mouse.getEventNanoseconds();
            switch (this.event_state) {
                case 1: {
                    this.event_state = 2;
                    final int dx = org.lwjgl.input.Mouse.getEventDX();
                    if (dx != 0) {
                        event.set(this.getX(), (float)dx, nanos);
                        return true;
                    }
                    continue;
                }
                case 2: {
                    this.event_state = 3;
                    final int dy = -org.lwjgl.input.Mouse.getEventDY();
                    if (dy != 0) {
                        event.set(this.getY(), (float)dy, nanos);
                        return true;
                    }
                    continue;
                }
                case 3: {
                    this.event_state = 4;
                    final int dwheel = org.lwjgl.input.Mouse.getEventDWheel();
                    if (dwheel != 0) {
                        event.set(this.getWheel(), (float)dwheel, nanos);
                        return true;
                    }
                    continue;
                }
                case 4: {
                    this.event_state = 5;
                    final int lwjgl_button = org.lwjgl.input.Mouse.getEventButton();
                    if (lwjgl_button == -1) {
                        continue;
                    }
                    final Button button = this.map(lwjgl_button);
                    if (button != null) {
                        event.set(button, org.lwjgl.input.Mouse.getEventButtonState() ? 1.0f : 0.0f, nanos);
                        return true;
                    }
                    continue;
                }
                case 5: {
                    if (!org.lwjgl.input.Mouse.next()) {
                        return false;
                    }
                    this.event_state = 1;
                    continue;
                }
            }
        }
    }
    
    static final class Axis extends AbstractComponent
    {
        Axis(final Component.Identifier.Axis axis_id) {
            super(axis_id.getName(), axis_id);
        }
        
        public boolean isRelative() {
            return true;
        }
        
        @Override
        protected float poll() throws IOException {
            return 0.0f;
        }
        
        @Override
        public boolean isAnalog() {
            return true;
        }
    }
    
    static final class Button extends AbstractComponent
    {
        private float value;
        
        Button(final Component.Identifier.Button button_id) {
            super(button_id.getName(), button_id);
        }
        
        void setValue(final float value) {
            this.value = value;
        }
        
        @Override
        protected float poll() throws IOException {
            return this.value;
        }
        
        public boolean isRelative() {
            return false;
        }
        
        @Override
        public boolean isAnalog() {
            return false;
        }
    }
}
