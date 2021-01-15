package saint.modstuff.modules;

import net.minecraft.network.play.client.C03PacketPlayer;
import saint.Saint;
import saint.comandstuff.Command;
import saint.eventstuff.Event;
import saint.eventstuff.events.PreMotion;
import saint.modstuff.ModManager;
import saint.modstuff.Module;
import saint.utilities.Logger;
import saint.utilities.TimeHelper;
import saint.valuestuff.Value;

public class AutoLog extends Module {
   private final TimeHelper time = new TimeHelper();
   private Value health = new Value("autolog_health", 4.0F);

   public AutoLog() {
      super("AutoLog", -5192482, ModManager.Category.EXPLOITS);
      this.setTag("Auto Log");
      Saint.getCommandManager().getContentList().add(new Command("autologhealth", "<hearts>", new String[]{"autologhp", "alh"}) {
         public void run(String message) {
            if (message.split(" ")[1].equalsIgnoreCase("-d")) {
               AutoLog.this.health.setValueState((Float)AutoLog.this.health.getDefaultValue());
            } else {
               AutoLog.this.health.setValueState(Float.parseFloat(message.split(" ")[1]) * 2.0F);
            }

            if ((Float)AutoLog.this.health.getValueState() > 10.0F) {
               AutoLog.this.health.setValueState(10.0F);
            } else if ((Float)AutoLog.this.health.getValueState() < 1.0F) {
               AutoLog.this.health.setValueState(1.0F);
            }

            Logger.writeChat("Auto Log health set to: " + AutoLog.this.health.getValueState());
         }
      });
   }

   public void onEvent(Event event) {
      if (event instanceof PreMotion && mc.thePlayer != null) {
         if (mc.thePlayer.getHealth() <= (Float)this.health.getValueState()) {
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Double.NaN, Double.NaN, Double.NaN, false));
            this.setEnabled(false);
         }

         if (mc.thePlayer.getHealth() <= (Float)this.health.getValueState() + 2.0F && this.time.hasReached(5000L)) {
            Saint.getNotificationManager().addWarn("Prepearing to log off!");
            this.time.reset();
         }
      }

   }
}
