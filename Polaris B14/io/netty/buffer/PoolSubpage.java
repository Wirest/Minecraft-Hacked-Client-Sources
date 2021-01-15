/*     */ package io.netty.buffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ final class PoolSubpage<T>
/*     */ {
/*     */   final PoolChunk<T> chunk;
/*     */   
/*     */ 
/*     */   private final int memoryMapIdx;
/*     */   
/*     */ 
/*     */   private final int runOffset;
/*     */   
/*     */ 
/*     */   private final int pageSize;
/*     */   
/*     */ 
/*     */   private final long[] bitmap;
/*     */   
/*     */ 
/*     */   PoolSubpage<T> prev;
/*     */   
/*     */ 
/*     */   PoolSubpage<T> next;
/*     */   
/*     */   boolean doNotDestroy;
/*     */   
/*     */   int elemSize;
/*     */   
/*     */   private int maxNumElems;
/*     */   
/*     */   private int bitmapLength;
/*     */   
/*     */   private int nextAvail;
/*     */   
/*     */   private int numAvail;
/*     */   
/*     */ 
/*     */   PoolSubpage(int pageSize)
/*     */   {
/*  42 */     this.chunk = null;
/*  43 */     this.memoryMapIdx = -1;
/*  44 */     this.runOffset = -1;
/*  45 */     this.elemSize = -1;
/*  46 */     this.pageSize = pageSize;
/*  47 */     this.bitmap = null;
/*     */   }
/*     */   
/*     */   PoolSubpage(PoolChunk<T> chunk, int memoryMapIdx, int runOffset, int pageSize, int elemSize) {
/*  51 */     this.chunk = chunk;
/*  52 */     this.memoryMapIdx = memoryMapIdx;
/*  53 */     this.runOffset = runOffset;
/*  54 */     this.pageSize = pageSize;
/*  55 */     this.bitmap = new long[pageSize >>> 10];
/*  56 */     init(elemSize);
/*     */   }
/*     */   
/*     */   void init(int elemSize) {
/*  60 */     this.doNotDestroy = true;
/*  61 */     this.elemSize = elemSize;
/*  62 */     if (elemSize != 0) {
/*  63 */       this.maxNumElems = (this.numAvail = this.pageSize / elemSize);
/*  64 */       this.nextAvail = 0;
/*  65 */       this.bitmapLength = (this.maxNumElems >>> 6);
/*  66 */       if ((this.maxNumElems & 0x3F) != 0) {
/*  67 */         this.bitmapLength += 1;
/*     */       }
/*     */       
/*  70 */       for (int i = 0; i < this.bitmapLength; i++) {
/*  71 */         this.bitmap[i] = 0L;
/*     */       }
/*     */     }
/*     */     
/*  75 */     addToPool();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   long allocate()
/*     */   {
/*  82 */     if (this.elemSize == 0) {
/*  83 */       return toHandle(0);
/*     */     }
/*     */     
/*  86 */     if ((this.numAvail == 0) || (!this.doNotDestroy)) {
/*  87 */       return -1L;
/*     */     }
/*     */     
/*  90 */     int bitmapIdx = getNextAvail();
/*  91 */     int q = bitmapIdx >>> 6;
/*  92 */     int r = bitmapIdx & 0x3F;
/*  93 */     assert ((this.bitmap[q] >>> r & 1L) == 0L);
/*  94 */     this.bitmap[q] |= 1L << r;
/*     */     
/*  96 */     if (--this.numAvail == 0) {
/*  97 */       removeFromPool();
/*     */     }
/*     */     
/* 100 */     return toHandle(bitmapIdx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   boolean free(int bitmapIdx)
/*     */   {
/* 109 */     if (this.elemSize == 0) {
/* 110 */       return true;
/*     */     }
/*     */     
/* 113 */     int q = bitmapIdx >>> 6;
/* 114 */     int r = bitmapIdx & 0x3F;
/* 115 */     assert ((this.bitmap[q] >>> r & 1L) != 0L);
/* 116 */     this.bitmap[q] ^= 1L << r;
/*     */     
/* 118 */     setNextAvail(bitmapIdx);
/*     */     
/* 120 */     if (this.numAvail++ == 0) {
/* 121 */       addToPool();
/* 122 */       return true;
/*     */     }
/*     */     
/* 125 */     if (this.numAvail != this.maxNumElems) {
/* 126 */       return true;
/*     */     }
/*     */     
/* 129 */     if (this.prev == this.next)
/*     */     {
/* 131 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 135 */     this.doNotDestroy = false;
/* 136 */     removeFromPool();
/* 137 */     return false;
/*     */   }
/*     */   
/*     */   private void addToPool()
/*     */   {
/* 142 */     PoolSubpage<T> head = this.chunk.arena.findSubpagePoolHead(this.elemSize);
/* 143 */     assert ((this.prev == null) && (this.next == null));
/* 144 */     this.prev = head;
/* 145 */     this.next = head.next;
/* 146 */     this.next.prev = this;
/* 147 */     head.next = this;
/*     */   }
/*     */   
/*     */   private void removeFromPool() {
/* 151 */     assert ((this.prev != null) && (this.next != null));
/* 152 */     this.prev.next = this.next;
/* 153 */     this.next.prev = this.prev;
/* 154 */     this.next = null;
/* 155 */     this.prev = null;
/*     */   }
/*     */   
/*     */   private void setNextAvail(int bitmapIdx) {
/* 159 */     this.nextAvail = bitmapIdx;
/*     */   }
/*     */   
/*     */   private int getNextAvail() {
/* 163 */     int nextAvail = this.nextAvail;
/* 164 */     if (nextAvail >= 0) {
/* 165 */       this.nextAvail = -1;
/* 166 */       return nextAvail;
/*     */     }
/* 168 */     return findNextAvail();
/*     */   }
/*     */   
/*     */   private int findNextAvail() {
/* 172 */     long[] bitmap = this.bitmap;
/* 173 */     int bitmapLength = this.bitmapLength;
/* 174 */     for (int i = 0; i < bitmapLength; i++) {
/* 175 */       long bits = bitmap[i];
/* 176 */       if ((bits ^ 0xFFFFFFFFFFFFFFFF) != 0L) {
/* 177 */         return findNextAvail0(i, bits);
/*     */       }
/*     */     }
/* 180 */     return -1;
/*     */   }
/*     */   
/*     */   private int findNextAvail0(int i, long bits) {
/* 184 */     int maxNumElems = this.maxNumElems;
/* 185 */     int baseVal = i << 6;
/*     */     
/* 187 */     for (int j = 0; j < 64; j++) {
/* 188 */       if ((bits & 1L) == 0L) {
/* 189 */         int val = baseVal | j;
/* 190 */         if (val >= maxNumElems) break;
/* 191 */         return val;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 196 */       bits >>>= 1;
/*     */     }
/* 198 */     return -1;
/*     */   }
/*     */   
/*     */   private long toHandle(int bitmapIdx) {
/* 202 */     return 0x4000000000000000 | bitmapIdx << 32 | this.memoryMapIdx;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 206 */     if (!this.doNotDestroy) {
/* 207 */       return "(" + this.memoryMapIdx + ": not in use)";
/*     */     }
/*     */     
/* 210 */     return String.valueOf('(') + this.memoryMapIdx + ": " + (this.maxNumElems - this.numAvail) + '/' + this.maxNumElems + ", offset: " + this.runOffset + ", length: " + this.pageSize + ", elemSize: " + this.elemSize + ')';
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\buffer\PoolSubpage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */