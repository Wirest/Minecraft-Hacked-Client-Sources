
package me.xatzdevelopments.xatz.client.events;



public final class EventRender3D extends Event
{
    private final float partialTicks;
    
    public EventRender3D(final float partialTicks) {
        this.partialTicks = partialTicks;
    }
    
    public float getPartialTicks() {
        return this.partialTicks;
    }
}
