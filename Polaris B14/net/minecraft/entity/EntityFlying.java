/*    */ package net.minecraft.entity;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.MathHelper;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public abstract class EntityFlying extends EntityLiving
/*    */ {
/*    */   public EntityFlying(World worldIn)
/*    */   {
/* 12 */     super(worldIn);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void fall(float distance, float damageMultiplier) {}
/*    */   
/*    */ 
/*    */ 
/*    */   protected void updateFallState(double y, boolean onGroundIn, Block blockIn, BlockPos pos) {}
/*    */   
/*    */ 
/*    */ 
/*    */   public void moveEntityWithHeading(float strafe, float forward)
/*    */   {
/* 28 */     if (isInWater())
/*    */     {
/* 30 */       moveFlying(strafe, forward, 0.02F);
/* 31 */       moveEntity(this.motionX, this.motionY, this.motionZ);
/* 32 */       this.motionX *= 0.800000011920929D;
/* 33 */       this.motionY *= 0.800000011920929D;
/* 34 */       this.motionZ *= 0.800000011920929D;
/*    */     }
/* 36 */     else if (isInLava())
/*    */     {
/* 38 */       moveFlying(strafe, forward, 0.02F);
/* 39 */       moveEntity(this.motionX, this.motionY, this.motionZ);
/* 40 */       this.motionX *= 0.5D;
/* 41 */       this.motionY *= 0.5D;
/* 42 */       this.motionZ *= 0.5D;
/*    */     }
/*    */     else
/*    */     {
/* 46 */       float f = 0.91F;
/*    */       
/* 48 */       if (this.onGround)
/*    */       {
/* 50 */         f = this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.posX), MathHelper.floor_double(getEntityBoundingBox().minY) - 1, MathHelper.floor_double(this.posZ))).getBlock().slipperiness * 0.91F;
/*    */       }
/*    */       
/* 53 */       float f1 = 0.16277136F / (f * f * f);
/* 54 */       moveFlying(strafe, forward, this.onGround ? 0.1F * f1 : 0.02F);
/* 55 */       f = 0.91F;
/*    */       
/* 57 */       if (this.onGround)
/*    */       {
/* 59 */         f = this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.posX), MathHelper.floor_double(getEntityBoundingBox().minY) - 1, MathHelper.floor_double(this.posZ))).getBlock().slipperiness * 0.91F;
/*    */       }
/*    */       
/* 62 */       moveEntity(this.motionX, this.motionY, this.motionZ);
/* 63 */       this.motionX *= f;
/* 64 */       this.motionY *= f;
/* 65 */       this.motionZ *= f;
/*    */     }
/*    */     
/* 68 */     this.prevLimbSwingAmount = this.limbSwingAmount;
/* 69 */     double d1 = this.posX - this.prevPosX;
/* 70 */     double d0 = this.posZ - this.prevPosZ;
/* 71 */     float f2 = MathHelper.sqrt_double(d1 * d1 + d0 * d0) * 4.0F;
/*    */     
/* 73 */     if (f2 > 1.0F)
/*    */     {
/* 75 */       f2 = 1.0F;
/*    */     }
/*    */     
/* 78 */     this.limbSwingAmount += (f2 - this.limbSwingAmount) * 0.4F;
/* 79 */     this.limbSwing += this.limbSwingAmount;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean isOnLadder()
/*    */   {
/* 87 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\EntityFlying.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */