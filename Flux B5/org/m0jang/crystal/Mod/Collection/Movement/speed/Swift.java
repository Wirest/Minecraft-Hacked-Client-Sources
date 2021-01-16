package org.m0jang.crystal.Mod.Collection.Movement.speed;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import org.m0jang.crystal.Events.EventMove;
import org.m0jang.crystal.Mod.SubModule;

public class Swift extends SubModule {
   private int motionDelay;

   public Swift() {
      super("Swift", "Speed");
   }

   @EventTarget
   public void onMove(EventMove event) {
      if (Minecraft.thePlayer.onGround) {
         ++this.motionDelay;
         this.motionDelay %= 2;
         if (this.motionDelay == 1) {
            Minecraft.thePlayer.motionX *= 2.58D;
            Minecraft.thePlayer.motionZ *= 2.58D;
         } else {
            Minecraft.thePlayer.motionX /= 1.5D;
            Minecraft.thePlayer.motionZ /= 1.5D;
         }

         Minecraft.thePlayer.moveStrafing *= 0.0F;
         Minecraft.thePlayer.motionY = 0.001D;
      }

      Minecraft.thePlayer.isAirBorne = false;
   }
}
