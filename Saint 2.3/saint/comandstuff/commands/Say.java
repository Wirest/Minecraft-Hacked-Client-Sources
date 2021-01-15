package saint.comandstuff.commands;

import net.minecraft.network.play.client.C01PacketChatMessage;
import saint.comandstuff.Command;

public class Say extends Command {
   public Say() {
      super("say", "<message>", "addchat", "saychat");
   }

   public void run(String message) {
      mc.getNetHandler().addToSendQueue(new C01PacketChatMessage(message.substring(4)));
   }
}
