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
/*    */ public class ItemFlintAndSteel extends Item
/*    */ {
/*    */   public ItemFlintAndSteel()
/*    */   {
/* 15 */     this.maxStackSize = 1;
/* 16 */     setMaxDamage(64);
/* 17 */     setCreativeTab(CreativeTabs.tabTools);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
/*    */   {
/* 25 */     pos = pos.offset(side);
/*    */     
/* 27 */     if (!playerIn.canPlayerEdit(pos, side, stack))
/*    */     {
/* 29 */       return false;
/*    */     }
/*    */     
/*    */ 
/* 33 */     if (worldIn.getBlockState(pos).getBlock().getMaterial() == net.minecraft.block.material.Material.air)
/*    */     {
/* 35 */       worldIn.playSoundEffect(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, "fire.ignite", 1.0F, itemRand.nextFloat() * 0.4F + 0.8F);
/* 36 */       worldIn.setBlockState(pos, net.minecraft.init.Blocks.fire.getDefaultState());
/*    */     }
/*    */     
/* 39 */     stack.damageItem(1, playerIn);
/* 40 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\item\ItemFlintAndSteel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */