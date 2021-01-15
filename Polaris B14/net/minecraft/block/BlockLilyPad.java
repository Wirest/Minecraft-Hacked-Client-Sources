/*    */ package net.minecraft.block;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.block.material.Material;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.creativetab.CreativeTabs;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.item.EntityBoat;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.util.AxisAlignedBB;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.world.IBlockAccess;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class BlockLilyPad extends BlockBush
/*    */ {
/*    */   protected BlockLilyPad()
/*    */   {
/* 19 */     float f = 0.5F;
/* 20 */     float f1 = 0.015625F;
/* 21 */     setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f1, 0.5F + f);
/* 22 */     setCreativeTab(CreativeTabs.tabDecorations);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity)
/*    */   {
/* 30 */     if ((collidingEntity == null) || (!(collidingEntity instanceof EntityBoat)))
/*    */     {
/* 32 */       super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
/*    */     }
/*    */   }
/*    */   
/*    */   public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
/*    */   {
/* 38 */     return new AxisAlignedBB(pos.getX() + this.minX, pos.getY() + this.minY, pos.getZ() + this.minZ, pos.getX() + this.maxX, pos.getY() + this.maxY, pos.getZ() + this.maxZ);
/*    */   }
/*    */   
/*    */   public int getBlockColor()
/*    */   {
/* 43 */     return 7455580;
/*    */   }
/*    */   
/*    */   public int getRenderColor(IBlockState state)
/*    */   {
/* 48 */     return 7455580;
/*    */   }
/*    */   
/*    */   public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass)
/*    */   {
/* 53 */     return 2129968;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected boolean canPlaceBlockOn(Block ground)
/*    */   {
/* 61 */     return ground == Blocks.water;
/*    */   }
/*    */   
/*    */   public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
/*    */   {
/* 66 */     if ((pos.getY() >= 0) && (pos.getY() < 256))
/*    */     {
/* 68 */       IBlockState iblockstate = worldIn.getBlockState(pos.down());
/* 69 */       return (iblockstate.getBlock().getMaterial() == Material.water) && (((Integer)iblockstate.getValue(BlockLiquid.LEVEL)).intValue() == 0);
/*    */     }
/*    */     
/*    */ 
/* 73 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getMetaFromState(IBlockState state)
/*    */   {
/* 82 */     return 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockLilyPad.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */