/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import java.util.Random;
/*    */ 
/*    */ public class EntityExplodeFX extends EntityFX
/*    */ {
/*    */   protected EntityExplodeFX(net.minecraft.world.World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn)
/*    */   {
/*  9 */     super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
/* 10 */     this.motionX = (xSpeedIn + (Math.random() * 2.0D - 1.0D) * 0.05000000074505806D);
/* 11 */     this.motionY = (ySpeedIn + (Math.random() * 2.0D - 1.0D) * 0.05000000074505806D);
/* 12 */     this.motionZ = (zSpeedIn + (Math.random() * 2.0D - 1.0D) * 0.05000000074505806D);
/* 13 */     this.particleRed = (this.particleGreen = this.particleBlue = this.rand.nextFloat() * 0.3F + 0.7F);
/* 14 */     this.particleScale = (this.rand.nextFloat() * this.rand.nextFloat() * 6.0F + 1.0F);
/* 15 */     this.particleMaxAge = ((int)(16.0D / (this.rand.nextFloat() * 0.8D + 0.2D)) + 2);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void onUpdate()
/*    */   {
/* 23 */     this.prevPosX = this.posX;
/* 24 */     this.prevPosY = this.posY;
/* 25 */     this.prevPosZ = this.posZ;
/*    */     
/* 27 */     if (this.particleAge++ >= this.particleMaxAge)
/*    */     {
/* 29 */       setDead();
/*    */     }
/*    */     
/* 32 */     setParticleTextureIndex(7 - this.particleAge * 8 / this.particleMaxAge);
/* 33 */     this.motionY += 0.004D;
/* 34 */     moveEntity(this.motionX, this.motionY, this.motionZ);
/* 35 */     this.motionX *= 0.8999999761581421D;
/* 36 */     this.motionY *= 0.8999999761581421D;
/* 37 */     this.motionZ *= 0.8999999761581421D;
/*    */     
/* 39 */     if (this.onGround)
/*    */     {
/* 41 */       this.motionX *= 0.699999988079071D;
/* 42 */       this.motionZ *= 0.699999988079071D;
/*    */     }
/*    */   }
/*    */   
/*    */   public static class Factory implements IParticleFactory
/*    */   {
/*    */     public EntityFX getEntityFX(int particleID, net.minecraft.world.World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
/*    */     {
/* 50 */       return new EntityExplodeFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\particle\EntityExplodeFX.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */