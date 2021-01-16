package org.m0jang.crystal.Mod.Collection.Movement.fly;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MovementInput;
import net.minecraft.util.Timer;
import org.m0jang.crystal.Events.EventUpdate;
import org.m0jang.crystal.Mod.SubModule;

public class Cube extends SubModule {
   private boolean flynig;
   private boolean second;
   double startY;

   public Cube() {
      super("Cubecraft", "Fly");
   }

   public void onEnable() {
      super.onEnable();
   }

   public void onDisable() {
      Timer.timerSpeed = 1.0F;
      super.onDisable();
   }

   @EventTarget
   public void onUpdate(EventUpdate event) {
      Timer var10000 = this.mc.timer;
      Timer.timerSpeed = 0.3F;
      Minecraft.thePlayer.motionY = 0.0D;
      Minecraft.thePlayer.setPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 1.0E-9D, Minecraft.thePlayer.posZ);
      double v1 = 1.0D;
      float v3 = MovementInput.moveForward;
      float v4 = MovementInput.moveStrafe;
      float v5 = Minecraft.thePlayer.rotationYaw;
      if ((v3 != 0.0F || v4 != 0.0F) && v3 != 0.0F) {
         if (v4 >= 1.0F) {
            v5 += (float)(v3 > 0.0F ? -45 : 45);
            v4 = 0.0F;
         } else if (v4 <= -1.0F) {
            v5 += (float)(v3 > 0.0F ? 45 : -45);
            v4 = 0.0F;
         }

         if (v3 > 0.0F) {
            v3 = 1.0F;
         } else if (v3 < 0.0F) {
            v3 = -1.0F;
         }
      }

      double v6 = Math.cos(Math.toRadians((double)(v5 + 90.0F)));
      double v8 = Math.sin(Math.toRadians((double)(v5 + 90.0F)));
      Minecraft.thePlayer.motionX = (double)v3 * v1 * v6 + (double)v4 * v1 * v8;
      Minecraft.thePlayer.motionZ = (double)v3 * v1 * v8 - (double)v4 * v1 * v6;
      if (Minecraft.gameSettings.keyBindJump.getIsKeyPressed()) {
         Minecraft.thePlayer.motionY = 0.5D * v1;
      }

      if (Minecraft.thePlayer.isSneaking()) {
         Minecraft.thePlayer.motionY = 0.5D * -v1;
      }

   }
}
