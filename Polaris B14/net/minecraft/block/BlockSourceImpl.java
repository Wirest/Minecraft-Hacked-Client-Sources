/*    */ package net.minecraft.block;
/*    */ 
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.dispenser.IBlockSource;
/*    */ import net.minecraft.tileentity.TileEntity;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class BlockSourceImpl implements IBlockSource
/*    */ {
/*    */   private final World worldObj;
/*    */   private final BlockPos pos;
/*    */   
/*    */   public BlockSourceImpl(World worldIn, BlockPos posIn)
/*    */   {
/* 16 */     this.worldObj = worldIn;
/* 17 */     this.pos = posIn;
/*    */   }
/*    */   
/*    */   public World getWorld()
/*    */   {
/* 22 */     return this.worldObj;
/*    */   }
/*    */   
/*    */   public double getX()
/*    */   {
/* 27 */     return this.pos.getX() + 0.5D;
/*    */   }
/*    */   
/*    */   public double getY()
/*    */   {
/* 32 */     return this.pos.getY() + 0.5D;
/*    */   }
/*    */   
/*    */   public double getZ()
/*    */   {
/* 37 */     return this.pos.getZ() + 0.5D;
/*    */   }
/*    */   
/*    */   public BlockPos getBlockPos()
/*    */   {
/* 42 */     return this.pos;
/*    */   }
/*    */   
/*    */   public int getBlockMetadata()
/*    */   {
/* 47 */     IBlockState iblockstate = this.worldObj.getBlockState(this.pos);
/* 48 */     return iblockstate.getBlock().getMetaFromState(iblockstate);
/*    */   }
/*    */   
/*    */   public <T extends TileEntity> T getBlockTileEntity()
/*    */   {
/* 53 */     return this.worldObj.getTileEntity(this.pos);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockSourceImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */