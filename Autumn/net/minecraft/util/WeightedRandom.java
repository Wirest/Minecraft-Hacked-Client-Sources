package net.minecraft.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

public class WeightedRandom {
   public static int getTotalWeight(Collection collection) {
      int i = 0;

      WeightedRandom.Item weightedrandom$item;
      for(Iterator var2 = collection.iterator(); var2.hasNext(); i += weightedrandom$item.itemWeight) {
         weightedrandom$item = (WeightedRandom.Item)var2.next();
      }

      return i;
   }

   public static WeightedRandom.Item getRandomItem(Random random, Collection collection, int totalWeight) {
      if (totalWeight <= 0) {
         throw new IllegalArgumentException();
      } else {
         int i = random.nextInt(totalWeight);
         return getRandomItem(collection, i);
      }
   }

   public static WeightedRandom.Item getRandomItem(Collection collection, int weight) {
      Iterator var2 = collection.iterator();

      WeightedRandom.Item t;
      do {
         if (!var2.hasNext()) {
            return (WeightedRandom.Item)null;
         }

         t = (WeightedRandom.Item)var2.next();
         weight -= t.itemWeight;
      } while(weight >= 0);

      return t;
   }

   public static WeightedRandom.Item getRandomItem(Random random, Collection collection) {
      return getRandomItem(random, collection, getTotalWeight(collection));
   }

   public static class Item {
      protected int itemWeight;

      public Item(int itemWeightIn) {
         this.itemWeight = itemWeightIn;
      }
   }
}
