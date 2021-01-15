package saint.modstuff.modules;

import java.util.Iterator;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.util.StringUtils;
import saint.Saint;
import saint.eventstuff.Event;
import saint.eventstuff.events.PacketSent;
import saint.modstuff.Module;

public class DashNames extends Module {
   public DashNames() {
      super("DashNames");
      this.setEnabled(true);
   }

   public void onEvent(Event event) {
      if (event instanceof PacketSent) {
         PacketSent sent = (PacketSent)event;
         if (sent.getPacket() instanceof C01PacketChatMessage) {
            C01PacketChatMessage chat = (C01PacketChatMessage)sent.getPacket();
            String message = chat.getMessage();

            String friend;
            String nameprotect;
            for(Iterator var6 = Saint.getFriendManager().getContents().keySet().iterator(); var6.hasNext(); message = message.replaceAll("(?i)-" + nameprotect, friend)) {
               friend = (String)var6.next();
               nameprotect = StringUtils.stripControlCodes((String)Saint.getFriendManager().getContents().get(friend));
            }

            sent.setPacket(new C01PacketChatMessage(message));
         }
      }

   }
}
