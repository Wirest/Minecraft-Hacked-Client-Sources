package org.m0jang.crystal.Mod.Collection.Movement.speed;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.util.Timer;
import org.m0jang.crystal.Events.EventMove;
import org.m0jang.crystal.Mod.SubModule;
import org.m0jang.crystal.Utils.TimeHelper;

public class SpartanSpeed extends SubModule {
   private boolean boost = true;
   TimeHelper timer = new TimeHelper();

   public SpartanSpeed() {
      super("SpartanSpeed", "Speed");
   }

   public void onEnable() {
      super.onEnable();
   }

   public void onDisable() {
      super.onDisable();
      Timer.timerSpeed = 1.0F;
   }

   @EventTarget
   public void onMove(EventMove event) {
      if (this.timer.hasPassed(3000.0D)) {
         this.boost = !this.boost;
         this.timer.reset();
      }

      if (this.boost) {
         Timer.timerSpeed = 1.6F;
      } else {
         Timer.timerSpeed = 1.0F;
      }

   }
}
