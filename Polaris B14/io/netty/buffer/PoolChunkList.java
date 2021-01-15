/*     */ package io.netty.buffer;
/*     */ 
/*     */ import io.netty.util.internal.StringUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class PoolChunkList<T>
/*     */ {
/*     */   private final PoolArena<T> arena;
/*     */   private final PoolChunkList<T> nextList;
/*     */   PoolChunkList<T> prevList;
/*     */   private final int minUsage;
/*     */   private final int maxUsage;
/*     */   private PoolChunk<T> head;
/*     */   
/*     */   PoolChunkList(PoolArena<T> arena, PoolChunkList<T> nextList, int minUsage, int maxUsage)
/*     */   {
/*  35 */     this.arena = arena;
/*  36 */     this.nextList = nextList;
/*  37 */     this.minUsage = minUsage;
/*  38 */     this.maxUsage = maxUsage;
/*     */   }
/*     */   
/*     */   boolean allocate(PooledByteBuf<T> buf, int reqCapacity, int normCapacity) {
/*  42 */     if (this.head == null) {
/*  43 */       return false;
/*     */     }
/*     */     
/*  46 */     PoolChunk<T> cur = this.head;
/*  47 */     for (;;) { long handle = cur.allocate(normCapacity);
/*  48 */       if (handle < 0L) {
/*  49 */         cur = cur.next;
/*  50 */         if (cur == null) {
/*  51 */           return false;
/*     */         }
/*     */       } else {
/*  54 */         cur.initBuf(buf, handle, reqCapacity);
/*  55 */         if (cur.usage() >= this.maxUsage) {
/*  56 */           remove(cur);
/*  57 */           this.nextList.add(cur);
/*     */         }
/*  59 */         return true;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   void free(PoolChunk<T> chunk, long handle) {
/*  65 */     chunk.free(handle);
/*  66 */     if (chunk.usage() < this.minUsage) {
/*  67 */       remove(chunk);
/*  68 */       if (this.prevList == null) {
/*  69 */         assert (chunk.usage() == 0);
/*  70 */         this.arena.destroyChunk(chunk);
/*     */       } else {
/*  72 */         this.prevList.add(chunk);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   void add(PoolChunk<T> chunk) {
/*  78 */     if (chunk.usage() >= this.maxUsage) {
/*  79 */       this.nextList.add(chunk);
/*  80 */       return;
/*     */     }
/*     */     
/*  83 */     chunk.parent = this;
/*  84 */     if (this.head == null) {
/*  85 */       this.head = chunk;
/*  86 */       chunk.prev = null;
/*  87 */       chunk.next = null;
/*     */     } else {
/*  89 */       chunk.prev = null;
/*  90 */       chunk.next = this.head;
/*  91 */       this.head.prev = chunk;
/*  92 */       this.head = chunk;
/*     */     }
/*     */   }
/*     */   
/*     */   private void remove(PoolChunk<T> cur) {
/*  97 */     if (cur == this.head) {
/*  98 */       this.head = cur.next;
/*  99 */       if (this.head != null) {
/* 100 */         this.head.prev = null;
/*     */       }
/*     */     } else {
/* 103 */       PoolChunk<T> next = cur.next;
/* 104 */       cur.prev.next = next;
/* 105 */       if (next != null) {
/* 106 */         next.prev = cur.prev;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 113 */     if (this.head == null) {
/* 114 */       return "none";
/*     */     }
/*     */     
/* 117 */     StringBuilder buf = new StringBuilder();
/* 118 */     PoolChunk<T> cur = this.head;
/* 119 */     for (;;) { buf.append(cur);
/* 120 */       cur = cur.next;
/* 121 */       if (cur == null) {
/*     */         break;
/*     */       }
/* 124 */       buf.append(StringUtil.NEWLINE);
/*     */     }
/*     */     
/* 127 */     return buf.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\buffer\PoolChunkList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */