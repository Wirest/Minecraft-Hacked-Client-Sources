package com.etb.client.event.events.game;

import com.etb.client.event.CancelableEvent;
import com.etb.client.event.CancelableEvent;

public class ChatEvent extends CancelableEvent {
	private String msg;

	public ChatEvent(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}
}
