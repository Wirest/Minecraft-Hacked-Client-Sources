/*     */ package net.minecraft.entity.item;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EntityXPOrb
/*     */   extends Entity
/*     */ {
/*     */   public int xpColor;
/*     */   public int xpOrbAge;
/*     */   public int delayBeforeCanPickup;
/*  24 */   private int xpOrbHealth = 5;
/*     */   
/*     */ 
/*     */   private int xpValue;
/*     */   
/*     */ 
/*     */   private EntityPlayer closestPlayer;
/*     */   
/*     */   private int xpTargetColor;
/*     */   
/*     */ 
/*     */   public EntityXPOrb(World worldIn, double x, double y, double z, int expValue)
/*     */   {
/*  37 */     super(worldIn);
/*  38 */     setSize(0.5F, 0.5F);
/*  39 */     setPosition(x, y, z);
/*  40 */     this.rotationYaw = ((float)(Math.random() * 360.0D));
/*  41 */     this.motionX = ((float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D) * 2.0F);
/*  42 */     this.motionY = ((float)(Math.random() * 0.2D) * 2.0F);
/*  43 */     this.motionZ = ((float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D) * 2.0F);
/*  44 */     this.xpValue = expValue;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean canTriggerWalking()
/*     */   {
/*  53 */     return false;
/*     */   }
/*     */   
/*     */   public EntityXPOrb(World worldIn)
/*     */   {
/*  58 */     super(worldIn);
/*  59 */     setSize(0.25F, 0.25F);
/*     */   }
/*     */   
/*     */ 
/*     */   protected void entityInit() {}
/*     */   
/*     */ 
/*     */   public int getBrightnessForRender(float partialTicks)
/*     */   {
/*  68 */     float f = 0.5F;
/*  69 */     f = MathHelper.clamp_float(f, 0.0F, 1.0F);
/*  70 */     int i = super.getBrightnessForRender(partialTicks);
/*  71 */     int j = i & 0xFF;
/*  72 */     int k = i >> 16 & 0xFF;
/*  73 */     j += (int)(f * 15.0F * 16.0F);
/*     */     
/*  75 */     if (j > 240)
/*     */     {
/*  77 */       j = 240;
/*     */     }
/*     */     
/*  80 */     return j | k << 16;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onUpdate()
/*     */   {
/*  88 */     super.onUpdate();
/*     */     
/*  90 */     if (this.delayBeforeCanPickup > 0)
/*     */     {
/*  92 */       this.delayBeforeCanPickup -= 1;
/*     */     }
/*     */     
/*  95 */     this.prevPosX = this.posX;
/*  96 */     this.prevPosY = this.posY;
/*  97 */     this.prevPosZ = this.posZ;
/*  98 */     this.motionY -= 0.029999999329447746D;
/*     */     
/* 100 */     if (this.worldObj.getBlockState(new BlockPos(this)).getBlock().getMaterial() == Material.lava)
/*     */     {
/* 102 */       this.motionY = 0.20000000298023224D;
/* 103 */       this.motionX = ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
/* 104 */       this.motionZ = ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
/* 105 */       playSound("random.fizz", 0.4F, 2.0F + this.rand.nextFloat() * 0.4F);
/*     */     }
/*     */     
/* 108 */     pushOutOfBlocks(this.posX, (getEntityBoundingBox().minY + getEntityBoundingBox().maxY) / 2.0D, this.posZ);
/* 109 */     double d0 = 8.0D;
/*     */     
/* 111 */     if (this.xpTargetColor < this.xpColor - 20 + getEntityId() % 100)
/*     */     {
/* 113 */       if ((this.closestPlayer == null) || (this.closestPlayer.getDistanceSqToEntity(this) > d0 * d0))
/*     */       {
/* 115 */         this.closestPlayer = this.worldObj.getClosestPlayerToEntity(this, d0);
/*     */       }
/*     */       
/* 118 */       this.xpTargetColor = this.xpColor;
/*     */     }
/*     */     
/* 121 */     if ((this.closestPlayer != null) && (this.closestPlayer.isSpectator()))
/*     */     {
/* 123 */       this.closestPlayer = null;
/*     */     }
/*     */     
/* 126 */     if (this.closestPlayer != null)
/*     */     {
/* 128 */       double d1 = (this.closestPlayer.posX - this.posX) / d0;
/* 129 */       double d2 = (this.closestPlayer.posY + this.closestPlayer.getEyeHeight() - this.posY) / d0;
/* 130 */       double d3 = (this.closestPlayer.posZ - this.posZ) / d0;
/* 131 */       double d4 = Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
/* 132 */       double d5 = 1.0D - d4;
/*     */       
/* 134 */       if (d5 > 0.0D)
/*     */       {
/* 136 */         d5 *= d5;
/* 137 */         this.motionX += d1 / d4 * d5 * 0.1D;
/* 138 */         this.motionY += d2 / d4 * d5 * 0.1D;
/* 139 */         this.motionZ += d3 / d4 * d5 * 0.1D;
/*     */       }
/*     */     }
/*     */     
/* 143 */     moveEntity(this.motionX, this.motionY, this.motionZ);
/* 144 */     float f = 0.98F;
/*     */     
/* 146 */     if (this.onGround)
/*     */     {
/* 148 */       f = this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.posX), MathHelper.floor_double(getEntityBoundingBox().minY) - 1, MathHelper.floor_double(this.posZ))).getBlock().slipperiness * 0.98F;
/*     */     }
/*     */     
/* 151 */     this.motionX *= f;
/* 152 */     this.motionY *= 0.9800000190734863D;
/* 153 */     this.motionZ *= f;
/*     */     
/* 155 */     if (this.onGround)
/*     */     {
/* 157 */       this.motionY *= -0.8999999761581421D;
/*     */     }
/*     */     
/* 160 */     this.xpColor += 1;
/* 161 */     this.xpOrbAge += 1;
/*     */     
/* 163 */     if (this.xpOrbAge >= 6000)
/*     */     {
/* 165 */       setDead();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean handleWaterMovement()
/*     */   {
/* 174 */     return this.worldObj.handleMaterialAcceleration(getEntityBoundingBox(), Material.water, this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void dealFireDamage(int amount)
/*     */   {
/* 183 */     attackEntityFrom(DamageSource.inFire, amount);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean attackEntityFrom(DamageSource source, float amount)
/*     */   {
/* 191 */     if (isEntityInvulnerable(source))
/*     */     {
/* 193 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 197 */     setBeenAttacked();
/* 198 */     this.xpOrbHealth = ((int)(this.xpOrbHealth - amount));
/*     */     
/* 200 */     if (this.xpOrbHealth <= 0)
/*     */     {
/* 202 */       setDead();
/*     */     }
/*     */     
/* 205 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound)
/*     */   {
/* 214 */     tagCompound.setShort("Health", (short)(byte)this.xpOrbHealth);
/* 215 */     tagCompound.setShort("Age", (short)this.xpOrbAge);
/* 216 */     tagCompound.setShort("Value", (short)this.xpValue);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund)
/*     */   {
/* 224 */     this.xpOrbHealth = (tagCompund.getShort("Health") & 0xFF);
/* 225 */     this.xpOrbAge = tagCompund.getShort("Age");
/* 226 */     this.xpValue = tagCompund.getShort("Value");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onCollideWithPlayer(EntityPlayer entityIn)
/*     */   {
/* 234 */     if (!this.worldObj.isRemote)
/*     */     {
/* 236 */       if ((this.delayBeforeCanPickup == 0) && (entityIn.xpCooldown == 0))
/*     */       {
/* 238 */         entityIn.xpCooldown = 2;
/* 239 */         this.worldObj.playSoundAtEntity(entityIn, "random.orb", 0.1F, 0.5F * ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.8F));
/* 240 */         entityIn.onItemPickup(this, 1);
/* 241 */         entityIn.addExperience(this.xpValue);
/* 242 */         setDead();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getXpValue()
/*     */   {
/* 252 */     return this.xpValue;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getTextureByXP()
/*     */   {
/* 261 */     return this.xpValue >= 3 ? 1 : this.xpValue >= 7 ? 2 : this.xpValue >= 17 ? 3 : this.xpValue >= 37 ? 4 : this.xpValue >= 73 ? 5 : this.xpValue >= 149 ? 6 : this.xpValue >= 307 ? 7 : this.xpValue >= 617 ? 8 : this.xpValue >= 1237 ? 9 : this.xpValue >= 2477 ? 10 : 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int getXPSplit(int expValue)
/*     */   {
/* 269 */     return expValue >= 3 ? 3 : expValue >= 7 ? 7 : expValue >= 17 ? 17 : expValue >= 37 ? 37 : expValue >= 73 ? 73 : expValue >= 149 ? 149 : expValue >= 307 ? 307 : expValue >= 617 ? 617 : expValue >= 1237 ? 1237 : expValue >= 2477 ? 2477 : 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canAttackWithItem()
/*     */   {
/* 277 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\item\EntityXPOrb.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */