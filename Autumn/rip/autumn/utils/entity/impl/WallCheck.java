package rip.autumn.utils.entity.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import rip.autumn.utils.entity.ICheck;

public final class WallCheck implements ICheck {
   public boolean validate(Entity entity) {
      return Minecraft.getMinecraft().thePlayer.canEntityBeSeen(entity);
   }
}
