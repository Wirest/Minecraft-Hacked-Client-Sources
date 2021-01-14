package store.shadowclient.client.event.events;

import store.shadowclient.client.event.Event;
import net.minecraft.client.gui.GuiScreen;

public class EventGuiClose extends Event {

	private final GuiScreen screen;
	
	public EventGuiClose(GuiScreen screen) {
		this.screen = screen;
	}
	
	public GuiScreen getScreen() {
		return screen;
	}
}
