package com.ihl.client.module;

import com.ihl.client.Helper;
import com.ihl.client.event.Event;
import com.ihl.client.event.EventHandler;
import com.ihl.client.event.EventPacket;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

@EventHandler(events = {EventPacket.class})
public class NoView extends Module {

    public NoView(String name, String desc, Category category, String keybind) {
        super(name, desc, category, keybind);
        initCommands(name.toLowerCase().replaceAll(" ", ""));
    }

    protected void onEvent(Event event) {
        if (event instanceof EventPacket) {
            EventPacket e = (EventPacket) event;
            if (e.type == Event.Type.RECEIVE) {
                if (e.packet instanceof S08PacketPlayerPosLook) {
                    S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) e.packet;
                    packet.yaw = Helper.player().rotationYaw;
                    packet.pitch = Helper.player().rotationPitch;
                }
            }
        }
    }

}
