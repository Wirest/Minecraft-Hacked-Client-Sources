package net.minecraft.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

public class WeightedRandom
{

    /**
     * Returns the total weight of all items in a collection.
     *  
     * @param collection Collection to get the total weight of
     */
    public static int getTotalWeight(Collection collection)
    {
        int var1 = 0;
        WeightedRandom.Item var3;

        for (Iterator var2 = collection.iterator(); var2.hasNext(); var1 += var3.itemWeight)
        {
            var3 = (WeightedRandom.Item)var2.next();
        }

        return var1;
    }

    /**
     * Returns a random choice from the input items, with a total weight value.
     *  
     * @param collection Collection of the input items
     */
    public static WeightedRandom.Item getRandomItem(Random random, Collection collection, int totalWeight)
    {
        if (totalWeight <= 0)
        {
            throw new IllegalArgumentException();
        }
        else
        {
            int var3 = random.nextInt(totalWeight);
            return getRandomItem(collection, var3);
        }
    }

    public static WeightedRandom.Item getRandomItem(Collection collection, int weight)
    {
        Iterator var2 = collection.iterator();
        WeightedRandom.Item var3;

        do
        {
            if (!var2.hasNext())
            {
                return null;
            }

            var3 = (WeightedRandom.Item)var2.next();
            weight -= var3.itemWeight;
        }
        while (weight >= 0);

        return var3;
    }

    /**
     * Returns a random choice from the input items.
     *  
     * @param collection Collection to get the random item from
     */
    public static WeightedRandom.Item getRandomItem(Random random, Collection collection)
    {
        return getRandomItem(random, collection, getTotalWeight(collection));
    }

    public static class Item
    {
        protected int itemWeight;

        public Item(int itemWeightIn)
        {
            this.itemWeight = itemWeightIn;
        }
    }
}
