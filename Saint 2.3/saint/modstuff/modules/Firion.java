package saint.modstuff.modules;

import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.StringUtils;
import saint.Saint;
import saint.comandstuff.Command;
import saint.eventstuff.Event;
import saint.eventstuff.events.PreMotion;
import saint.modstuff.ModManager;
import saint.modstuff.Module;
import saint.utilities.Logger;
import saint.utilities.TimeHelper;
import saint.valuestuff.Value;

public class Firion extends Module {
   private final Value fire = new Value("firion_antifire", false);
   private final Value potion = new Value("firion_antipotion", true);
   private boolean cleansing = false;
   private String potionName = "";
   private final TimeHelper time = new TimeHelper();
   private final TimeHelper time2 = new TimeHelper();

   public Firion() {
      super("Firion", -8388608, ModManager.Category.EXPLOITS);
      Saint.getCommandManager().getContentList().add(new Command("firion", "<antifire/antipotions>", new String[0]) {
         public void run(String message) {
            if (message.split(" ")[1].equalsIgnoreCase("antifire")) {
               Firion.this.fire.setValueState(!(Boolean)Firion.this.fire.getValueState());
               Logger.writeChat("Firion will " + ((Boolean)Firion.this.fire.getValueState() ? "now" : "no longer") + " get rid of fire.");
            } else if (message.split(" ")[1].equalsIgnoreCase("antipotions")) {
               Firion.this.potion.setValueState(!(Boolean)Firion.this.potion.getValueState());
               Logger.writeChat("Firion will " + ((Boolean)Firion.this.potion.getValueState() ? "now" : "no longer") + " get rid of bad potion effects.");
            } else {
               Logger.writeChat("Option not valid! Available options: antifire, anitpotions.");
            }

         }
      });
   }

   public void onEvent(Event event) {
      if (event instanceof PreMotion) {
         if ((Boolean)this.potion.getValueState()) {
            Potion[] var5;
            int var4 = (var5 = Potion.potionTypes).length;

            for(int var3 = 0; var3 < var4; ++var3) {
               Potion potion = var5[var3];
               if (potion != null && potion.isBadEffect()) {
                  this.potionName = potion.getName();
                  PotionEffect effect = mc.thePlayer.getActivePotionEffect(potion);
                  if (effect != null) {
                     if (this.time.hasReached(1000L) && effect.getDuration() < 10000) {
                        this.cleansing = true;
                        this.time.reset();
                     }

                     if (effect.getDuration() < 10000 && mc.thePlayer.onGround) {
                        for(int i = 0; i < effect.getDuration() / 20; ++i) {
                           mc.getNetHandler().addToSendQueue(new C03PacketPlayer(mc.thePlayer.onGround));
                        }
                     }
                  }
               }
            }
         }

         if (this.cleansing) {
            String msg = StringUtils.stripControlCodes("Cleansing [§c" + this.potionName + "§f]" + " ! [Firion]");
            Saint.getNotificationManager().addInfo(msg);
            this.cleansing = false;
         }

         if ((Boolean)this.fire.getValueState() && mc.thePlayer.isBurning()) {
            if (this.time2.hasReached(5000L) && mc.thePlayer.getActivePotionEffect(Potion.fireResistance) == null) {
               Saint.getNotificationManager().addInfo("Stopping fire! [Firion]");
               this.time2.reset();
            }

            if (mc.thePlayer.getActivePotionEffect(Potion.fireResistance) == null) {
               for(int x = 0; x < 120; ++x) {
                  mc.getNetHandler().addToSendQueue(new C03PacketPlayer(mc.thePlayer.onGround));
               }
            }
         }
      }

   }
}
