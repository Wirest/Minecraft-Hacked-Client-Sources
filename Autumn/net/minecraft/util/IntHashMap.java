package net.minecraft.util;

public class IntHashMap {
   private transient IntHashMap.Entry[] slots = new IntHashMap.Entry[16];
   private transient int count;
   private int threshold = 12;
   private final float growFactor = 0.75F;

   private static int computeHash(int integer) {
      integer = integer ^ integer >>> 20 ^ integer >>> 12;
      return integer ^ integer >>> 7 ^ integer >>> 4;
   }

   private static int getSlotIndex(int hash, int slotCount) {
      return hash & slotCount - 1;
   }

   public Object lookup(int p_76041_1_) {
      int i = computeHash(p_76041_1_);

      for(IntHashMap.Entry entry = this.slots[getSlotIndex(i, this.slots.length)]; entry != null; entry = entry.nextEntry) {
         if (entry.hashEntry == p_76041_1_) {
            return entry.valueEntry;
         }
      }

      return null;
   }

   public boolean containsItem(int p_76037_1_) {
      return this.lookupEntry(p_76037_1_) != null;
   }

   final IntHashMap.Entry lookupEntry(int p_76045_1_) {
      int i = computeHash(p_76045_1_);

      for(IntHashMap.Entry entry = this.slots[getSlotIndex(i, this.slots.length)]; entry != null; entry = entry.nextEntry) {
         if (entry.hashEntry == p_76045_1_) {
            return entry;
         }
      }

      return null;
   }

   public void addKey(int p_76038_1_, Object p_76038_2_) {
      int i = computeHash(p_76038_1_);
      int j = getSlotIndex(i, this.slots.length);

      for(IntHashMap.Entry entry = this.slots[j]; entry != null; entry = entry.nextEntry) {
         if (entry.hashEntry == p_76038_1_) {
            entry.valueEntry = p_76038_2_;
            return;
         }
      }

      this.insert(i, p_76038_1_, p_76038_2_, j);
   }

   private void grow(int p_76047_1_) {
      IntHashMap.Entry[] entry = this.slots;
      int i = entry.length;
      if (i == 1073741824) {
         this.threshold = Integer.MAX_VALUE;
      } else {
         IntHashMap.Entry[] entry1 = new IntHashMap.Entry[p_76047_1_];
         this.copyTo(entry1);
         this.slots = entry1;
         float var10001 = (float)p_76047_1_;
         this.getClass();
         this.threshold = (int)(var10001 * 0.75F);
      }

   }

   private void copyTo(IntHashMap.Entry[] p_76048_1_) {
      IntHashMap.Entry[] entry = this.slots;
      int i = p_76048_1_.length;

      for(int j = 0; j < entry.length; ++j) {
         IntHashMap.Entry entry1 = entry[j];
         if (entry1 != null) {
            entry[j] = null;

            IntHashMap.Entry entry2;
            do {
               entry2 = entry1.nextEntry;
               int k = getSlotIndex(entry1.slotHash, i);
               entry1.nextEntry = p_76048_1_[k];
               p_76048_1_[k] = entry1;
               entry1 = entry2;
            } while(entry2 != null);
         }
      }

   }

   public Object removeObject(int p_76049_1_) {
      IntHashMap.Entry entry = this.removeEntry(p_76049_1_);
      return entry == null ? null : entry.valueEntry;
   }

   final IntHashMap.Entry removeEntry(int p_76036_1_) {
      int i = computeHash(p_76036_1_);
      int j = getSlotIndex(i, this.slots.length);
      IntHashMap.Entry entry = this.slots[j];

      IntHashMap.Entry entry1;
      IntHashMap.Entry entry2;
      for(entry1 = entry; entry1 != null; entry1 = entry2) {
         entry2 = entry1.nextEntry;
         if (entry1.hashEntry == p_76036_1_) {
            --this.count;
            if (entry == entry1) {
               this.slots[j] = entry2;
            } else {
               entry.nextEntry = entry2;
            }

            return entry1;
         }

         entry = entry1;
      }

      return entry1;
   }

   public void clearMap() {
      IntHashMap.Entry[] entry = this.slots;

      for(int i = 0; i < entry.length; ++i) {
         entry[i] = null;
      }

      this.count = 0;
   }

   private void insert(int p_76040_1_, int p_76040_2_, Object p_76040_3_, int p_76040_4_) {
      IntHashMap.Entry entry = this.slots[p_76040_4_];
      this.slots[p_76040_4_] = new IntHashMap.Entry(p_76040_1_, p_76040_2_, p_76040_3_, entry);
      if (this.count++ >= this.threshold) {
         this.grow(2 * this.slots.length);
      }

   }

   static class Entry {
      final int hashEntry;
      Object valueEntry;
      IntHashMap.Entry nextEntry;
      final int slotHash;

      Entry(int p_i1552_1_, int p_i1552_2_, Object p_i1552_3_, IntHashMap.Entry p_i1552_4_) {
         this.valueEntry = p_i1552_3_;
         this.nextEntry = p_i1552_4_;
         this.hashEntry = p_i1552_2_;
         this.slotHash = p_i1552_1_;
      }

      public final int getHash() {
         return this.hashEntry;
      }

      public final Object getValue() {
         return this.valueEntry;
      }

      public final boolean equals(Object p_equals_1_) {
         if (!(p_equals_1_ instanceof IntHashMap.Entry)) {
            return false;
         } else {
            IntHashMap.Entry entry = (IntHashMap.Entry)p_equals_1_;
            Object object = this.getHash();
            Object object1 = entry.getHash();
            if (object == object1 || object != null && object.equals(object1)) {
               Object object2 = this.getValue();
               Object object3 = entry.getValue();
               if (object2 == object3 || object2 != null && object2.equals(object3)) {
                  return true;
               }
            }

            return false;
         }
      }

      public final int hashCode() {
         return IntHashMap.computeHash(this.hashEntry);
      }

      public final String toString() {
         return this.getHash() + "=" + this.getValue();
      }
   }
}
