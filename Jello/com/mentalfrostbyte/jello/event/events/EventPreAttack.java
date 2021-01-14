package com.mentalfrostbyte.jello.event.events;

import net.minecraft.entity.Entity;

public class EventPreAttack
implements Event {
    private Entity attacker;
    private Entity target;

    public EventPreAttack(Entity target) {
        this.target = target;
    }

    public Entity getAttacker() {
        return this.attacker;
    }

    public Entity getTarget() {
        return this.target;
    }
}

