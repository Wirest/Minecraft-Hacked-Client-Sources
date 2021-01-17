/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.util.command.cmds;

import java.util.ArrayList;
import me.slowly.client.Client;
import me.slowly.client.irc.IRCChat;
import me.slowly.client.irc.IRCProfile;
import me.slowly.client.util.command.Command;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class CommandUserList
extends Command {
    public CommandUserList(String[] commands) {
        super(commands);
    }

    private void append(String msg) {
        if (Minecraft.getMinecraft().theWorld != null) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(msg));
        }
    }

    @Override
    public void onCmd(String[] args) {
        ArrayList<IRCProfile> userList = Client.getInstance().getIrcChat().userList;
        this.append("\u00a7eOnline Users: \u00a7f" + userList.size());
        String message = "Me";
        for (IRCProfile profile : userList) {
            String s = profile.getName();
            message = String.valueOf(message) + ", " + s;
        }
        this.append("\u00a7eUsers: \u00a7f " + message);
    }
}

