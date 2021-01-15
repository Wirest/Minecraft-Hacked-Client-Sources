/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class EntityFishWakeFX extends EntityFX
/*    */ {
/*    */   protected EntityFishWakeFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double p_i45073_8_, double p_i45073_10_, double p_i45073_12_)
/*    */   {
/*  9 */     super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
/* 10 */     this.motionX *= 0.30000001192092896D;
/* 11 */     this.motionY = (Math.random() * 0.20000000298023224D + 0.10000000149011612D);
/* 12 */     this.motionZ *= 0.30000001192092896D;
/* 13 */     this.particleRed = 1.0F;
/* 14 */     this.particleGreen = 1.0F;
/* 15 */     this.particleBlue = 1.0F;
/* 16 */     setParticleTextureIndex(19);
/* 17 */     setSize(0.01F, 0.01F);
/* 18 */     this.particleMaxAge = ((int)(8.0D / (Math.random() * 0.8D + 0.2D)));
/* 19 */     this.particleGravity = 0.0F;
/* 20 */     this.motionX = p_i45073_8_;
/* 21 */     this.motionY = p_i45073_10_;
/* 22 */     this.motionZ = p_i45073_12_;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void onUpdate()
/*    */   {
/* 30 */     this.prevPosX = this.posX;
/* 31 */     this.prevPosY = this.posY;
/* 32 */     this.prevPosZ = this.posZ;
/* 33 */     this.motionY -= this.particleGravity;
/* 34 */     moveEntity(this.motionX, this.motionY, this.motionZ);
/* 35 */     this.motionX *= 0.9800000190734863D;
/* 36 */     this.motionY *= 0.9800000190734863D;
/* 37 */     this.motionZ *= 0.9800000190734863D;
/* 38 */     int i = 60 - this.particleMaxAge;
/* 39 */     float f = i * 0.001F;
/* 40 */     setSize(f, f);
/* 41 */     setParticleTextureIndex(19 + i % 4);
/*    */     
/* 43 */     if (this.particleMaxAge-- <= 0)
/*    */     {
/* 45 */       setDead();
/*    */     }
/*    */   }
/*    */   
/*    */   public static class Factory implements IParticleFactory
/*    */   {
/*    */     public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
/*    */     {
/* 53 */       return new EntityFishWakeFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\particle\EntityFishWakeFX.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */