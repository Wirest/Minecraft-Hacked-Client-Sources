package moonx.ohare.client.event.impl.game;

import moonx.ohare.client.event.cancelable.CancelableEvent;

public class ChatEvent extends CancelableEvent {
	private String msg;

	public ChatEvent(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}
}
