package com.mentalfrostbyte.jello.event.events;

import com.mentalfrostbyte.jello.event.events.callables.EventCancellable;

import net.minecraft.util.BlockPos;

public class EventJoinServer
extends EventCancellable {
    String ip;

    public EventJoinServer(String serverIP) {
        this.ip = serverIP;
    }

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

    
}

