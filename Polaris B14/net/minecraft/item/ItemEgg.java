/*    */ package net.minecraft.item;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.creativetab.CreativeTabs;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.entity.player.PlayerCapabilities;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class ItemEgg extends Item
/*    */ {
/*    */   public ItemEgg()
/*    */   {
/* 13 */     this.maxStackSize = 16;
/* 14 */     setCreativeTab(CreativeTabs.tabMaterials);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
/*    */   {
/* 22 */     if (!playerIn.capabilities.isCreativeMode)
/*    */     {
/* 24 */       itemStackIn.stackSize -= 1;
/*    */     }
/*    */     
/* 27 */     worldIn.playSoundAtEntity(playerIn, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
/*    */     
/* 29 */     if (!worldIn.isRemote)
/*    */     {
/* 31 */       worldIn.spawnEntityInWorld(new net.minecraft.entity.projectile.EntityEgg(worldIn, playerIn));
/*    */     }
/*    */     
/* 34 */     playerIn.triggerAchievement(net.minecraft.stats.StatList.objectUseStats[Item.getIdFromItem(this)]);
/* 35 */     return itemStackIn;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\item\ItemEgg.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */