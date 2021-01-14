package store.shadowclient.client.hud.draggablehud.hudmods.impl;

import store.shadowclient.client.event.events.EventCancelable;

public class KeyEvent extends EventCancelable {
	final int key;

	public KeyEvent(int key) {
		this.key = key;
	}

	public int getKey() {
		return key;
	}
}
