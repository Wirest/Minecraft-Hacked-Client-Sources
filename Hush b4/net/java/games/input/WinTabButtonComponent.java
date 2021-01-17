// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.input;

public class WinTabButtonComponent extends WinTabComponent
{
    private int index;
    
    protected WinTabButtonComponent(final WinTabContext context, final int parentDevice, final String name, final Component.Identifier id, final int index) {
        super(context, parentDevice, name, id);
        this.index = index;
    }
    
    public Event processPacket(final WinTabPacket packet) {
        Event newEvent = null;
        final float newValue = ((packet.PK_BUTTONS & (int)Math.pow(2.0, this.index)) > 0) ? 1.0f : 0.0f;
        if (newValue != this.getPollData()) {
            this.lastKnownValue = newValue;
            newEvent = new Event();
            newEvent.set(this, newValue, packet.PK_TIME * 1000L);
            return newEvent;
        }
        return newEvent;
    }
}
