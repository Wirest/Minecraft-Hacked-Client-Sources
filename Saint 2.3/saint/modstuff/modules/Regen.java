package saint.modstuff.modules;

import net.minecraft.network.play.client.C03PacketPlayer;
import saint.Saint;
import saint.comandstuff.Command;
import saint.eventstuff.Event;
import saint.eventstuff.events.PreMotion;
import saint.modstuff.ModManager;
import saint.modstuff.Module;
import saint.utilities.BlockHelper;
import saint.utilities.Logger;
import saint.valuestuff.Value;

public class Regen extends Module {
   private final Value packets = new Value("regen_packets", 200);

   public Regen() {
      super("Regen", -16728065, ModManager.Category.PLAYER);
      Saint.getCommandManager().getContentList().add(new Command("regenpackets", "<amount>", new String[]{"regenp", "rp"}) {
         public void run(String message) {
            if (message.split(" ")[1].equalsIgnoreCase("-d")) {
               Regen.this.packets.setValueState((Integer)Regen.this.packets.getDefaultValue());
            } else {
               Regen.this.packets.setValueState(Integer.parseInt(message.split(" ")[1]));
            }

            if ((Integer)Regen.this.packets.getValueState() > 1000) {
               Regen.this.packets.setValueState(1000);
            } else if ((Integer)Regen.this.packets.getValueState() < 5) {
               Regen.this.packets.setValueState(5);
            }

            Logger.writeChat("Regen Packets amount set to: " + Regen.this.packets.getValueState());
         }
      });
   }

   private boolean isViable() {
      return !mc.thePlayer.isInWater() && mc.thePlayer.onGround && mc.thePlayer.getHealth() < 20.0F && mc.thePlayer.getFoodStats().getFoodLevel() > 17 && !BlockHelper.isOnLiquid() && !BlockHelper.isInLiquid();
   }

   public void onEvent(Event event) {
      if (event instanceof PreMotion && this.isViable()) {
         for(int p = 0; p < (Integer)this.packets.getValueState(); ++p) {
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer(mc.thePlayer.onGround));
         }
      }

   }
}
