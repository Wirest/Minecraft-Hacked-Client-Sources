package cn.kody.debug.events;

import com.darkmagician6.eventapi.events.Event;
import com.darkmagician6.eventapi.types.EventType;

import net.minecraft.entity.Entity;

public class EventStep
implements Event {
    private float height;
    private final EventType eventType;

    public EventStep(EventType eventType, float height) {
        this.eventType = eventType;
        this.height = height;
    }


    public float getHeight() {
        return this.height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public EventType getEventType() {
        return this.eventType;
    }
}

