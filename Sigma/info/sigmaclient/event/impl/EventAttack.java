package info.sigmaclient.event.impl;

import info.sigmaclient.event.Event;
import net.minecraft.entity.Entity;

public class EventAttack extends Event {
    private Entity entity;
    private boolean preAttack;

    public void fire(Entity targetEntity, boolean preAttack) {
        this.entity = targetEntity;
        this.preAttack = preAttack;
        super.fire();
    }

    public Entity getEntity() {
        return entity;
    }

    public boolean isPreAttack() {
        return preAttack;
    }

    public boolean isPostAttack() {
        return !preAttack;
    }
}
