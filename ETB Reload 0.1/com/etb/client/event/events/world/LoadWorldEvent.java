package com.etb.client.event.events.world;

import com.etb.client.event.Event;

import net.minecraft.client.multiplayer.WorldClient;

public class LoadWorldEvent extends Event {
    private WorldClient worldClient;

    public LoadWorldEvent(WorldClient worldClient) {
        this.worldClient = worldClient;
    }

    public WorldClient getWorldClient() {
        return worldClient;
    }
}