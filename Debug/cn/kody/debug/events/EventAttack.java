package cn.kody.debug.events;

import com.darkmagician6.eventapi.events.Event;
import net.minecraft.entity.Entity;

public class EventAttack
implements Event {
    private Entity entity;

    public EventAttack(Entity targetEntity) {
        this.entity = targetEntity;
    }

    public Entity getEntity() {
        return this.entity;
    }
}

