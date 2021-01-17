// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.input;

import java.io.IOException;

final class LinuxEventComponent
{
    private final LinuxEventDevice device;
    private final Component.Identifier identifier;
    private final Controller.Type button_trait;
    private final boolean is_relative;
    private final LinuxAxisDescriptor descriptor;
    private final int min;
    private final int max;
    private final int flat;
    
    public LinuxEventComponent(final LinuxEventDevice device, final Component.Identifier identifier, final boolean is_relative, final int native_type, final int native_code) throws IOException {
        this.device = device;
        this.identifier = identifier;
        if (native_type == 1) {
            this.button_trait = LinuxNativeTypesMap.guessButtonTrait(native_code);
        }
        else {
            this.button_trait = Controller.Type.UNKNOWN;
        }
        this.is_relative = is_relative;
        (this.descriptor = new LinuxAxisDescriptor()).set(native_type, native_code);
        if (native_type == 3) {
            final LinuxAbsInfo abs_info = new LinuxAbsInfo();
            this.getAbsInfo(abs_info);
            this.min = abs_info.getMin();
            this.max = abs_info.getMax();
            this.flat = abs_info.getFlat();
        }
        else {
            this.min = Integer.MIN_VALUE;
            this.max = Integer.MAX_VALUE;
            this.flat = 0;
        }
    }
    
    public final LinuxEventDevice getDevice() {
        return this.device;
    }
    
    public final void getAbsInfo(final LinuxAbsInfo abs_info) throws IOException {
        assert this.descriptor.getType() == 3;
        this.device.getAbsInfo(this.descriptor.getCode(), abs_info);
    }
    
    public final Controller.Type getButtonTrait() {
        return this.button_trait;
    }
    
    public final Component.Identifier getIdentifier() {
        return this.identifier;
    }
    
    public final LinuxAxisDescriptor getDescriptor() {
        return this.descriptor;
    }
    
    public final boolean isRelative() {
        return this.is_relative;
    }
    
    public final boolean isAnalog() {
        return this.identifier instanceof Component.Identifier.Axis && this.identifier != Component.Identifier.Axis.POV;
    }
    
    final float convertValue(float value) {
        if (!(this.identifier instanceof Component.Identifier.Axis) || this.is_relative) {
            return value;
        }
        if (this.min == this.max) {
            return 0.0f;
        }
        if (value > this.max) {
            value = (float)this.max;
        }
        else if (value < this.min) {
            value = (float)this.min;
        }
        return 2.0f * (value - this.min) / (this.max - this.min) - 1.0f;
    }
    
    final float getDeadZone() {
        return this.flat / (2.0f * (this.max - this.min));
    }
}
