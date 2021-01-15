/*    */ package net.minecraft.enchantment;
/*    */ 
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class EnchantmentArrowFire extends Enchantment
/*    */ {
/*    */   public EnchantmentArrowFire(int enchID, ResourceLocation enchName, int enchWeight)
/*    */   {
/*  9 */     super(enchID, enchName, enchWeight, EnumEnchantmentType.BOW);
/* 10 */     setName("arrowFire");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getMinEnchantability(int enchantmentLevel)
/*    */   {
/* 18 */     return 20;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getMaxEnchantability(int enchantmentLevel)
/*    */   {
/* 26 */     return 50;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getMaxLevel()
/*    */   {
/* 34 */     return 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\enchantment\EnchantmentArrowFire.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */