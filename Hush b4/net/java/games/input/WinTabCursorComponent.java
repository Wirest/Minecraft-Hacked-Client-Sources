// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.input;

public class WinTabCursorComponent extends WinTabComponent
{
    private int index;
    
    protected WinTabCursorComponent(final WinTabContext context, final int parentDevice, final String name, final Component.Identifier id, final int index) {
        super(context, parentDevice, name, id);
        this.index = index;
    }
    
    public Event processPacket(final WinTabPacket packet) {
        Event newEvent = null;
        if (packet.PK_CURSOR == this.index && this.lastKnownValue == 0.0f) {
            this.lastKnownValue = 1.0f;
            newEvent = new Event();
            newEvent.set(this, this.lastKnownValue, packet.PK_TIME * 1000L);
        }
        else if (packet.PK_CURSOR != this.index && this.lastKnownValue == 1.0f) {
            this.lastKnownValue = 0.0f;
            newEvent = new Event();
            newEvent.set(this, this.lastKnownValue, packet.PK_TIME * 1000L);
        }
        return newEvent;
    }
}
