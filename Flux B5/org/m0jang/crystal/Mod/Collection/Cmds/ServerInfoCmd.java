package org.m0jang.crystal.Mod.Collection.Cmds;

import net.minecraft.client.Minecraft;
import org.m0jang.crystal.Mod.Command;
import org.m0jang.crystal.Utils.ChatUtils;

@Command.Info(
   name = "si",
   syntax = {},
   help = "Displays current server info."
)
public class ServerInfoCmd extends Command {
   public void execute(String[] args) throws Command.Error {
      if (!Minecraft.getMinecraft().isSingleplayer()) {
         ChatUtils.sendMessageToPlayer("Â\2476IP:Â\247r " + Minecraft.getMinecraft().getCurrentServerData().serverIP);
         ChatUtils.sendMessageToPlayer("Â\2476Version: Â\247r" + Minecraft.getMinecraft().getCurrentServerData().gameVersion);
      } else {
         ChatUtils.sendMessageToPlayer("Multiplayer only!");
      }

   }
}
