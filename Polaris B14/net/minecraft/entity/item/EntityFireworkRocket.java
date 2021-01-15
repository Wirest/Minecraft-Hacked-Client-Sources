/*     */ package net.minecraft.entity.item;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.entity.DataWatcher;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EntityFireworkRocket
/*     */   extends Entity
/*     */ {
/*     */   private int fireworkAge;
/*     */   private int lifetime;
/*     */   
/*     */   public EntityFireworkRocket(World worldIn)
/*     */   {
/*  22 */     super(worldIn);
/*  23 */     setSize(0.25F, 0.25F);
/*     */   }
/*     */   
/*     */   protected void entityInit()
/*     */   {
/*  28 */     this.dataWatcher.addObjectByDataType(8, 5);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isInRangeToRenderDist(double distance)
/*     */   {
/*  37 */     return distance < 4096.0D;
/*     */   }
/*     */   
/*     */   public EntityFireworkRocket(World worldIn, double x, double y, double z, ItemStack givenItem)
/*     */   {
/*  42 */     super(worldIn);
/*  43 */     this.fireworkAge = 0;
/*  44 */     setSize(0.25F, 0.25F);
/*  45 */     setPosition(x, y, z);
/*  46 */     int i = 1;
/*     */     
/*  48 */     if ((givenItem != null) && (givenItem.hasTagCompound()))
/*     */     {
/*  50 */       this.dataWatcher.updateObject(8, givenItem);
/*  51 */       NBTTagCompound nbttagcompound = givenItem.getTagCompound();
/*  52 */       NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("Fireworks");
/*     */       
/*  54 */       if (nbttagcompound1 != null)
/*     */       {
/*  56 */         i += nbttagcompound1.getByte("Flight");
/*     */       }
/*     */     }
/*     */     
/*  60 */     this.motionX = (this.rand.nextGaussian() * 0.001D);
/*  61 */     this.motionZ = (this.rand.nextGaussian() * 0.001D);
/*  62 */     this.motionY = 0.05D;
/*  63 */     this.lifetime = (10 * i + this.rand.nextInt(6) + this.rand.nextInt(7));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setVelocity(double x, double y, double z)
/*     */   {
/*  71 */     this.motionX = x;
/*  72 */     this.motionY = y;
/*  73 */     this.motionZ = z;
/*     */     
/*  75 */     if ((this.prevRotationPitch == 0.0F) && (this.prevRotationYaw == 0.0F))
/*     */     {
/*  77 */       float f = MathHelper.sqrt_double(x * x + z * z);
/*  78 */       this.prevRotationYaw = (this.rotationYaw = (float)(MathHelper.func_181159_b(x, z) * 180.0D / 3.141592653589793D));
/*  79 */       this.prevRotationPitch = (this.rotationPitch = (float)(MathHelper.func_181159_b(y, f) * 180.0D / 3.141592653589793D));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onUpdate()
/*     */   {
/*  88 */     this.lastTickPosX = this.posX;
/*  89 */     this.lastTickPosY = this.posY;
/*  90 */     this.lastTickPosZ = this.posZ;
/*  91 */     super.onUpdate();
/*  92 */     this.motionX *= 1.15D;
/*  93 */     this.motionZ *= 1.15D;
/*  94 */     this.motionY += 0.04D;
/*  95 */     moveEntity(this.motionX, this.motionY, this.motionZ);
/*  96 */     float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
/*  97 */     this.rotationYaw = ((float)(MathHelper.func_181159_b(this.motionX, this.motionZ) * 180.0D / 3.141592653589793D));
/*     */     
/*  99 */     for (this.rotationPitch = ((float)(MathHelper.func_181159_b(this.motionY, f) * 180.0D / 3.141592653589793D)); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 104 */     while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
/*     */     {
/* 106 */       this.prevRotationPitch += 360.0F;
/*     */     }
/*     */     
/* 109 */     while (this.rotationYaw - this.prevRotationYaw < -180.0F)
/*     */     {
/* 111 */       this.prevRotationYaw -= 360.0F;
/*     */     }
/*     */     
/* 114 */     while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
/*     */     {
/* 116 */       this.prevRotationYaw += 360.0F;
/*     */     }
/*     */     
/* 119 */     this.rotationPitch = (this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F);
/* 120 */     this.rotationYaw = (this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F);
/*     */     
/* 122 */     if ((this.fireworkAge == 0) && (!isSilent()))
/*     */     {
/* 124 */       this.worldObj.playSoundAtEntity(this, "fireworks.launch", 3.0F, 1.0F);
/*     */     }
/*     */     
/* 127 */     this.fireworkAge += 1;
/*     */     
/* 129 */     if ((this.worldObj.isRemote) && (this.fireworkAge % 2 < 2))
/*     */     {
/* 131 */       this.worldObj.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, this.posX, this.posY - 0.3D, this.posZ, this.rand.nextGaussian() * 0.05D, -this.motionY * 0.5D, this.rand.nextGaussian() * 0.05D, new int[0]);
/*     */     }
/*     */     
/* 134 */     if ((!this.worldObj.isRemote) && (this.fireworkAge > this.lifetime))
/*     */     {
/* 136 */       this.worldObj.setEntityState(this, (byte)17);
/* 137 */       setDead();
/*     */     }
/*     */   }
/*     */   
/*     */   public void handleStatusUpdate(byte id)
/*     */   {
/* 143 */     if ((id == 17) && (this.worldObj.isRemote))
/*     */     {
/* 145 */       ItemStack itemstack = this.dataWatcher.getWatchableObjectItemStack(8);
/* 146 */       NBTTagCompound nbttagcompound = null;
/*     */       
/* 148 */       if ((itemstack != null) && (itemstack.hasTagCompound()))
/*     */       {
/* 150 */         nbttagcompound = itemstack.getTagCompound().getCompoundTag("Fireworks");
/*     */       }
/*     */       
/* 153 */       this.worldObj.makeFireworks(this.posX, this.posY, this.posZ, this.motionX, this.motionY, this.motionZ, nbttagcompound);
/*     */     }
/*     */     
/* 156 */     super.handleStatusUpdate(id);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound)
/*     */   {
/* 164 */     tagCompound.setInteger("Life", this.fireworkAge);
/* 165 */     tagCompound.setInteger("LifeTime", this.lifetime);
/* 166 */     ItemStack itemstack = this.dataWatcher.getWatchableObjectItemStack(8);
/*     */     
/* 168 */     if (itemstack != null)
/*     */     {
/* 170 */       NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 171 */       itemstack.writeToNBT(nbttagcompound);
/* 172 */       tagCompound.setTag("FireworksItem", nbttagcompound);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund)
/*     */   {
/* 181 */     this.fireworkAge = tagCompund.getInteger("Life");
/* 182 */     this.lifetime = tagCompund.getInteger("LifeTime");
/* 183 */     NBTTagCompound nbttagcompound = tagCompund.getCompoundTag("FireworksItem");
/*     */     
/* 185 */     if (nbttagcompound != null)
/*     */     {
/* 187 */       ItemStack itemstack = ItemStack.loadItemStackFromNBT(nbttagcompound);
/*     */       
/* 189 */       if (itemstack != null)
/*     */       {
/* 191 */         this.dataWatcher.updateObject(8, itemstack);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public float getBrightness(float partialTicks)
/*     */   {
/* 201 */     return super.getBrightness(partialTicks);
/*     */   }
/*     */   
/*     */   public int getBrightnessForRender(float partialTicks)
/*     */   {
/* 206 */     return super.getBrightnessForRender(partialTicks);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canAttackWithItem()
/*     */   {
/* 214 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\item\EntityFireworkRocket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */