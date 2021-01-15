/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.BlockLiquid;
/*    */ import net.minecraft.block.material.Material;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class EntityRainFX extends EntityFX
/*    */ {
/*    */   protected EntityRainFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn)
/*    */   {
/* 15 */     super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
/* 16 */     this.motionX *= 0.30000001192092896D;
/* 17 */     this.motionY = (Math.random() * 0.20000000298023224D + 0.10000000149011612D);
/* 18 */     this.motionZ *= 0.30000001192092896D;
/* 19 */     this.particleRed = 1.0F;
/* 20 */     this.particleGreen = 1.0F;
/* 21 */     this.particleBlue = 1.0F;
/* 22 */     setParticleTextureIndex(19 + this.rand.nextInt(4));
/* 23 */     setSize(0.01F, 0.01F);
/* 24 */     this.particleGravity = 0.06F;
/* 25 */     this.particleMaxAge = ((int)(8.0D / (Math.random() * 0.8D + 0.2D)));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void onUpdate()
/*    */   {
/* 33 */     this.prevPosX = this.posX;
/* 34 */     this.prevPosY = this.posY;
/* 35 */     this.prevPosZ = this.posZ;
/* 36 */     this.motionY -= this.particleGravity;
/* 37 */     moveEntity(this.motionX, this.motionY, this.motionZ);
/* 38 */     this.motionX *= 0.9800000190734863D;
/* 39 */     this.motionY *= 0.9800000190734863D;
/* 40 */     this.motionZ *= 0.9800000190734863D;
/*    */     
/* 42 */     if (this.particleMaxAge-- <= 0)
/*    */     {
/* 44 */       setDead();
/*    */     }
/*    */     
/* 47 */     if (this.onGround)
/*    */     {
/* 49 */       if (Math.random() < 0.5D)
/*    */       {
/* 51 */         setDead();
/*    */       }
/*    */       
/* 54 */       this.motionX *= 0.699999988079071D;
/* 55 */       this.motionZ *= 0.699999988079071D;
/*    */     }
/*    */     
/* 58 */     BlockPos blockpos = new BlockPos(this);
/* 59 */     IBlockState iblockstate = this.worldObj.getBlockState(blockpos);
/* 60 */     Block block = iblockstate.getBlock();
/* 61 */     block.setBlockBoundsBasedOnState(this.worldObj, blockpos);
/* 62 */     Material material = iblockstate.getBlock().getMaterial();
/*    */     
/* 64 */     if ((material.isLiquid()) || (material.isSolid()))
/*    */     {
/* 66 */       double d0 = 0.0D;
/*    */       
/* 68 */       if ((iblockstate.getBlock() instanceof BlockLiquid))
/*    */       {
/* 70 */         d0 = 1.0F - BlockLiquid.getLiquidHeightPercent(((Integer)iblockstate.getValue(BlockLiquid.LEVEL)).intValue());
/*    */       }
/*    */       else
/*    */       {
/* 74 */         d0 = block.getBlockBoundsMaxY();
/*    */       }
/*    */       
/* 77 */       double d1 = net.minecraft.util.MathHelper.floor_double(this.posY) + d0;
/*    */       
/* 79 */       if (this.posY < d1)
/*    */       {
/* 81 */         setDead();
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public static class Factory implements IParticleFactory
/*    */   {
/*    */     public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
/*    */     {
/* 90 */       return new EntityRainFX(worldIn, xCoordIn, yCoordIn, zCoordIn);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\particle\EntityRainFX.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */