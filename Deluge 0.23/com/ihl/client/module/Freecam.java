package com.ihl.client.module;

import com.ihl.client.Helper;
import com.ihl.client.event.*;
import com.ihl.client.module.option.Option;
import com.ihl.client.module.option.ValueBoolean;
import com.ihl.client.util.HelperUtil;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

@EventHandler(events = {EventPlayerLiving.class, EventPlayerMove.class, EventPacket.class})

public class Freecam extends Module {

    private EntityOtherPlayerMP ghost;

    public Freecam(String name, String desc, Category category, String keybind) {
        super(name, desc, category, keybind);
        options.put("noclip", new Option("Noclip", "Ghost through blocks", new ValueBoolean(true), Option.Type.BOOLEAN));
        initCommands(name.toLowerCase().replaceAll(" ", ""));
    }

    public void enable() {
        super.enable();
        if (!HelperUtil.inGame()) {
            return;
        }
        ghost = new EntityOtherPlayerMP(Helper.world(), Helper.mc().getSession().getProfile());
        ghost.clonePlayer(Helper.player(), true);
        copyEntity(ghost, Helper.player());
        Helper.world().addEntityToWorld(-id, ghost);
        Helper.player().capabilities.isFlying = true;
        Helper.player().onGround = false;
    }

    public void disable() {
        super.disable();
        if (!HelperUtil.inGame()) {
            return;
        }
        Helper.player().clonePlayer(ghost, true);
        copyEntity(Helper.player(), ghost);
        Helper.world().removeEntityFromWorld(-id);
        Helper.player().capabilities.isFlying = Helper.player().capabilities.allowFlying;
    }

    protected void onEvent(Event event) {
        if (event instanceof EventPlayerLiving) {
            EventPlayerLiving e = (EventPlayerLiving) event;
            if (e.type == Event.Type.PRE) {
                Helper.player().capabilities.isFlying = true;
            }
        } else if (event instanceof EventPacket) {
            EventPacket e = (EventPacket) event;
            if (e.type == Event.Type.SEND) {
                if (e.packet instanceof C03PacketPlayer) {
                    e.cancel();
                }
            } else if (e.type == Event.Type.RECEIVE) {
                if (e.packet instanceof S08PacketPlayerPosLook) {
                    e.cancel();
                }
            }
        }
    }

    private void copyEntity(EntityLivingBase to, EntityLivingBase from) {
        to.copyLocationAndAnglesFrom(from);
        to.rotationYaw = from.rotationYaw;
        to.rotationYawHead = from.rotationYawHead;
        to.rotationPitch = from.rotationPitch;
    }
}
