package VenusClient.online.Utils.TargetFinder;

import net.minecraft.entity.Entity;

@FunctionalInterface
public interface ICheck {
  boolean validate(Entity paramEntity);
}
