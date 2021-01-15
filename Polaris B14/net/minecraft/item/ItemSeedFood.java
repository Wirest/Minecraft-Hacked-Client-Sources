/*    */ package net.minecraft.item;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class ItemSeedFood
/*    */   extends ItemFood
/*    */ {
/*    */   private Block crops;
/*    */   private Block soilId;
/*    */   
/*    */   public ItemSeedFood(int healAmount, float saturation, Block crops, Block soil)
/*    */   {
/* 18 */     super(healAmount, saturation, false);
/* 19 */     this.crops = crops;
/* 20 */     this.soilId = soil;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
/*    */   {
/* 28 */     if (side != EnumFacing.UP)
/*    */     {
/* 30 */       return false;
/*    */     }
/* 32 */     if (!playerIn.canPlayerEdit(pos.offset(side), side, stack))
/*    */     {
/* 34 */       return false;
/*    */     }
/* 36 */     if ((worldIn.getBlockState(pos).getBlock() == this.soilId) && (worldIn.isAirBlock(pos.up())))
/*    */     {
/* 38 */       worldIn.setBlockState(pos.up(), this.crops.getDefaultState());
/* 39 */       stack.stackSize -= 1;
/* 40 */       return true;
/*    */     }
/*    */     
/*    */ 
/* 44 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\item\ItemSeedFood.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */