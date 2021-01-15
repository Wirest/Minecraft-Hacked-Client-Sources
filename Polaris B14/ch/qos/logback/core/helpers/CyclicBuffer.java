/*     */ package ch.qos.logback.core.helpers;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CyclicBuffer<E>
/*     */ {
/*     */   E[] ea;
/*     */   int first;
/*     */   int last;
/*     */   int numElems;
/*     */   int maxSize;
/*     */   
/*     */   public CyclicBuffer(int maxSize)
/*     */     throws IllegalArgumentException
/*     */   {
/*  44 */     if (maxSize < 1) {
/*  45 */       throw new IllegalArgumentException("The maxSize argument (" + maxSize + ") is not a positive integer.");
/*     */     }
/*     */     
/*  48 */     init(maxSize);
/*     */   }
/*     */   
/*     */   public CyclicBuffer(CyclicBuffer<E> other) {
/*  52 */     this.maxSize = other.maxSize;
/*  53 */     this.ea = ((Object[])new Object[this.maxSize]);
/*  54 */     System.arraycopy(other.ea, 0, this.ea, 0, this.maxSize);
/*  55 */     this.last = other.last;
/*  56 */     this.first = other.first;
/*  57 */     this.numElems = other.numElems;
/*     */   }
/*     */   
/*     */   private void init(int maxSize)
/*     */   {
/*  62 */     this.maxSize = maxSize;
/*  63 */     this.ea = ((Object[])new Object[maxSize]);
/*  64 */     this.first = 0;
/*  65 */     this.last = 0;
/*  66 */     this.numElems = 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void clear()
/*     */   {
/*  73 */     init(this.maxSize);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void add(E event)
/*     */   {
/*  81 */     this.ea[this.last] = event;
/*  82 */     if (++this.last == this.maxSize) {
/*  83 */       this.last = 0;
/*     */     }
/*  85 */     if (this.numElems < this.maxSize) {
/*  86 */       this.numElems += 1;
/*  87 */     } else if (++this.first == this.maxSize) {
/*  88 */       this.first = 0;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public E get(int i)
/*     */   {
/*  97 */     if ((i < 0) || (i >= this.numElems)) {
/*  98 */       return null;
/*     */     }
/* 100 */     return (E)this.ea[((this.first + i) % this.maxSize)];
/*     */   }
/*     */   
/*     */   public int getMaxSize() {
/* 104 */     return this.maxSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public E get()
/*     */   {
/* 112 */     E r = null;
/* 113 */     if (this.numElems > 0) {
/* 114 */       this.numElems -= 1;
/* 115 */       r = this.ea[this.first];
/* 116 */       this.ea[this.first] = null;
/* 117 */       if (++this.first == this.maxSize)
/* 118 */         this.first = 0;
/*     */     }
/* 120 */     return r;
/*     */   }
/*     */   
/*     */   public List<E> asList() {
/* 124 */     List<E> tList = new ArrayList();
/* 125 */     for (int i = 0; i < length(); i++) {
/* 126 */       tList.add(get(i));
/*     */     }
/* 128 */     return tList;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int length()
/*     */   {
/* 136 */     return this.numElems;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void resize(int newSize)
/*     */   {
/* 147 */     if (newSize < 0) {
/* 148 */       throw new IllegalArgumentException("Negative array size [" + newSize + "] not allowed.");
/*     */     }
/*     */     
/* 151 */     if (newSize == this.numElems) {
/* 152 */       return;
/*     */     }
/*     */     
/* 155 */     E[] temp = (Object[])new Object[newSize];
/*     */     
/* 157 */     int loopLen = newSize < this.numElems ? newSize : this.numElems;
/*     */     
/* 159 */     for (int i = 0; i < loopLen; i++) {
/* 160 */       temp[i] = this.ea[this.first];
/* 161 */       this.ea[this.first] = null;
/* 162 */       if (++this.first == this.numElems)
/* 163 */         this.first = 0;
/*     */     }
/* 165 */     this.ea = temp;
/* 166 */     this.first = 0;
/* 167 */     this.numElems = loopLen;
/* 168 */     this.maxSize = newSize;
/* 169 */     if (loopLen == newSize) {
/* 170 */       this.last = 0;
/*     */     } else {
/* 172 */       this.last = loopLen;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\helpers\CyclicBuffer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */