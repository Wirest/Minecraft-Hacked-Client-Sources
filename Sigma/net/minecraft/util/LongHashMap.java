package net.minecraft.util;

public class LongHashMap {
    /**
     * the array of all elements in the hash
     */
    private transient LongHashMap.Entry[] hashArray = new LongHashMap.Entry[4096];

    /**
     * the number of elements in the hash array
     */
    private transient int numHashElements;
    private int field_180201_c;

    /**
     * the maximum amount of elements in the hash (probably 3/4 the size due to
     * meh hashing function)
     */
    private int capacity = 3072;

    /**
     * percent of the hasharray that can be used without hash colliding probably
     */
    private final float percentUseable = 0.75F;

    /**
     * count of times elements have been added/removed
     */
    private transient volatile int modCount;
    private static final String __OBFID = "CL_00001492";

    public LongHashMap() {
        field_180201_c = hashArray.length - 1;
    }

    /**
     * returns the hashed key given the original key
     */
    private static int getHashedKey(long originalKey) {
        return (int) (originalKey ^ originalKey >>> 27);
    }

    /**
     * the hash function
     */
    private static int hash(int integer) {
        integer ^= integer >>> 20 ^ integer >>> 12;
        return integer ^ integer >>> 7 ^ integer >>> 4;
    }

    /**
     * gets the index in the hash given the array length and the hashed key
     */
    private static int getHashIndex(int p_76158_0_, int p_76158_1_) {
        return p_76158_0_ & p_76158_1_;
    }

    public int getNumHashElements() {
        return numHashElements;
    }

    /**
     * get the value from the map given the key
     */
    public Object getValueByKey(long p_76164_1_) {
        int var3 = LongHashMap.getHashedKey(p_76164_1_);

        for (LongHashMap.Entry var4 = hashArray[LongHashMap.getHashIndex(var3, field_180201_c)]; var4 != null; var4 = var4.nextEntry) {
            if (var4.key == p_76164_1_) {
                return var4.value;
            }
        }

        return null;
    }

    public boolean containsItem(long p_76161_1_) {
        return getEntry(p_76161_1_) != null;
    }

    final LongHashMap.Entry getEntry(long p_76160_1_) {
        int var3 = LongHashMap.getHashedKey(p_76160_1_);

        for (LongHashMap.Entry var4 = hashArray[LongHashMap.getHashIndex(var3, field_180201_c)]; var4 != null; var4 = var4.nextEntry) {
            if (var4.key == p_76160_1_) {
                return var4;
            }
        }

        return null;
    }

    /**
     * Add a key-value pair.
     */
    public void add(long p_76163_1_, Object p_76163_3_) {
        int var4 = LongHashMap.getHashedKey(p_76163_1_);
        int var5 = LongHashMap.getHashIndex(var4, field_180201_c);

        for (LongHashMap.Entry var6 = hashArray[var5]; var6 != null; var6 = var6.nextEntry) {
            if (var6.key == p_76163_1_) {
                var6.value = p_76163_3_;
                return;
            }
        }

        ++modCount;
        createKey(var4, p_76163_1_, p_76163_3_, var5);
    }

    /**
     * resizes the table
     */
    private void resizeTable(int p_76153_1_) {
        LongHashMap.Entry[] var2 = hashArray;
        int var3 = var2.length;

        if (var3 == 1073741824) {
            capacity = Integer.MAX_VALUE;
        } else {
            LongHashMap.Entry[] var4 = new LongHashMap.Entry[p_76153_1_];
            copyHashTableTo(var4);
            hashArray = var4;
            field_180201_c = hashArray.length - 1;
            float var10001 = p_76153_1_;
            this.getClass();
            capacity = (int) (var10001 * 0.75F);
        }
    }

    /**
     * copies the hash table to the specified array
     */
    private void copyHashTableTo(LongHashMap.Entry[] p_76154_1_) {
        LongHashMap.Entry[] var2 = hashArray;
        int var3 = p_76154_1_.length;

        for (int var4 = 0; var4 < var2.length; ++var4) {
            LongHashMap.Entry var5 = var2[var4];

            if (var5 != null) {
                var2[var4] = null;
                LongHashMap.Entry var6;

                do {
                    var6 = var5.nextEntry;
                    int var7 = LongHashMap.getHashIndex(var5.hash, var3 - 1);
                    var5.nextEntry = p_76154_1_[var7];
                    p_76154_1_[var7] = var5;
                    var5 = var6;
                } while (var6 != null);
            }
        }
    }

    /**
     * calls the removeKey method and returns removed object
     */
    public Object remove(long p_76159_1_) {
        LongHashMap.Entry var3 = removeKey(p_76159_1_);
        return var3 == null ? null : var3.value;
    }

    /**
     * removes the key from the hash linked list
     */
    final LongHashMap.Entry removeKey(long p_76152_1_) {
        int var3 = LongHashMap.getHashedKey(p_76152_1_);
        int var4 = LongHashMap.getHashIndex(var3, field_180201_c);
        LongHashMap.Entry var5 = hashArray[var4];
        LongHashMap.Entry var6;
        LongHashMap.Entry var7;

        for (var6 = var5; var6 != null; var6 = var7) {
            var7 = var6.nextEntry;

            if (var6.key == p_76152_1_) {
                ++modCount;
                --numHashElements;

                if (var5 == var6) {
                    hashArray[var4] = var7;
                } else {
                    var5.nextEntry = var7;
                }

                return var6;
            }

            var5 = var6;
        }

        return var6;
    }

    /**
     * creates the key in the hash table
     */
    private void createKey(int p_76156_1_, long p_76156_2_, Object p_76156_4_, int p_76156_5_) {
        LongHashMap.Entry var6 = hashArray[p_76156_5_];
        hashArray[p_76156_5_] = new LongHashMap.Entry(p_76156_1_, p_76156_2_, p_76156_4_, var6);

        if (numHashElements++ >= capacity) {
            resizeTable(2 * hashArray.length);
        }
    }

    public double getKeyDistribution() {
        int countValid = 0;

        for (Entry element : hashArray) {
            if (element != null) {
                ++countValid;
            }
        }

        return 1.0D * countValid / numHashElements;
    }

    static class Entry {
        final long key;
        Object value;
        LongHashMap.Entry nextEntry;
        final int hash;
        private static final String __OBFID = "CL_00001493";

        Entry(int p_i1553_1_, long p_i1553_2_, Object p_i1553_4_, LongHashMap.Entry p_i1553_5_) {
            value = p_i1553_4_;
            nextEntry = p_i1553_5_;
            key = p_i1553_2_;
            hash = p_i1553_1_;
        }

        public final long getKey() {
            return key;
        }

        public final Object getValue() {
            return value;
        }

        @Override
        public final boolean equals(Object p_equals_1_) {
            if (!(p_equals_1_ instanceof LongHashMap.Entry)) {
                return false;
            } else {
                LongHashMap.Entry var2 = (LongHashMap.Entry) p_equals_1_;
                Long var3 = Long.valueOf(getKey());
                Long var4 = Long.valueOf(var2.getKey());

                if (var3 == var4 || var3 != null && var3.equals(var4)) {
                    Object var5 = getValue();
                    Object var6 = var2.getValue();

                    if (var5 == var6 || var5 != null && var5.equals(var6)) {
                        return true;
                    }
                }

                return false;
            }
        }

        @Override
        public final int hashCode() {
            return LongHashMap.getHashedKey(key);
        }

        @Override
        public final String toString() {
            return getKey() + "=" + getValue();
        }
    }
}
