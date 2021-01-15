/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class EntityBubbleFX extends EntityFX
/*    */ {
/*    */   protected EntityBubbleFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn)
/*    */   {
/* 11 */     super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
/* 12 */     this.particleRed = 1.0F;
/* 13 */     this.particleGreen = 1.0F;
/* 14 */     this.particleBlue = 1.0F;
/* 15 */     setParticleTextureIndex(32);
/* 16 */     setSize(0.02F, 0.02F);
/* 17 */     this.particleScale *= (this.rand.nextFloat() * 0.6F + 0.2F);
/* 18 */     this.motionX = (xSpeedIn * 0.20000000298023224D + (Math.random() * 2.0D - 1.0D) * 0.019999999552965164D);
/* 19 */     this.motionY = (ySpeedIn * 0.20000000298023224D + (Math.random() * 2.0D - 1.0D) * 0.019999999552965164D);
/* 20 */     this.motionZ = (zSpeedIn * 0.20000000298023224D + (Math.random() * 2.0D - 1.0D) * 0.019999999552965164D);
/* 21 */     this.particleMaxAge = ((int)(8.0D / (Math.random() * 0.8D + 0.2D)));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void onUpdate()
/*    */   {
/* 29 */     this.prevPosX = this.posX;
/* 30 */     this.prevPosY = this.posY;
/* 31 */     this.prevPosZ = this.posZ;
/* 32 */     this.motionY += 0.002D;
/* 33 */     moveEntity(this.motionX, this.motionY, this.motionZ);
/* 34 */     this.motionX *= 0.8500000238418579D;
/* 35 */     this.motionY *= 0.8500000238418579D;
/* 36 */     this.motionZ *= 0.8500000238418579D;
/*    */     
/* 38 */     if (this.worldObj.getBlockState(new net.minecraft.util.BlockPos(this)).getBlock().getMaterial() != net.minecraft.block.material.Material.water)
/*    */     {
/* 40 */       setDead();
/*    */     }
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
/* 53 */       return new EntityBubbleFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\particle\EntityBubbleFX.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */