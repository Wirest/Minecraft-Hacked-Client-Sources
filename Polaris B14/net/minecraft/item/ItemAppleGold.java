/*    */ package net.minecraft.item;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.creativetab.CreativeTabs;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.potion.Potion;
/*    */ import net.minecraft.potion.PotionEffect;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class ItemAppleGold extends ItemFood
/*    */ {
/*    */   public ItemAppleGold(int amount, float saturation, boolean isWolfFood)
/*    */   {
/* 14 */     super(amount, saturation, isWolfFood);
/* 15 */     setHasSubtypes(true);
/*    */   }
/*    */   
/*    */   public boolean hasEffect(ItemStack stack)
/*    */   {
/* 20 */     return stack.getMetadata() > 0;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public EnumRarity getRarity(ItemStack stack)
/*    */   {
/* 28 */     return stack.getMetadata() == 0 ? EnumRarity.RARE : EnumRarity.EPIC;
/*    */   }
/*    */   
/*    */   protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player)
/*    */   {
/* 33 */     if (!worldIn.isRemote)
/*    */     {
/* 35 */       player.addPotionEffect(new PotionEffect(Potion.absorption.id, 2400, 0));
/*    */     }
/*    */     
/* 38 */     if (stack.getMetadata() > 0)
/*    */     {
/* 40 */       if (!worldIn.isRemote)
/*    */       {
/* 42 */         player.addPotionEffect(new PotionEffect(Potion.regeneration.id, 600, 4));
/* 43 */         player.addPotionEffect(new PotionEffect(Potion.resistance.id, 6000, 0));
/* 44 */         player.addPotionEffect(new PotionEffect(Potion.fireResistance.id, 6000, 0));
/*    */       }
/*    */       
/*    */     }
/*    */     else {
/* 49 */       super.onFoodEaten(stack, worldIn, player);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
/*    */   {
/* 58 */     subItems.add(new ItemStack(itemIn, 1, 0));
/* 59 */     subItems.add(new ItemStack(itemIn, 1, 1));
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\item\ItemAppleGold.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */