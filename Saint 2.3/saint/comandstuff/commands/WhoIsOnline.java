package saint.comandstuff.commands;

import java.util.ArrayList;
import java.util.Iterator;
import saint.comandstuff.Command;
import saint.irc.SyncData;
import saint.irc.SyncUtil;
import saint.utilities.Logger;

public class WhoIsOnline extends Command {
   public WhoIsOnline() {
      super("whoisonline", "none", "whoson", "wio");
   }

   public void run(String message) {
      ArrayList dataArrayList = SyncUtil.getLatestDataForUsers();
      if (dataArrayList != null) {
         if (dataArrayList.isEmpty()) {
            Logger.writeChat("No one is online.");
         }

         Iterator var4 = dataArrayList.iterator();

         while(var4.hasNext()) {
            SyncData data = (SyncData)var4.next();
            Logger.writeChat(data.getUser() + ", using " + data.getInfo() + " is " + (data.getServer() == null ? "online" : "in-game at " + data.getServer()) + (data.getServer() != null ? ", coords: X:" + data.getX() + ", Y:" + data.getY() + ", Z:" + data.getZ() : "") + ".");
         }
      }

   }
}
