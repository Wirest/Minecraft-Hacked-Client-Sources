/*     */ package io.netty.util;
/*     */ 
/*     */ import io.netty.util.concurrent.FastThreadLocal;
/*     */ import io.netty.util.internal.SystemPropertyUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.util.Arrays;
/*     */ import java.util.Map;
/*     */ import java.util.WeakHashMap;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Recycler<T>
/*     */ {
/*  37 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(Recycler.class);
/*     */   
/*  39 */   private static final AtomicInteger ID_GENERATOR = new AtomicInteger(Integer.MIN_VALUE);
/*  40 */   private static final int OWN_THREAD_ID = ID_GENERATOR.getAndIncrement();
/*     */   
/*     */ 
/*     */   private static final int DEFAULT_MAX_CAPACITY;
/*     */   
/*     */ 
/*     */   static
/*     */   {
/*  48 */     int maxCapacity = SystemPropertyUtil.getInt("io.netty.recycler.maxCapacity", 0);
/*  49 */     if (maxCapacity <= 0)
/*     */     {
/*  51 */       maxCapacity = 262144;
/*     */     }
/*     */     
/*  54 */     DEFAULT_MAX_CAPACITY = maxCapacity;
/*  55 */     if (logger.isDebugEnabled())
/*  56 */       logger.debug("-Dio.netty.recycler.maxCapacity: {}", Integer.valueOf(DEFAULT_MAX_CAPACITY));
/*     */   }
/*     */   
/*  59 */   private static final int INITIAL_CAPACITY = Math.min(DEFAULT_MAX_CAPACITY, 256);
/*     */   
/*     */   private final int maxCapacity;
/*     */   
/*  63 */   private final FastThreadLocal<Stack<T>> threadLocal = new FastThreadLocal()
/*     */   {
/*     */     protected Recycler.Stack<T> initialValue() {
/*  66 */       return new Recycler.Stack(Recycler.this, Thread.currentThread(), Recycler.this.maxCapacity);
/*     */     }
/*     */   };
/*     */   
/*     */   protected Recycler() {
/*  71 */     this(DEFAULT_MAX_CAPACITY);
/*     */   }
/*     */   
/*     */   protected Recycler(int maxCapacity) {
/*  75 */     this.maxCapacity = Math.max(0, maxCapacity);
/*     */   }
/*     */   
/*     */   public final T get()
/*     */   {
/*  80 */     Stack<T> stack = (Stack)this.threadLocal.get();
/*  81 */     DefaultHandle<T> handle = stack.pop();
/*  82 */     if (handle == null) {
/*  83 */       handle = stack.newHandle();
/*  84 */       handle.value = newObject(handle);
/*     */     }
/*  86 */     return (T)handle.value;
/*     */   }
/*     */   
/*     */   public final boolean recycle(T o, Handle<T> handle) {
/*  90 */     DefaultHandle<T> h = (DefaultHandle)handle;
/*  91 */     if (h.stack.parent != this) {
/*  92 */       return false;
/*     */     }
/*     */     
/*  95 */     h.recycle(o);
/*  96 */     return true;
/*     */   }
/*     */   
/*     */   final int threadLocalCapacity() {
/* 100 */     return ((Stack)this.threadLocal.get()).elements.length;
/*     */   }
/*     */   
/*     */   final int threadLocalSize() {
/* 104 */     return ((Stack)this.threadLocal.get()).size;
/*     */   }
/*     */   
/*     */   protected abstract T newObject(Handle<T> paramHandle);
/*     */   
/*     */   public static abstract interface Handle<T>
/*     */   {
/*     */     public abstract void recycle(T paramT);
/*     */   }
/*     */   
/*     */   static final class DefaultHandle<T> implements Recycler.Handle<T> {
/*     */     private int lastRecycledId;
/*     */     private int recycleId;
/*     */     private Recycler.Stack<?> stack;
/*     */     private Object value;
/*     */     
/*     */     DefaultHandle(Recycler.Stack<?> stack) {
/* 121 */       this.stack = stack;
/*     */     }
/*     */     
/*     */     public void recycle(Object object)
/*     */     {
/* 126 */       if (object != this.value) {
/* 127 */         throw new IllegalArgumentException("object does not belong to handle");
/*     */       }
/* 129 */       Thread thread = Thread.currentThread();
/* 130 */       if (thread == this.stack.thread) {
/* 131 */         this.stack.push(this);
/* 132 */         return;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 137 */       Map<Recycler.Stack<?>, Recycler.WeakOrderQueue> delayedRecycled = (Map)Recycler.DELAYED_RECYCLED.get();
/* 138 */       Recycler.WeakOrderQueue queue = (Recycler.WeakOrderQueue)delayedRecycled.get(this.stack);
/* 139 */       if (queue == null) {
/* 140 */         delayedRecycled.put(this.stack, queue = new Recycler.WeakOrderQueue(this.stack, thread));
/*     */       }
/* 142 */       queue.add(this);
/*     */     }
/*     */   }
/*     */   
/* 146 */   private static final FastThreadLocal<Map<Stack<?>, WeakOrderQueue>> DELAYED_RECYCLED = new FastThreadLocal()
/*     */   {
/*     */     protected Map<Recycler.Stack<?>, Recycler.WeakOrderQueue> initialValue()
/*     */     {
/* 150 */       return new WeakHashMap();
/*     */     }
/*     */   };
/*     */   
/*     */   private static final class WeakOrderQueue {
/*     */     private static final int LINK_CAPACITY = 16;
/*     */     private Link head;
/*     */     private Link tail;
/*     */     private WeakOrderQueue next;
/*     */     private final WeakReference<Thread> owner;
/*     */     
/*     */     private static final class Link extends AtomicInteger {
/* 162 */       private final Recycler.DefaultHandle<?>[] elements = new Recycler.DefaultHandle[16];
/*     */       
/*     */ 
/*     */       private int readIndex;
/*     */       
/*     */ 
/*     */       private Link next;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 173 */     private final int id = Recycler.ID_GENERATOR.getAndIncrement();
/*     */     
/*     */     WeakOrderQueue(Recycler.Stack<?> stack, Thread thread) {
/* 176 */       this.head = (this.tail = new Link(null));
/* 177 */       this.owner = new WeakReference(thread);
/* 178 */       synchronized (stack) {
/* 179 */         this.next = stack.head;
/* 180 */         stack.head = this;
/*     */       }
/*     */     }
/*     */     
/*     */     void add(Recycler.DefaultHandle<?> handle) {
/* 185 */       Recycler.DefaultHandle.access$902(handle, this.id);
/*     */       
/* 187 */       Link tail = this.tail;
/*     */       int writeIndex;
/* 189 */       if ((writeIndex = tail.get()) == 16) {
/* 190 */         this.tail = (tail = tail.next = new Link(null));
/* 191 */         writeIndex = tail.get();
/*     */       }
/* 193 */       tail.elements[writeIndex] = handle;
/* 194 */       Recycler.DefaultHandle.access$202(handle, null);
/*     */       
/*     */ 
/* 197 */       tail.lazySet(writeIndex + 1);
/*     */     }
/*     */     
/*     */     boolean hasFinalData() {
/* 201 */       return this.tail.readIndex != this.tail.get();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     boolean transfer(Recycler.Stack<?> dst)
/*     */     {
/* 208 */       Link head = this.head;
/* 209 */       if (head == null) {
/* 210 */         return false;
/*     */       }
/*     */       
/* 213 */       if (head.readIndex == 16) {
/* 214 */         if (head.next == null) {
/* 215 */           return false;
/*     */         }
/* 217 */         this.head = (head = head.next);
/*     */       }
/*     */       
/* 220 */       int srcStart = head.readIndex;
/* 221 */       int srcEnd = head.get();
/* 222 */       int srcSize = srcEnd - srcStart;
/* 223 */       if (srcSize == 0) {
/* 224 */         return false;
/*     */       }
/*     */       
/* 227 */       int dstSize = dst.size;
/* 228 */       int expectedCapacity = dstSize + srcSize;
/*     */       
/* 230 */       if (expectedCapacity > dst.elements.length) {
/* 231 */         int actualCapacity = dst.increaseCapacity(expectedCapacity);
/* 232 */         srcEnd = Math.min(srcStart + actualCapacity - dstSize, srcEnd);
/*     */       }
/*     */       
/* 235 */       if (srcStart != srcEnd) {
/* 236 */         Recycler.DefaultHandle[] srcElems = head.elements;
/* 237 */         Recycler.DefaultHandle[] dstElems = dst.elements;
/* 238 */         int newDstSize = dstSize;
/* 239 */         for (int i = srcStart; i < srcEnd; i++) {
/* 240 */           Recycler.DefaultHandle element = srcElems[i];
/* 241 */           if (Recycler.DefaultHandle.access$1300(element) == 0) {
/* 242 */             Recycler.DefaultHandle.access$1302(element, Recycler.DefaultHandle.access$900(element));
/* 243 */           } else if (Recycler.DefaultHandle.access$1300(element) != Recycler.DefaultHandle.access$900(element)) {
/* 244 */             throw new IllegalStateException("recycled already");
/*     */           }
/* 246 */           Recycler.DefaultHandle.access$202(element, dst);
/* 247 */           dstElems[(newDstSize++)] = element;
/* 248 */           srcElems[i] = null;
/*     */         }
/* 250 */         dst.size = newDstSize;
/*     */         
/* 252 */         if ((srcEnd == 16) && (head.next != null)) {
/* 253 */           this.head = head.next;
/*     */         }
/*     */         
/* 256 */         head.readIndex = srcEnd;
/* 257 */         return true;
/*     */       }
/*     */       
/* 260 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   static final class Stack<T>
/*     */   {
/*     */     final Recycler<T> parent;
/*     */     
/*     */     final Thread thread;
/*     */     
/*     */     private Recycler.DefaultHandle<?>[] elements;
/*     */     
/*     */     private final int maxCapacity;
/*     */     private int size;
/*     */     private volatile Recycler.WeakOrderQueue head;
/*     */     private Recycler.WeakOrderQueue cursor;
/*     */     private Recycler.WeakOrderQueue prev;
/*     */     
/*     */     Stack(Recycler<T> parent, Thread thread, int maxCapacity)
/*     */     {
/* 281 */       this.parent = parent;
/* 282 */       this.thread = thread;
/* 283 */       this.maxCapacity = maxCapacity;
/* 284 */       this.elements = new Recycler.DefaultHandle[Math.min(Recycler.INITIAL_CAPACITY, maxCapacity)];
/*     */     }
/*     */     
/*     */     int increaseCapacity(int expectedCapacity) {
/* 288 */       int newCapacity = this.elements.length;
/* 289 */       int maxCapacity = this.maxCapacity;
/*     */       do {
/* 291 */         newCapacity <<= 1;
/* 292 */       } while ((newCapacity < expectedCapacity) && (newCapacity < maxCapacity));
/*     */       
/* 294 */       newCapacity = Math.min(newCapacity, maxCapacity);
/* 295 */       if (newCapacity != this.elements.length) {
/* 296 */         this.elements = ((Recycler.DefaultHandle[])Arrays.copyOf(this.elements, newCapacity));
/*     */       }
/*     */       
/* 299 */       return newCapacity;
/*     */     }
/*     */     
/*     */     Recycler.DefaultHandle<T> pop()
/*     */     {
/* 304 */       int size = this.size;
/* 305 */       if (size == 0) {
/* 306 */         if (!scavenge()) {
/* 307 */           return null;
/*     */         }
/* 309 */         size = this.size;
/*     */       }
/* 311 */       size--;
/* 312 */       Recycler.DefaultHandle ret = this.elements[size];
/* 313 */       if (Recycler.DefaultHandle.access$900(ret) != Recycler.DefaultHandle.access$1300(ret)) {
/* 314 */         throw new IllegalStateException("recycled multiple times");
/*     */       }
/* 316 */       Recycler.DefaultHandle.access$1302(ret, 0);
/* 317 */       Recycler.DefaultHandle.access$902(ret, 0);
/* 318 */       this.size = size;
/* 319 */       return ret;
/*     */     }
/*     */     
/*     */     boolean scavenge()
/*     */     {
/* 324 */       if (scavengeSome()) {
/* 325 */         return true;
/*     */       }
/*     */       
/*     */ 
/* 329 */       this.prev = null;
/* 330 */       this.cursor = this.head;
/* 331 */       return false;
/*     */     }
/*     */     
/*     */     boolean scavengeSome() {
/* 335 */       Recycler.WeakOrderQueue cursor = this.cursor;
/* 336 */       if (cursor == null) {
/* 337 */         cursor = this.head;
/* 338 */         if (cursor == null) {
/* 339 */           return false;
/*     */         }
/*     */       }
/*     */       
/* 343 */       boolean success = false;
/* 344 */       Recycler.WeakOrderQueue prev = this.prev;
/*     */       do {
/* 346 */         if (cursor.transfer(this)) {
/* 347 */           success = true;
/* 348 */           break;
/*     */         }
/*     */         
/* 351 */         Recycler.WeakOrderQueue next = Recycler.WeakOrderQueue.access$1500(cursor);
/* 352 */         if (Recycler.WeakOrderQueue.access$1600(cursor).get() == null)
/*     */         {
/*     */ 
/*     */ 
/* 356 */           if (cursor.hasFinalData())
/*     */           {
/* 358 */             while (cursor.transfer(this)) {
/* 359 */               success = true;
/*     */             }
/*     */           }
/*     */           
/*     */ 
/*     */ 
/* 365 */           if (prev != null) {
/* 366 */             Recycler.WeakOrderQueue.access$1502(prev, next);
/*     */           }
/*     */         } else {
/* 369 */           prev = cursor;
/*     */         }
/*     */         
/* 372 */         cursor = next;
/*     */       }
/* 374 */       while ((cursor != null) && (!success));
/*     */       
/* 376 */       this.prev = prev;
/* 377 */       this.cursor = cursor;
/* 378 */       return success;
/*     */     }
/*     */     
/*     */     void push(Recycler.DefaultHandle<?> item) {
/* 382 */       if ((Recycler.DefaultHandle.access$1300(item) | Recycler.DefaultHandle.access$900(item)) != 0) {
/* 383 */         throw new IllegalStateException("recycled already");
/*     */       }
/* 385 */       Recycler.DefaultHandle.access$1302(item, Recycler.DefaultHandle.access$902(item, Recycler.OWN_THREAD_ID));
/*     */       
/* 387 */       int size = this.size;
/* 388 */       if (size >= this.maxCapacity)
/*     */       {
/* 390 */         return;
/*     */       }
/* 392 */       if (size == this.elements.length) {
/* 393 */         this.elements = ((Recycler.DefaultHandle[])Arrays.copyOf(this.elements, Math.min(size << 1, this.maxCapacity)));
/*     */       }
/*     */       
/* 396 */       this.elements[size] = item;
/* 397 */       this.size = (size + 1);
/*     */     }
/*     */     
/*     */     Recycler.DefaultHandle<T> newHandle() {
/* 401 */       return new Recycler.DefaultHandle(this);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\Recycler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */