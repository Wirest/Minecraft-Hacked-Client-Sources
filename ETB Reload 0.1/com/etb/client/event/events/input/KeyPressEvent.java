package com.etb.client.event.events.input;

import com.etb.client.event.Event;

public class KeyPressEvent extends Event {
	private int key;

	public KeyPressEvent(int key) {
		this.key = key;
	}

	public int getKey() {
		return this.key;
	}
}
