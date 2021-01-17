// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.input;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

public class WinTabComponent extends AbstractComponent
{
    public static final int XAxis = 1;
    public static final int YAxis = 2;
    public static final int ZAxis = 3;
    public static final int NPressureAxis = 4;
    public static final int TPressureAxis = 5;
    public static final int OrientationAxis = 6;
    public static final int RotationAxis = 7;
    private int min;
    private int max;
    protected float lastKnownValue;
    private boolean analog;
    
    protected WinTabComponent(final WinTabContext context, final int parentDevice, final String name, final Component.Identifier id, final int min, final int max) {
        super(name, id);
        this.min = min;
        this.max = max;
        this.analog = true;
    }
    
    protected WinTabComponent(final WinTabContext context, final int parentDevice, final String name, final Component.Identifier id) {
        super(name, id);
        this.min = 0;
        this.max = 1;
        this.analog = false;
    }
    
    protected float poll() throws IOException {
        return this.lastKnownValue;
    }
    
    public boolean isAnalog() {
        return this.analog;
    }
    
    public boolean isRelative() {
        return false;
    }
    
    public static List createComponents(final WinTabContext context, final int parentDevice, final int axisId, final int[] axisRanges) {
        final List components = new ArrayList();
        switch (axisId) {
            case 1: {
                final Component.Identifier id = Component.Identifier.Axis.X;
                components.add(new WinTabComponent(context, parentDevice, id.getName(), id, axisRanges[0], axisRanges[1]));
                break;
            }
            case 2: {
                final Component.Identifier id = Component.Identifier.Axis.Y;
                components.add(new WinTabComponent(context, parentDevice, id.getName(), id, axisRanges[0], axisRanges[1]));
                break;
            }
            case 3: {
                final Component.Identifier id = Component.Identifier.Axis.Z;
                components.add(new WinTabComponent(context, parentDevice, id.getName(), id, axisRanges[0], axisRanges[1]));
                break;
            }
            case 4: {
                final Component.Identifier id = Component.Identifier.Axis.X_FORCE;
                components.add(new WinTabComponent(context, parentDevice, id.getName(), id, axisRanges[0], axisRanges[1]));
                break;
            }
            case 5: {
                final Component.Identifier id = Component.Identifier.Axis.Y_FORCE;
                components.add(new WinTabComponent(context, parentDevice, id.getName(), id, axisRanges[0], axisRanges[1]));
                break;
            }
            case 6: {
                Component.Identifier id = Component.Identifier.Axis.RX;
                components.add(new WinTabComponent(context, parentDevice, id.getName(), id, axisRanges[0], axisRanges[1]));
                id = Component.Identifier.Axis.RY;
                components.add(new WinTabComponent(context, parentDevice, id.getName(), id, axisRanges[2], axisRanges[3]));
                id = Component.Identifier.Axis.RZ;
                components.add(new WinTabComponent(context, parentDevice, id.getName(), id, axisRanges[4], axisRanges[5]));
                break;
            }
            case 7: {
                Component.Identifier id = Component.Identifier.Axis.RX;
                components.add(new WinTabComponent(context, parentDevice, id.getName(), id, axisRanges[0], axisRanges[1]));
                id = Component.Identifier.Axis.RY;
                components.add(new WinTabComponent(context, parentDevice, id.getName(), id, axisRanges[2], axisRanges[3]));
                id = Component.Identifier.Axis.RZ;
                components.add(new WinTabComponent(context, parentDevice, id.getName(), id, axisRanges[4], axisRanges[5]));
                break;
            }
        }
        return components;
    }
    
    public static Collection createButtons(final WinTabContext context, final int deviceIndex, final int numberOfButtons) {
        final List buttons = new ArrayList();
        for (int i = 0; i < numberOfButtons; ++i) {
            try {
                final Class buttonIdClass = Component.Identifier.Button.class;
                final Field idField = buttonIdClass.getField("_" + i);
                final Component.Identifier id = (Component.Identifier)idField.get(null);
                buttons.add(new WinTabButtonComponent(context, deviceIndex, id.getName(), id, i));
            }
            catch (SecurityException e) {
                e.printStackTrace();
            }
            catch (NoSuchFieldException e2) {
                e2.printStackTrace();
            }
            catch (IllegalArgumentException e3) {
                e3.printStackTrace();
            }
            catch (IllegalAccessException e4) {
                e4.printStackTrace();
            }
        }
        return buttons;
    }
    
    public Event processPacket(final WinTabPacket packet) {
        float newValue = this.lastKnownValue;
        if (this.getIdentifier() == Component.Identifier.Axis.X) {
            newValue = this.normalise((float)packet.PK_X);
        }
        if (this.getIdentifier() == Component.Identifier.Axis.Y) {
            newValue = this.normalise((float)packet.PK_Y);
        }
        if (this.getIdentifier() == Component.Identifier.Axis.Z) {
            newValue = this.normalise((float)packet.PK_Z);
        }
        if (this.getIdentifier() == Component.Identifier.Axis.X_FORCE) {
            newValue = this.normalise((float)packet.PK_NORMAL_PRESSURE);
        }
        if (this.getIdentifier() == Component.Identifier.Axis.Y_FORCE) {
            newValue = this.normalise((float)packet.PK_TANGENT_PRESSURE);
        }
        if (this.getIdentifier() == Component.Identifier.Axis.RX) {
            newValue = this.normalise((float)packet.PK_ORIENTATION_ALT);
        }
        if (this.getIdentifier() == Component.Identifier.Axis.RY) {
            newValue = this.normalise((float)packet.PK_ORIENTATION_AZ);
        }
        if (this.getIdentifier() == Component.Identifier.Axis.RZ) {
            newValue = this.normalise((float)packet.PK_ORIENTATION_TWIST);
        }
        if (newValue != this.getPollData()) {
            this.lastKnownValue = newValue;
            final Event newEvent = new Event();
            newEvent.set(this, newValue, packet.PK_TIME * 1000L);
            return newEvent;
        }
        return null;
    }
    
    private float normalise(final float value) {
        if (this.max == this.min) {
            return value;
        }
        final float bottom = (float)(this.max - this.min);
        return (value - this.min) / bottom;
    }
    
    public static Collection createCursors(final WinTabContext context, final int deviceIndex, final String[] cursorNames) {
        final List cursors = new ArrayList();
        for (int i = 0; i < cursorNames.length; ++i) {
            Component.Identifier id;
            if (cursorNames[i].matches("Puck")) {
                id = Component.Identifier.Button.TOOL_FINGER;
            }
            else if (cursorNames[i].matches("Eraser.*")) {
                id = Component.Identifier.Button.TOOL_RUBBER;
            }
            else {
                id = Component.Identifier.Button.TOOL_PEN;
            }
            cursors.add(new WinTabCursorComponent(context, deviceIndex, id.getName(), id, i));
        }
        return cursors;
    }
}
