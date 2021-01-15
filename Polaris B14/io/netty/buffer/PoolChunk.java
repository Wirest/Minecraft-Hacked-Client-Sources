/*     */ package io.netty.buffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class PoolChunk<T>
/*     */ {
/*     */   final PoolArena<T> arena;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   final T memory;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   final boolean unpooled;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final byte[] memoryMap;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final byte[] depthMap;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final PoolSubpage<T>[] subpages;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final int subpageOverflowMask;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final int pageSize;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final int pageShifts;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final int maxOrder;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final int chunkSize;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final int log2ChunkSize;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final int maxSubpageAllocs;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final byte unusable;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int freeBytes;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   PoolChunkList<T> parent;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   PoolChunk<T> prev;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   PoolChunk<T> next;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   PoolChunk(PoolArena<T> arena, T memory, int pageSize, int maxOrder, int pageShifts, int chunkSize)
/*     */   {
/* 134 */     this.unpooled = false;
/* 135 */     this.arena = arena;
/* 136 */     this.memory = memory;
/* 137 */     this.pageSize = pageSize;
/* 138 */     this.pageShifts = pageShifts;
/* 139 */     this.maxOrder = maxOrder;
/* 140 */     this.chunkSize = chunkSize;
/* 141 */     this.unusable = ((byte)(maxOrder + 1));
/* 142 */     this.log2ChunkSize = log2(chunkSize);
/* 143 */     this.subpageOverflowMask = (pageSize - 1 ^ 0xFFFFFFFF);
/* 144 */     this.freeBytes = chunkSize;
/*     */     
/* 146 */     assert (maxOrder < 30) : ("maxOrder should be < 30, but is: " + maxOrder);
/* 147 */     this.maxSubpageAllocs = (1 << maxOrder);
/*     */     
/*     */ 
/* 150 */     this.memoryMap = new byte[this.maxSubpageAllocs << 1];
/* 151 */     this.depthMap = new byte[this.memoryMap.length];
/* 152 */     int memoryMapIndex = 1;
/* 153 */     for (int d = 0; d <= maxOrder; d++) {
/* 154 */       int depth = 1 << d;
/* 155 */       for (int p = 0; p < depth; p++)
/*     */       {
/* 157 */         this.memoryMap[memoryMapIndex] = ((byte)d);
/* 158 */         this.depthMap[memoryMapIndex] = ((byte)d);
/* 159 */         memoryMapIndex++;
/*     */       }
/*     */     }
/*     */     
/* 163 */     this.subpages = newSubpageArray(this.maxSubpageAllocs);
/*     */   }
/*     */   
/*     */   PoolChunk(PoolArena<T> arena, T memory, int size)
/*     */   {
/* 168 */     this.unpooled = true;
/* 169 */     this.arena = arena;
/* 170 */     this.memory = memory;
/* 171 */     this.memoryMap = null;
/* 172 */     this.depthMap = null;
/* 173 */     this.subpages = null;
/* 174 */     this.subpageOverflowMask = 0;
/* 175 */     this.pageSize = 0;
/* 176 */     this.pageShifts = 0;
/* 177 */     this.maxOrder = 0;
/* 178 */     this.unusable = ((byte)(this.maxOrder + 1));
/* 179 */     this.chunkSize = size;
/* 180 */     this.log2ChunkSize = log2(this.chunkSize);
/* 181 */     this.maxSubpageAllocs = 0;
/*     */   }
/*     */   
/*     */   private PoolSubpage<T>[] newSubpageArray(int size)
/*     */   {
/* 186 */     return new PoolSubpage[size];
/*     */   }
/*     */   
/*     */   int usage() {
/* 190 */     int freeBytes = this.freeBytes;
/* 191 */     if (freeBytes == 0) {
/* 192 */       return 100;
/*     */     }
/*     */     
/* 195 */     int freePercentage = (int)(freeBytes * 100L / this.chunkSize);
/* 196 */     if (freePercentage == 0) {
/* 197 */       return 99;
/*     */     }
/* 199 */     return 100 - freePercentage;
/*     */   }
/*     */   
/*     */   long allocate(int normCapacity) {
/* 203 */     if ((normCapacity & this.subpageOverflowMask) != 0) {
/* 204 */       return allocateRun(normCapacity);
/*     */     }
/* 206 */     return allocateSubpage(normCapacity);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void updateParentsAlloc(int id)
/*     */   {
/* 219 */     while (id > 1) {
/* 220 */       int parentId = id >>> 1;
/* 221 */       byte val1 = value(id);
/* 222 */       byte val2 = value(id ^ 0x1);
/* 223 */       byte val = val1 < val2 ? val1 : val2;
/* 224 */       setValue(parentId, val);
/* 225 */       id = parentId;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void updateParentsFree(int id)
/*     */   {
/* 237 */     int logChild = depth(id) + 1;
/* 238 */     while (id > 1) {
/* 239 */       int parentId = id >>> 1;
/* 240 */       byte val1 = value(id);
/* 241 */       byte val2 = value(id ^ 0x1);
/* 242 */       logChild--;
/*     */       
/* 244 */       if ((val1 == logChild) && (val2 == logChild)) {
/* 245 */         setValue(parentId, (byte)(logChild - 1));
/*     */       } else {
/* 247 */         byte val = val1 < val2 ? val1 : val2;
/* 248 */         setValue(parentId, val);
/*     */       }
/*     */       
/* 251 */       id = parentId;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int allocateNode(int d)
/*     */   {
/* 263 */     int id = 1;
/* 264 */     int initial = -(1 << d);
/* 265 */     byte val = value(id);
/* 266 */     if (val > d) {
/* 267 */       return -1;
/*     */     }
/* 269 */     while ((val < d) || ((id & initial) == 0)) {
/* 270 */       id <<= 1;
/* 271 */       val = value(id);
/* 272 */       if (val > d) {
/* 273 */         id ^= 0x1;
/* 274 */         val = value(id);
/*     */       }
/*     */     }
/* 277 */     byte value = value(id);
/* 278 */     if ((!$assertionsDisabled) && ((value != d) || ((id & initial) != 1 << d))) { throw new AssertionError(String.format("val = %d, id & initial = %d, d = %d", new Object[] { Byte.valueOf(value), Integer.valueOf(id & initial), Integer.valueOf(d) }));
/*     */     }
/* 280 */     setValue(id, this.unusable);
/* 281 */     updateParentsAlloc(id);
/* 282 */     return id;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private long allocateRun(int normCapacity)
/*     */   {
/* 292 */     int d = this.maxOrder - (log2(normCapacity) - this.pageShifts);
/* 293 */     int id = allocateNode(d);
/* 294 */     if (id < 0) {
/* 295 */       return id;
/*     */     }
/* 297 */     this.freeBytes -= runLength(id);
/* 298 */     return id;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private long allocateSubpage(int normCapacity)
/*     */   {
/* 309 */     int d = this.maxOrder;
/* 310 */     int id = allocateNode(d);
/* 311 */     if (id < 0) {
/* 312 */       return id;
/*     */     }
/*     */     
/* 315 */     PoolSubpage<T>[] subpages = this.subpages;
/* 316 */     int pageSize = this.pageSize;
/*     */     
/* 318 */     this.freeBytes -= pageSize;
/*     */     
/* 320 */     int subpageIdx = subpageIdx(id);
/* 321 */     PoolSubpage<T> subpage = subpages[subpageIdx];
/* 322 */     if (subpage == null) {
/* 323 */       subpage = new PoolSubpage(this, id, runOffset(id), pageSize, normCapacity);
/* 324 */       subpages[subpageIdx] = subpage;
/*     */     } else {
/* 326 */       subpage.init(normCapacity);
/*     */     }
/* 328 */     return subpage.allocate();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   void free(long handle)
/*     */   {
/* 340 */     int memoryMapIdx = (int)handle;
/* 341 */     int bitmapIdx = (int)(handle >>> 32);
/*     */     
/* 343 */     if (bitmapIdx != 0) {
/* 344 */       PoolSubpage<T> subpage = this.subpages[subpageIdx(memoryMapIdx)];
/* 345 */       assert ((subpage != null) && (subpage.doNotDestroy));
/* 346 */       if (subpage.free(bitmapIdx & 0x3FFFFFFF)) {
/* 347 */         return;
/*     */       }
/*     */     }
/* 350 */     this.freeBytes += runLength(memoryMapIdx);
/* 351 */     setValue(memoryMapIdx, depth(memoryMapIdx));
/* 352 */     updateParentsFree(memoryMapIdx);
/*     */   }
/*     */   
/*     */   void initBuf(PooledByteBuf<T> buf, long handle, int reqCapacity) {
/* 356 */     int memoryMapIdx = (int)handle;
/* 357 */     int bitmapIdx = (int)(handle >>> 32);
/* 358 */     if (bitmapIdx == 0) {
/* 359 */       byte val = value(memoryMapIdx);
/* 360 */       assert (val == this.unusable) : String.valueOf(val);
/* 361 */       buf.init(this, handle, runOffset(memoryMapIdx), reqCapacity, runLength(memoryMapIdx));
/*     */     } else {
/* 363 */       initBufWithSubpage(buf, handle, bitmapIdx, reqCapacity);
/*     */     }
/*     */   }
/*     */   
/*     */   void initBufWithSubpage(PooledByteBuf<T> buf, long handle, int reqCapacity) {
/* 368 */     initBufWithSubpage(buf, handle, (int)(handle >>> 32), reqCapacity);
/*     */   }
/*     */   
/*     */   private void initBufWithSubpage(PooledByteBuf<T> buf, long handle, int bitmapIdx, int reqCapacity) {
/* 372 */     assert (bitmapIdx != 0);
/*     */     
/* 374 */     int memoryMapIdx = (int)handle;
/*     */     
/* 376 */     PoolSubpage<T> subpage = this.subpages[subpageIdx(memoryMapIdx)];
/* 377 */     assert (subpage.doNotDestroy);
/* 378 */     assert (reqCapacity <= subpage.elemSize);
/*     */     
/* 380 */     buf.init(this, handle, runOffset(memoryMapIdx) + (bitmapIdx & 0x3FFFFFFF) * subpage.elemSize, reqCapacity, subpage.elemSize);
/*     */   }
/*     */   
/*     */ 
/*     */   private byte value(int id)
/*     */   {
/* 386 */     return this.memoryMap[id];
/*     */   }
/*     */   
/*     */   private void setValue(int id, byte val) {
/* 390 */     this.memoryMap[id] = val;
/*     */   }
/*     */   
/*     */   private byte depth(int id) {
/* 394 */     return this.depthMap[id];
/*     */   }
/*     */   
/*     */   private static int log2(int val)
/*     */   {
/* 399 */     return 31 - Integer.numberOfLeadingZeros(val);
/*     */   }
/*     */   
/*     */   private int runLength(int id)
/*     */   {
/* 404 */     return 1 << this.log2ChunkSize - depth(id);
/*     */   }
/*     */   
/*     */   private int runOffset(int id)
/*     */   {
/* 409 */     int shift = id ^ 1 << depth(id);
/* 410 */     return shift * runLength(id);
/*     */   }
/*     */   
/*     */   private int subpageIdx(int memoryMapIdx) {
/* 414 */     return memoryMapIdx ^ this.maxSubpageAllocs;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 419 */     return "Chunk(" + Integer.toHexString(System.identityHashCode(this)) + ": " + usage() + "%, " + (this.chunkSize - this.freeBytes) + '/' + this.chunkSize + ')';
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\buffer\PoolChunk.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */