package de.iotacb.client.events.input;

import com.darkmagician6.eventapi.events.Event;

public class KeyPressedEvent implements Event {

    private final int key;

    public KeyPressedEvent(int key) {
        this.key = key;
    }

    public int getKey() {
        return this.key;
    }
	
}
