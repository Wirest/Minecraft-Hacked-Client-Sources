/*    */ package net.minecraft.enchantment;
/*    */ 
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class EnchantmentKnockback extends Enchantment
/*    */ {
/*    */   protected EnchantmentKnockback(int p_i45768_1_, ResourceLocation p_i45768_2_, int p_i45768_3_)
/*    */   {
/*  9 */     super(p_i45768_1_, p_i45768_2_, p_i45768_3_, EnumEnchantmentType.WEAPON);
/* 10 */     setName("knockback");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getMinEnchantability(int enchantmentLevel)
/*    */   {
/* 18 */     return 5 + 20 * (enchantmentLevel - 1);
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


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\enchantment\EnchantmentKnockback.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */