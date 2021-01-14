package me.Corbis.Execution.event.events;

import me.Corbis.Execution.event.Event;

public class EventSoundPlay extends Event {
    float volume;
    float pitch;

    public EventSoundPlay(float volume, float pitch) {
        this.volume = volume;
        this.pitch = pitch;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }
}
