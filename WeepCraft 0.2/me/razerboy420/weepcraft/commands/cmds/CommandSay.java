/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.commands.cmds;

import me.razerboy420.weepcraft.commands.Command;
import me.razerboy420.weepcraft.util.Wrapper;
import net.minecraft.network.play.client.CPacketChatMessage;

public class CommandSay
extends Command {
    public CommandSay() {
        super(new String[]{"say"}, "Say things like \".help\" in chat!", ".say <saying>");
    }

    @Override
    public boolean runCommand(String command, String[] args) {
        try {
            Wrapper.sendPacket(new CPacketChatMessage(command.replace(".say ", "")));
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
}

