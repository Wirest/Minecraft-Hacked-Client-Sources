package VenusClient.online.Utils.TargetFinder.impl;

import VenusClient.online.Utils.TargetFinder.ICheck;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;

public final class VoidCheck implements ICheck {
  public boolean validate(Entity entity) {
    return isBlockUnder(entity);
  }
  
  private boolean isBlockUnder(Entity entity) {
    for (int offset = 0; offset < entity.posY + entity.getEyeHeight(); offset += 2) {
      AxisAlignedBB boundingBox = entity.getEntityBoundingBox().offset(0.0D, -offset, 0.0D);
      if (!(Minecraft.getMinecraft()).theWorld.getCollidingBoundingBoxes(entity, boundingBox).isEmpty())
        return true; 
    } 
    return false;
  }
}
