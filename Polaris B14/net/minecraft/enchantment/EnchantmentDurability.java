/*    */ package net.minecraft.enchantment;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.item.ItemArmor;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class EnchantmentDurability extends Enchantment
/*    */ {
/*    */   protected EnchantmentDurability(int enchID, ResourceLocation enchName, int enchWeight)
/*    */   {
/* 12 */     super(enchID, enchName, enchWeight, EnumEnchantmentType.BREAKABLE);
/* 13 */     setName("durability");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getMinEnchantability(int enchantmentLevel)
/*    */   {
/* 21 */     return 5 + (enchantmentLevel - 1) * 8;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getMaxEnchantability(int enchantmentLevel)
/*    */   {
/* 29 */     return super.getMinEnchantability(enchantmentLevel) + 50;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getMaxLevel()
/*    */   {
/* 37 */     return 3;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean canApply(ItemStack stack)
/*    */   {
/* 45 */     return stack.isItemStackDamageable() ? true : super.canApply(stack);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static boolean negateDamage(ItemStack p_92097_0_, int p_92097_1_, Random p_92097_2_)
/*    */   {
/* 55 */     return (!(p_92097_0_.getItem() instanceof ItemArmor)) || (p_92097_2_.nextFloat() >= 0.6F);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\enchantment\EnchantmentDurability.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */