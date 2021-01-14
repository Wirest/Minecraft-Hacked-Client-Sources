package net.minecraft.entity.item;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityBoat extends Entity {
   private boolean isBoatEmpty;
   private double speedMultiplier;
   private int boatPosRotationIncrements;
   private double boatX;
   private double boatY;
   private double boatZ;
   private double boatYaw;
   private double boatPitch;
   private double velocityX;
   private double velocityY;
   private double velocityZ;

   public EntityBoat(World worldIn) {
      super(worldIn);
      this.isBoatEmpty = true;
      this.speedMultiplier = 0.07D;
      this.preventEntitySpawning = true;
      this.setSize(1.5F, 0.6F);
   }

   protected boolean canTriggerWalking() {
      return false;
   }

   protected void entityInit() {
      this.dataWatcher.addObject(17, new Integer(0));
      this.dataWatcher.addObject(18, new Integer(1));
      this.dataWatcher.addObject(19, new Float(0.0F));
   }

   public AxisAlignedBB getCollisionBox(Entity entityIn) {
      return entityIn.getEntityBoundingBox();
   }

   public AxisAlignedBB getCollisionBoundingBox() {
      return this.getEntityBoundingBox();
   }

   public boolean canBePushed() {
      return true;
   }

   public EntityBoat(World worldIn, double p_i1705_2_, double p_i1705_4_, double p_i1705_6_) {
      this(worldIn);
      this.setPosition(p_i1705_2_, p_i1705_4_, p_i1705_6_);
      this.motionX = 0.0D;
      this.motionY = 0.0D;
      this.motionZ = 0.0D;
      this.prevPosX = p_i1705_2_;
      this.prevPosY = p_i1705_4_;
      this.prevPosZ = p_i1705_6_;
   }

   public double getMountedYOffset() {
      return -0.3D;
   }

   public boolean attackEntityFrom(DamageSource source, float amount) {
      if (this.isEntityInvulnerable(source)) {
         return false;
      } else if (!this.worldObj.isRemote && !this.isDead) {
         if (this.riddenByEntity != null && this.riddenByEntity == source.getEntity() && source instanceof EntityDamageSourceIndirect) {
            return false;
         } else {
            this.setForwardDirection(-this.getForwardDirection());
            this.setTimeSinceHit(10);
            this.setDamageTaken(this.getDamageTaken() + amount * 10.0F);
            this.setBeenAttacked();
            boolean flag = source.getEntity() instanceof EntityPlayer && ((EntityPlayer)source.getEntity()).capabilities.isCreativeMode;
            if (flag || this.getDamageTaken() > 40.0F) {
               if (this.riddenByEntity != null) {
                  this.riddenByEntity.mountEntity(this);
               }

               if (!flag && this.worldObj.getGameRules().getBoolean("doEntityDrops")) {
                  this.dropItemWithOffset(Items.boat, 1, 0.0F);
               }

               this.setDead();
            }

            return true;
         }
      } else {
         return true;
      }
   }

   public void performHurtAnimation() {
      this.setForwardDirection(-this.getForwardDirection());
      this.setTimeSinceHit(10);
      this.setDamageTaken(this.getDamageTaken() * 11.0F);
   }

   public boolean canBeCollidedWith() {
      return !this.isDead;
   }

   public void setPositionAndRotation2(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean p_180426_10_) {
      if (p_180426_10_ && this.riddenByEntity != null) {
         this.prevPosX = this.posX = x;
         this.prevPosY = this.posY = y;
         this.prevPosZ = this.posZ = z;
         this.rotationYaw = yaw;
         this.rotationPitch = pitch;
         this.boatPosRotationIncrements = 0;
         this.setPosition(x, y, z);
         this.motionX = this.velocityX = 0.0D;
         this.motionY = this.velocityY = 0.0D;
         this.motionZ = this.velocityZ = 0.0D;
      } else {
         if (this.isBoatEmpty) {
            this.boatPosRotationIncrements = posRotationIncrements + 5;
         } else {
            double d0 = x - this.posX;
            double d1 = y - this.posY;
            double d2 = z - this.posZ;
            double d3 = d0 * d0 + d1 * d1 + d2 * d2;
            if (d3 <= 1.0D) {
               return;
            }

            this.boatPosRotationIncrements = 3;
         }

         this.boatX = x;
         this.boatY = y;
         this.boatZ = z;
         this.boatYaw = (double)yaw;
         this.boatPitch = (double)pitch;
         this.motionX = this.velocityX;
         this.motionY = this.velocityY;
         this.motionZ = this.velocityZ;
      }

   }

   public void setVelocity(double x, double y, double z) {
      this.velocityX = this.motionX = x;
      this.velocityY = this.motionY = y;
      this.velocityZ = this.motionZ = z;
   }

   public void onUpdate() {
      super.onUpdate();
      if (this.getTimeSinceHit() > 0) {
         this.setTimeSinceHit(this.getTimeSinceHit() - 1);
      }

      if (this.getDamageTaken() > 0.0F) {
         this.setDamageTaken(this.getDamageTaken() - 1.0F);
      }

      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      int i = 5;
      double d0 = 0.0D;

      for(int j = 0; j < i; ++j) {
         double d1 = this.getEntityBoundingBox().minY + (this.getEntityBoundingBox().maxY - this.getEntityBoundingBox().minY) * (double)(j + 0) / (double)i - 0.125D;
         double d3 = this.getEntityBoundingBox().minY + (this.getEntityBoundingBox().maxY - this.getEntityBoundingBox().minY) * (double)(j + 1) / (double)i - 0.125D;
         AxisAlignedBB axisalignedbb = new AxisAlignedBB(this.getEntityBoundingBox().minX, d1, this.getEntityBoundingBox().minZ, this.getEntityBoundingBox().maxX, d3, this.getEntityBoundingBox().maxZ);
         if (this.worldObj.isAABBInMaterial(axisalignedbb, Material.water)) {
            d0 += 1.0D / (double)i;
         }
      }

      double d9 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
      double d11;
      double d15;
      int i2;
      if (d9 > 0.2975D) {
         d11 = Math.cos((double)this.rotationYaw * 3.141592653589793D / 180.0D);
         d15 = Math.sin((double)this.rotationYaw * 3.141592653589793D / 180.0D);

         for(i2 = 0; (double)i2 < 1.0D + d9 * 60.0D; ++i2) {
            double d5 = (double)(this.rand.nextFloat() * 2.0F - 1.0F);
            double d6 = (double)(this.rand.nextInt(2) * 2 - 1) * 0.7D;
            double d7;
            double d8;
            if (this.rand.nextBoolean()) {
               d7 = this.posX - d11 * d5 * 0.8D + d15 * d6;
               d8 = this.posZ - d15 * d5 * 0.8D - d11 * d6;
               this.worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, d7, this.posY - 0.125D, d8, this.motionX, this.motionY, this.motionZ);
            } else {
               d7 = this.posX + d11 + d15 * d5 * 0.7D;
               d8 = this.posZ + d15 - d11 * d5 * 0.7D;
               this.worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, d7, this.posY - 0.125D, d8, this.motionX, this.motionY, this.motionZ);
            }
         }
      }

      double d18;
      double d21;
      if (this.worldObj.isRemote && this.isBoatEmpty) {
         if (this.boatPosRotationIncrements > 0) {
            d11 = this.posX + (this.boatX - this.posX) / (double)this.boatPosRotationIncrements;
            d15 = this.posY + (this.boatY - this.posY) / (double)this.boatPosRotationIncrements;
            d18 = this.posZ + (this.boatZ - this.posZ) / (double)this.boatPosRotationIncrements;
            d21 = MathHelper.wrapAngleTo180_double(this.boatYaw - (double)this.rotationYaw);
            this.rotationYaw = (float)((double)this.rotationYaw + d21 / (double)this.boatPosRotationIncrements);
            this.rotationPitch = (float)((double)this.rotationPitch + (this.boatPitch - (double)this.rotationPitch) / (double)this.boatPosRotationIncrements);
            --this.boatPosRotationIncrements;
            this.setPosition(d11, d15, d18);
            this.setRotation(this.rotationYaw, this.rotationPitch);
         } else {
            d11 = this.posX + this.motionX;
            d15 = this.posY + this.motionY;
            d18 = this.posZ + this.motionZ;
            this.setPosition(d11, d15, d18);
            if (this.onGround) {
               this.motionX *= 0.5D;
               this.motionY *= 0.5D;
               this.motionZ *= 0.5D;
            }

            this.motionX *= 0.9900000095367432D;
            this.motionY *= 0.949999988079071D;
            this.motionZ *= 0.9900000095367432D;
         }
      } else {
         if (d0 < 1.0D) {
            d11 = d0 * 2.0D - 1.0D;
            this.motionY += 0.03999999910593033D * d11;
         } else {
            if (this.motionY < 0.0D) {
               this.motionY /= 2.0D;
            }

            this.motionY += 0.007000000216066837D;
         }

         if (this.riddenByEntity instanceof EntityLivingBase) {
            EntityLivingBase entitylivingbase = (EntityLivingBase)this.riddenByEntity;
            float f = this.riddenByEntity.rotationYaw + -entitylivingbase.moveStrafing * 90.0F;
            this.motionX += -Math.sin((double)(f * 3.1415927F / 180.0F)) * this.speedMultiplier * (double)entitylivingbase.moveForward * 0.05000000074505806D;
            this.motionZ += Math.cos((double)(f * 3.1415927F / 180.0F)) * this.speedMultiplier * (double)entitylivingbase.moveForward * 0.05000000074505806D;
         }

         d11 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
         if (d11 > 0.35D) {
            d15 = 0.35D / d11;
            this.motionX *= d15;
            this.motionZ *= d15;
            d11 = 0.35D;
         }

         if (d11 > d9 && this.speedMultiplier < 0.35D) {
            this.speedMultiplier += (0.35D - this.speedMultiplier) / 35.0D;
            if (this.speedMultiplier > 0.35D) {
               this.speedMultiplier = 0.35D;
            }
         } else {
            this.speedMultiplier -= (this.speedMultiplier - 0.07D) / 35.0D;
            if (this.speedMultiplier < 0.07D) {
               this.speedMultiplier = 0.07D;
            }
         }

         int k1;
         for(k1 = 0; k1 < 4; ++k1) {
            int l1 = MathHelper.floor_double(this.posX + ((double)(k1 % 2) - 0.5D) * 0.8D);
            i2 = MathHelper.floor_double(this.posZ + ((double)(k1 / 2) - 0.5D) * 0.8D);

            for(int j2 = 0; j2 < 2; ++j2) {
               int l = MathHelper.floor_double(this.posY) + j2;
               BlockPos blockpos = new BlockPos(l1, l, i2);
               Block block = this.worldObj.getBlockState(blockpos).getBlock();
               if (block == Blocks.snow_layer) {
                  this.worldObj.setBlockToAir(blockpos);
                  this.isCollidedHorizontally = false;
               } else if (block == Blocks.waterlily) {
                  this.worldObj.destroyBlock(blockpos, true);
                  this.isCollidedHorizontally = false;
               }
            }
         }

         if (this.onGround) {
            this.motionX *= 0.5D;
            this.motionY *= 0.5D;
            this.motionZ *= 0.5D;
         }

         this.moveEntity(this.motionX, this.motionY, this.motionZ);
         if (this.isCollidedHorizontally && d9 > 0.2975D) {
            if (!this.worldObj.isRemote && !this.isDead) {
               this.setDead();
               if (this.worldObj.getGameRules().getBoolean("doEntityDrops")) {
                  for(k1 = 0; k1 < 3; ++k1) {
                     this.dropItemWithOffset(Item.getItemFromBlock(Blocks.planks), 1, 0.0F);
                  }

                  for(k1 = 0; k1 < 2; ++k1) {
                     this.dropItemWithOffset(Items.stick, 1, 0.0F);
                  }
               }
            }
         } else {
            this.motionX *= 0.9900000095367432D;
            this.motionY *= 0.949999988079071D;
            this.motionZ *= 0.9900000095367432D;
         }

         this.rotationPitch = 0.0F;
         d15 = (double)this.rotationYaw;
         d18 = this.prevPosX - this.posX;
         d21 = this.prevPosZ - this.posZ;
         if (d18 * d18 + d21 * d21 > 0.001D) {
            d15 = (double)((float)(MathHelper.func_181159_b(d21, d18) * 180.0D / 3.141592653589793D));
         }

         double d23 = MathHelper.wrapAngleTo180_double(d15 - (double)this.rotationYaw);
         if (d23 > 20.0D) {
            d23 = 20.0D;
         }

         if (d23 < -20.0D) {
            d23 = -20.0D;
         }

         this.rotationYaw = (float)((double)this.rotationYaw + d23);
         this.setRotation(this.rotationYaw, this.rotationPitch);
         if (!this.worldObj.isRemote) {
            List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(0.20000000298023224D, 0.0D, 0.20000000298023224D));
            if (list != null && !list.isEmpty()) {
               for(int k2 = 0; k2 < list.size(); ++k2) {
                  Entity entity = (Entity)list.get(k2);
                  if (entity != this.riddenByEntity && entity.canBePushed() && entity instanceof EntityBoat) {
                     entity.applyEntityCollision(this);
                  }
               }
            }

            if (this.riddenByEntity != null && this.riddenByEntity.isDead) {
               this.riddenByEntity = null;
            }
         }
      }

   }

   public void updateRiderPosition() {
      if (this.riddenByEntity != null) {
         double d0 = Math.cos((double)this.rotationYaw * 3.141592653589793D / 180.0D) * 0.4D;
         double d1 = Math.sin((double)this.rotationYaw * 3.141592653589793D / 180.0D) * 0.4D;
         this.riddenByEntity.setPosition(this.posX + d0, this.posY + this.getMountedYOffset() + this.riddenByEntity.getYOffset(), this.posZ + d1);
      }

   }

   protected void writeEntityToNBT(NBTTagCompound tagCompound) {
   }

   protected void readEntityFromNBT(NBTTagCompound tagCompund) {
   }

   public boolean interactFirst(EntityPlayer playerIn) {
      if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityPlayer && this.riddenByEntity != playerIn) {
         return true;
      } else {
         if (!this.worldObj.isRemote) {
            playerIn.mountEntity(this);
         }

         return true;
      }
   }

   protected void updateFallState(double y, boolean onGroundIn, Block blockIn, BlockPos pos) {
      if (onGroundIn) {
         if (this.fallDistance > 3.0F) {
            this.fall(this.fallDistance, 1.0F);
            if (!this.worldObj.isRemote && !this.isDead) {
               this.setDead();
               if (this.worldObj.getGameRules().getBoolean("doEntityDrops")) {
                  int j;
                  for(j = 0; j < 3; ++j) {
                     this.dropItemWithOffset(Item.getItemFromBlock(Blocks.planks), 1, 0.0F);
                  }

                  for(j = 0; j < 2; ++j) {
                     this.dropItemWithOffset(Items.stick, 1, 0.0F);
                  }
               }
            }

            this.fallDistance = 0.0F;
         }
      } else if (this.worldObj.getBlockState((new BlockPos(this)).down()).getBlock().getMaterial() != Material.water && y < 0.0D) {
         this.fallDistance = (float)((double)this.fallDistance - y);
      }

   }

   public void setDamageTaken(float p_70266_1_) {
      this.dataWatcher.updateObject(19, p_70266_1_);
   }

   public float getDamageTaken() {
      return this.dataWatcher.getWatchableObjectFloat(19);
   }

   public void setTimeSinceHit(int p_70265_1_) {
      this.dataWatcher.updateObject(17, p_70265_1_);
   }

   public int getTimeSinceHit() {
      return this.dataWatcher.getWatchableObjectInt(17);
   }

   public void setForwardDirection(int p_70269_1_) {
      this.dataWatcher.updateObject(18, p_70269_1_);
   }

   public int getForwardDirection() {
      return this.dataWatcher.getWatchableObjectInt(18);
   }

   public void setIsBoatEmpty(boolean p_70270_1_) {
      this.isBoatEmpty = p_70270_1_;
   }
}
