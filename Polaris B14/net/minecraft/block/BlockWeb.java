/*    */ package net.minecraft.block;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.block.material.Material;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.creativetab.CreativeTabs;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.util.AxisAlignedBB;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.EnumWorldBlockLayer;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class BlockWeb extends Block
/*    */ {
/*    */   public BlockWeb()
/*    */   {
/* 19 */     super(Material.web);
/* 20 */     setCreativeTab(CreativeTabs.tabDecorations);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
/*    */   {
/* 28 */     entityIn.setInWeb();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean isOpaqueCube()
/*    */   {
/* 36 */     return false;
/*    */   }
/*    */   
/*    */   public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
/*    */   {
/* 41 */     return null;
/*    */   }
/*    */   
/*    */   public boolean isFullCube()
/*    */   {
/* 46 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public Item getItemDropped(IBlockState state, Random rand, int fortune)
/*    */   {
/* 54 */     return Items.string;
/*    */   }
/*    */   
/*    */   protected boolean canSilkHarvest()
/*    */   {
/* 59 */     return true;
/*    */   }
/*    */   
/*    */   public EnumWorldBlockLayer getBlockLayer()
/*    */   {
/* 64 */     return EnumWorldBlockLayer.CUTOUT;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockWeb.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */