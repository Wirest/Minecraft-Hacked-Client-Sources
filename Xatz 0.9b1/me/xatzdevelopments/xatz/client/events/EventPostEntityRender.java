
package me.xatzdevelopments.xatz.client.events;

import net.minecraft.entity.Entity;


public final class EventPostEntityRender extends Event
{
    private final Entity entity;
    
    public EventPostEntityRender(final Entity entity) {
        this.entity = entity;
    }
    
    public Entity getEntity() {
        return this.entity;
    }
}
