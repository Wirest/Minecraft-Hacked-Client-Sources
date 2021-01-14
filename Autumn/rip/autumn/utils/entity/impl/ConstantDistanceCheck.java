package rip.autumn.utils.entity.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import rip.autumn.utils.entity.ICheck;

public final class ConstantDistanceCheck implements ICheck {
   private final float distance;

   public ConstantDistanceCheck(float distance) {
      this.distance = distance;
   }

   public boolean validate(Entity entity) {
      return Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entity) <= this.distance;
   }
}
