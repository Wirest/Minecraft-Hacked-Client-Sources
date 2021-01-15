/*    */ package net.minecraft.enchantment;
/*    */ 
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class EnchantmentDigging extends Enchantment
/*    */ {
/*    */   protected EnchantmentDigging(int enchID, ResourceLocation enchName, int enchWeight)
/*    */   {
/* 11 */     super(enchID, enchName, enchWeight, EnumEnchantmentType.DIGGER);
/* 12 */     setName("digging");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getMinEnchantability(int enchantmentLevel)
/*    */   {
/* 20 */     return 1 + 10 * (enchantmentLevel - 1);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getMaxEnchantability(int enchantmentLevel)
/*    */   {
/* 28 */     return super.getMinEnchantability(enchantmentLevel) + 50;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getMaxLevel()
/*    */   {
/* 36 */     return 5;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean canApply(ItemStack stack)
/*    */   {
/* 44 */     return stack.getItem() == Items.shears ? true : super.canApply(stack);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\enchantment\EnchantmentDigging.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */