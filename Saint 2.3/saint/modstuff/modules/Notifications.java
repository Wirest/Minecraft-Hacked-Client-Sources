package saint.modstuff.modules;

import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.StringUtils;
import saint.Saint;
import saint.eventstuff.Event;
import saint.eventstuff.events.PreMotion;
import saint.eventstuff.events.RecievePacket;
import saint.modstuff.Module;
import saint.utilities.TimeHelper;

public class Notifications extends Module {
   private boolean sendFoodNotify;
   private boolean sendHealthNotify;
   private final TimeHelper time = new TimeHelper();

   public Notifications() {
      super("Notifications");
      this.setEnabled(true);
   }

   public void onEvent(Event event) {
      if (event instanceof RecievePacket) {
         RecievePacket rec = (RecievePacket)event;
         if (rec.getPacket() instanceof S02PacketChat) {
            S02PacketChat chat = (S02PacketChat)rec.getPacket();
            String message = StringUtils.stripControlCodes(chat.func_148915_c().getFormattedText()).toLowerCase();
            String[] hacksAliases;
            if (message.contains("ground items will be removed in ")) {
               hacksAliases = message.split("be removed in ");
               Saint.getNotificationManager().addWarn(String.format("Ground items will be removed in '%s'.", hacksAliases[1]));
            }

            if (StringUtils.stripControlCodes(message).contains("please slow down chat") && this.time.hasReached(2000L)) {
               Saint.getNotificationManager().addWarn("You are about to get kicked cause of spamming!");
               this.time.reset();
            }

            if (StringUtils.stripControlCodes(message).contains("has invited you to") && this.time.hasReached(2000L)) {
               Saint.getNotificationManager().addInfo("A player has invited you to their faction!");
               this.time.reset();
            }

            if (StringUtils.stripControlCodes(message).contains("has kicked you") && this.time.hasReached(2000L)) {
               Saint.getNotificationManager().addWarn("A player has kicked you out of their faction!");
               this.time.reset();
            }

            if (!message.contains(mc.thePlayer.getName().toLowerCase()) && !message.contains("-> me")) {
               if (event instanceof PreMotion) {
                  if (!this.sendFoodNotify && mc.thePlayer.getFoodStats().getFoodLevel() <= 6) {
                     this.sendFoodNotify = true;
                     Saint.getNotificationManager().addWarn("Your food level is low!");
                  } else if (this.sendFoodNotify && mc.thePlayer.getFoodStats().getFoodLevel() > 6) {
                     this.sendFoodNotify = false;
                  }

                  if (!this.sendHealthNotify && mc.thePlayer.getHealth() <= 6.0F) {
                     this.sendHealthNotify = true;
                     Saint.getNotificationManager().addWarn("Your health is low!");
                  } else if (this.sendHealthNotify && mc.thePlayer.getHealth() > 6.0F) {
                     this.sendHealthNotify = false;
                  }
               }
            } else {
               hacksAliases = new String[]{"hack", "hak", "cheat", "nodus", "huzuni", "record", "ban", "hck", "chiter", "hax", "cheater", "cheating", "hacking", "modder", "client"};
               String[] fanboyAliases = new String[]{"sub", "fan", "favorit", "<3", ":D"};
               String[] var10 = hacksAliases;
               int var9 = hacksAliases.length;

               String alias;
               int var8;
               for(var8 = 0; var8 < var9; ++var8) {
                  alias = var10[var8];
                  if (message.contains(alias) && this.time.hasReached(2000L)) {
                     Saint.getNotificationManager().addWarn("A player may have called you a hacker in chat.");
                     this.time.reset();
                  }
               }

               var10 = fanboyAliases;
               var9 = fanboyAliases.length;

               for(var8 = 0; var8 < var9; ++var8) {
                  alias = var10[var8];
                  if (message.contains(alias) && this.time.hasReached(2000L)) {
                     Saint.getNotificationManager().addInfo("You have a FANBOY!");
                     this.time.reset();
                  }
               }
            }
         }
      }

   }
}
