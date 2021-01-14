package rip.autumn.module.impl.player;

import me.zane.basicbus.api.annotations.Listener;
import net.minecraft.network.play.client.C01PacketChatMessage;
import rip.autumn.annotations.Label;
import rip.autumn.events.packet.SendPacketEvent;
import rip.autumn.module.Module;
import rip.autumn.module.ModuleCategory;
import rip.autumn.module.annotations.Aliases;
import rip.autumn.module.annotations.Category;

@Label("Chat Bypass")
@Category(ModuleCategory.PLAYER)
@Aliases({"chatbypass"})
public final class ChatBypassMod extends Module {
   @Listener(SendPacketEvent.class)
   public final void onSendPacket(SendPacketEvent event) {
      if (event.getPacket() instanceof C01PacketChatMessage) {
         C01PacketChatMessage packetChatMessage = (C01PacketChatMessage)event.getPacket();
         if (packetChatMessage.getMessage().startsWith("/")) {
            return;
         }

         event.setCancelled();
         StringBuilder msg = new StringBuilder();
         char[] var4 = packetChatMessage.getMessage().toCharArray();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            char character = var4[var6];
            msg.append(character + "\u061c");
         }

         mc.getNetHandler().addToSendQueueSilent(new C01PacketChatMessage(msg.toString().replaceFirst("%", "")));
      }

   }
}
