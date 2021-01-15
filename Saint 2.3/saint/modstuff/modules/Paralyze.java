package saint.modstuff.modules;

import net.minecraft.network.play.client.C03PacketPlayer;
import saint.eventstuff.Event;
import saint.eventstuff.events.OnUpdate;
import saint.modstuff.ModManager;
import saint.modstuff.Module;

public class Paralyze extends Module {
   public Paralyze() {
      super("Paralyze", -2894893, ModManager.Category.COMBAT);
   }

   public void onEvent(Event event) {
      if (event instanceof OnUpdate) {
         if (!mc.theWorld.getEntitiesWithinAABBExcludingEntity(mc.thePlayer, mc.thePlayer.boundingBox).isEmpty()) {
            this.setColor(-256);

            for(int i = 0; i < 1337; ++i) {
               mc.getNetHandler().addToSendQueue(new C03PacketPlayer(mc.thePlayer.onGround));
            }
         } else {
            this.setColor(-2894893);
         }
      }

   }
}
