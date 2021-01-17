// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.command;

public class KeyControl implements Control
{
    private int keycode;
    
    public KeyControl(final int keycode) {
        this.keycode = keycode;
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof KeyControl && ((KeyControl)o).keycode == this.keycode;
    }
    
    @Override
    public int hashCode() {
        return this.keycode;
    }
}
