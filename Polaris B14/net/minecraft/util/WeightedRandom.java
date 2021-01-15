/*    */ package net.minecraft.util;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.Random;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WeightedRandom
/*    */ {
/*    */   public static int getTotalWeight(Collection<? extends Item> collection)
/*    */   {
/* 13 */     int i = 0;
/*    */     
/* 15 */     for (Item weightedrandom$item : collection)
/*    */     {
/* 17 */       i += weightedrandom$item.itemWeight;
/*    */     }
/*    */     
/* 20 */     return i;
/*    */   }
/*    */   
/*    */   public static <T extends Item> T getRandomItem(Random random, Collection<T> collection, int totalWeight)
/*    */   {
/* 25 */     if (totalWeight <= 0)
/*    */     {
/* 27 */       throw new IllegalArgumentException();
/*    */     }
/*    */     
/*    */ 
/* 31 */     int i = random.nextInt(totalWeight);
/* 32 */     return getRandomItem(collection, i);
/*    */   }
/*    */   
/*    */ 
/*    */   public static <T extends Item> T getRandomItem(Collection<T> collection, int weight)
/*    */   {
/* 38 */     for (T t : collection)
/*    */     {
/* 40 */       weight -= t.itemWeight;
/*    */       
/* 42 */       if (weight < 0)
/*    */       {
/* 44 */         return t;
/*    */       }
/*    */     }
/*    */     
/* 48 */     return null;
/*    */   }
/*    */   
/*    */   public static <T extends Item> T getRandomItem(Random random, Collection<T> collection)
/*    */   {
/* 53 */     return getRandomItem(random, collection, getTotalWeight(collection));
/*    */   }
/*    */   
/*    */   public static class Item
/*    */   {
/*    */     protected int itemWeight;
/*    */     
/*    */     public Item(int itemWeightIn)
/*    */     {
/* 62 */       this.itemWeight = itemWeightIn;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\util\WeightedRandom.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */