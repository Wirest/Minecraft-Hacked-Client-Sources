package net.minecraft.MoveEvents;

import net.minecraft.entity.Entity;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.Event;

public class EventEntityInteract extends Event{

    private Entity entity;

    public EventEntityInteract(Entity entity){
        this.entity = entity;
    }

    public Entity getEntity(){
        return entity;
    }
}
