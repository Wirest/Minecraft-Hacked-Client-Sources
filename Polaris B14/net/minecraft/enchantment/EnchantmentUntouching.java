/*    */ package net.minecraft.enchantment;
/*    */ 
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class EnchantmentUntouching extends Enchantment
/*    */ {
/*    */   protected EnchantmentUntouching(int p_i45763_1_, ResourceLocation p_i45763_2_, int p_i45763_3_)
/*    */   {
/* 11 */     super(p_i45763_1_, p_i45763_2_, p_i45763_3_, EnumEnchantmentType.DIGGER);
/* 12 */     setName("untouching");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getMinEnchantability(int enchantmentLevel)
/*    */   {
/* 20 */     return 15;
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
/* 36 */     return 1;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean canApplyTogether(Enchantment ench)
/*    */   {
/* 44 */     return (super.canApplyTogether(ench)) && (ench.effectId != fortune.effectId);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean canApply(ItemStack stack)
/*    */   {
/* 52 */     return stack.getItem() == Items.shears ? true : super.canApply(stack);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\enchantment\EnchantmentUntouching.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */