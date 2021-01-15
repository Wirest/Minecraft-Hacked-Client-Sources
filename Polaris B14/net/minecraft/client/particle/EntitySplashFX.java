/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class EntitySplashFX extends EntityRainFX
/*    */ {
/*    */   protected EntitySplashFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn)
/*    */   {
/*  9 */     super(worldIn, xCoordIn, yCoordIn, zCoordIn);
/* 10 */     this.particleGravity = 0.04F;
/* 11 */     nextTextureIndexX();
/*    */     
/* 13 */     if ((ySpeedIn == 0.0D) && ((xSpeedIn != 0.0D) || (zSpeedIn != 0.0D)))
/*    */     {
/* 15 */       this.motionX = xSpeedIn;
/* 16 */       this.motionY = (ySpeedIn + 0.1D);
/* 17 */       this.motionZ = zSpeedIn;
/*    */     }
/*    */   }
/*    */   
/*    */   public static class Factory implements IParticleFactory
/*    */   {
/*    */     public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
/*    */     {
/* 25 */       return new EntitySplashFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\particle\EntitySplashFX.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */