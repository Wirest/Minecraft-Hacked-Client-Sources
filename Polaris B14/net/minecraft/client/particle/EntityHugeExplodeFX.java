/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.client.renderer.WorldRenderer;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.util.EnumParticleTypes;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class EntityHugeExplodeFX
/*    */   extends EntityFX
/*    */ {
/*    */   private int timeSinceStart;
/* 13 */   private int maximumTime = 8;
/*    */   
/*    */   protected EntityHugeExplodeFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double p_i1214_8_, double p_i1214_10_, double p_i1214_12_)
/*    */   {
/* 17 */     super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void renderParticle(WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_) {}
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void onUpdate()
/*    */   {
/* 32 */     for (int i = 0; i < 6; i++)
/*    */     {
/* 34 */       double d0 = this.posX + (this.rand.nextDouble() - this.rand.nextDouble()) * 4.0D;
/* 35 */       double d1 = this.posY + (this.rand.nextDouble() - this.rand.nextDouble()) * 4.0D;
/* 36 */       double d2 = this.posZ + (this.rand.nextDouble() - this.rand.nextDouble()) * 4.0D;
/* 37 */       this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, d0, d1, d2, this.timeSinceStart / this.maximumTime, 0.0D, 0.0D, new int[0]);
/*    */     }
/*    */     
/* 40 */     this.timeSinceStart += 1;
/*    */     
/* 42 */     if (this.timeSinceStart == this.maximumTime)
/*    */     {
/* 44 */       setDead();
/*    */     }
/*    */   }
/*    */   
/*    */   public int getFXLayer()
/*    */   {
/* 50 */     return 1;
/*    */   }
/*    */   
/*    */   public static class Factory implements IParticleFactory
/*    */   {
/*    */     public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
/*    */     {
/* 57 */       return new EntityHugeExplodeFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\particle\EntityHugeExplodeFX.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */