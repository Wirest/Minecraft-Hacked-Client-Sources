package org.m0jang.crystal.Mod.Collection.Movement;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import org.m0jang.crystal.Events.EventUpdate;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;

public class AirJump extends Module {
   public AirJump() {
      super("AirJump", Category.Movement, false);
   }

   public void onEnable() {
      super.onEnable();
   }

   public void onDisable() {
      super.onDisable();
   }

   @EventTarget
   public void onUpdate(EventUpdate event) {
      Minecraft.thePlayer.onGround = true;
      Minecraft.thePlayer.isAirBorne = false;
      Minecraft.thePlayer.fallDistance = 0.0F;
   }

   public static float get360Angle(float angle) {
      float output = angle;
      if (angle < 0.0F) {
         output = angle + 360.0F;
      }

      return output;
   }
}
