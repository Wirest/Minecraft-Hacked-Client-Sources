package org.m0jang.crystal.Mod.Collection.Movement.speed;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Timer;
import org.m0jang.crystal.Events.EventState;
import org.m0jang.crystal.Events.EventUpdate;
import org.m0jang.crystal.Mod.SubModule;

public class KohiHop extends SubModule {
   public KohiHop() {
      super("KohiHop", "Speed");
   }

   public void onDisable() {
      Timer.timerSpeed = 1.0F;
      super.onDisable();
   }

   @EventTarget
   public void onUpdate(EventUpdate event) {
      if (Minecraft.thePlayer.isMoving()) {
         Minecraft.thePlayer.setSpeed(Minecraft.thePlayer.getSpeed());
         if (event.state == EventState.PRE) {
            Minecraft.thePlayer.setSpeed(Minecraft.thePlayer.getSpeed() + 0.035D);
            if (Minecraft.thePlayer.onGround) {
               Minecraft.thePlayer.motionY = 0.4D;
            } else if (Minecraft.thePlayer.motionY < -0.3D) {
               Minecraft.thePlayer.motionY = -0.5D;
               Minecraft.thePlayer.setSpeed(0.6D);
            } else {
               Timer var10000;
               if (Minecraft.thePlayer.motionY > 0.2D && !Minecraft.thePlayer.onGround) {
                  var10000 = this.mc.timer;
                  Timer.timerSpeed = 4.0F;
               } else {
                  var10000 = this.mc.timer;
                  Timer.timerSpeed = 1.0F;
                  Minecraft.thePlayer.setSpeed(0.6D);
               }
            }
         }

      }
   }
}
