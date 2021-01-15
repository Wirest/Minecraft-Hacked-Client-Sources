/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.network.NetHandlerPlayClient
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C01PacketChatMessage
 */
package me.aristhena.lucid.commands;

import me.aristhena.lucid.management.command.Com;
import me.aristhena.lucid.management.command.Command;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C01PacketChatMessage;

@Com(names={"ncp"})
public class Ncp
extends Command {
    @Override
    public void runCommand(String[] args) {
        Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue((Packet)new C01PacketChatMessage("/testncp input"));
        Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue((Packet)new C01PacketChatMessage("/testncp input " + args[1]));
    }

    @Override
    public String getHelp() {
        return "Ncp - ncp (name).";
    }
}

