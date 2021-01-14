package me.Corbis.Execution.event.events;

import me.Corbis.Execution.event.Event;

public class EventRenderHotbar extends Event {
    public State state;
    public EventRenderHotbar(State state){
        this.state = state;
    }
}
