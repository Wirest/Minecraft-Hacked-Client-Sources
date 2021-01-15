/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.server.S2DPacketOpenWindow
 */
package me.aristhena.lucid.modules.misc;

import me.aristhena.lucid.eventapi.EventTarget;
import me.aristhena.lucid.eventapi.events.PacketReceiveEvent;
import me.aristhena.lucid.management.module.Mod;
import me.aristhena.lucid.management.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S2DPacketOpenWindow;

@Mod(name="Inventory+")
public class InventoryPlus
extends Module {
    public static boolean shouldCancel = true;

    @EventTarget
    private void onPacketSend(PacketReceiveEvent event) {
        if (event.packet instanceof S2DPacketOpenWindow) {
            shouldCancel = false;
            this.mc.thePlayer.closeScreen();
            shouldCancel = true;
        }
    }

    public static boolean cancelClose() {
        return false;
    }
}

