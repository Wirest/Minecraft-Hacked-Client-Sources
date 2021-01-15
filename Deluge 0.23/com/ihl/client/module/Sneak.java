package com.ihl.client.module;

import com.ihl.client.Helper;
import com.ihl.client.event.Event;
import com.ihl.client.event.EventHandler;
import com.ihl.client.event.EventPacket;
import com.ihl.client.event.EventPlayerMotion;
import com.ihl.client.util.HelperUtil;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0BPacketEntityAction;

@EventHandler(events = {EventPacket.class, EventPlayerMotion.class})
public class Sneak extends Module {

    public Sneak(String name, String desc, Category category, String keybind) {
        super(name, desc, category, keybind);
        initCommands(name.toLowerCase().replaceAll(" ", ""));
    }

    public void disable() {
        super.disable();
        if (!HelperUtil.inGame()) {
            return;
        }
        Helper.player().sendQueue.addToSendQueue(new C0BPacketEntityAction(Helper.player(), C0BPacketEntityAction.Action.STOP_SNEAKING));
    }

    protected void onEvent(Event event) {
        if (event instanceof EventPlayerMotion) {
            EventPlayerMotion e = (EventPlayerMotion) event;
            if (e.type == Event.Type.PRE) {
                Helper.player().sendQueue.addToSendQueue(new C0BPacketEntityAction(Helper.player(), C0BPacketEntityAction.Action.STOP_SNEAKING));
            } else if (e.type == Event.Type.POST) {
                Helper.player().sendQueue.addToSendQueue(new C0BPacketEntityAction(Helper.player(), C0BPacketEntityAction.Action.START_SNEAKING));
            }
        } else if (event instanceof EventPacket) {
            EventPacket e = (EventPacket) event;
            if (e.packet instanceof C08PacketPlayerBlockPlacement) {
                Helper.player().sendQueue.addToSendQueue(new C0BPacketEntityAction(Helper.player(), C0BPacketEntityAction.Action.STOP_SNEAKING));
            }
        }
    }
}
