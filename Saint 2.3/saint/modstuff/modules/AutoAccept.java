package saint.modstuff.modules;

import java.util.Iterator;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.StringUtils;
import saint.Saint;
import saint.eventstuff.Event;
import saint.eventstuff.events.RecievePacket;
import saint.modstuff.Module;
import saint.utilities.Logger;
import saint.utilities.TimeHelper;

public class AutoAccept extends Module {
   private final TimeHelper time = new TimeHelper();

   public AutoAccept() {
      super("AutoAccept");
      this.setEnabled(true);
   }

   public void onEvent(Event event) {
      if (event instanceof RecievePacket) {
         RecievePacket receive = (RecievePacket)event;
         if (receive.getPacket() instanceof S02PacketChat) {
            S02PacketChat chat = (S02PacketChat)receive.getPacket();
            String message = StringUtils.stripControlCodes(chat.func_148915_c().getFormattedText());
            Iterator var6 = Saint.getFriendManager().getContents().keySet().iterator();

            while(true) {
               String name;
               do {
                  do {
                     if (!var6.hasNext()) {
                        return;
                     }

                     name = (String)var6.next();
                  } while(!message.contains(name));
               } while(!message.contains("has requested to teleport to you.") && !message.contains("has requested you teleport to them."));

               mc.thePlayer.sendChatMessage("/tpyes " + name);
               if (this.time.hasReached(2000L)) {
                  Logger.writeChat("Accepted request from: §a" + name + "§f.");
                  this.time.reset();
               }
            }
         }
      }

   }
}
