/*    */ package net.minecraft.item;
/*    */ 
/*    */ import net.minecraft.creativetab.CreativeTabs;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.entity.player.PlayerCapabilities;
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class ItemBucketMilk extends Item
/*    */ {
/*    */   public ItemBucketMilk()
/*    */   {
/* 13 */     setMaxStackSize(1);
/* 14 */     setCreativeTab(CreativeTabs.tabMisc);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityPlayer playerIn)
/*    */   {
/* 23 */     if (!playerIn.capabilities.isCreativeMode)
/*    */     {
/* 25 */       stack.stackSize -= 1;
/*    */     }
/*    */     
/* 28 */     if (!worldIn.isRemote)
/*    */     {
/* 30 */       playerIn.clearActivePotions();
/*    */     }
/*    */     
/* 33 */     playerIn.triggerAchievement(net.minecraft.stats.StatList.objectUseStats[Item.getIdFromItem(this)]);
/* 34 */     return stack.stackSize <= 0 ? new ItemStack(Items.bucket) : stack;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getMaxItemUseDuration(ItemStack stack)
/*    */   {
/* 42 */     return 32;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public EnumAction getItemUseAction(ItemStack stack)
/*    */   {
/* 50 */     return EnumAction.DRINK;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
/*    */   {
/* 58 */     playerIn.setItemInUse(itemStackIn, getMaxItemUseDuration(itemStackIn));
/* 59 */     return itemStackIn;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\item\ItemBucketMilk.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */