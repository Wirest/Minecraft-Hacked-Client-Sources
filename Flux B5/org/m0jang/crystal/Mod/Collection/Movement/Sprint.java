package org.m0jang.crystal.Mod.Collection.Movement;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MovementInput;
import org.m0jang.crystal.Events.EventUpdate;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;

public class Sprint extends Module {
   public Sprint() {
      super("Sprint", Category.Movement, false);
   }

   public void onEnable() {
      super.onEnable();
   }

   public void onDisable() {
      super.onDisable();
   }

   @EventTarget
   public void onUpdate(EventUpdate event) {
      if ((MovementInput.moveForward != 0.0F || MovementInput.moveStrafe != 0.0F) && Minecraft.thePlayer.getFoodStats().getFoodLevel() > 6 && !Minecraft.thePlayer.isSneaking()) {
         Minecraft.thePlayer.setSprinting(true);
      }

   }
}
