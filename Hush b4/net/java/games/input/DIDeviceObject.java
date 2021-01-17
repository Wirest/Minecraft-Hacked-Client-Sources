// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.input;

import java.io.IOException;

final class DIDeviceObject
{
    private static final int WHEEL_SCALE = 120;
    private final IDirectInputDevice device;
    private final byte[] guid;
    private final int identifier;
    private final int type;
    private final int instance;
    private final int guid_type;
    private final int flags;
    private final String name;
    private final Component.Identifier id;
    private final int format_offset;
    private final long min;
    private final long max;
    private final int deadzone;
    private int last_poll_value;
    private int last_event_value;
    
    public DIDeviceObject(final IDirectInputDevice device, final Component.Identifier id, final byte[] guid, final int guid_type, final int identifier, final int type, final int instance, final int flags, final String name, final int format_offset) throws IOException {
        this.device = device;
        this.id = id;
        this.guid = guid;
        this.identifier = identifier;
        this.type = type;
        this.instance = instance;
        this.guid_type = guid_type;
        this.flags = flags;
        this.name = name;
        this.format_offset = format_offset;
        if (this.isAxis() && !this.isRelative()) {
            final long[] range = device.getRangeProperty(identifier);
            this.min = range[0];
            this.max = range[1];
            this.deadzone = device.getDeadzoneProperty(identifier);
        }
        else {
            this.min = -2147483648L;
            this.max = 2147483647L;
            this.deadzone = 0;
        }
    }
    
    public final synchronized int getRelativePollValue(final int current_abs_value) {
        if (this.device.areAxesRelative()) {
            return current_abs_value;
        }
        final int rel_value = current_abs_value - this.last_poll_value;
        this.last_poll_value = current_abs_value;
        return rel_value;
    }
    
    public final synchronized int getRelativeEventValue(final int current_abs_value) {
        if (this.device.areAxesRelative()) {
            return current_abs_value;
        }
        final int rel_value = current_abs_value - this.last_event_value;
        this.last_event_value = current_abs_value;
        return rel_value;
    }
    
    public final int getGUIDType() {
        return this.guid_type;
    }
    
    public final int getFormatOffset() {
        return this.format_offset;
    }
    
    public final IDirectInputDevice getDevice() {
        return this.device;
    }
    
    public final int getDIIdentifier() {
        return this.identifier;
    }
    
    public final Component.Identifier getIdentifier() {
        return this.id;
    }
    
    public final String getName() {
        return this.name;
    }
    
    public final int getInstance() {
        return this.instance;
    }
    
    public final int getType() {
        return this.type;
    }
    
    public final byte[] getGUID() {
        return this.guid;
    }
    
    public final int getFlags() {
        return this.flags;
    }
    
    public final long getMin() {
        return this.min;
    }
    
    public final long getMax() {
        return this.max;
    }
    
    public final float getDeadzone() {
        return (float)this.deadzone;
    }
    
    public final boolean isButton() {
        return (this.type & 0xC) != 0x0;
    }
    
    public final boolean isAxis() {
        return (this.type & 0x3) != 0x0;
    }
    
    public final boolean isRelative() {
        return this.isAxis() && (this.type & 0x1) != 0x0;
    }
    
    public final boolean isAnalog() {
        return this.isAxis() && this.id != Component.Identifier.Axis.POV;
    }
    
    public final float convertValue(final float value) {
        if (this.getDevice().getType() == 18 && this.id == Component.Identifier.Axis.Z) {
            return value / 120.0f;
        }
        if (this.isButton()) {
            return (((int)value & 0x80) != 0x0) ? 1.0f : 0.0f;
        }
        if (this.id == Component.Identifier.Axis.POV) {
            final int int_value = (int)value;
            if ((int_value & 0xFFFF) == 0xFFFF) {
                return 0.0f;
            }
            final int slice = 2250;
            if (int_value >= 0 && int_value < slice) {
                return 0.25f;
            }
            if (int_value < 3 * slice) {
                return 0.375f;
            }
            if (int_value < 5 * slice) {
                return 0.5f;
            }
            if (int_value < 7 * slice) {
                return 0.625f;
            }
            if (int_value < 9 * slice) {
                return 0.75f;
            }
            if (int_value < 11 * slice) {
                return 0.875f;
            }
            if (int_value < 13 * slice) {
                return 1.0f;
            }
            if (int_value < 15 * slice) {
                return 0.125f;
            }
            return 0.25f;
        }
        else {
            if (this.isAxis() && !this.isRelative()) {
                return 2.0f * (value - this.min) / (this.max - this.min) - 1.0f;
            }
            return value;
        }
    }
}
