package me.Corbis.Execution.event.events;

import me.Corbis.Execution.event.Event;
import net.minecraft.entity.Entity;

public class EventAttack extends Event {
    public Entity target;

    public EventAttack(Entity target) {
        this.target = target;
    }

    public Entity getTarget() {
        return target;
    }

    public void setTarget(Entity target) {
        this.target = target;
    }
}
