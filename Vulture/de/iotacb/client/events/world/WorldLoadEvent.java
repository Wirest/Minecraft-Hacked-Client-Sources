package de.iotacb.client.events.world;

import com.darkmagician6.eventapi.events.Event;

import net.minecraft.client.multiplayer.WorldClient;

public class WorldLoadEvent implements Event {

	private WorldClient world;
	
	public WorldLoadEvent(WorldClient worldClient) {
		this.world = worldClient;
	}
	
	public WorldClient getWorld() {
		return world;
	}
	
}
