/*    */ package net.minecraft.item;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.creativetab.CreativeTabs;
/*    */ import net.minecraft.entity.item.EntityExpBottle;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class ItemExpBottle extends Item
/*    */ {
/*    */   public ItemExpBottle()
/*    */   {
/* 13 */     setCreativeTab(CreativeTabs.tabMisc);
/*    */   }
/*    */   
/*    */   public boolean hasEffect(ItemStack stack)
/*    */   {
/* 18 */     return true;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
/*    */   {
/* 26 */     if (!playerIn.capabilities.isCreativeMode)
/*    */     {
/* 28 */       itemStackIn.stackSize -= 1;
/*    */     }
/*    */     
/* 31 */     worldIn.playSoundAtEntity(playerIn, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
/*    */     
/* 33 */     if (!worldIn.isRemote)
/*    */     {
/* 35 */       worldIn.spawnEntityInWorld(new EntityExpBottle(worldIn, playerIn));
/*    */     }
/*    */     
/* 38 */     playerIn.triggerAchievement(net.minecraft.stats.StatList.objectUseStats[Item.getIdFromItem(this)]);
/* 39 */     return itemStackIn;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\item\ItemExpBottle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */