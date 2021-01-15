package saint.modstuff.modules;

import net.minecraft.network.play.client.C03PacketPlayer;
import saint.Saint;
import saint.comandstuff.Command;
import saint.eventstuff.Event;
import saint.eventstuff.events.EveryTick;
import saint.modstuff.ModManager;
import saint.modstuff.Module;
import saint.utilities.Logger;
import saint.valuestuff.Value;

public class Revive extends Module {
   private final Value health = new Value("revive_health", 8.0F);

   public Revive() {
      super("Revive", -5383962, ModManager.Category.PLAYER);
      Saint.getCommandManager().getContentList().add(new Command("revivehealth", "<hearts>", new String[]{"revivehp", "rh"}) {
         public void run(String message) {
            if (message.split(" ")[1].equalsIgnoreCase("-d")) {
               Revive.this.health.setValueState((Float)Revive.this.health.getDefaultValue());
            } else {
               Revive.this.health.setValueState(Float.parseFloat(message.split(" ")[1]) * 2.0F);
            }

            if ((Float)Revive.this.health.getValueState() > 20.0F) {
               Revive.this.health.setValueState(20.0F);
            } else if ((Float)Revive.this.health.getValueState() < 1.0F) {
               Revive.this.health.setValueState(1.0F);
            }

            Logger.writeChat("Revive health set to: " + Revive.this.health.getValueState());
         }
      });
   }

   public void onEvent(Event event) {
      if (event instanceof EveryTick && mc.thePlayer != null && (Float)this.health.getValueState() >= mc.thePlayer.getHealth()) {
         mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.0D, mc.thePlayer.posZ, mc.thePlayer.onGround));
         mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 2.0D - 999.0D, mc.thePlayer.posZ, mc.thePlayer.onGround));
      }

   }
}
