package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;

public class EntityAISit extends EntityAIBase {
   private EntityTameable theEntity;
   private boolean isSitting;
   private static final String __OBFID = "CL_00001613";

   public EntityAISit(EntityTameable p_i1654_1_) {
      this.theEntity = p_i1654_1_;
      this.setMutexBits(5);
   }

   public boolean shouldExecute() {
      if (!this.theEntity.isTamed()) {
         return false;
      } else if (this.theEntity.isInWater()) {
         return false;
      } else if (!this.theEntity.onGround) {
         return false;
      } else {
         EntityLivingBase var1 = this.theEntity.func_180492_cm();
         return var1 == null ? true : (this.theEntity.getDistanceSqToEntity(var1) < 144.0D && var1.getAITarget() != null ? false : this.isSitting);
      }
   }

   public void startExecuting() {
      this.theEntity.getNavigator().clearPathEntity();
      this.theEntity.setSitting(true);
   }

   public void resetTask() {
      this.theEntity.setSitting(false);
   }

   public void setSitting(boolean p_75270_1_) {
      this.isSitting = p_75270_1_;
   }
}
