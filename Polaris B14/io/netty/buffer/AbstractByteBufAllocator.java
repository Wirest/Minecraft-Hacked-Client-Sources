/*     */ package io.netty.buffer;
/*     */ 
/*     */ import io.netty.util.ResourceLeak;
/*     */ import io.netty.util.ResourceLeakDetector;
/*     */ import io.netty.util.internal.PlatformDependent;
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
/*     */ public abstract class AbstractByteBufAllocator
/*     */   implements ByteBufAllocator
/*     */ {
/*     */   private static final int DEFAULT_INITIAL_CAPACITY = 256;
/*     */   private static final int DEFAULT_MAX_COMPONENTS = 16;
/*     */   private final boolean directByDefault;
/*     */   private final ByteBuf emptyBuf;
/*     */   
/*     */   protected static ByteBuf toLeakAwareBuffer(ByteBuf buf)
/*     */   {
/*     */     ResourceLeak leak;
/*  33 */     switch (ResourceLeakDetector.getLevel()) {
/*     */     case SIMPLE: 
/*  35 */       leak = AbstractByteBuf.leakDetector.open(buf);
/*  36 */       if (leak != null) {
/*  37 */         buf = new SimpleLeakAwareByteBuf(buf, leak);
/*     */       }
/*     */       break;
/*     */     case ADVANCED: 
/*     */     case PARANOID: 
/*  42 */       leak = AbstractByteBuf.leakDetector.open(buf);
/*  43 */       if (leak != null) {
/*  44 */         buf = new AdvancedLeakAwareByteBuf(buf, leak);
/*     */       }
/*     */       break;
/*     */     }
/*  48 */     return buf;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected AbstractByteBufAllocator()
/*     */   {
/*  58 */     this(false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected AbstractByteBufAllocator(boolean preferDirect)
/*     */   {
/*  68 */     this.directByDefault = ((preferDirect) && (PlatformDependent.hasUnsafe()));
/*  69 */     this.emptyBuf = new EmptyByteBuf(this);
/*     */   }
/*     */   
/*     */   public ByteBuf buffer()
/*     */   {
/*  74 */     if (this.directByDefault) {
/*  75 */       return directBuffer();
/*     */     }
/*  77 */     return heapBuffer();
/*     */   }
/*     */   
/*     */   public ByteBuf buffer(int initialCapacity)
/*     */   {
/*  82 */     if (this.directByDefault) {
/*  83 */       return directBuffer(initialCapacity);
/*     */     }
/*  85 */     return heapBuffer(initialCapacity);
/*     */   }
/*     */   
/*     */   public ByteBuf buffer(int initialCapacity, int maxCapacity)
/*     */   {
/*  90 */     if (this.directByDefault) {
/*  91 */       return directBuffer(initialCapacity, maxCapacity);
/*     */     }
/*  93 */     return heapBuffer(initialCapacity, maxCapacity);
/*     */   }
/*     */   
/*     */   public ByteBuf ioBuffer()
/*     */   {
/*  98 */     if (PlatformDependent.hasUnsafe()) {
/*  99 */       return directBuffer(256);
/*     */     }
/* 101 */     return heapBuffer(256);
/*     */   }
/*     */   
/*     */   public ByteBuf ioBuffer(int initialCapacity)
/*     */   {
/* 106 */     if (PlatformDependent.hasUnsafe()) {
/* 107 */       return directBuffer(initialCapacity);
/*     */     }
/* 109 */     return heapBuffer(initialCapacity);
/*     */   }
/*     */   
/*     */   public ByteBuf ioBuffer(int initialCapacity, int maxCapacity)
/*     */   {
/* 114 */     if (PlatformDependent.hasUnsafe()) {
/* 115 */       return directBuffer(initialCapacity, maxCapacity);
/*     */     }
/* 117 */     return heapBuffer(initialCapacity, maxCapacity);
/*     */   }
/*     */   
/*     */   public ByteBuf heapBuffer()
/*     */   {
/* 122 */     return heapBuffer(256, Integer.MAX_VALUE);
/*     */   }
/*     */   
/*     */   public ByteBuf heapBuffer(int initialCapacity)
/*     */   {
/* 127 */     return heapBuffer(initialCapacity, Integer.MAX_VALUE);
/*     */   }
/*     */   
/*     */   public ByteBuf heapBuffer(int initialCapacity, int maxCapacity)
/*     */   {
/* 132 */     if ((initialCapacity == 0) && (maxCapacity == 0)) {
/* 133 */       return this.emptyBuf;
/*     */     }
/* 135 */     validate(initialCapacity, maxCapacity);
/* 136 */     return newHeapBuffer(initialCapacity, maxCapacity);
/*     */   }
/*     */   
/*     */   public ByteBuf directBuffer()
/*     */   {
/* 141 */     return directBuffer(256, Integer.MAX_VALUE);
/*     */   }
/*     */   
/*     */   public ByteBuf directBuffer(int initialCapacity)
/*     */   {
/* 146 */     return directBuffer(initialCapacity, Integer.MAX_VALUE);
/*     */   }
/*     */   
/*     */   public ByteBuf directBuffer(int initialCapacity, int maxCapacity)
/*     */   {
/* 151 */     if ((initialCapacity == 0) && (maxCapacity == 0)) {
/* 152 */       return this.emptyBuf;
/*     */     }
/* 154 */     validate(initialCapacity, maxCapacity);
/* 155 */     return newDirectBuffer(initialCapacity, maxCapacity);
/*     */   }
/*     */   
/*     */   public CompositeByteBuf compositeBuffer()
/*     */   {
/* 160 */     if (this.directByDefault) {
/* 161 */       return compositeDirectBuffer();
/*     */     }
/* 163 */     return compositeHeapBuffer();
/*     */   }
/*     */   
/*     */   public CompositeByteBuf compositeBuffer(int maxNumComponents)
/*     */   {
/* 168 */     if (this.directByDefault) {
/* 169 */       return compositeDirectBuffer(maxNumComponents);
/*     */     }
/* 171 */     return compositeHeapBuffer(maxNumComponents);
/*     */   }
/*     */   
/*     */   public CompositeByteBuf compositeHeapBuffer()
/*     */   {
/* 176 */     return compositeHeapBuffer(16);
/*     */   }
/*     */   
/*     */   public CompositeByteBuf compositeHeapBuffer(int maxNumComponents)
/*     */   {
/* 181 */     return new CompositeByteBuf(this, false, maxNumComponents);
/*     */   }
/*     */   
/*     */   public CompositeByteBuf compositeDirectBuffer()
/*     */   {
/* 186 */     return compositeDirectBuffer(16);
/*     */   }
/*     */   
/*     */   public CompositeByteBuf compositeDirectBuffer(int maxNumComponents)
/*     */   {
/* 191 */     return new CompositeByteBuf(this, true, maxNumComponents);
/*     */   }
/*     */   
/*     */   private static void validate(int initialCapacity, int maxCapacity) {
/* 195 */     if (initialCapacity < 0) {
/* 196 */       throw new IllegalArgumentException("initialCapacity: " + initialCapacity + " (expectd: 0+)");
/*     */     }
/* 198 */     if (initialCapacity > maxCapacity) {
/* 199 */       throw new IllegalArgumentException(String.format("initialCapacity: %d (expected: not greater than maxCapacity(%d)", new Object[] { Integer.valueOf(initialCapacity), Integer.valueOf(maxCapacity) }));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract ByteBuf newHeapBuffer(int paramInt1, int paramInt2);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract ByteBuf newDirectBuffer(int paramInt1, int paramInt2);
/*     */   
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 217 */     return StringUtil.simpleClassName(this) + "(directByDefault: " + this.directByDefault + ')';
/*     */   }
/*     */   
/*     */   public int calculateNewCapacity(int minNewCapacity, int maxCapacity)
/*     */   {
/* 222 */     if (minNewCapacity < 0) {
/* 223 */       throw new IllegalArgumentException("minNewCapacity: " + minNewCapacity + " (expectd: 0+)");
/*     */     }
/* 225 */     if (minNewCapacity > maxCapacity) {
/* 226 */       throw new IllegalArgumentException(String.format("minNewCapacity: %d (expected: not greater than maxCapacity(%d)", new Object[] { Integer.valueOf(minNewCapacity), Integer.valueOf(maxCapacity) }));
/*     */     }
/*     */     
/*     */ 
/* 230 */     int threshold = 4194304;
/*     */     
/* 232 */     if (minNewCapacity == 4194304) {
/* 233 */       return 4194304;
/*     */     }
/*     */     
/*     */ 
/* 237 */     if (minNewCapacity > 4194304) {
/* 238 */       int newCapacity = minNewCapacity / 4194304 * 4194304;
/* 239 */       if (newCapacity > maxCapacity - 4194304) {
/* 240 */         newCapacity = maxCapacity;
/*     */       } else {
/* 242 */         newCapacity += 4194304;
/*     */       }
/* 244 */       return newCapacity;
/*     */     }
/*     */     
/*     */ 
/* 248 */     int newCapacity = 64;
/* 249 */     while (newCapacity < minNewCapacity) {
/* 250 */       newCapacity <<= 1;
/*     */     }
/*     */     
/* 253 */     return Math.min(newCapacity, maxCapacity);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\buffer\AbstractByteBufAllocator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */