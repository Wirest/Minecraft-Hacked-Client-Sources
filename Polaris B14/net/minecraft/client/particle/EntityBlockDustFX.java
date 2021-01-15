/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class EntityBlockDustFX extends EntityDiggingFX
/*    */ {
/*    */   protected EntityBlockDustFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, IBlockState state)
/*    */   {
/* 11 */     super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, state);
/* 12 */     this.motionX = xSpeedIn;
/* 13 */     this.motionY = ySpeedIn;
/* 14 */     this.motionZ = zSpeedIn;
/*    */   }
/*    */   
/*    */   public static class Factory implements IParticleFactory
/*    */   {
/*    */     public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
/*    */     {
/* 21 */       IBlockState iblockstate = Block.getStateById(p_178902_15_[0]);
/* 22 */       return iblockstate.getBlock().getRenderType() == -1 ? null : new EntityBlockDustFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, iblockstate).func_174845_l();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\particle\EntityBlockDustFX.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */