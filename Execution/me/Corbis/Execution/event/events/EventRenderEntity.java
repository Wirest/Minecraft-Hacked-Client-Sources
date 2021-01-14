package me.Corbis.Execution.event.events;

import me.Corbis.Execution.event.Event;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class EventRenderEntity extends Event {
    Entity entity;
    State state;

    public EventRenderEntity(Entity entity, State state) {
        this.entity = entity;
        this.state = state;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(EntityLivingBase entity) {
        this.entity = entity;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
