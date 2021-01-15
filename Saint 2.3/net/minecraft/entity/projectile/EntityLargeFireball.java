package net.minecraft.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityLargeFireball extends EntityFireball {
   public int field_92057_e = 1;
   private static final String __OBFID = "CL_00001719";

   public EntityLargeFireball(World worldIn) {
      super(worldIn);
   }

   public EntityLargeFireball(World worldIn, double p_i1768_2_, double p_i1768_4_, double p_i1768_6_, double p_i1768_8_, double p_i1768_10_, double p_i1768_12_) {
      super(worldIn, p_i1768_2_, p_i1768_4_, p_i1768_6_, p_i1768_8_, p_i1768_10_, p_i1768_12_);
   }

   public EntityLargeFireball(World worldIn, EntityLivingBase p_i1769_2_, double p_i1769_3_, double p_i1769_5_, double p_i1769_7_) {
      super(worldIn, p_i1769_2_, p_i1769_3_, p_i1769_5_, p_i1769_7_);
   }

   protected void onImpact(MovingObjectPosition p_70227_1_) {
      if (!this.worldObj.isRemote) {
         if (p_70227_1_.entityHit != null) {
            p_70227_1_.entityHit.attackEntityFrom(DamageSource.causeFireballDamage(this, this.shootingEntity), 6.0F);
            this.func_174815_a(this.shootingEntity, p_70227_1_.entityHit);
         }

         boolean var2 = this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing");
         this.worldObj.newExplosion((Entity)null, this.posX, this.posY, this.posZ, (float)this.field_92057_e, var2, var2);
         this.setDead();
      }

   }

   public void writeEntityToNBT(NBTTagCompound tagCompound) {
      super.writeEntityToNBT(tagCompound);
      tagCompound.setInteger("ExplosionPower", this.field_92057_e);
   }

   public void readEntityFromNBT(NBTTagCompound tagCompund) {
      super.readEntityFromNBT(tagCompund);
      if (tagCompund.hasKey("ExplosionPower", 99)) {
         this.field_92057_e = tagCompund.getInteger("ExplosionPower");
      }

   }
}
