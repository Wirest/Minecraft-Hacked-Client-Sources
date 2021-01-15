/*    */ package net.minecraft.util;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.enchantment.EnchantmentHelper;
/*    */ import net.minecraft.item.ItemStack;
/*    */ 
/*    */ public class WeightedRandomFishable extends WeightedRandom.Item
/*    */ {
/*    */   private final ItemStack returnStack;
/*    */   private float maxDamagePercent;
/*    */   private boolean enchantable;
/*    */   
/*    */   public WeightedRandomFishable(ItemStack returnStackIn, int itemWeightIn)
/*    */   {
/* 15 */     super(itemWeightIn);
/* 16 */     this.returnStack = returnStackIn;
/*    */   }
/*    */   
/*    */   public ItemStack getItemStack(Random random)
/*    */   {
/* 21 */     ItemStack itemstack = this.returnStack.copy();
/*    */     
/* 23 */     if (this.maxDamagePercent > 0.0F)
/*    */     {
/* 25 */       int i = (int)(this.maxDamagePercent * this.returnStack.getMaxDamage());
/* 26 */       int j = itemstack.getMaxDamage() - random.nextInt(random.nextInt(i) + 1);
/*    */       
/* 28 */       if (j > i)
/*    */       {
/* 30 */         j = i;
/*    */       }
/*    */       
/* 33 */       if (j < 1)
/*    */       {
/* 35 */         j = 1;
/*    */       }
/*    */       
/* 38 */       itemstack.setItemDamage(j);
/*    */     }
/*    */     
/* 41 */     if (this.enchantable)
/*    */     {
/* 43 */       EnchantmentHelper.addRandomEnchantment(random, itemstack, 30);
/*    */     }
/*    */     
/* 46 */     return itemstack;
/*    */   }
/*    */   
/*    */   public WeightedRandomFishable setMaxDamagePercent(float maxDamagePercentIn)
/*    */   {
/* 51 */     this.maxDamagePercent = maxDamagePercentIn;
/* 52 */     return this;
/*    */   }
/*    */   
/*    */   public WeightedRandomFishable setEnchantable()
/*    */   {
/* 57 */     this.enchantable = true;
/* 58 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\util\WeightedRandomFishable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */