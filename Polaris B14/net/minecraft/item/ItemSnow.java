/*    */ package net.minecraft.item;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.Block.SoundType;
/*    */ import net.minecraft.block.BlockSnow;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class ItemSnow extends ItemBlock
/*    */ {
/*    */   public ItemSnow(Block block)
/*    */   {
/* 16 */     super(block);
/* 17 */     setMaxDamage(0);
/* 18 */     setHasSubtypes(true);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
/*    */   {
/* 26 */     if (stack.stackSize == 0)
/*    */     {
/* 28 */       return false;
/*    */     }
/* 30 */     if (!playerIn.canPlayerEdit(pos, side, stack))
/*    */     {
/* 32 */       return false;
/*    */     }
/*    */     
/*    */ 
/* 36 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/* 37 */     Block block = iblockstate.getBlock();
/* 38 */     BlockPos blockpos = pos;
/*    */     
/* 40 */     if (((side != EnumFacing.UP) || (block != this.block)) && (!block.isReplaceable(worldIn, pos)))
/*    */     {
/* 42 */       blockpos = pos.offset(side);
/* 43 */       iblockstate = worldIn.getBlockState(blockpos);
/* 44 */       block = iblockstate.getBlock();
/*    */     }
/*    */     
/* 47 */     if (block == this.block)
/*    */     {
/* 49 */       int i = ((Integer)iblockstate.getValue(BlockSnow.LAYERS)).intValue();
/*    */       
/* 51 */       if (i <= 7)
/*    */       {
/* 53 */         IBlockState iblockstate1 = iblockstate.withProperty(BlockSnow.LAYERS, Integer.valueOf(i + 1));
/* 54 */         net.minecraft.util.AxisAlignedBB axisalignedbb = this.block.getCollisionBoundingBox(worldIn, blockpos, iblockstate1);
/*    */         
/* 56 */         if ((axisalignedbb != null) && (worldIn.checkNoEntityCollision(axisalignedbb)) && (worldIn.setBlockState(blockpos, iblockstate1, 2)))
/*    */         {
/* 58 */           worldIn.playSoundEffect(blockpos.getX() + 0.5F, blockpos.getY() + 0.5F, blockpos.getZ() + 0.5F, this.block.stepSound.getPlaceSound(), (this.block.stepSound.getVolume() + 1.0F) / 2.0F, this.block.stepSound.getFrequency() * 0.8F);
/* 59 */           stack.stackSize -= 1;
/* 60 */           return true;
/*    */         }
/*    */       }
/*    */     }
/*    */     
/* 65 */     return super.onItemUse(stack, playerIn, worldIn, blockpos, side, hitX, hitY, hitZ);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getMetadata(int damage)
/*    */   {
/* 75 */     return damage;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\item\ItemSnow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */