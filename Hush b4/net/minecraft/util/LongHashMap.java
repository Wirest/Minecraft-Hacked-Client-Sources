// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

public class LongHashMap
{
    private transient Entry[] hashArray;
    private transient int numHashElements;
    private int mask;
    private int capacity;
    private final float percentUseable = 0.75f;
    private transient volatile int modCount;
    private static final String __OBFID = "CL_00001492";
    
    public LongHashMap() {
        this.hashArray = new Entry[4096];
        this.capacity = 3072;
        this.mask = this.hashArray.length - 1;
    }
    
    private static int getHashedKey(final long originalKey) {
        return (int)(originalKey ^ originalKey >>> 27);
    }
    
    private static int hash(int integer) {
        integer = (integer ^ integer >>> 20 ^ integer >>> 12);
        return integer ^ integer >>> 7 ^ integer >>> 4;
    }
    
    private static int getHashIndex(final int p_76158_0_, final int p_76158_1_) {
        return p_76158_0_ & p_76158_1_;
    }
    
    public int getNumHashElements() {
        return this.numHashElements;
    }
    
    public Object getValueByKey(final long p_76164_1_) {
        final int i = getHashedKey(p_76164_1_);
        for (Entry longhashmap$entry = this.hashArray[getHashIndex(i, this.mask)]; longhashmap$entry != null; longhashmap$entry = longhashmap$entry.nextEntry) {
            if (longhashmap$entry.key == p_76164_1_) {
                return longhashmap$entry.value;
            }
        }
        return null;
    }
    
    public boolean containsItem(final long p_76161_1_) {
        return this.getEntry(p_76161_1_) != null;
    }
    
    final Entry getEntry(final long p_76160_1_) {
        final int i = getHashedKey(p_76160_1_);
        for (Entry longhashmap$entry = this.hashArray[getHashIndex(i, this.mask)]; longhashmap$entry != null; longhashmap$entry = longhashmap$entry.nextEntry) {
            if (longhashmap$entry.key == p_76160_1_) {
                return longhashmap$entry;
            }
        }
        return null;
    }
    
    public void add(final long p_76163_1_, final Object p_76163_3_) {
        final int i = getHashedKey(p_76163_1_);
        final int j = getHashIndex(i, this.mask);
        for (Entry longhashmap$entry = this.hashArray[j]; longhashmap$entry != null; longhashmap$entry = longhashmap$entry.nextEntry) {
            if (longhashmap$entry.key == p_76163_1_) {
                longhashmap$entry.value = p_76163_3_;
                return;
            }
        }
        ++this.modCount;
        this.createKey(i, p_76163_1_, p_76163_3_, j);
    }
    
    private void resizeTable(final int p_76153_1_) {
        final Entry[] alonghashmap$entry = this.hashArray;
        final int i = alonghashmap$entry.length;
        if (i == 1073741824) {
            this.capacity = Integer.MAX_VALUE;
        }
        else {
            final Entry[] alonghashmap$entry2 = new Entry[p_76153_1_];
            this.copyHashTableTo(alonghashmap$entry2);
            this.hashArray = alonghashmap$entry2;
            this.mask = this.hashArray.length - 1;
            final float f = (float)p_76153_1_;
            this.getClass();
            this.capacity = (int)(f * 0.75f);
        }
    }
    
    private void copyHashTableTo(final Entry[] p_76154_1_) {
        final Entry[] alonghashmap$entry = this.hashArray;
        final int i = p_76154_1_.length;
        for (int j = 0; j < alonghashmap$entry.length; ++j) {
            Entry longhashmap$entry = alonghashmap$entry[j];
            if (longhashmap$entry != null) {
                alonghashmap$entry[j] = null;
                Entry longhashmap$entry2;
                do {
                    longhashmap$entry2 = longhashmap$entry.nextEntry;
                    final int k = getHashIndex(longhashmap$entry.hash, i - 1);
                    longhashmap$entry.nextEntry = p_76154_1_[k];
                    p_76154_1_[k] = longhashmap$entry;
                } while ((longhashmap$entry = longhashmap$entry2) != null);
            }
        }
    }
    
    public Object remove(final long p_76159_1_) {
        final Entry longhashmap$entry = this.removeKey(p_76159_1_);
        return (longhashmap$entry == null) ? null : longhashmap$entry.value;
    }
    
    final Entry removeKey(final long p_76152_1_) {
        final int i = getHashedKey(p_76152_1_);
        final int j = getHashIndex(i, this.mask);
        Entry longhashmap$entry2;
        Entry longhashmap$entry3;
        for (Entry longhashmap$entry = longhashmap$entry2 = this.hashArray[j]; longhashmap$entry2 != null; longhashmap$entry2 = longhashmap$entry3) {
            longhashmap$entry3 = longhashmap$entry2.nextEntry;
            if (longhashmap$entry2.key == p_76152_1_) {
                ++this.modCount;
                --this.numHashElements;
                if (longhashmap$entry == longhashmap$entry2) {
                    this.hashArray[j] = longhashmap$entry3;
                }
                else {
                    longhashmap$entry.nextEntry = longhashmap$entry3;
                }
                return longhashmap$entry2;
            }
            longhashmap$entry = longhashmap$entry2;
        }
        return longhashmap$entry2;
    }
    
    private void createKey(final int p_76156_1_, final long p_76156_2_, final Object p_76156_4_, final int p_76156_5_) {
        final Entry longhashmap$entry = this.hashArray[p_76156_5_];
        this.hashArray[p_76156_5_] = new Entry(p_76156_1_, p_76156_2_, p_76156_4_, longhashmap$entry);
        if (this.numHashElements++ >= this.capacity) {
            this.resizeTable(2 * this.hashArray.length);
        }
    }
    
    public double getKeyDistribution() {
        int i = 0;
        for (int j = 0; j < this.hashArray.length; ++j) {
            if (this.hashArray[j] != null) {
                ++i;
            }
        }
        return 1.0 * i / this.numHashElements;
    }
    
    static class Entry
    {
        final long key;
        Object value;
        Entry nextEntry;
        final int hash;
        private static final String __OBFID = "CL_00001493";
        
        Entry(final int p_i1553_1_, final long p_i1553_2_, final Object p_i1553_4_, final Entry p_i1553_5_) {
            this.value = p_i1553_4_;
            this.nextEntry = p_i1553_5_;
            this.key = p_i1553_2_;
            this.hash = p_i1553_1_;
        }
        
        public final long getKey() {
            return this.key;
        }
        
        public final Object getValue() {
            return this.value;
        }
        
        @Override
        public final boolean equals(final Object p_equals_1_) {
            if (!(p_equals_1_ instanceof Entry)) {
                return false;
            }
            final Entry longhashmap$entry = (Entry)p_equals_1_;
            final Long olong = this.getKey();
            final Long olong2 = longhashmap$entry.getKey();
            if (olong == olong2 || (olong != null && olong.equals(olong2))) {
                final Object object = this.getValue();
                final Object object2 = longhashmap$entry.getValue();
                if (object == object2 || (object != null && object.equals(object2))) {
                    return true;
                }
            }
            return false;
        }
        
        @Override
        public final int hashCode() {
            return getHashedKey(this.key);
        }
        
        @Override
        public final String toString() {
            return String.valueOf(this.getKey()) + "=" + this.getValue();
        }
    }
}
