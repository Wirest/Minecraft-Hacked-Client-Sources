package org.m0jang.crystal.Mod.Collection.Movement;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import org.m0jang.crystal.Wrapper;
import org.m0jang.crystal.Events.EventUpdate;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;
import org.m0jang.crystal.Utils.TimeHelper;

public class Jesus extends Module {
   private TimeHelper time = new TimeHelper();

   public Jesus() {
      super("Jesus", Category.Movement, false);
   }

   public void onEnable() {
      super.onEnable();
   }

   public void onDisable() {
      super.onDisable();
   }

   @EventTarget
   private void onUpdate(EventUpdate event) {
      if (Minecraft.thePlayer != null && Wrapper.isInLiquid() && Minecraft.thePlayer.isInsideOfMaterial(Material.air) && !Minecraft.thePlayer.isSneaking()) {
         if (this.time.hasPassed(50.0D)) {
            Minecraft.thePlayer.motionY = 0.01D;
            this.time.reset();
         }

         Minecraft.thePlayer.motionY = 0.05D;
         Minecraft.thePlayer.jumpMovementFactor *= 1.12F;
      }

   }
}
