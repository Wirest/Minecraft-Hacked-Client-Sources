/*     */ package io.netty.buffer;
/*     */ 
/*     */ import io.netty.util.Recycler.Handle;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
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
/*     */ abstract class PooledByteBuf<T>
/*     */   extends AbstractReferenceCountedByteBuf
/*     */ {
/*     */   private final Recycler.Handle<PooledByteBuf<T>> recyclerHandle;
/*     */   protected PoolChunk<T> chunk;
/*     */   protected long handle;
/*     */   protected T memory;
/*     */   protected int offset;
/*     */   protected int length;
/*     */   int maxLength;
/*     */   Thread initThread;
/*     */   private ByteBuffer tmpNioBuf;
/*     */   
/*     */   protected PooledByteBuf(Recycler.Handle<? extends PooledByteBuf<T>> recyclerHandle, int maxCapacity)
/*     */   {
/*  40 */     super(maxCapacity);
/*  41 */     this.recyclerHandle = recyclerHandle;
/*     */   }
/*     */   
/*     */   void init(PoolChunk<T> chunk, long handle, int offset, int length, int maxLength) {
/*  45 */     assert (handle >= 0L);
/*  46 */     assert (chunk != null);
/*     */     
/*  48 */     this.chunk = chunk;
/*  49 */     this.handle = handle;
/*  50 */     this.memory = chunk.memory;
/*  51 */     this.offset = offset;
/*  52 */     this.length = length;
/*  53 */     this.maxLength = maxLength;
/*  54 */     setIndex(0, 0);
/*  55 */     this.tmpNioBuf = null;
/*  56 */     this.initThread = Thread.currentThread();
/*     */   }
/*     */   
/*     */   void initUnpooled(PoolChunk<T> chunk, int length) {
/*  60 */     assert (chunk != null);
/*     */     
/*  62 */     this.chunk = chunk;
/*  63 */     this.handle = 0L;
/*  64 */     this.memory = chunk.memory;
/*  65 */     this.offset = 0;
/*  66 */     this.length = (this.maxLength = length);
/*  67 */     setIndex(0, 0);
/*  68 */     this.tmpNioBuf = null;
/*  69 */     this.initThread = Thread.currentThread();
/*     */   }
/*     */   
/*     */   public final int capacity()
/*     */   {
/*  74 */     return this.length;
/*     */   }
/*     */   
/*     */   public final ByteBuf capacity(int newCapacity)
/*     */   {
/*  79 */     ensureAccessible();
/*     */     
/*     */ 
/*  82 */     if (this.chunk.unpooled) {
/*  83 */       if (newCapacity == this.length) {
/*  84 */         return this;
/*     */       }
/*     */     }
/*  87 */     else if (newCapacity > this.length) {
/*  88 */       if (newCapacity <= this.maxLength) {
/*  89 */         this.length = newCapacity;
/*  90 */         return this;
/*     */       }
/*  92 */     } else if (newCapacity < this.length) {
/*  93 */       if (newCapacity > this.maxLength >>> 1) {
/*  94 */         if (this.maxLength <= 512) {
/*  95 */           if (newCapacity > this.maxLength - 16) {
/*  96 */             this.length = newCapacity;
/*  97 */             setIndex(Math.min(readerIndex(), newCapacity), Math.min(writerIndex(), newCapacity));
/*  98 */             return this;
/*     */           }
/*     */         } else {
/* 101 */           this.length = newCapacity;
/* 102 */           setIndex(Math.min(readerIndex(), newCapacity), Math.min(writerIndex(), newCapacity));
/* 103 */           return this;
/*     */         }
/*     */       }
/*     */     } else {
/* 107 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 112 */     this.chunk.arena.reallocate(this, newCapacity, true);
/* 113 */     return this;
/*     */   }
/*     */   
/*     */   public final ByteBufAllocator alloc()
/*     */   {
/* 118 */     return this.chunk.arena.parent;
/*     */   }
/*     */   
/*     */   public final ByteOrder order()
/*     */   {
/* 123 */     return ByteOrder.BIG_ENDIAN;
/*     */   }
/*     */   
/*     */   public final ByteBuf unwrap()
/*     */   {
/* 128 */     return null;
/*     */   }
/*     */   
/*     */   protected final ByteBuffer internalNioBuffer() {
/* 132 */     ByteBuffer tmpNioBuf = this.tmpNioBuf;
/* 133 */     if (tmpNioBuf == null) {
/* 134 */       this.tmpNioBuf = (tmpNioBuf = newInternalNioBuffer(this.memory));
/*     */     }
/* 136 */     return tmpNioBuf;
/*     */   }
/*     */   
/*     */   protected abstract ByteBuffer newInternalNioBuffer(T paramT);
/*     */   
/*     */   protected final void deallocate()
/*     */   {
/* 143 */     if (this.handle >= 0L) {
/* 144 */       long handle = this.handle;
/* 145 */       this.handle = -1L;
/* 146 */       this.memory = null;
/* 147 */       boolean sameThread = this.initThread == Thread.currentThread();
/* 148 */       this.initThread = null;
/* 149 */       this.chunk.arena.free(this.chunk, handle, this.maxLength, sameThread);
/* 150 */       recycle();
/*     */     }
/*     */   }
/*     */   
/*     */   private void recycle() {
/* 155 */     this.recyclerHandle.recycle(this);
/*     */   }
/*     */   
/*     */   protected final int idx(int index) {
/* 159 */     return this.offset + index;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\buffer\PooledByteBuf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */