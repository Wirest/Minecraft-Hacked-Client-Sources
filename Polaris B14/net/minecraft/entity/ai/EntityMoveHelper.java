/*     */ package net.minecraft.entity.ai;
/*     */ 
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.ai.attributes.IAttributeInstance;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.MathHelper;
/*     */ 
/*     */ 
/*     */ public class EntityMoveHelper
/*     */ {
/*     */   protected EntityLiving entity;
/*     */   protected double posX;
/*     */   protected double posY;
/*     */   protected double posZ;
/*     */   protected double speed;
/*     */   protected boolean update;
/*     */   
/*     */   public EntityMoveHelper(EntityLiving entitylivingIn)
/*     */   {
/*  21 */     this.entity = entitylivingIn;
/*  22 */     this.posX = entitylivingIn.posX;
/*  23 */     this.posY = entitylivingIn.posY;
/*  24 */     this.posZ = entitylivingIn.posZ;
/*     */   }
/*     */   
/*     */   public boolean isUpdating()
/*     */   {
/*  29 */     return this.update;
/*     */   }
/*     */   
/*     */   public double getSpeed()
/*     */   {
/*  34 */     return this.speed;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setMoveTo(double x, double y, double z, double speedIn)
/*     */   {
/*  42 */     this.posX = x;
/*  43 */     this.posY = y;
/*  44 */     this.posZ = z;
/*  45 */     this.speed = speedIn;
/*  46 */     this.update = true;
/*     */   }
/*     */   
/*     */   public void onUpdateMoveHelper()
/*     */   {
/*  51 */     this.entity.setMoveForward(0.0F);
/*     */     
/*  53 */     if (this.update)
/*     */     {
/*  55 */       this.update = false;
/*  56 */       int i = MathHelper.floor_double(this.entity.getEntityBoundingBox().minY + 0.5D);
/*  57 */       double d0 = this.posX - this.entity.posX;
/*  58 */       double d1 = this.posZ - this.entity.posZ;
/*  59 */       double d2 = this.posY - i;
/*  60 */       double d3 = d0 * d0 + d2 * d2 + d1 * d1;
/*     */       
/*  62 */       if (d3 >= 2.500000277905201E-7D)
/*     */       {
/*  64 */         float f = (float)(MathHelper.func_181159_b(d1, d0) * 180.0D / 3.141592653589793D) - 90.0F;
/*  65 */         this.entity.rotationYaw = limitAngle(this.entity.rotationYaw, f, 30.0F);
/*  66 */         this.entity.setAIMoveSpeed((float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue()));
/*     */         
/*  68 */         if ((d2 > 0.0D) && (d0 * d0 + d1 * d1 < 1.0D))
/*     */         {
/*  70 */           this.entity.getJumpHelper().setJumping();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected float limitAngle(float p_75639_1_, float p_75639_2_, float p_75639_3_)
/*     */   {
/*  81 */     float f = MathHelper.wrapAngleTo180_float(p_75639_2_ - p_75639_1_);
/*     */     
/*  83 */     if (f > p_75639_3_)
/*     */     {
/*  85 */       f = p_75639_3_;
/*     */     }
/*     */     
/*  88 */     if (f < -p_75639_3_)
/*     */     {
/*  90 */       f = -p_75639_3_;
/*     */     }
/*     */     
/*  93 */     float f1 = p_75639_1_ + f;
/*     */     
/*  95 */     if (f1 < 0.0F)
/*     */     {
/*  97 */       f1 += 360.0F;
/*     */     }
/*  99 */     else if (f1 > 360.0F)
/*     */     {
/* 101 */       f1 -= 360.0F;
/*     */     }
/*     */     
/* 104 */     return f1;
/*     */   }
/*     */   
/*     */   public double getX()
/*     */   {
/* 109 */     return this.posX;
/*     */   }
/*     */   
/*     */   public double getY()
/*     */   {
/* 114 */     return this.posY;
/*     */   }
/*     */   
/*     */   public double getZ()
/*     */   {
/* 119 */     return this.posZ;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\ai\EntityMoveHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */