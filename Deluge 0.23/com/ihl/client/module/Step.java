package com.ihl.client.module;

import com.ihl.client.Helper;
import com.ihl.client.event.*;
import com.ihl.client.util.HelperUtil;

import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C03PacketPlayer;

@EventHandler(events = {EventPacket.class, EventEntityStep.class})
public class Step extends Module {

    private double y;
    private boolean step;

    public Step(String name, String desc, Category category, String keybind) {
        super(name, desc, category, keybind);
        initCommands(name.toLowerCase().replaceAll(" ", ""));
    }

    public void disable() {
        super.disable();
        if (!HelperUtil.inGame()) {
            return;
        }
        Helper.player().stepHeight = 0.5f;
    }

    protected void onEvent(Event event) {
        if (event instanceof EventEntityStep) {
            EventEntityStep e = (EventEntityStep) event;
            if (e.type == Event.Type.PRE) {
                if (e.entity == Helper.player() && !(Helper.world().getBlockState(Helper.player().getPosition()).getBlock() == Blocks.ladder) && !Helper.player().isInWater()) {
                    y = Helper.player().posY;
                    Helper.player().stepHeight = 1.3f;
                }
            } else if (e.type == Event.Type.POST) {
                if (Helper.player().getEntityBoundingBox().minY - y > 0.5) {
                    step = Helper.player().onGround && !Helper.player().isInWater() && !Helper.player().isCollidedHorizontally;
                    Helper.player().stepHeight = 0.5f;
                }
            }
        } else if (event instanceof EventPacket) {
            EventPacket e = (EventPacket) event;
            if (e.type == Event.Type.SEND) {
                if (e.packet instanceof C03PacketPlayer) {
                    C03PacketPlayer packet = (C03PacketPlayer) e.packet;
                    if (step) {
                        packet.y += 0.06499999761581421D;
                        step = false;
                    }
                }
            }
        }
    }

}
