package net.minecraft.entity.monster;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.world.World;

public abstract class EntityGolem extends EntityCreature implements IAnimals {
   private static final String __OBFID = "CL_00001644";

   public EntityGolem(World worldIn) {
      super(worldIn);
   }

   public void fall(float distance, float damageMultiplier) {
   }

   protected String getLivingSound() {
      return "none";
   }

   protected String getHurtSound() {
      return "none";
   }

   protected String getDeathSound() {
      return "none";
   }

   public int getTalkInterval() {
      return 120;
   }

   protected boolean canDespawn() {
      return false;
   }
}
