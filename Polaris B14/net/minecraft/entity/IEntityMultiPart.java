package net.minecraft.entity;

import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public abstract interface IEntityMultiPart
{
  public abstract World getWorld();
  
  public abstract boolean attackEntityFromPart(EntityDragonPart paramEntityDragonPart, DamageSource paramDamageSource, float paramFloat);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\IEntityMultiPart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */