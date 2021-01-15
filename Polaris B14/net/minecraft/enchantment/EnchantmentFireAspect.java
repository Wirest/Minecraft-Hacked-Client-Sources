/*    */ package net.minecraft.enchantment;
/*    */ 
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class EnchantmentFireAspect extends Enchantment
/*    */ {
/*    */   protected EnchantmentFireAspect(int enchID, ResourceLocation enchName, int enchWeight)
/*    */   {
/*  9 */     super(enchID, enchName, enchWeight, EnumEnchantmentType.WEAPON);
/* 10 */     setName("fire");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getMinEnchantability(int enchantmentLevel)
/*    */   {
/* 18 */     return 10 + 20 * (enchantmentLevel - 1);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getMaxEnchantability(int enchantmentLevel)
/*    */   {
/* 26 */     return super.getMinEnchantability(enchantmentLevel) + 50;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getMaxLevel()
/*    */   {
/* 34 */     return 2;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\enchantment\EnchantmentFireAspect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */