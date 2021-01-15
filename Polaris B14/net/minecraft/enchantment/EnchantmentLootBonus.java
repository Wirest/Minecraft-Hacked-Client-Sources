/*    */ package net.minecraft.enchantment;
/*    */ 
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class EnchantmentLootBonus extends Enchantment
/*    */ {
/*    */   protected EnchantmentLootBonus(int p_i45767_1_, ResourceLocation p_i45767_2_, int p_i45767_3_, EnumEnchantmentType p_i45767_4_)
/*    */   {
/*  9 */     super(p_i45767_1_, p_i45767_2_, p_i45767_3_, p_i45767_4_);
/*    */     
/* 11 */     if (p_i45767_4_ == EnumEnchantmentType.DIGGER)
/*    */     {
/* 13 */       setName("lootBonusDigger");
/*    */     }
/* 15 */     else if (p_i45767_4_ == EnumEnchantmentType.FISHING_ROD)
/*    */     {
/* 17 */       setName("lootBonusFishing");
/*    */     }
/*    */     else
/*    */     {
/* 21 */       setName("lootBonus");
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getMinEnchantability(int enchantmentLevel)
/*    */   {
/* 30 */     return 15 + (enchantmentLevel - 1) * 9;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getMaxEnchantability(int enchantmentLevel)
/*    */   {
/* 38 */     return super.getMinEnchantability(enchantmentLevel) + 50;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getMaxLevel()
/*    */   {
/* 46 */     return 3;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean canApplyTogether(Enchantment ench)
/*    */   {
/* 54 */     return (super.canApplyTogether(ench)) && (ench.effectId != silkTouch.effectId);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\enchantment\EnchantmentLootBonus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */