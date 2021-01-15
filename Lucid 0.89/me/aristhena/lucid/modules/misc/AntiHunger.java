/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C03PacketPlayer
 */
package me.aristhena.lucid.modules.misc;

import me.aristhena.lucid.eventapi.EventTarget;
import me.aristhena.lucid.eventapi.events.PacketSendEvent;
import me.aristhena.lucid.management.module.Module;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

public class AntiHunger
extends Module {
    @EventTarget
    private void onPacketSend(PacketSendEvent event) {
        if (event.packet instanceof C03PacketPlayer) {
            C03PacketPlayer packet = (C03PacketPlayer)event.packet;
            packet.onGround = false;
        }
    }
}

