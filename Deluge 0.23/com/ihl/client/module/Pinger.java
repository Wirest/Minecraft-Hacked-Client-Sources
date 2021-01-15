package com.ihl.client.module;

import com.ihl.client.event.Event;
import com.ihl.client.event.EventHandler;
import com.ihl.client.event.EventPacket;
import net.minecraft.network.play.client.C00PacketKeepAlive;

@EventHandler(events = {EventPacket.class})
public class Pinger extends Module {

    public Pinger(String name, String desc, Category category, String keybind) {
        super(name, desc, category, keybind);
        initCommands(name.toLowerCase().replaceAll(" ", ""));
    }

    protected void onEvent(Event event) {
        if (event instanceof EventPacket) {
            EventPacket e = (EventPacket) event;
            if (e.type == Event.Type.SEND) {
                if (e.packet instanceof C00PacketKeepAlive) {
                    C00PacketKeepAlive packet = (C00PacketKeepAlive) e.packet;
                    packet.key = Integer.MAX_VALUE;
                }
            }
        }
    }

}
