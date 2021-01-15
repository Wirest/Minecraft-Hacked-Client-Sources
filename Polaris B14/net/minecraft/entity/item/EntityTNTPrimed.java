/*     */ package net.minecraft.entity.item;
/*     */ 
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityTNTPrimed
/*     */   extends Entity
/*     */ {
/*     */   public int fuse;
/*     */   private EntityLivingBase tntPlacedBy;
/*     */   
/*     */   public EntityTNTPrimed(World worldIn)
/*     */   {
/*  17 */     super(worldIn);
/*  18 */     this.preventEntitySpawning = true;
/*  19 */     setSize(0.98F, 0.98F);
/*     */   }
/*     */   
/*     */   public EntityTNTPrimed(World worldIn, double p_i1730_2_, double p_i1730_4_, double p_i1730_6_, EntityLivingBase p_i1730_8_)
/*     */   {
/*  24 */     this(worldIn);
/*  25 */     setPosition(p_i1730_2_, p_i1730_4_, p_i1730_6_);
/*  26 */     float f = (float)(Math.random() * 3.141592653589793D * 2.0D);
/*  27 */     this.motionX = (-(float)Math.sin(f) * 0.02F);
/*  28 */     this.motionY = 0.20000000298023224D;
/*  29 */     this.motionZ = (-(float)Math.cos(f) * 0.02F);
/*  30 */     this.fuse = 80;
/*  31 */     this.prevPosX = p_i1730_2_;
/*  32 */     this.prevPosY = p_i1730_4_;
/*  33 */     this.prevPosZ = p_i1730_6_;
/*  34 */     this.tntPlacedBy = p_i1730_8_;
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
/*     */   protected boolean canTriggerWalking()
/*     */   {
/*  47 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canBeCollidedWith()
/*     */   {
/*  55 */     return !this.isDead;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onUpdate()
/*     */   {
/*  63 */     this.prevPosX = this.posX;
/*  64 */     this.prevPosY = this.posY;
/*  65 */     this.prevPosZ = this.posZ;
/*  66 */     this.motionY -= 0.03999999910593033D;
/*  67 */     moveEntity(this.motionX, this.motionY, this.motionZ);
/*  68 */     this.motionX *= 0.9800000190734863D;
/*  69 */     this.motionY *= 0.9800000190734863D;
/*  70 */     this.motionZ *= 0.9800000190734863D;
/*     */     
/*  72 */     if (this.onGround)
/*     */     {
/*  74 */       this.motionX *= 0.699999988079071D;
/*  75 */       this.motionZ *= 0.699999988079071D;
/*  76 */       this.motionY *= -0.5D;
/*     */     }
/*     */     
/*  79 */     if (this.fuse-- <= 0)
/*     */     {
/*  81 */       setDead();
/*     */       
/*  83 */       if (!this.worldObj.isRemote)
/*     */       {
/*  85 */         explode();
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/*  90 */       handleWaterMovement();
/*  91 */       this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
/*     */     }
/*     */   }
/*     */   
/*     */   private void explode()
/*     */   {
/*  97 */     float f = 4.0F;
/*  98 */     this.worldObj.createExplosion(this, this.posX, this.posY + this.height / 16.0F, this.posZ, f, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void writeEntityToNBT(NBTTagCompound tagCompound)
/*     */   {
/* 106 */     tagCompound.setByte("Fuse", (byte)this.fuse);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void readEntityFromNBT(NBTTagCompound tagCompund)
/*     */   {
/* 114 */     this.fuse = tagCompund.getByte("Fuse");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public EntityLivingBase getTntPlacedBy()
/*     */   {
/* 122 */     return this.tntPlacedBy;
/*     */   }
/*     */   
/*     */   public float getEyeHeight()
/*     */   {
/* 127 */     return 0.0F;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\item\EntityTNTPrimed.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */