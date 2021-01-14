package net.minecraft.entity.ai;

import com.google.common.base.Predicate;
import net.minecraft.entity.passive.EntityTameable;

public class EntityAITargetNonTamed extends EntityAINearestAttackableTarget {
   private EntityTameable theTameable;

   public EntityAITargetNonTamed(EntityTameable entityIn, Class classTarget, boolean checkSight, Predicate targetSelector) {
      super(entityIn, classTarget, 10, checkSight, false, targetSelector);
      this.theTameable = entityIn;
   }

   public boolean shouldExecute() {
      return !this.theTameable.isTamed() && super.shouldExecute();
   }
}
