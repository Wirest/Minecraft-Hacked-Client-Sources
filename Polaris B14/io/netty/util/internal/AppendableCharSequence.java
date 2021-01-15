/*     */ package io.netty.util.internal;
/*     */ 
/*     */ import java.util.Arrays;
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
/*     */ public final class AppendableCharSequence
/*     */   implements CharSequence, Appendable
/*     */ {
/*     */   private char[] chars;
/*     */   private int pos;
/*     */   
/*     */   public AppendableCharSequence(int length)
/*     */   {
/*  26 */     if (length < 1) {
/*  27 */       throw new IllegalArgumentException("length: " + length + " (length: >= 1)");
/*     */     }
/*  29 */     this.chars = new char[length];
/*     */   }
/*     */   
/*     */   private AppendableCharSequence(char[] chars) {
/*  33 */     this.chars = chars;
/*  34 */     this.pos = chars.length;
/*     */   }
/*     */   
/*     */   public int length()
/*     */   {
/*  39 */     return this.pos;
/*     */   }
/*     */   
/*     */   public char charAt(int index)
/*     */   {
/*  44 */     if (index > this.pos) {
/*  45 */       throw new IndexOutOfBoundsException();
/*     */     }
/*  47 */     return this.chars[index];
/*     */   }
/*     */   
/*     */   public AppendableCharSequence subSequence(int start, int end)
/*     */   {
/*  52 */     return new AppendableCharSequence(Arrays.copyOfRange(this.chars, start, end));
/*     */   }
/*     */   
/*     */   public AppendableCharSequence append(char c)
/*     */   {
/*  57 */     if (this.pos == this.chars.length) {
/*  58 */       char[] old = this.chars;
/*     */       
/*  60 */       int len = old.length << 1;
/*  61 */       if (len < 0) {
/*  62 */         throw new IllegalStateException();
/*     */       }
/*  64 */       this.chars = new char[len];
/*  65 */       System.arraycopy(old, 0, this.chars, 0, old.length);
/*     */     }
/*  67 */     this.chars[(this.pos++)] = c;
/*  68 */     return this;
/*     */   }
/*     */   
/*     */   public AppendableCharSequence append(CharSequence csq)
/*     */   {
/*  73 */     return append(csq, 0, csq.length());
/*     */   }
/*     */   
/*     */   public AppendableCharSequence append(CharSequence csq, int start, int end)
/*     */   {
/*  78 */     if (csq.length() < end) {
/*  79 */       throw new IndexOutOfBoundsException();
/*     */     }
/*  81 */     int length = end - start;
/*  82 */     if (length > this.chars.length - this.pos) {
/*  83 */       this.chars = expand(this.chars, this.pos + length, this.pos);
/*     */     }
/*  85 */     if ((csq instanceof AppendableCharSequence))
/*     */     {
/*  87 */       AppendableCharSequence seq = (AppendableCharSequence)csq;
/*  88 */       char[] src = seq.chars;
/*  89 */       System.arraycopy(src, start, this.chars, this.pos, length);
/*  90 */       this.pos += length;
/*  91 */       return this;
/*     */     }
/*  93 */     for (int i = start; i < end; i++) {
/*  94 */       this.chars[(this.pos++)] = csq.charAt(i);
/*     */     }
/*     */     
/*  97 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void reset()
/*     */   {
/* 105 */     this.pos = 0;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 110 */     return new String(this.chars, 0, this.pos);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String substring(int start, int end)
/*     */   {
/* 117 */     int length = end - start;
/* 118 */     if ((start > this.pos) || (length > this.pos)) {
/* 119 */       throw new IndexOutOfBoundsException();
/*     */     }
/* 121 */     return new String(this.chars, start, length);
/*     */   }
/*     */   
/*     */   private static char[] expand(char[] array, int neededSpace, int size) {
/* 125 */     int newCapacity = array.length;
/*     */     do
/*     */     {
/* 128 */       newCapacity <<= 1;
/*     */       
/* 130 */       if (newCapacity < 0) {
/* 131 */         throw new IllegalStateException();
/*     */       }
/*     */       
/* 134 */     } while (neededSpace > newCapacity);
/*     */     
/* 136 */     char[] newArray = new char[newCapacity];
/* 137 */     System.arraycopy(array, 0, newArray, 0, size);
/*     */     
/* 139 */     return newArray;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\internal\AppendableCharSequence.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */