package info.spicyclient.events.listeners;

import info.spicyclient.events.Event;
import net.minecraft.client.gui.inventory.GuiChest;

public class EventOpenChest extends Event<EventOpenChest> {
	
	public EventOpenChest(GuiChest chest) {
		this.chest = chest;
	}
	
	public GuiChest chest = null;
	
}
