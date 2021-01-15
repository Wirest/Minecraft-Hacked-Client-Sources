package net.minecraft.entity.monster;

import java.util.Random;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIFindEntityNearestPlayer;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityGhast extends EntityFlying implements IMob {
   private int explosionStrength = 1;
   private static final String __OBFID = "CL_00001689";

   public EntityGhast(World worldIn) {
      super(worldIn);
      this.setSize(4.0F, 4.0F);
      this.isImmuneToFire = true;
      this.experienceValue = 5;
      this.moveHelper = new EntityGhast.GhastMoveHelper();
      this.tasks.addTask(5, new EntityGhast.AIRandomFly());
      this.tasks.addTask(7, new EntityGhast.AILookAround());
      this.tasks.addTask(7, new EntityGhast.AIFireballAttack());
      this.targetTasks.addTask(1, new EntityAIFindEntityNearestPlayer(this));
   }

   public boolean func_110182_bF() {
      return this.dataWatcher.getWatchableObjectByte(16) != 0;
   }

   public void func_175454_a(boolean p_175454_1_) {
      this.dataWatcher.updateObject(16, (byte)(p_175454_1_ ? 1 : 0));
   }

   public int func_175453_cd() {
      return this.explosionStrength;
   }

   public void onUpdate() {
      super.onUpdate();
      if (!this.worldObj.isRemote && this.worldObj.getDifficulty() == EnumDifficulty.PEACEFUL) {
         this.setDead();
      }

   }

   public boolean attackEntityFrom(DamageSource source, float amount) {
      if (this.func_180431_b(source)) {
         return false;
      } else if ("fireball".equals(source.getDamageType()) && source.getEntity() instanceof EntityPlayer) {
         super.attackEntityFrom(source, 1000.0F);
         ((EntityPlayer)source.getEntity()).triggerAchievement(AchievementList.ghast);
         return true;
      } else {
         return super.attackEntityFrom(source, amount);
      }
   }

   protected void entityInit() {
      super.entityInit();
      this.dataWatcher.addObject(16, (byte)0);
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
      this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(100.0D);
   }

   protected String getLivingSound() {
      return "mob.ghast.moan";
   }

   protected String getHurtSound() {
      return "mob.ghast.scream";
   }

   protected String getDeathSound() {
      return "mob.ghast.death";
   }

   protected Item getDropItem() {
      return Items.gunpowder;
   }

   protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
      int var3 = this.rand.nextInt(2) + this.rand.nextInt(1 + p_70628_2_);

      int var4;
      for(var4 = 0; var4 < var3; ++var4) {
         this.dropItem(Items.ghast_tear, 1);
      }

      var3 = this.rand.nextInt(3) + this.rand.nextInt(1 + p_70628_2_);

      for(var4 = 0; var4 < var3; ++var4) {
         this.dropItem(Items.gunpowder, 1);
      }

   }

   protected float getSoundVolume() {
      return 10.0F;
   }

   public boolean getCanSpawnHere() {
      return this.rand.nextInt(20) == 0 && super.getCanSpawnHere() && this.worldObj.getDifficulty() != EnumDifficulty.PEACEFUL;
   }

   public int getMaxSpawnedInChunk() {
      return 1;
   }

   public void writeEntityToNBT(NBTTagCompound tagCompound) {
      super.writeEntityToNBT(tagCompound);
      tagCompound.setInteger("ExplosionPower", this.explosionStrength);
   }

   public void readEntityFromNBT(NBTTagCompound tagCompund) {
      super.readEntityFromNBT(tagCompund);
      if (tagCompund.hasKey("ExplosionPower", 99)) {
         this.explosionStrength = tagCompund.getInteger("ExplosionPower");
      }

   }

   public float getEyeHeight() {
      return 2.6F;
   }

   class AIFireballAttack extends EntityAIBase {
      private EntityGhast field_179470_b = EntityGhast.this;
      public int field_179471_a;
      private static final String __OBFID = "CL_00002215";

      public boolean shouldExecute() {
         return this.field_179470_b.getAttackTarget() != null;
      }

      public void startExecuting() {
         this.field_179471_a = 0;
      }

      public void resetTask() {
         this.field_179470_b.func_175454_a(false);
      }

      public void updateTask() {
         EntityLivingBase var1 = this.field_179470_b.getAttackTarget();
         double var2 = 64.0D;
         if (var1.getDistanceSqToEntity(this.field_179470_b) < var2 * var2 && this.field_179470_b.canEntityBeSeen(var1)) {
            World var4 = this.field_179470_b.worldObj;
            ++this.field_179471_a;
            if (this.field_179471_a == 10) {
               var4.playAuxSFXAtEntity((EntityPlayer)null, 1007, new BlockPos(this.field_179470_b), 0);
            }

            if (this.field_179471_a == 20) {
               double var5 = 4.0D;
               Vec3 var7 = this.field_179470_b.getLook(1.0F);
               double var8 = var1.posX - (this.field_179470_b.posX + var7.xCoord * var5);
               double var10 = var1.getEntityBoundingBox().minY + (double)(var1.height / 2.0F) - (0.5D + this.field_179470_b.posY + (double)(this.field_179470_b.height / 2.0F));
               double var12 = var1.posZ - (this.field_179470_b.posZ + var7.zCoord * var5);
               var4.playAuxSFXAtEntity((EntityPlayer)null, 1008, new BlockPos(this.field_179470_b), 0);
               EntityLargeFireball var14 = new EntityLargeFireball(var4, this.field_179470_b, var8, var10, var12);
               var14.field_92057_e = this.field_179470_b.func_175453_cd();
               var14.posX = this.field_179470_b.posX + var7.xCoord * var5;
               var14.posY = this.field_179470_b.posY + (double)(this.field_179470_b.height / 2.0F) + 0.5D;
               var14.posZ = this.field_179470_b.posZ + var7.zCoord * var5;
               var4.spawnEntityInWorld(var14);
               this.field_179471_a = -40;
            }
         } else if (this.field_179471_a > 0) {
            --this.field_179471_a;
         }

         this.field_179470_b.func_175454_a(this.field_179471_a > 10);
      }
   }

   class AILookAround extends EntityAIBase {
      private EntityGhast field_179472_a = EntityGhast.this;
      private static final String __OBFID = "CL_00002217";

      public AILookAround() {
         this.setMutexBits(2);
      }

      public boolean shouldExecute() {
         return true;
      }

      public void updateTask() {
         if (this.field_179472_a.getAttackTarget() == null) {
            this.field_179472_a.renderYawOffset = this.field_179472_a.rotationYaw = -((float)Math.atan2(this.field_179472_a.motionX, this.field_179472_a.motionZ)) * 180.0F / 3.1415927F;
         } else {
            EntityLivingBase var1 = this.field_179472_a.getAttackTarget();
            double var2 = 64.0D;
            if (var1.getDistanceSqToEntity(this.field_179472_a) < var2 * var2) {
               double var4 = var1.posX - this.field_179472_a.posX;
               double var6 = var1.posZ - this.field_179472_a.posZ;
               this.field_179472_a.renderYawOffset = this.field_179472_a.rotationYaw = -((float)Math.atan2(var4, var6)) * 180.0F / 3.1415927F;
            }
         }

      }
   }

   class AIRandomFly extends EntityAIBase {
      private EntityGhast field_179454_a = EntityGhast.this;
      private static final String __OBFID = "CL_00002214";

      public AIRandomFly() {
         this.setMutexBits(1);
      }

      public boolean shouldExecute() {
         EntityMoveHelper var1 = this.field_179454_a.getMoveHelper();
         if (!var1.isUpdating()) {
            return true;
         } else {
            double var2 = var1.func_179917_d() - this.field_179454_a.posX;
            double var4 = var1.func_179919_e() - this.field_179454_a.posY;
            double var6 = var1.func_179918_f() - this.field_179454_a.posZ;
            double var8 = var2 * var2 + var4 * var4 + var6 * var6;
            return var8 < 1.0D || var8 > 3600.0D;
         }
      }

      public boolean continueExecuting() {
         return false;
      }

      public void startExecuting() {
         Random var1 = this.field_179454_a.getRNG();
         double var2 = this.field_179454_a.posX + (double)((var1.nextFloat() * 2.0F - 1.0F) * 16.0F);
         double var4 = this.field_179454_a.posY + (double)((var1.nextFloat() * 2.0F - 1.0F) * 16.0F);
         double var6 = this.field_179454_a.posZ + (double)((var1.nextFloat() * 2.0F - 1.0F) * 16.0F);
         this.field_179454_a.getMoveHelper().setMoveTo(var2, var4, var6, 1.0D);
      }
   }

   class GhastMoveHelper extends EntityMoveHelper {
      private EntityGhast field_179927_g = EntityGhast.this;
      private int field_179928_h;
      private static final String __OBFID = "CL_00002216";

      public GhastMoveHelper() {
         super(EntityGhast.this);
      }

      public void onUpdateMoveHelper() {
         if (this.update) {
            double var1 = this.posX - this.field_179927_g.posX;
            double var3 = this.posY - this.field_179927_g.posY;
            double var5 = this.posZ - this.field_179927_g.posZ;
            double var7 = var1 * var1 + var3 * var3 + var5 * var5;
            if (this.field_179928_h-- <= 0) {
               this.field_179928_h += this.field_179927_g.getRNG().nextInt(5) + 2;
               var7 = (double)MathHelper.sqrt_double(var7);
               if (this.func_179926_b(this.posX, this.posY, this.posZ, var7)) {
                  EntityGhast var10000 = this.field_179927_g;
                  var10000.motionX += var1 / var7 * 0.1D;
                  var10000 = this.field_179927_g;
                  var10000.motionY += var3 / var7 * 0.1D;
                  var10000 = this.field_179927_g;
                  var10000.motionZ += var5 / var7 * 0.1D;
               } else {
                  this.update = false;
               }
            }
         }

      }

      private boolean func_179926_b(double p_179926_1_, double p_179926_3_, double p_179926_5_, double p_179926_7_) {
         double var9 = (p_179926_1_ - this.field_179927_g.posX) / p_179926_7_;
         double var11 = (p_179926_3_ - this.field_179927_g.posY) / p_179926_7_;
         double var13 = (p_179926_5_ - this.field_179927_g.posZ) / p_179926_7_;
         AxisAlignedBB var15 = this.field_179927_g.getEntityBoundingBox();

         for(int var16 = 1; (double)var16 < p_179926_7_; ++var16) {
            var15 = var15.offset(var9, var11, var13);
            if (!this.field_179927_g.worldObj.getCollidingBoundingBoxes(this.field_179927_g, var15).isEmpty()) {
               return false;
            }
         }

         return true;
      }
   }
}
