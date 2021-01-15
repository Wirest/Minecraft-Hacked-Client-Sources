/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class EntityCritFX extends EntitySmokeFX
/*    */ {
/*    */   protected EntityCritFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double p_i1201_8_, double p_i1201_10_, double p_i1201_12_)
/*    */   {
/*  9 */     super(worldIn, xCoordIn, yCoordIn, zCoordIn, p_i1201_8_, p_i1201_10_, p_i1201_12_, 2.5F);
/*    */   }
/*    */   
/*    */   public static class Factory implements IParticleFactory
/*    */   {
/*    */     public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
/*    */     {
/* 16 */       return new EntityCritFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\particle\EntityCritFX.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */