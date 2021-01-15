

package me.xatzdevelopments.xatz.client.events;

import net.minecraft.entity.Entity;


public final class EventPreEntityRender extends Event
{
    private final Entity entity;
    
    public EventPreEntityRender(final Entity entity) {
        this.entity = entity;
    }
    
    public Entity getEntity() {
        return this.entity;
    }
}
