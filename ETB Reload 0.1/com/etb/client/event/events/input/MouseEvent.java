package com.etb.client.event.events.input;

import com.etb.client.event.Event;

public class MouseEvent extends Event {
	private int button;

	public MouseEvent(final int button) {
		this.button = button;
	}

	public int getButton() {
		return this.button;
	}

	public void setButton(final int button) {
		this.button = button;
	}
}
