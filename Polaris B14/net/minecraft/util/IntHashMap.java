/*     */ package net.minecraft.util;
/*     */ 
/*     */ public class IntHashMap<V>
/*     */ {
/*   5 */   private transient Entry<V>[] slots = new Entry[16];
/*     */   
/*     */ 
/*     */   private transient int count;
/*     */   
/*     */ 
/*  11 */   private int threshold = 12;
/*     */   
/*     */ 
/*  14 */   private final float growFactor = 0.75F;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static int computeHash(int integer)
/*     */   {
/*  21 */     integer = integer ^ integer >>> 20 ^ integer >>> 12;
/*  22 */     return integer ^ integer >>> 7 ^ integer >>> 4;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static int getSlotIndex(int hash, int slotCount)
/*     */   {
/*  30 */     return hash & slotCount - 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public V lookup(int p_76041_1_)
/*     */   {
/*  38 */     int i = computeHash(p_76041_1_);
/*     */     
/*  40 */     for (Entry<V> entry = this.slots[getSlotIndex(i, this.slots.length)]; entry != null; entry = entry.nextEntry)
/*     */     {
/*  42 */       if (entry.hashEntry == p_76041_1_)
/*     */       {
/*  44 */         return (V)entry.valueEntry;
/*     */       }
/*     */     }
/*     */     
/*  48 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean containsItem(int p_76037_1_)
/*     */   {
/*  56 */     return lookupEntry(p_76037_1_) != null;
/*     */   }
/*     */   
/*     */   final Entry<V> lookupEntry(int p_76045_1_)
/*     */   {
/*  61 */     int i = computeHash(p_76045_1_);
/*     */     
/*  63 */     for (Entry<V> entry = this.slots[getSlotIndex(i, this.slots.length)]; entry != null; entry = entry.nextEntry)
/*     */     {
/*  65 */       if (entry.hashEntry == p_76045_1_)
/*     */       {
/*  67 */         return entry;
/*     */       }
/*     */     }
/*     */     
/*  71 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addKey(int p_76038_1_, V p_76038_2_)
/*     */   {
/*  79 */     int i = computeHash(p_76038_1_);
/*  80 */     int j = getSlotIndex(i, this.slots.length);
/*     */     
/*  82 */     for (Entry<V> entry = this.slots[j]; entry != null; entry = entry.nextEntry)
/*     */     {
/*  84 */       if (entry.hashEntry == p_76038_1_)
/*     */       {
/*  86 */         entry.valueEntry = p_76038_2_;
/*  87 */         return;
/*     */       }
/*     */     }
/*     */     
/*  91 */     insert(i, p_76038_1_, p_76038_2_, j);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void grow(int p_76047_1_)
/*     */   {
/*  99 */     Entry[] entry = this.slots;
/* 100 */     int i = entry.length;
/*     */     
/* 102 */     if (i == 1073741824)
/*     */     {
/* 104 */       this.threshold = Integer.MAX_VALUE;
/*     */     }
/*     */     else
/*     */     {
/* 108 */       Entry[] entry1 = new Entry[p_76047_1_];
/* 109 */       copyTo(entry1);
/* 110 */       this.slots = entry1;
/* 111 */       this.threshold = ((int)(p_76047_1_ * 0.75F));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void copyTo(Entry<V>[] p_76048_1_)
/*     */   {
/* 120 */     Entry[] entry = this.slots;
/* 121 */     int i = p_76048_1_.length;
/*     */     
/* 123 */     for (int j = 0; j < entry.length; j++)
/*     */     {
/* 125 */       Entry<V> entry1 = entry[j];
/*     */       
/* 127 */       if (entry1 != null)
/*     */       {
/* 129 */         entry[j] = null;
/*     */         Entry<V> entry2;
/*     */         do
/*     */         {
/* 133 */           entry2 = entry1.nextEntry;
/* 134 */           int k = getSlotIndex(entry1.slotHash, i);
/* 135 */           entry1.nextEntry = p_76048_1_[k];
/* 136 */           p_76048_1_[k] = entry1;
/* 137 */           entry1 = entry2;
/*     */         }
/* 139 */         while (entry2 != null);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public V removeObject(int p_76049_1_)
/*     */   {
/* 153 */     Entry<V> entry = removeEntry(p_76049_1_);
/* 154 */     return entry == null ? null : entry.valueEntry;
/*     */   }
/*     */   
/*     */   final Entry<V> removeEntry(int p_76036_1_)
/*     */   {
/* 159 */     int i = computeHash(p_76036_1_);
/* 160 */     int j = getSlotIndex(i, this.slots.length);
/* 161 */     Entry<V> entry = this.slots[j];
/*     */     
/*     */     Entry<V> entry2;
/*     */     
/* 165 */     for (Entry<V> entry1 = entry; entry1 != null; entry1 = entry2)
/*     */     {
/* 167 */       entry2 = entry1.nextEntry;
/*     */       
/* 169 */       if (entry1.hashEntry == p_76036_1_)
/*     */       {
/* 171 */         this.count -= 1;
/*     */         
/* 173 */         if (entry == entry1)
/*     */         {
/* 175 */           this.slots[j] = entry2;
/*     */         }
/*     */         else
/*     */         {
/* 179 */           entry.nextEntry = entry2;
/*     */         }
/*     */         
/* 182 */         return entry1;
/*     */       }
/*     */       
/* 185 */       entry = entry1;
/*     */     }
/*     */     
/* 188 */     return entry1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void clearMap()
/*     */   {
/* 196 */     Entry[] entry = this.slots;
/*     */     
/* 198 */     for (int i = 0; i < entry.length; i++)
/*     */     {
/* 200 */       entry[i] = null;
/*     */     }
/*     */     
/* 203 */     this.count = 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void insert(int p_76040_1_, int p_76040_2_, V p_76040_3_, int p_76040_4_)
/*     */   {
/* 211 */     Entry<V> entry = this.slots[p_76040_4_];
/* 212 */     this.slots[p_76040_4_] = new Entry(p_76040_1_, p_76040_2_, p_76040_3_, entry);
/*     */     
/* 214 */     if (this.count++ >= this.threshold)
/*     */     {
/* 216 */       grow(2 * this.slots.length);
/*     */     }
/*     */   }
/*     */   
/*     */   static class Entry<V>
/*     */   {
/*     */     final int hashEntry;
/*     */     V valueEntry;
/*     */     Entry<V> nextEntry;
/*     */     final int slotHash;
/*     */     
/*     */     Entry(int p_i1552_1_, int p_i1552_2_, V p_i1552_3_, Entry<V> p_i1552_4_)
/*     */     {
/* 229 */       this.valueEntry = p_i1552_3_;
/* 230 */       this.nextEntry = p_i1552_4_;
/* 231 */       this.hashEntry = p_i1552_2_;
/* 232 */       this.slotHash = p_i1552_1_;
/*     */     }
/*     */     
/*     */     public final int getHash()
/*     */     {
/* 237 */       return this.hashEntry;
/*     */     }
/*     */     
/*     */     public final V getValue()
/*     */     {
/* 242 */       return (V)this.valueEntry;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object p_equals_1_)
/*     */     {
/* 247 */       if (!(p_equals_1_ instanceof Entry))
/*     */       {
/* 249 */         return false;
/*     */       }
/*     */       
/*     */ 
/* 253 */       Entry<V> entry = (Entry)p_equals_1_;
/* 254 */       Object object = Integer.valueOf(getHash());
/* 255 */       Object object1 = Integer.valueOf(entry.getHash());
/*     */       
/* 257 */       if ((object == object1) || ((object != null) && (object.equals(object1))))
/*     */       {
/* 259 */         Object object2 = getValue();
/* 260 */         Object object3 = entry.getValue();
/*     */         
/* 262 */         if ((object2 == object3) || ((object2 != null) && (object2.equals(object3))))
/*     */         {
/* 264 */           return true;
/*     */         }
/*     */       }
/*     */       
/* 268 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */     public final int hashCode()
/*     */     {
/* 274 */       return IntHashMap.computeHash(this.hashEntry);
/*     */     }
/*     */     
/*     */     public final String toString()
/*     */     {
/* 279 */       return getHash() + "=" + getValue();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\util\IntHashMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */