/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.util.command.cmds;

import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.command.Command;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C01PacketChatMessage;

public class CommandSay
extends Command {
    public static boolean blockedmsg = false;

    public CommandSay(String[] commands) {
        super(commands);
        this.setArgs(".say <text>");
    }

    @Override
    public void onCmd(String[] args) {
        String msg = "";
        if (args.length <= 1) {
            ClientUtil.sendClientMessage(this.getArgs(), ClientNotification.Type.WARNING);
            return;
        }
        int i = 1;
        while (i < args.length) {
            msg = String.valueOf(String.valueOf(msg)) + args[i] + " ";
            ++i;
        }
        Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C01PacketChatMessage(msg));
        super.onCmd(args);
    }
}

