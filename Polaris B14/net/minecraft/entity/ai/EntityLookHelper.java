/*     */ package net.minecraft.entity.ai;
/*     */ 
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.pathfinding.PathNavigate;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.MathHelper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EntityLookHelper
/*     */ {
/*     */   private EntityLiving entity;
/*     */   private float deltaLookYaw;
/*     */   private float deltaLookPitch;
/*     */   private boolean isLooking;
/*     */   private double posX;
/*     */   private double posY;
/*     */   private double posZ;
/*     */   
/*     */   public EntityLookHelper(EntityLiving entitylivingIn)
/*     */   {
/*  30 */     this.entity = entitylivingIn;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLookPositionWithEntity(Entity entityIn, float deltaYaw, float deltaPitch)
/*     */   {
/*  38 */     this.posX = entityIn.posX;
/*     */     
/*  40 */     if ((entityIn instanceof EntityLivingBase))
/*     */     {
/*  42 */       this.posY = (entityIn.posY + entityIn.getEyeHeight());
/*     */     }
/*     */     else
/*     */     {
/*  46 */       this.posY = ((entityIn.getEntityBoundingBox().minY + entityIn.getEntityBoundingBox().maxY) / 2.0D);
/*     */     }
/*     */     
/*  49 */     this.posZ = entityIn.posZ;
/*  50 */     this.deltaLookYaw = deltaYaw;
/*  51 */     this.deltaLookPitch = deltaPitch;
/*  52 */     this.isLooking = true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLookPosition(double x, double y, double z, float deltaYaw, float deltaPitch)
/*     */   {
/*  60 */     this.posX = x;
/*  61 */     this.posY = y;
/*  62 */     this.posZ = z;
/*  63 */     this.deltaLookYaw = deltaYaw;
/*  64 */     this.deltaLookPitch = deltaPitch;
/*  65 */     this.isLooking = true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onUpdateLook()
/*     */   {
/*  73 */     this.entity.rotationPitch = 0.0F;
/*     */     
/*  75 */     if (this.isLooking)
/*     */     {
/*  77 */       this.isLooking = false;
/*  78 */       double d0 = this.posX - this.entity.posX;
/*  79 */       double d1 = this.posY - (this.entity.posY + this.entity.getEyeHeight());
/*  80 */       double d2 = this.posZ - this.entity.posZ;
/*  81 */       double d3 = MathHelper.sqrt_double(d0 * d0 + d2 * d2);
/*  82 */       float f = (float)(MathHelper.func_181159_b(d2, d0) * 180.0D / 3.141592653589793D) - 90.0F;
/*  83 */       float f1 = (float)-(MathHelper.func_181159_b(d1, d3) * 180.0D / 3.141592653589793D);
/*  84 */       this.entity.rotationPitch = updateRotation(this.entity.rotationPitch, f1, this.deltaLookPitch);
/*  85 */       this.entity.rotationYawHead = updateRotation(this.entity.rotationYawHead, f, this.deltaLookYaw);
/*     */     }
/*     */     else
/*     */     {
/*  89 */       this.entity.rotationYawHead = updateRotation(this.entity.rotationYawHead, this.entity.renderYawOffset, 10.0F);
/*     */     }
/*     */     
/*  92 */     float f2 = MathHelper.wrapAngleTo180_float(this.entity.rotationYawHead - this.entity.renderYawOffset);
/*     */     
/*  94 */     if (!this.entity.getNavigator().noPath())
/*     */     {
/*  96 */       if (f2 < -75.0F)
/*     */       {
/*  98 */         this.entity.rotationYawHead = (this.entity.renderYawOffset - 75.0F);
/*     */       }
/*     */       
/* 101 */       if (f2 > 75.0F)
/*     */       {
/* 103 */         this.entity.rotationYawHead = (this.entity.renderYawOffset + 75.0F);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private float updateRotation(float p_75652_1_, float p_75652_2_, float p_75652_3_)
/*     */   {
/* 110 */     float f = MathHelper.wrapAngleTo180_float(p_75652_2_ - p_75652_1_);
/*     */     
/* 112 */     if (f > p_75652_3_)
/*     */     {
/* 114 */       f = p_75652_3_;
/*     */     }
/*     */     
/* 117 */     if (f < -p_75652_3_)
/*     */     {
/* 119 */       f = -p_75652_3_;
/*     */     }
/*     */     
/* 122 */     return p_75652_1_ + f;
/*     */   }
/*     */   
/*     */   public boolean getIsLooking()
/*     */   {
/* 127 */     return this.isLooking;
/*     */   }
/*     */   
/*     */   public double getLookPosX()
/*     */   {
/* 132 */     return this.posX;
/*     */   }
/*     */   
/*     */   public double getLookPosY()
/*     */   {
/* 137 */     return this.posY;
/*     */   }
/*     */   
/*     */   public double getLookPosZ()
/*     */   {
/* 142 */     return this.posZ;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\ai\EntityLookHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */