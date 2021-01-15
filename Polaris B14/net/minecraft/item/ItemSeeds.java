/*    */ package net.minecraft.item;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.creativetab.CreativeTabs;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class ItemSeeds
/*    */   extends Item
/*    */ {
/*    */   private Block crops;
/*    */   private Block soilBlockID;
/*    */   
/*    */   public ItemSeeds(Block crops, Block soil)
/*    */   {
/* 19 */     this.crops = crops;
/* 20 */     this.soilBlockID = soil;
/* 21 */     setCreativeTab(CreativeTabs.tabMaterials);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
/*    */   {
/* 29 */     if (side != EnumFacing.UP)
/*    */     {
/* 31 */       return false;
/*    */     }
/* 33 */     if (!playerIn.canPlayerEdit(pos.offset(side), side, stack))
/*    */     {
/* 35 */       return false;
/*    */     }
/* 37 */     if ((worldIn.getBlockState(pos).getBlock() == this.soilBlockID) && (worldIn.isAirBlock(pos.up())))
/*    */     {
/* 39 */       worldIn.setBlockState(pos.up(), this.crops.getDefaultState());
/* 40 */       stack.stackSize -= 1;
/* 41 */       return true;
/*    */     }
/*    */     
/*    */ 
/* 45 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\item\ItemSeeds.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */