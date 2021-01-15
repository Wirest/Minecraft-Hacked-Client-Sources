/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class EntityEnchantmentTableParticleFX extends EntityFX
/*    */ {
/*    */   private float field_70565_a;
/*    */   private double coordX;
/*    */   private double coordY;
/*    */   private double coordZ;
/*    */   
/*    */   protected EntityEnchantmentTableParticleFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn)
/*    */   {
/* 14 */     super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
/* 15 */     this.motionX = xSpeedIn;
/* 16 */     this.motionY = ySpeedIn;
/* 17 */     this.motionZ = zSpeedIn;
/* 18 */     this.coordX = xCoordIn;
/* 19 */     this.coordY = yCoordIn;
/* 20 */     this.coordZ = zCoordIn;
/* 21 */     this.posX = (this.prevPosX = xCoordIn + xSpeedIn);
/* 22 */     this.posY = (this.prevPosY = yCoordIn + ySpeedIn);
/* 23 */     this.posZ = (this.prevPosZ = zCoordIn + zSpeedIn);
/* 24 */     float f = this.rand.nextFloat() * 0.6F + 0.4F;
/* 25 */     this.field_70565_a = (this.particleScale = this.rand.nextFloat() * 0.5F + 0.2F);
/* 26 */     this.particleRed = (this.particleGreen = this.particleBlue = 1.0F * f);
/* 27 */     this.particleGreen *= 0.9F;
/* 28 */     this.particleRed *= 0.9F;
/* 29 */     this.particleMaxAge = ((int)(Math.random() * 10.0D) + 30);
/* 30 */     this.noClip = true;
/* 31 */     setParticleTextureIndex((int)(Math.random() * 26.0D + 1.0D + 224.0D));
/*    */   }
/*    */   
/*    */   public int getBrightnessForRender(float partialTicks)
/*    */   {
/* 36 */     int i = super.getBrightnessForRender(partialTicks);
/* 37 */     float f = this.particleAge / this.particleMaxAge;
/* 38 */     f *= f;
/* 39 */     f *= f;
/* 40 */     int j = i & 0xFF;
/* 41 */     int k = i >> 16 & 0xFF;
/* 42 */     k += (int)(f * 15.0F * 16.0F);
/*    */     
/* 44 */     if (k > 240)
/*    */     {
/* 46 */       k = 240;
/*    */     }
/*    */     
/* 49 */     return j | k << 16;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public float getBrightness(float partialTicks)
/*    */   {
/* 57 */     float f = super.getBrightness(partialTicks);
/* 58 */     float f1 = this.particleAge / this.particleMaxAge;
/* 59 */     f1 *= f1;
/* 60 */     f1 *= f1;
/* 61 */     return f * (1.0F - f1) + f1;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void onUpdate()
/*    */   {
/* 69 */     this.prevPosX = this.posX;
/* 70 */     this.prevPosY = this.posY;
/* 71 */     this.prevPosZ = this.posZ;
/* 72 */     float f = this.particleAge / this.particleMaxAge;
/* 73 */     f = 1.0F - f;
/* 74 */     float f1 = 1.0F - f;
/* 75 */     f1 *= f1;
/* 76 */     f1 *= f1;
/* 77 */     this.posX = (this.coordX + this.motionX * f);
/* 78 */     this.posY = (this.coordY + this.motionY * f - f1 * 1.2F);
/* 79 */     this.posZ = (this.coordZ + this.motionZ * f);
/*    */     
/* 81 */     if (this.particleAge++ >= this.particleMaxAge)
/*    */     {
/* 83 */       setDead();
/*    */     }
/*    */   }
/*    */   
/*    */   public static class EnchantmentTable implements IParticleFactory
/*    */   {
/*    */     public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
/*    */     {
/* 91 */       return new EntityEnchantmentTableParticleFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\particle\EntityEnchantmentTableParticleFX.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */