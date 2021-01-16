package org.m0jang.crystal.Mod.Collection.Movement;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;

import org.m0jang.crystal.Wrapper;
import org.m0jang.crystal.Events.EventMove;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;
import org.m0jang.crystal.Utils.TimeHelper;

public class FastSwim extends Module {
   TimeHelper timer = new TimeHelper();

   public FastSwim() {
      super("FastSwim", Category.Movement, false);
   }

   @EventTarget
   private void onUpdate(EventMove event) {
      if (Minecraft.thePlayer != null && Minecraft.theWorld != null) {
         if (Wrapper.isInLiquid() && this.timer.hasPassed(1000.0D)) {
            Minecraft.thePlayer.motionX *= 1.0D;
            Minecraft.thePlayer.motionZ *= 1.0D;
            Minecraft.thePlayer.motionY = 0.4D;
            this.timer.reset();
         }

      }
   }
}
