/*     */ package net.minecraft.entity.item;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EntityEnderEye
/*     */   extends Entity
/*     */ {
/*     */   private double targetX;
/*     */   private double targetY;
/*     */   private double targetZ;
/*     */   private int despawnTimer;
/*     */   private boolean shatterOrDrop;
/*     */   
/*     */   public EntityEnderEye(World worldIn)
/*     */   {
/*  27 */     super(worldIn);
/*  28 */     setSize(0.25F, 0.25F);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void entityInit() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isInRangeToRenderDist(double distance)
/*     */   {
/*  41 */     double d0 = getEntityBoundingBox().getAverageEdgeLength() * 4.0D;
/*     */     
/*  43 */     if (Double.isNaN(d0))
/*     */     {
/*  45 */       d0 = 4.0D;
/*     */     }
/*     */     
/*  48 */     d0 *= 64.0D;
/*  49 */     return distance < d0 * d0;
/*     */   }
/*     */   
/*     */   public EntityEnderEye(World worldIn, double x, double y, double z)
/*     */   {
/*  54 */     super(worldIn);
/*  55 */     this.despawnTimer = 0;
/*  56 */     setSize(0.25F, 0.25F);
/*  57 */     setPosition(x, y, z);
/*     */   }
/*     */   
/*     */   public void moveTowards(BlockPos p_180465_1_)
/*     */   {
/*  62 */     double d0 = p_180465_1_.getX();
/*  63 */     int i = p_180465_1_.getY();
/*  64 */     double d1 = p_180465_1_.getZ();
/*  65 */     double d2 = d0 - this.posX;
/*  66 */     double d3 = d1 - this.posZ;
/*  67 */     float f = MathHelper.sqrt_double(d2 * d2 + d3 * d3);
/*     */     
/*  69 */     if (f > 12.0F)
/*     */     {
/*  71 */       this.targetX = (this.posX + d2 / f * 12.0D);
/*  72 */       this.targetZ = (this.posZ + d3 / f * 12.0D);
/*  73 */       this.targetY = (this.posY + 8.0D);
/*     */     }
/*     */     else
/*     */     {
/*  77 */       this.targetX = d0;
/*  78 */       this.targetY = i;
/*  79 */       this.targetZ = d1;
/*     */     }
/*     */     
/*  82 */     this.despawnTimer = 0;
/*  83 */     this.shatterOrDrop = (this.rand.nextInt(5) > 0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setVelocity(double x, double y, double z)
/*     */   {
/*  91 */     this.motionX = x;
/*  92 */     this.motionY = y;
/*  93 */     this.motionZ = z;
/*     */     
/*  95 */     if ((this.prevRotationPitch == 0.0F) && (this.prevRotationYaw == 0.0F))
/*     */     {
/*  97 */       float f = MathHelper.sqrt_double(x * x + z * z);
/*  98 */       this.prevRotationYaw = (this.rotationYaw = (float)(MathHelper.func_181159_b(x, z) * 180.0D / 3.141592653589793D));
/*  99 */       this.prevRotationPitch = (this.rotationPitch = (float)(MathHelper.func_181159_b(y, f) * 180.0D / 3.141592653589793D));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onUpdate()
/*     */   {
/* 108 */     this.lastTickPosX = this.posX;
/* 109 */     this.lastTickPosY = this.posY;
/* 110 */     this.lastTickPosZ = this.posZ;
/* 111 */     super.onUpdate();
/* 112 */     this.posX += this.motionX;
/* 113 */     this.posY += this.motionY;
/* 114 */     this.posZ += this.motionZ;
/* 115 */     float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
/* 116 */     this.rotationYaw = ((float)(MathHelper.func_181159_b(this.motionX, this.motionZ) * 180.0D / 3.141592653589793D));
/*     */     
/* 118 */     for (this.rotationPitch = ((float)(MathHelper.func_181159_b(this.motionY, f) * 180.0D / 3.141592653589793D)); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 123 */     while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
/*     */     {
/* 125 */       this.prevRotationPitch += 360.0F;
/*     */     }
/*     */     
/* 128 */     while (this.rotationYaw - this.prevRotationYaw < -180.0F)
/*     */     {
/* 130 */       this.prevRotationYaw -= 360.0F;
/*     */     }
/*     */     
/* 133 */     while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
/*     */     {
/* 135 */       this.prevRotationYaw += 360.0F;
/*     */     }
/*     */     
/* 138 */     this.rotationPitch = (this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F);
/* 139 */     this.rotationYaw = (this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F);
/*     */     
/* 141 */     if (!this.worldObj.isRemote)
/*     */     {
/* 143 */       double d0 = this.targetX - this.posX;
/* 144 */       double d1 = this.targetZ - this.posZ;
/* 145 */       float f1 = (float)Math.sqrt(d0 * d0 + d1 * d1);
/* 146 */       float f2 = (float)MathHelper.func_181159_b(d1, d0);
/* 147 */       double d2 = f + (f1 - f) * 0.0025D;
/*     */       
/* 149 */       if (f1 < 1.0F)
/*     */       {
/* 151 */         d2 *= 0.8D;
/* 152 */         this.motionY *= 0.8D;
/*     */       }
/*     */       
/* 155 */       this.motionX = (Math.cos(f2) * d2);
/* 156 */       this.motionZ = (Math.sin(f2) * d2);
/*     */       
/* 158 */       if (this.posY < this.targetY)
/*     */       {
/* 160 */         this.motionY += (1.0D - this.motionY) * 0.014999999664723873D;
/*     */       }
/*     */       else
/*     */       {
/* 164 */         this.motionY += (-1.0D - this.motionY) * 0.014999999664723873D;
/*     */       }
/*     */     }
/*     */     
/* 168 */     float f3 = 0.25F;
/*     */     
/* 170 */     if (isInWater())
/*     */     {
/* 172 */       for (int i = 0; i < 4; i++)
/*     */       {
/* 174 */         this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * f3, this.posY - this.motionY * f3, this.posZ - this.motionZ * f3, this.motionX, this.motionY, this.motionZ, new int[0]);
/*     */       }
/*     */       
/*     */     }
/*     */     else {
/* 179 */       this.worldObj.spawnParticle(EnumParticleTypes.PORTAL, this.posX - this.motionX * f3 + this.rand.nextDouble() * 0.6D - 0.3D, this.posY - this.motionY * f3 - 0.5D, this.posZ - this.motionZ * f3 + this.rand.nextDouble() * 0.6D - 0.3D, this.motionX, this.motionY, this.motionZ, new int[0]);
/*     */     }
/*     */     
/* 182 */     if (!this.worldObj.isRemote)
/*     */     {
/* 184 */       setPosition(this.posX, this.posY, this.posZ);
/* 185 */       this.despawnTimer += 1;
/*     */       
/* 187 */       if ((this.despawnTimer > 80) && (!this.worldObj.isRemote))
/*     */       {
/* 189 */         setDead();
/*     */         
/* 191 */         if (this.shatterOrDrop)
/*     */         {
/* 193 */           this.worldObj.spawnEntityInWorld(new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, new ItemStack(Items.ender_eye)));
/*     */         }
/*     */         else
/*     */         {
/* 197 */           this.worldObj.playAuxSFX(2003, new BlockPos(this), 0);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public float getBrightness(float partialTicks)
/*     */   {
/* 222 */     return 1.0F;
/*     */   }
/*     */   
/*     */   public int getBrightnessForRender(float partialTicks)
/*     */   {
/* 227 */     return 15728880;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canAttackWithItem()
/*     */   {
/* 235 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\item\EntityEnderEye.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */