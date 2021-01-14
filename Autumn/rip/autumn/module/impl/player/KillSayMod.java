package rip.autumn.module.impl.player;

import me.zane.basicbus.api.annotations.Listener;
import net.minecraft.network.play.server.S02PacketChat;
import rip.autumn.annotations.Label;
import rip.autumn.events.packet.ReceivePacketEvent;
import rip.autumn.module.Module;
import rip.autumn.module.ModuleCategory;
import rip.autumn.module.annotations.Aliases;
import rip.autumn.module.annotations.Category;

@Label("Kill Say")
@Category(ModuleCategory.PLAYER)
@Aliases({"killsay", "killsults"})
public final class KillSayMod extends Module {
   private int messageIndex;
   private static final String[] MESSAGES = new String[]{"%s go eat estrogen femtard", "%s, KANKER AAP", "how did %s even hit the launch game button", "report me %s im really scared", "why is this fat retard %s begging me to turn off my hacks", "sorry %s, this bypass value is exclusive", "%s seriously? go back to cubecraft monkey brain", "Stop crying %s or ill put you back in your cage", "%s how big are your balls? bet they aren't as big as recons"};

   @Listener(ReceivePacketEvent.class)
   public final void onReceivePacket(ReceivePacketEvent event) {
      if (event.getPacket() instanceof S02PacketChat) {
         S02PacketChat packet = (S02PacketChat)event.getPacket();
         String text = packet.getChatComponent().getUnformattedText();
         if (mc.getCurrentServerData().serverIP.contains("mineplex")) {
            text = text.substring(text.indexOf(" "));
         }

         if (text.contains("by " + mc.thePlayer.getName())) {
            if (this.messageIndex >= MESSAGES.length) {
               this.messageIndex = 0;
            }

            mc.thePlayer.sendChatMessage(String.format(MESSAGES[this.messageIndex], text.split(" ")[0]));
            ++this.messageIndex;
         }
      }

   }
}
