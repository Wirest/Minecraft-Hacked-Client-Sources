package saint.modstuff.modules;

import saint.eventstuff.Event;
import saint.eventstuff.events.PreMotion;
import saint.modstuff.Module;
import saint.utilities.TimeHelper;

public class Stuck extends Module {
   private final TimeHelper time = new TimeHelper();
   private final TimeHelper time2 = new TimeHelper();

   public Stuck() {
      super("Stuck");
   }

   public void onEvent(Event event) {
      if (event instanceof PreMotion && KillAura.lastAttackedTarget != null && mc.thePlayer.getDistanceToEntity(KillAura.lastAttackedTarget) <= 1.75F) {
         mc.thePlayer.setPosition(KillAura.lastAttackedTarget.posX, KillAura.lastAttackedTarget.posY, KillAura.lastAttackedTarget.posZ);
         if (!this.time.hasReached(1000L)) {
            mc.thePlayer.onGround = true;
            this.time.reset();
         }
      }

   }
}
