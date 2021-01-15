/*    */ package net.minecraft.block;
/*    */ 
/*    */ import net.minecraft.block.material.Material;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.util.AxisAlignedBB;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class BlockAir extends Block
/*    */ {
/*    */   protected BlockAir()
/*    */   {
/* 13 */     super(Material.air);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getRenderType()
/*    */   {
/* 21 */     return -1;
/*    */   }
/*    */   
/*    */   public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
/*    */   {
/* 26 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean isOpaqueCube()
/*    */   {
/* 34 */     return false;
/*    */   }
/*    */   
/*    */   public boolean canCollideCheck(IBlockState state, boolean hitIfLiquid)
/*    */   {
/* 39 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {}
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean isReplaceable(World worldIn, BlockPos pos)
/*    */   {
/* 54 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockAir.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */