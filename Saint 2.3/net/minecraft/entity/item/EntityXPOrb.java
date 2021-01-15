package net.minecraft.entity.item;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityXPOrb extends Entity {
   public int xpColor;
   public int xpOrbAge;
   public int field_70532_c;
   private int xpOrbHealth = 5;
   private int xpValue;
   private EntityPlayer closestPlayer;
   private int xpTargetColor;
   private static final String __OBFID = "CL_00001544";

   public EntityXPOrb(World worldIn, double p_i1585_2_, double p_i1585_4_, double p_i1585_6_, int p_i1585_8_) {
      super(worldIn);
      this.setSize(0.5F, 0.5F);
      this.setPosition(p_i1585_2_, p_i1585_4_, p_i1585_6_);
      this.rotationYaw = (float)(Math.random() * 360.0D);
      this.motionX = (double)((float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D) * 2.0F);
      this.motionY = (double)((float)(Math.random() * 0.2D) * 2.0F);
      this.motionZ = (double)((float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D) * 2.0F);
      this.xpValue = p_i1585_8_;
   }

   protected boolean canTriggerWalking() {
      return false;
   }

   public EntityXPOrb(World worldIn) {
      super(worldIn);
      this.setSize(0.25F, 0.25F);
   }

   protected void entityInit() {
   }

   public int getBrightnessForRender(float p_70070_1_) {
      float var2 = 0.5F;
      var2 = MathHelper.clamp_float(var2, 0.0F, 1.0F);
      int var3 = super.getBrightnessForRender(p_70070_1_);
      int var4 = var3 & 255;
      int var5 = var3 >> 16 & 255;
      var4 += (int)(var2 * 15.0F * 16.0F);
      if (var4 > 240) {
         var4 = 240;
      }

      return var4 | var5 << 16;
   }

   public void onUpdate() {
      super.onUpdate();
      if (this.field_70532_c > 0) {
         --this.field_70532_c;
      }

      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      this.motionY -= 0.029999999329447746D;
      if (this.worldObj.getBlockState(new BlockPos(this)).getBlock().getMaterial() == Material.lava) {
         this.motionY = 0.20000000298023224D;
         this.motionX = (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
         this.motionZ = (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
         this.playSound("random.fizz", 0.4F, 2.0F + this.rand.nextFloat() * 0.4F);
      }

      this.pushOutOfBlocks(this.posX, (this.getEntityBoundingBox().minY + this.getEntityBoundingBox().maxY) / 2.0D, this.posZ);
      double var1 = 8.0D;
      if (this.xpTargetColor < this.xpColor - 20 + this.getEntityId() % 100) {
         if (this.closestPlayer == null || this.closestPlayer.getDistanceSqToEntity(this) > var1 * var1) {
            this.closestPlayer = this.worldObj.getClosestPlayerToEntity(this, var1);
         }

         this.xpTargetColor = this.xpColor;
      }

      if (this.closestPlayer != null && this.closestPlayer.func_175149_v()) {
         this.closestPlayer = null;
      }

      if (this.closestPlayer != null) {
         double var3 = (this.closestPlayer.posX - this.posX) / var1;
         double var5 = (this.closestPlayer.posY + (double)this.closestPlayer.getEyeHeight() - this.posY) / var1;
         double var7 = (this.closestPlayer.posZ - this.posZ) / var1;
         double var9 = Math.sqrt(var3 * var3 + var5 * var5 + var7 * var7);
         double var11 = 1.0D - var9;
         if (var11 > 0.0D) {
            var11 *= var11;
            this.motionX += var3 / var9 * var11 * 0.1D;
            this.motionY += var5 / var9 * var11 * 0.1D;
            this.motionZ += var7 / var9 * var11 * 0.1D;
         }
      }

      this.moveEntity(this.motionX, this.motionY, this.motionZ);
      float var13 = 0.98F;
      if (this.onGround) {
         var13 = this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.getEntityBoundingBox().minY) - 1, MathHelper.floor_double(this.posZ))).getBlock().slipperiness * 0.98F;
      }

      this.motionX *= (double)var13;
      this.motionY *= 0.9800000190734863D;
      this.motionZ *= (double)var13;
      if (this.onGround) {
         this.motionY *= -0.8999999761581421D;
      }

      ++this.xpColor;
      ++this.xpOrbAge;
      if (this.xpOrbAge >= 6000) {
         this.setDead();
      }

   }

   public boolean handleWaterMovement() {
      return this.worldObj.handleMaterialAcceleration(this.getEntityBoundingBox(), Material.water, this);
   }

   protected void dealFireDamage(int amount) {
      this.attackEntityFrom(DamageSource.inFire, (float)amount);
   }

   public boolean attackEntityFrom(DamageSource source, float amount) {
      if (this.func_180431_b(source)) {
         return false;
      } else {
         this.setBeenAttacked();
         this.xpOrbHealth = (int)((float)this.xpOrbHealth - amount);
         if (this.xpOrbHealth <= 0) {
            this.setDead();
         }

         return false;
      }
   }

   public void writeEntityToNBT(NBTTagCompound tagCompound) {
      tagCompound.setShort("Health", (short)((byte)this.xpOrbHealth));
      tagCompound.setShort("Age", (short)this.xpOrbAge);
      tagCompound.setShort("Value", (short)this.xpValue);
   }

   public void readEntityFromNBT(NBTTagCompound tagCompund) {
      this.xpOrbHealth = tagCompund.getShort("Health") & 255;
      this.xpOrbAge = tagCompund.getShort("Age");
      this.xpValue = tagCompund.getShort("Value");
   }

   public void onCollideWithPlayer(EntityPlayer entityIn) {
      if (!this.worldObj.isRemote && this.field_70532_c == 0 && entityIn.xpCooldown == 0) {
         entityIn.xpCooldown = 2;
         this.worldObj.playSoundAtEntity(entityIn, "random.orb", 0.1F, 0.5F * ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.8F));
         entityIn.onItemPickup(this, 1);
         entityIn.addExperience(this.xpValue);
         this.setDead();
      }

   }

   public int getXpValue() {
      return this.xpValue;
   }

   public int getTextureByXP() {
      return this.xpValue >= 2477 ? 10 : (this.xpValue >= 1237 ? 9 : (this.xpValue >= 617 ? 8 : (this.xpValue >= 307 ? 7 : (this.xpValue >= 149 ? 6 : (this.xpValue >= 73 ? 5 : (this.xpValue >= 37 ? 4 : (this.xpValue >= 17 ? 3 : (this.xpValue >= 7 ? 2 : (this.xpValue >= 3 ? 1 : 0)))))))));
   }

   public static int getXPSplit(int p_70527_0_) {
      return p_70527_0_ >= 2477 ? 2477 : (p_70527_0_ >= 1237 ? 1237 : (p_70527_0_ >= 617 ? 617 : (p_70527_0_ >= 307 ? 307 : (p_70527_0_ >= 149 ? 149 : (p_70527_0_ >= 73 ? 73 : (p_70527_0_ >= 37 ? 37 : (p_70527_0_ >= 17 ? 17 : (p_70527_0_ >= 7 ? 7 : (p_70527_0_ >= 3 ? 3 : 1)))))))));
   }

   public boolean canAttackWithItem() {
      return false;
   }
}
