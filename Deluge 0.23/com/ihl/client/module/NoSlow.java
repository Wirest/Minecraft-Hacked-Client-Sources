package com.ihl.client.module;

import com.ihl.client.Helper;
import com.ihl.client.event.Event;
import com.ihl.client.event.EventHandler;
import com.ihl.client.event.EventPacket;
import com.ihl.client.event.EventPlayerUpdate;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

@EventHandler(events = {EventPacket.class, EventPlayerUpdate.class})
public class NoSlow extends Module {

    public NoSlow(String name, String desc, Category category, String keybind) {
        super(name, desc, category, keybind);
        initCommands(name.toLowerCase().replaceAll(" ", ""));
    }

    protected void onEvent(Event event) {
        if (event instanceof EventPlayerUpdate) {
            EventPlayerUpdate e = (EventPlayerUpdate) event;
            if (e.type == Event.Type.PRE) {
                if (Helper.player().isBlocking()) {
                    Helper.mc().getNetHandler().getNetworkManager().sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN), null);
                }
            } else if (e.type == Event.Type.POST) {
                if (Helper.player().isBlocking()) {
                    Helper.mc().getNetHandler().getNetworkManager().sendPacket(new C08PacketPlayerBlockPlacement(Helper.player().inventory.getCurrentItem()), null);
                }
            }
        } else if (event instanceof EventPacket) {
            EventPacket e = (EventPacket) event;
            if (e.type == Event.Type.SEND) {
                if (e.packet instanceof C08PacketPlayerBlockPlacement) {
                    if (Helper.player().isBlocking()) {
                        event.cancel();
                    }
                }
            }
        }
    }

}
