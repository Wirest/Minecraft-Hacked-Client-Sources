/*     */ package io.netty.channel.nio;
/*     */ 
/*     */ import java.nio.channels.SelectionKey;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Iterator;
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
/*     */ final class SelectedSelectionKeySet
/*     */   extends AbstractSet<SelectionKey>
/*     */ {
/*     */   private SelectionKey[] keysA;
/*     */   private int keysASize;
/*     */   private SelectionKey[] keysB;
/*     */   private int keysBSize;
/*  29 */   private boolean isA = true;
/*     */   
/*     */   SelectedSelectionKeySet() {
/*  32 */     this.keysA = new SelectionKey['Ѐ'];
/*  33 */     this.keysB = ((SelectionKey[])this.keysA.clone());
/*     */   }
/*     */   
/*     */   public boolean add(SelectionKey o)
/*     */   {
/*  38 */     if (o == null) {
/*  39 */       return false;
/*     */     }
/*     */     
/*  42 */     if (this.isA) {
/*  43 */       int size = this.keysASize;
/*  44 */       this.keysA[(size++)] = o;
/*  45 */       this.keysASize = size;
/*  46 */       if (size == this.keysA.length) {
/*  47 */         doubleCapacityA();
/*     */       }
/*     */     } else {
/*  50 */       int size = this.keysBSize;
/*  51 */       this.keysB[(size++)] = o;
/*  52 */       this.keysBSize = size;
/*  53 */       if (size == this.keysB.length) {
/*  54 */         doubleCapacityB();
/*     */       }
/*     */     }
/*     */     
/*  58 */     return true;
/*     */   }
/*     */   
/*     */   private void doubleCapacityA() {
/*  62 */     SelectionKey[] newKeysA = new SelectionKey[this.keysA.length << 1];
/*  63 */     System.arraycopy(this.keysA, 0, newKeysA, 0, this.keysASize);
/*  64 */     this.keysA = newKeysA;
/*     */   }
/*     */   
/*     */   private void doubleCapacityB() {
/*  68 */     SelectionKey[] newKeysB = new SelectionKey[this.keysB.length << 1];
/*  69 */     System.arraycopy(this.keysB, 0, newKeysB, 0, this.keysBSize);
/*  70 */     this.keysB = newKeysB;
/*     */   }
/*     */   
/*     */   SelectionKey[] flip() {
/*  74 */     if (this.isA) {
/*  75 */       this.isA = false;
/*  76 */       this.keysA[this.keysASize] = null;
/*  77 */       this.keysBSize = 0;
/*  78 */       return this.keysA;
/*     */     }
/*  80 */     this.isA = true;
/*  81 */     this.keysB[this.keysBSize] = null;
/*  82 */     this.keysASize = 0;
/*  83 */     return this.keysB;
/*     */   }
/*     */   
/*     */ 
/*     */   public int size()
/*     */   {
/*  89 */     if (this.isA) {
/*  90 */       return this.keysASize;
/*     */     }
/*  92 */     return this.keysBSize;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean remove(Object o)
/*     */   {
/*  98 */     return false;
/*     */   }
/*     */   
/*     */   public boolean contains(Object o)
/*     */   {
/* 103 */     return false;
/*     */   }
/*     */   
/*     */   public Iterator<SelectionKey> iterator()
/*     */   {
/* 108 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\nio\SelectedSelectionKeySet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */