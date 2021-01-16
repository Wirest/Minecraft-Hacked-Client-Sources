/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.commands.cmds;

import me.razerboy420.weepcraft.commands.Command;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;

public class CommandNCPClip
extends Command {
    public CommandNCPClip() {
        super(new String[]{"ncpclip"}, "Go down on ncp servers. 1.10+", "\".ncpclip\"");
    }

    @Override
    public boolean runCommand(String command, String[] args) {
        try {
            Minecraft.getMinecraft().getConnection().sendPacket(new CPacketPlayer.Position(Minecraft.getMinecraft().player.posX, Minecraft.getMinecraft().player.posY - 0.0624, Minecraft.getMinecraft().player.posZ, false));
            Minecraft.getMinecraft().getConnection().sendPacket(new CPacketPlayer.Position(Minecraft.getMinecraft().player.posX, -1337.0 + Minecraft.getMinecraft().player.posY, Minecraft.getMinecraft().player.posZ, true));
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
}

