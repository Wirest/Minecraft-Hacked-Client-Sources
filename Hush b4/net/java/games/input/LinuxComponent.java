// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.input;

import java.io.IOException;

class LinuxComponent extends AbstractComponent
{
    private final LinuxEventComponent component;
    
    public LinuxComponent(final LinuxEventComponent component) {
        super(component.getIdentifier().getName(), component.getIdentifier());
        this.component = component;
    }
    
    public final boolean isRelative() {
        return this.component.isRelative();
    }
    
    public final boolean isAnalog() {
        return this.component.isAnalog();
    }
    
    protected float poll() throws IOException {
        return this.convertValue(LinuxControllers.poll(this.component), this.component.getDescriptor());
    }
    
    float convertValue(final float value, final LinuxAxisDescriptor descriptor) {
        return this.getComponent().convertValue(value);
    }
    
    public final float getDeadZone() {
        return this.component.getDeadZone();
    }
    
    public final LinuxEventComponent getComponent() {
        return this.component;
    }
}
