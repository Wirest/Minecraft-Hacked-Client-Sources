/*    */ package net.minecraft.item;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.Block.SoundType;
/*    */ import net.minecraft.block.BlockSnow;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class ItemReed extends Item
/*    */ {
/*    */   private Block block;
/*    */   
/*    */   public ItemReed(Block block)
/*    */   {
/* 19 */     this.block = block;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
/*    */   {
/* 27 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/* 28 */     Block block = iblockstate.getBlock();
/*    */     
/* 30 */     if ((block == Blocks.snow_layer) && (((Integer)iblockstate.getValue(BlockSnow.LAYERS)).intValue() < 1))
/*    */     {
/* 32 */       side = EnumFacing.UP;
/*    */     }
/* 34 */     else if (!block.isReplaceable(worldIn, pos))
/*    */     {
/* 36 */       pos = pos.offset(side);
/*    */     }
/*    */     
/* 39 */     if (!playerIn.canPlayerEdit(pos, side, stack))
/*    */     {
/* 41 */       return false;
/*    */     }
/* 43 */     if (stack.stackSize == 0)
/*    */     {
/* 45 */       return false;
/*    */     }
/*    */     
/*    */ 
/* 49 */     if (worldIn.canBlockBePlaced(this.block, pos, false, side, null, stack))
/*    */     {
/* 51 */       IBlockState iblockstate1 = this.block.onBlockPlaced(worldIn, pos, side, hitX, hitY, hitZ, 0, playerIn);
/*    */       
/* 53 */       if (worldIn.setBlockState(pos, iblockstate1, 3))
/*    */       {
/* 55 */         iblockstate1 = worldIn.getBlockState(pos);
/*    */         
/* 57 */         if (iblockstate1.getBlock() == this.block)
/*    */         {
/* 59 */           ItemBlock.setTileEntityNBT(worldIn, playerIn, pos, stack);
/* 60 */           iblockstate1.getBlock().onBlockPlacedBy(worldIn, pos, iblockstate1, playerIn, stack);
/*    */         }
/*    */         
/* 63 */         worldIn.playSoundEffect(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, this.block.stepSound.getPlaceSound(), (this.block.stepSound.getVolume() + 1.0F) / 2.0F, this.block.stepSound.getFrequency() * 0.8F);
/* 64 */         stack.stackSize -= 1;
/* 65 */         return true;
/*    */       }
/*    */     }
/*    */     
/* 69 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\item\ItemReed.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */