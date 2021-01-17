// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.input;

public abstract class Keyboard extends AbstractController
{
    protected Keyboard(final String name, final Component[] keys, final Controller[] children, final Rumbler[] rumblers) {
        super(name, keys, children, rumblers);
    }
    
    public Controller.Type getType() {
        return Controller.Type.KEYBOARD;
    }
    
    public final boolean isKeyDown(final Component.Identifier.Key key_id) {
        final Component key = this.getComponent(key_id);
        return key != null && key.getPollData() != 0.0f;
    }
}
