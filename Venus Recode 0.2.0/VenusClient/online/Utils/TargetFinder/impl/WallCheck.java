package VenusClient.online.Utils.TargetFinder.impl;

import VenusClient.online.Utils.TargetFinder.ICheck;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

public final class WallCheck implements ICheck {
  public boolean validate(Entity entity) {
    return (Minecraft.getMinecraft()).thePlayer.canEntityBeSeen(entity);
  }
}
