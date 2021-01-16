package org.m0jang.crystal.Mod.Collection.Cmds;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.network.play.server.S3APacketTabComplete;
import org.m0jang.crystal.Events.EventPacketReceive;
import org.m0jang.crystal.Mod.Command;
import org.m0jang.crystal.Utils.ChatUtils;
import org.m0jang.crystal.Utils.TimeHelper;

@Command.Info(
   name = "pd",
   syntax = {},
   help = "Tries to find server plugins."
)
public class PDCmd extends Command {
   TimeHelper timer = new TimeHelper();

   @EventTarget
   public void onReceivePacket(EventPacketReceive event) {
      if (event.packet instanceof S3APacketTabComplete) {
         S3APacketTabComplete packet = (S3APacketTabComplete)event.packet;
         String[] commands = packet.func_149630_c();
         String message = "";
         int size = 0;
         String[] array = commands;
         int length = commands.length;

         for(int i = 0; i < length; ++i) {
            String command = array[i];
            String pluginName = command.split(":")[0].substring(1);
            if (!message.contains(pluginName) && command.contains(":") && !pluginName.equalsIgnoreCase("minecraft") && !pluginName.equalsIgnoreCase("bukkit")) {
               ++size;
               if (message.isEmpty()) {
                  message = message + pluginName;
               } else {
                  message = message + "Â\2478, Â\247f" + pluginName;
               }
            }
         }

         if (!message.isEmpty()) {
            ChatUtils.sendMessageToPlayer("Plugins (" + size + "): Â\247f" + message + "Â\2478.");
         } else {
            ChatUtils.sendMessageToPlayer("Plugins: None Found!");
         }

         event.setCancelled(true);
         EventManager.unregister(this);
      }

      if (this.timer.hasPassed(20000.0D)) {
         EventManager.unregister(this);
         ChatUtils.sendMessageToPlayer("Stopped listening for an S3APacketTabComplete! Took too long (20s)!");
      }

   }

   public void execute(String[] args) throws Command.Error {
      this.timer.reset();
      EventManager.register(this);
      Minecraft.thePlayer.sendQueue.addToSendQueue(new C14PacketTabComplete("/"));
      ChatUtils.sendMessageToPlayer("Listening for a S3APacketTabComplete for 20s!");
   }
}
