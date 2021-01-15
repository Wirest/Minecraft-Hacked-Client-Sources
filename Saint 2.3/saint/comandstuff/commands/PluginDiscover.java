package saint.comandstuff.commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.network.play.server.S3APacketTabComplete;
import saint.Saint;
import saint.comandstuff.Command;
import saint.eventstuff.Event;
import saint.eventstuff.Listener;
import saint.eventstuff.events.RecievePacket;
import saint.utilities.Logger;

public class PluginDiscover extends Command {
   public Listener listener = new Listener() {
      public void onEvent(Event event) {
         PluginDiscover com = (PluginDiscover)Saint.getCommandManager().getCommandUsingName("plugindiscover");
         if (event instanceof RecievePacket) {
            RecievePacket rec = (RecievePacket)event;
            com.plugins = new ArrayList();
            if (rec.getPacket() instanceof S3APacketTabComplete) {
               S3APacketTabComplete packet = (S3APacketTabComplete)rec.getPacket();
               rec.setCancelled(true);
               String[] var8;
               int var7 = (var8 = packet.func_149630_c()).length;

               for(int var6 = 0; var6 < var7; ++var6) {
                  String command = var8[var6];
                  String[] split = command.split(":");
                  if (split.length > 1 && !com.plugins.contains(split[0].substring(1))) {
                     com.plugins.add(split[0].substring(1));
                  }
               }

               StringBuilder pluginBuilder = new StringBuilder();
               pluginBuilder.append("Found plugins: ");
               Iterator var12 = com.plugins.iterator();

               while(var12.hasNext()) {
                  String plugin = (String)var12.next();
                  pluginBuilder.append(plugin);
                  pluginBuilder.append(", ");
               }

               Logger.writeChat(pluginBuilder.substring(0, pluginBuilder.length() - 2));
               Saint.getEventManager().removeListener(PluginDiscover.this.listener);
            }
         }

      }
   };
   public List plugins = new ArrayList();

   public PluginDiscover() {
      super("plugindiscover", "name", "plugins", "pgd");
   }

   public void run(String message) {
      Logger.writeChat("Discovering plugins, please wait...");
      mc.getNetHandler().addToSendQueue(new C14PacketTabComplete("/"));
      Saint.getEventManager().addListener(this.listener);
   }
}
