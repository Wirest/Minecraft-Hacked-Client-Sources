// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.input;

import java.io.IOException;

final class LinuxJoystickButton extends AbstractComponent
{
    private float value;
    
    public LinuxJoystickButton(final Component.Identifier button_id) {
        super(button_id.getName(), button_id);
    }
    
    public final boolean isRelative() {
        return false;
    }
    
    final void setValue(final float value) {
        this.value = value;
    }
    
    protected final float poll() throws IOException {
        return this.value;
    }
}
