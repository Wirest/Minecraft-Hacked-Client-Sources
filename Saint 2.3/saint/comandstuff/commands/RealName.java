package saint.comandstuff.commands;

import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.util.StringUtils;
import saint.Saint;
import saint.comandstuff.Command;
import saint.utilities.Logger;

public class RealName extends Command {
   public RealName() {
      super("realname", "<alias>", "rn");
   }

   public void run(String message) {
      boolean found = false;
      Iterator var4 = Minecraft.getMinecraft().getNetHandler().playerInfoMap.keySet().iterator();

      while(var4.hasNext()) {
         Object o = var4.next();
         GuiPlayerTabOverlay playerInfo = (GuiPlayerTabOverlay)o;
         String name = StringUtils.stripControlCodes(playerInfo.playerName);
         if (Saint.getFriendManager().isFriend(name)) {
            String protect = (String)Saint.getFriendManager().getContents().get(name);
            if (message.substring((message.split(" ")[0] + " ").length()).equalsIgnoreCase(protect)) {
               Logger.writeChat("Friend \"§3" + protect + "§r\" real name is: " + name);
               found = true;
            }
         }
      }

      if (!found) {
         Logger.writeChat("Friend \"" + message.split(" ")[1] + "\" was not found!");
      }

   }
}
