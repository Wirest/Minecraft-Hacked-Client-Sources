package net.minecraft.entity.boss;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityWither extends EntityMob implements IBossDisplayData, IRangedAttackMob {
   private float[] field_82220_d = new float[2];
   private float[] field_82221_e = new float[2];
   private float[] field_82217_f = new float[2];
   private float[] field_82218_g = new float[2];
   private int[] field_82223_h = new int[2];
   private int[] field_82224_i = new int[2];
   private int blockBreakCounter;
   private static final Predicate attackEntitySelector = new Predicate() {
      public boolean apply(Entity p_apply_1_) {
         return p_apply_1_ instanceof EntityLivingBase && ((EntityLivingBase)p_apply_1_).getCreatureAttribute() != EnumCreatureAttribute.UNDEAD;
      }
   };

   public EntityWither(World worldIn) {
      super(worldIn);
      this.setHealth(this.getMaxHealth());
      this.setSize(0.9F, 3.5F);
      this.isImmuneToFire = true;
      ((PathNavigateGround)this.getNavigator()).setCanSwim(true);
      this.tasks.addTask(0, new EntityAISwimming(this));
      this.tasks.addTask(2, new EntityAIArrowAttack(this, 1.0D, 40, 20.0F));
      this.tasks.addTask(5, new EntityAIWander(this, 1.0D));
      this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      this.tasks.addTask(7, new EntityAILookIdle(this));
      this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
      this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityLiving.class, 0, false, false, attackEntitySelector));
      this.experienceValue = 50;
   }

   protected void entityInit() {
      super.entityInit();
      this.dataWatcher.addObject(17, new Integer(0));
      this.dataWatcher.addObject(18, new Integer(0));
      this.dataWatcher.addObject(19, new Integer(0));
      this.dataWatcher.addObject(20, new Integer(0));
   }

   public void writeEntityToNBT(NBTTagCompound tagCompound) {
      super.writeEntityToNBT(tagCompound);
      tagCompound.setInteger("Invul", this.getInvulTime());
   }

   public void readEntityFromNBT(NBTTagCompound tagCompund) {
      super.readEntityFromNBT(tagCompund);
      this.setInvulTime(tagCompund.getInteger("Invul"));
   }

   protected String getLivingSound() {
      return "mob.wither.idle";
   }

   protected String getHurtSound() {
      return "mob.wither.hurt";
   }

   protected String getDeathSound() {
      return "mob.wither.death";
   }

   public void onLivingUpdate() {
      this.motionY *= 0.6000000238418579D;
      double d11;
      double d12;
      double d13;
      if (!this.worldObj.isRemote && this.getWatchedTargetId(0) > 0) {
         Entity entity = this.worldObj.getEntityByID(this.getWatchedTargetId(0));
         if (entity != null) {
            if (this.posY < entity.posY || !this.isArmored() && this.posY < entity.posY + 5.0D) {
               if (this.motionY < 0.0D) {
                  this.motionY = 0.0D;
               }

               this.motionY += (0.5D - this.motionY) * 0.6000000238418579D;
            }

            double d0 = entity.posX - this.posX;
            d11 = entity.posZ - this.posZ;
            d12 = d0 * d0 + d11 * d11;
            if (d12 > 9.0D) {
               d13 = (double)MathHelper.sqrt_double(d12);
               this.motionX += (d0 / d13 * 0.5D - this.motionX) * 0.6000000238418579D;
               this.motionZ += (d11 / d13 * 0.5D - this.motionZ) * 0.6000000238418579D;
            }
         }
      }

      if (this.motionX * this.motionX + this.motionZ * this.motionZ > 0.05000000074505806D) {
         this.rotationYaw = (float)MathHelper.func_181159_b(this.motionZ, this.motionX) * 57.295776F - 90.0F;
      }

      super.onLivingUpdate();

      int j;
      for(j = 0; j < 2; ++j) {
         this.field_82218_g[j] = this.field_82221_e[j];
         this.field_82217_f[j] = this.field_82220_d[j];
      }

      int i1;
      for(j = 0; j < 2; ++j) {
         i1 = this.getWatchedTargetId(j + 1);
         Entity entity1 = null;
         if (i1 > 0) {
            entity1 = this.worldObj.getEntityByID(i1);
         }

         if (entity1 != null) {
            d11 = this.func_82214_u(j + 1);
            d12 = this.func_82208_v(j + 1);
            d13 = this.func_82213_w(j + 1);
            double d6 = entity1.posX - d11;
            double d7 = entity1.posY + (double)entity1.getEyeHeight() - d12;
            double d8 = entity1.posZ - d13;
            double d9 = (double)MathHelper.sqrt_double(d6 * d6 + d8 * d8);
            float f = (float)(MathHelper.func_181159_b(d8, d6) * 180.0D / 3.141592653589793D) - 90.0F;
            float f1 = (float)(-(MathHelper.func_181159_b(d7, d9) * 180.0D / 3.141592653589793D));
            this.field_82220_d[j] = this.func_82204_b(this.field_82220_d[j], f1, 40.0F);
            this.field_82221_e[j] = this.func_82204_b(this.field_82221_e[j], f, 10.0F);
         } else {
            this.field_82221_e[j] = this.func_82204_b(this.field_82221_e[j], this.renderYawOffset, 10.0F);
         }
      }

      boolean flag = this.isArmored();

      for(i1 = 0; i1 < 3; ++i1) {
         double d10 = this.func_82214_u(i1);
         double d2 = this.func_82208_v(i1);
         double d4 = this.func_82213_w(i1);
         this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d10 + this.rand.nextGaussian() * 0.30000001192092896D, d2 + this.rand.nextGaussian() * 0.30000001192092896D, d4 + this.rand.nextGaussian() * 0.30000001192092896D, 0.0D, 0.0D, 0.0D);
         if (flag && this.worldObj.rand.nextInt(4) == 0) {
            this.worldObj.spawnParticle(EnumParticleTypes.SPELL_MOB, d10 + this.rand.nextGaussian() * 0.30000001192092896D, d2 + this.rand.nextGaussian() * 0.30000001192092896D, d4 + this.rand.nextGaussian() * 0.30000001192092896D, 0.699999988079071D, 0.699999988079071D, 0.5D);
         }
      }

      if (this.getInvulTime() > 0) {
         for(i1 = 0; i1 < 3; ++i1) {
            this.worldObj.spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX + this.rand.nextGaussian() * 1.0D, this.posY + (double)(this.rand.nextFloat() * 3.3F), this.posZ + this.rand.nextGaussian() * 1.0D, 0.699999988079071D, 0.699999988079071D, 0.8999999761581421D);
         }
      }

   }

   protected void updateAITasks() {
      int i;
      if (this.getInvulTime() > 0) {
         i = this.getInvulTime() - 1;
         if (i <= 0) {
            this.worldObj.newExplosion(this, this.posX, this.posY + (double)this.getEyeHeight(), this.posZ, 7.0F, false, this.worldObj.getGameRules().getBoolean("mobGriefing"));
            this.worldObj.playBroadcastSound(1013, new BlockPos(this), 0);
         }

         this.setInvulTime(i);
         if (this.ticksExisted % 10 == 0) {
            this.heal(10.0F);
         }
      } else {
         super.updateAITasks();

         int l1;
         int i2;
         for(i = 1; i < 3; ++i) {
            if (this.ticksExisted >= this.field_82223_h[i - 1]) {
               this.field_82223_h[i - 1] = this.ticksExisted + 10 + this.rand.nextInt(10);
               if (this.worldObj.getDifficulty() == EnumDifficulty.NORMAL || this.worldObj.getDifficulty() == EnumDifficulty.HARD) {
                  l1 = i - 1;
                  i2 = this.field_82224_i[i - 1];
                  this.field_82224_i[l1] = this.field_82224_i[i - 1] + 1;
                  if (i2 > 15) {
                     float f = 10.0F;
                     float f1 = 5.0F;
                     double d0 = MathHelper.getRandomDoubleInRange(this.rand, this.posX - (double)f, this.posX + (double)f);
                     double d1 = MathHelper.getRandomDoubleInRange(this.rand, this.posY - (double)f1, this.posY + (double)f1);
                     double d2 = MathHelper.getRandomDoubleInRange(this.rand, this.posZ - (double)f, this.posZ + (double)f);
                     this.launchWitherSkullToCoords(i + 1, d0, d1, d2, true);
                     this.field_82224_i[i - 1] = 0;
                  }
               }

               l1 = this.getWatchedTargetId(i);
               if (l1 > 0) {
                  Entity entity = this.worldObj.getEntityByID(l1);
                  if (entity != null && entity.isEntityAlive() && this.getDistanceSqToEntity(entity) <= 900.0D && this.canEntityBeSeen(entity)) {
                     if (entity instanceof EntityPlayer && ((EntityPlayer)entity).capabilities.disableDamage) {
                        this.updateWatchedTargetId(i, 0);
                     } else {
                        this.launchWitherSkullToEntity(i + 1, (EntityLivingBase)entity);
                        this.field_82223_h[i - 1] = this.ticksExisted + 40 + this.rand.nextInt(20);
                        this.field_82224_i[i - 1] = 0;
                     }
                  } else {
                     this.updateWatchedTargetId(i, 0);
                  }
               } else {
                  List list = this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().expand(20.0D, 8.0D, 20.0D), Predicates.and(attackEntitySelector, EntitySelectors.NOT_SPECTATING));

                  for(int j2 = 0; j2 < 10 && !list.isEmpty(); ++j2) {
                     EntityLivingBase entitylivingbase = (EntityLivingBase)list.get(this.rand.nextInt(list.size()));
                     if (entitylivingbase != this && entitylivingbase.isEntityAlive() && this.canEntityBeSeen(entitylivingbase)) {
                        if (entitylivingbase instanceof EntityPlayer) {
                           if (!((EntityPlayer)entitylivingbase).capabilities.disableDamage) {
                              this.updateWatchedTargetId(i, entitylivingbase.getEntityId());
                           }
                        } else {
                           this.updateWatchedTargetId(i, entitylivingbase.getEntityId());
                        }
                        break;
                     }

                     list.remove(entitylivingbase);
                  }
               }
            }
         }

         if (this.getAttackTarget() != null) {
            this.updateWatchedTargetId(0, this.getAttackTarget().getEntityId());
         } else {
            this.updateWatchedTargetId(0, 0);
         }

         if (this.blockBreakCounter > 0) {
            --this.blockBreakCounter;
            if (this.blockBreakCounter == 0 && this.worldObj.getGameRules().getBoolean("mobGriefing")) {
               i = MathHelper.floor_double(this.posY);
               l1 = MathHelper.floor_double(this.posX);
               i2 = MathHelper.floor_double(this.posZ);
               boolean flag = false;
               int k2 = -1;

               while(true) {
                  if (k2 > 1) {
                     if (flag) {
                        this.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1012, new BlockPos(this), 0);
                     }
                     break;
                  }

                  for(int l2 = -1; l2 <= 1; ++l2) {
                     for(int j = 0; j <= 3; ++j) {
                        int i3 = l1 + k2;
                        int k = i + j;
                        int l = i2 + l2;
                        BlockPos blockpos = new BlockPos(i3, k, l);
                        Block block = this.worldObj.getBlockState(blockpos).getBlock();
                        if (block.getMaterial() != Material.air && func_181033_a(block)) {
                           flag = this.worldObj.destroyBlock(blockpos, true) || flag;
                        }
                     }
                  }

                  ++k2;
               }
            }
         }

         if (this.ticksExisted % 20 == 0) {
            this.heal(1.0F);
         }
      }

   }

   public static boolean func_181033_a(Block p_181033_0_) {
      return p_181033_0_ != Blocks.bedrock && p_181033_0_ != Blocks.end_portal && p_181033_0_ != Blocks.end_portal_frame && p_181033_0_ != Blocks.command_block && p_181033_0_ != Blocks.barrier;
   }

   public void func_82206_m() {
      this.setInvulTime(220);
      this.setHealth(this.getMaxHealth() / 3.0F);
   }

   public void setInWeb() {
   }

   public int getTotalArmorValue() {
      return 4;
   }

   private double func_82214_u(int p_82214_1_) {
      if (p_82214_1_ <= 0) {
         return this.posX;
      } else {
         float f = (this.renderYawOffset + (float)(180 * (p_82214_1_ - 1))) / 180.0F * 3.1415927F;
         float f1 = MathHelper.cos(f);
         return this.posX + (double)f1 * 1.3D;
      }
   }

   private double func_82208_v(int p_82208_1_) {
      return p_82208_1_ <= 0 ? this.posY + 3.0D : this.posY + 2.2D;
   }

   private double func_82213_w(int p_82213_1_) {
      if (p_82213_1_ <= 0) {
         return this.posZ;
      } else {
         float f = (this.renderYawOffset + (float)(180 * (p_82213_1_ - 1))) / 180.0F * 3.1415927F;
         float f1 = MathHelper.sin(f);
         return this.posZ + (double)f1 * 1.3D;
      }
   }

   private float func_82204_b(float p_82204_1_, float p_82204_2_, float p_82204_3_) {
      float f = MathHelper.wrapAngleTo180_float(p_82204_2_ - p_82204_1_);
      if (f > p_82204_3_) {
         f = p_82204_3_;
      }

      if (f < -p_82204_3_) {
         f = -p_82204_3_;
      }

      return p_82204_1_ + f;
   }

   private void launchWitherSkullToEntity(int p_82216_1_, EntityLivingBase p_82216_2_) {
      this.launchWitherSkullToCoords(p_82216_1_, p_82216_2_.posX, p_82216_2_.posY + (double)p_82216_2_.getEyeHeight() * 0.5D, p_82216_2_.posZ, p_82216_1_ == 0 && this.rand.nextFloat() < 0.001F);
   }

   private void launchWitherSkullToCoords(int p_82209_1_, double x, double y, double z, boolean invulnerable) {
      this.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1014, new BlockPos(this), 0);
      double d0 = this.func_82214_u(p_82209_1_);
      double d1 = this.func_82208_v(p_82209_1_);
      double d2 = this.func_82213_w(p_82209_1_);
      double d3 = x - d0;
      double d4 = y - d1;
      double d5 = z - d2;
      EntityWitherSkull entitywitherskull = new EntityWitherSkull(this.worldObj, this, d3, d4, d5);
      if (invulnerable) {
         entitywitherskull.setInvulnerable(true);
      }

      entitywitherskull.posY = d1;
      entitywitherskull.posX = d0;
      entitywitherskull.posZ = d2;
      this.worldObj.spawnEntityInWorld(entitywitherskull);
   }

   public void attackEntityWithRangedAttack(EntityLivingBase p_82196_1_, float p_82196_2_) {
      this.launchWitherSkullToEntity(0, p_82196_1_);
   }

   public boolean attackEntityFrom(DamageSource source, float amount) {
      if (this.isEntityInvulnerable(source)) {
         return false;
      } else if (source != DamageSource.drown && !(source.getEntity() instanceof EntityWither)) {
         if (this.getInvulTime() > 0 && source != DamageSource.outOfWorld) {
            return false;
         } else {
            Entity entity1;
            if (this.isArmored()) {
               entity1 = source.getSourceOfDamage();
               if (entity1 instanceof EntityArrow) {
                  return false;
               }
            }

            entity1 = source.getEntity();
            if (entity1 != null && !(entity1 instanceof EntityPlayer) && entity1 instanceof EntityLivingBase && ((EntityLivingBase)entity1).getCreatureAttribute() == this.getCreatureAttribute()) {
               return false;
            } else {
               if (this.blockBreakCounter <= 0) {
                  this.blockBreakCounter = 20;
               }

               for(int i = 0; i < this.field_82224_i.length; ++i) {
                  int[] var10000 = this.field_82224_i;
                  var10000[i] += 3;
               }

               return super.attackEntityFrom(source, amount);
            }
         }
      } else {
         return false;
      }
   }

   protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
      EntityItem entityitem = this.dropItem(Items.nether_star, 1);
      if (entityitem != null) {
         entityitem.setNoDespawn();
      }

      if (!this.worldObj.isRemote) {
         Iterator var4 = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, this.getEntityBoundingBox().expand(50.0D, 100.0D, 50.0D)).iterator();

         while(var4.hasNext()) {
            EntityPlayer entityplayer = (EntityPlayer)var4.next();
            entityplayer.triggerAchievement(AchievementList.killWither);
         }
      }

   }

   protected void despawnEntity() {
      this.entityAge = 0;
   }

   public int getBrightnessForRender(float partialTicks) {
      return 15728880;
   }

   public void fall(float distance, float damageMultiplier) {
   }

   public void addPotionEffect(PotionEffect potioneffectIn) {
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(300.0D);
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.6000000238418579D);
      this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(40.0D);
   }

   public float func_82207_a(int p_82207_1_) {
      return this.field_82221_e[p_82207_1_];
   }

   public float func_82210_r(int p_82210_1_) {
      return this.field_82220_d[p_82210_1_];
   }

   public int getInvulTime() {
      return this.dataWatcher.getWatchableObjectInt(20);
   }

   public void setInvulTime(int p_82215_1_) {
      this.dataWatcher.updateObject(20, p_82215_1_);
   }

   public int getWatchedTargetId(int p_82203_1_) {
      return this.dataWatcher.getWatchableObjectInt(17 + p_82203_1_);
   }

   public void updateWatchedTargetId(int targetOffset, int newId) {
      this.dataWatcher.updateObject(17 + targetOffset, newId);
   }

   public boolean isArmored() {
      return this.getHealth() <= this.getMaxHealth() / 2.0F;
   }

   public EnumCreatureAttribute getCreatureAttribute() {
      return EnumCreatureAttribute.UNDEAD;
   }

   public void mountEntity(Entity entityIn) {
      this.ridingEntity = null;
   }
}
