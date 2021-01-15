/*    */ package net.minecraft.block;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.block.material.MapColor;
/*    */ import net.minecraft.block.material.Material;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.creativetab.CreativeTabs;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.util.AxisAlignedBB;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.EnumWorldBlockLayer;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class BlockBush extends Block
/*    */ {
/*    */   protected BlockBush()
/*    */   {
/* 18 */     this(Material.plants);
/*    */   }
/*    */   
/*    */   protected BlockBush(Material materialIn)
/*    */   {
/* 23 */     this(materialIn, materialIn.getMaterialMapColor());
/*    */   }
/*    */   
/*    */   protected BlockBush(Material p_i46452_1_, MapColor p_i46452_2_)
/*    */   {
/* 28 */     super(p_i46452_1_, p_i46452_2_);
/* 29 */     setTickRandomly(true);
/* 30 */     float f = 0.2F;
/* 31 */     setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 3.0F, 0.5F + f);
/* 32 */     setCreativeTab(CreativeTabs.tabDecorations);
/*    */   }
/*    */   
/*    */   public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
/*    */   {
/* 37 */     return (super.canPlaceBlockAt(worldIn, pos)) && (canPlaceBlockOn(worldIn.getBlockState(pos.down()).getBlock()));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected boolean canPlaceBlockOn(Block ground)
/*    */   {
/* 45 */     return (ground == Blocks.grass) || (ground == Blocks.dirt) || (ground == Blocks.farmland);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
/*    */   {
/* 53 */     super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
/* 54 */     checkAndDropBlock(worldIn, pos, state);
/*    */   }
/*    */   
/*    */   public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
/*    */   {
/* 59 */     checkAndDropBlock(worldIn, pos, state);
/*    */   }
/*    */   
/*    */   protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state)
/*    */   {
/* 64 */     if (!canBlockStay(worldIn, pos, state))
/*    */     {
/* 66 */       dropBlockAsItem(worldIn, pos, state, 0);
/* 67 */       worldIn.setBlockState(pos, Blocks.air.getDefaultState(), 3);
/*    */     }
/*    */   }
/*    */   
/*    */   public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
/*    */   {
/* 73 */     return canPlaceBlockOn(worldIn.getBlockState(pos.down()).getBlock());
/*    */   }
/*    */   
/*    */   public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
/*    */   {
/* 78 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean isOpaqueCube()
/*    */   {
/* 86 */     return false;
/*    */   }
/*    */   
/*    */   public boolean isFullCube()
/*    */   {
/* 91 */     return false;
/*    */   }
/*    */   
/*    */   public EnumWorldBlockLayer getBlockLayer()
/*    */   {
/* 96 */     return EnumWorldBlockLayer.CUTOUT;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockBush.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */