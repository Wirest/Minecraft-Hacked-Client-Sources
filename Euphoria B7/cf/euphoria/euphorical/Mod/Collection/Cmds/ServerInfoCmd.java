// 
// Decompiled by Procyon v0.5.30
// 

package cf.euphoria.euphorical.Mod.Collection.Cmds;

import cf.euphoria.euphorical.Mod.Cmd;
import cf.euphoria.euphorical.Mod.Cmd.Info;
import cf.euphoria.euphorical.Utils.ChatUtils;
import net.minecraft.client.Minecraft;

@Info(name = "si", syntax = {}, help = "Displays current server info")
public class ServerInfoCmd extends Cmd
{
    @Override
    public void execute(final String[] p0) throws Error {
        if (!Minecraft.getMinecraft().isSingleplayer()) {
            ChatUtils.sendMessageToPlayer("§6IP:§r " + Minecraft.getMinecraft().getCurrentServerData().serverIP);
            ChatUtils.sendMessageToPlayer("§6Version: §r" + Minecraft.getMinecraft().getCurrentServerData().gameVersion);
        }
        else {
            ChatUtils.sendMessageToPlayer("Multiplayer only!");
        }
    }
}
