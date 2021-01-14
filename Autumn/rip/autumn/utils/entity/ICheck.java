package rip.autumn.utils.entity;

import net.minecraft.entity.Entity;

@FunctionalInterface
public interface ICheck {
   boolean validate(Entity var1);
}
