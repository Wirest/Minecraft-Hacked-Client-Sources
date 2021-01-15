package net.minecraft.util;

public class IntHashMap
{
    /** An array of HashEntries representing the heads of hash slot lists */
    private transient IntHashMap.Entry[] slots = new IntHashMap.Entry[16];

    /** The number of items stored in this map */
    private transient int count;

    /** The grow threshold */
    private int threshold = 12;

    /** The scale factor used to determine when to grow the table */
    private final float growFactor = 0.75F;
    private static final String __OBFID = "CL_00001490";

    /**
     * Makes the passed in integer suitable for hashing by a number of shifts
     */
    private static int computeHash(int p_76044_0_)
    {
        p_76044_0_ ^= p_76044_0_ >>> 20 ^ p_76044_0_ >>> 12;
        return p_76044_0_ ^ p_76044_0_ >>> 7 ^ p_76044_0_ >>> 4;
    }

    /**
     * Computes the index of the slot for the hash and slot count passed in.
     */
    private static int getSlotIndex(int p_76043_0_, int p_76043_1_)
    {
        return p_76043_0_ & p_76043_1_ - 1;
    }

    /**
     * Returns the object associated to a key
     */
    public Object lookup(int p_76041_1_)
    {
        int var2 = computeHash(p_76041_1_);

        for (IntHashMap.Entry var3 = this.slots[getSlotIndex(var2, this.slots.length)]; var3 != null; var3 = var3.nextEntry)
        {
            if (var3.hashEntry == p_76041_1_)
            {
                return var3.valueEntry;
            }
        }

        return null;
    }

    /**
     * Returns true if this hash table contains the specified item.
     */
    public boolean containsItem(int p_76037_1_)
    {
        return this.lookupEntry(p_76037_1_) != null;
    }

    /**
     * Returns the internal entry for a key
     */
    final IntHashMap.Entry lookupEntry(int p_76045_1_)
    {
        int var2 = computeHash(p_76045_1_);

        for (IntHashMap.Entry var3 = this.slots[getSlotIndex(var2, this.slots.length)]; var3 != null; var3 = var3.nextEntry)
        {
            if (var3.hashEntry == p_76045_1_)
            {
                return var3;
            }
        }

        return null;
    }

    /**
     * Adds a key and associated value to this map
     */
    public void addKey(int p_76038_1_, Object p_76038_2_)
    {
        int var3 = computeHash(p_76038_1_);
        int var4 = getSlotIndex(var3, this.slots.length);

        for (IntHashMap.Entry var5 = this.slots[var4]; var5 != null; var5 = var5.nextEntry)
        {
            if (var5.hashEntry == p_76038_1_)
            {
                var5.valueEntry = p_76038_2_;
                return;
            }
        }

        this.insert(var3, p_76038_1_, p_76038_2_, var4);
    }

    /**
     * Increases the number of hash slots
     */
    private void grow(int p_76047_1_)
    {
        IntHashMap.Entry[] var2 = this.slots;
        int var3 = var2.length;

        if (var3 == 1073741824)
        {
            this.threshold = Integer.MAX_VALUE;
        }
        else
        {
            IntHashMap.Entry[] var4 = new IntHashMap.Entry[p_76047_1_];
            this.copyTo(var4);
            this.slots = var4;
            this.threshold = (int)((float)p_76047_1_ * this.growFactor);
        }
    }

    /**
     * Copies the hash slots to a new array
     */
    private void copyTo(IntHashMap.Entry[] p_76048_1_)
    {
        IntHashMap.Entry[] var2 = this.slots;
        int var3 = p_76048_1_.length;

        for (int var4 = 0; var4 < var2.length; ++var4)
        {
            IntHashMap.Entry var5 = var2[var4];

            if (var5 != null)
            {
                var2[var4] = null;
                IntHashMap.Entry var6;

                do
                {
                    var6 = var5.nextEntry;
                    int var7 = getSlotIndex(var5.slotHash, var3);
                    var5.nextEntry = p_76048_1_[var7];
                    p_76048_1_[var7] = var5;
                    var5 = var6;
                }
                while (var6 != null);
            }
        }
    }

    /**
     * Removes the specified object from the map and returns it
     */
    public Object removeObject(int p_76049_1_)
    {
        IntHashMap.Entry var2 = this.removeEntry(p_76049_1_);
        return var2 == null ? null : var2.valueEntry;
    }

    /**
     * Removes the specified entry from the map and returns it
     */
    final IntHashMap.Entry removeEntry(int p_76036_1_)
    {
        int var2 = computeHash(p_76036_1_);
        int var3 = getSlotIndex(var2, this.slots.length);
        IntHashMap.Entry var4 = this.slots[var3];
        IntHashMap.Entry var5;
        IntHashMap.Entry var6;

        for (var5 = var4; var5 != null; var5 = var6)
        {
            var6 = var5.nextEntry;

            if (var5.hashEntry == p_76036_1_)
            {
                --this.count;

                if (var4 == var5)
                {
                    this.slots[var3] = var6;
                }
                else
                {
                    var4.nextEntry = var6;
                }

                return var5;
            }

            var4 = var5;
        }

        return var5;
    }

    /**
     * Removes all entries from the map
     */
    public void clearMap()
    {
        IntHashMap.Entry[] var1 = this.slots;

        for (int var2 = 0; var2 < var1.length; ++var2)
        {
            var1[var2] = null;
        }

        this.count = 0;
    }

    /**
     * Adds an object to a slot
     */
    private void insert(int p_76040_1_, int p_76040_2_, Object p_76040_3_, int p_76040_4_)
    {
        IntHashMap.Entry var5 = this.slots[p_76040_4_];
        this.slots[p_76040_4_] = new IntHashMap.Entry(p_76040_1_, p_76040_2_, p_76040_3_, var5);

        if (this.count++ >= this.threshold)
        {
            this.grow(2 * this.slots.length);
        }
    }

    static class Entry
    {
        final int hashEntry;
        Object valueEntry;
        IntHashMap.Entry nextEntry;
        final int slotHash;
        private static final String __OBFID = "CL_00001491";

        Entry(int p_i1552_1_, int p_i1552_2_, Object p_i1552_3_, IntHashMap.Entry p_i1552_4_)
        {
            this.valueEntry = p_i1552_3_;
            this.nextEntry = p_i1552_4_;
            this.hashEntry = p_i1552_2_;
            this.slotHash = p_i1552_1_;
        }

        public final int getHash()
        {
            return this.hashEntry;
        }

        public final Object getValue()
        {
            return this.valueEntry;
        }

        public final boolean equals(Object p_equals_1_)
        {
            if (!(p_equals_1_ instanceof IntHashMap.Entry))
            {
                return false;
            }
            else
            {
                IntHashMap.Entry var2 = (IntHashMap.Entry)p_equals_1_;
                Integer var3 = Integer.valueOf(this.getHash());
                Integer var4 = Integer.valueOf(var2.getHash());

                if (var3 == var4 || var3 != null && var3.equals(var4))
                {
                    Object var5 = this.getValue();
                    Object var6 = var2.getValue();

                    if (var5 == var6 || var5 != null && var5.equals(var6))
                    {
                        return true;
                    }
                }

                return false;
            }
        }

        public final int hashCode()
        {
            return IntHashMap.computeHash(this.hashEntry);
        }

        public final String toString()
        {
            return this.getHash() + "=" + this.getValue();
        }
    }
}
