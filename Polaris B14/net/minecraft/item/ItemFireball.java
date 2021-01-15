/*    */ package net.minecraft.item;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.block.BlockFire;
/*    */ import net.minecraft.creativetab.CreativeTabs;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class ItemFireball extends Item
/*    */ {
/*    */   public ItemFireball()
/*    */   {
/* 15 */     setCreativeTab(CreativeTabs.tabMisc);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
/*    */   {
/* 23 */     if (worldIn.isRemote)
/*    */     {
/* 25 */       return true;
/*    */     }
/*    */     
/*    */ 
/* 29 */     pos = pos.offset(side);
/*    */     
/* 31 */     if (!playerIn.canPlayerEdit(pos, side, stack))
/*    */     {
/* 33 */       return false;
/*    */     }
/*    */     
/*    */ 
/* 37 */     if (worldIn.getBlockState(pos).getBlock().getMaterial() == net.minecraft.block.material.Material.air)
/*    */     {
/* 39 */       worldIn.playSoundEffect(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, "item.fireCharge.use", 1.0F, (itemRand.nextFloat() - itemRand.nextFloat()) * 0.2F + 1.0F);
/* 40 */       worldIn.setBlockState(pos, net.minecraft.init.Blocks.fire.getDefaultState());
/*    */     }
/*    */     
/* 43 */     if (!playerIn.capabilities.isCreativeMode)
/*    */     {
/* 45 */       stack.stackSize -= 1;
/*    */     }
/*    */     
/* 48 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\item\ItemFireball.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */