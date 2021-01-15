/*     */ package io.netty.util;
/*     */ 
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import java.util.concurrent.atomic.AtomicReferenceArray;
/*     */ import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
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
/*     */ public class DefaultAttributeMap
/*     */   implements AttributeMap
/*     */ {
/*     */   private static final AtomicReferenceFieldUpdater<DefaultAttributeMap, AtomicReferenceArray> updater;
/*     */   private static final int BUCKET_SIZE = 4;
/*     */   private static final int MASK = 3;
/*     */   private volatile AtomicReferenceArray<DefaultAttribute<?>> attributes;
/*     */   
/*     */   static
/*     */   {
/*  35 */     AtomicReferenceFieldUpdater<DefaultAttributeMap, AtomicReferenceArray> referenceFieldUpdater = PlatformDependent.newAtomicReferenceFieldUpdater(DefaultAttributeMap.class, "attributes");
/*     */     
/*  37 */     if (referenceFieldUpdater == null) {
/*  38 */       referenceFieldUpdater = AtomicReferenceFieldUpdater.newUpdater(DefaultAttributeMap.class, AtomicReferenceArray.class, "attributes");
/*     */     }
/*     */     
/*  41 */     updater = referenceFieldUpdater;
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
/*     */   public <T> Attribute<T> attr(AttributeKey<T> key)
/*     */   {
/*  54 */     if (key == null) {
/*  55 */       throw new NullPointerException("key");
/*     */     }
/*  57 */     AtomicReferenceArray<DefaultAttribute<?>> attributes = this.attributes;
/*  58 */     if (attributes == null)
/*     */     {
/*  60 */       attributes = new AtomicReferenceArray(4);
/*     */       
/*  62 */       if (!updater.compareAndSet(this, null, attributes)) {
/*  63 */         attributes = this.attributes;
/*     */       }
/*     */     }
/*     */     
/*  67 */     int i = index(key);
/*  68 */     DefaultAttribute<?> head = (DefaultAttribute)attributes.get(i);
/*  69 */     if (head == null)
/*     */     {
/*     */ 
/*  72 */       head = new DefaultAttribute(key);
/*  73 */       if (attributes.compareAndSet(i, null, head))
/*     */       {
/*  75 */         return head;
/*     */       }
/*  77 */       head = (DefaultAttribute)attributes.get(i);
/*     */     }
/*     */     
/*     */ 
/*  81 */     synchronized (head) {
/*  82 */       DefaultAttribute<?> curr = head;
/*     */       
/*  84 */       if ((!curr.removed) && (curr.key == key)) {
/*  85 */         return curr;
/*     */       }
/*     */       
/*  88 */       DefaultAttribute<?> next = curr.next;
/*  89 */       if (next == null) {
/*  90 */         DefaultAttribute<T> attr = new DefaultAttribute(head, key);
/*  91 */         curr.next = attr;
/*  92 */         attr.prev = curr;
/*  93 */         return attr;
/*     */       }
/*  95 */       curr = next;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public <T> boolean hasAttr(AttributeKey<T> key)
/*     */   {
/* 103 */     if (key == null) {
/* 104 */       throw new NullPointerException("key");
/*     */     }
/* 106 */     AtomicReferenceArray<DefaultAttribute<?>> attributes = this.attributes;
/* 107 */     if (attributes == null)
/*     */     {
/* 109 */       return false;
/*     */     }
/*     */     
/* 112 */     int i = index(key);
/* 113 */     DefaultAttribute<?> head = (DefaultAttribute)attributes.get(i);
/* 114 */     if (head == null)
/*     */     {
/* 116 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 120 */     if ((head.key == key) && (!head.removed)) {
/* 121 */       return true;
/*     */     }
/*     */     
/* 124 */     synchronized (head)
/*     */     {
/* 126 */       DefaultAttribute<?> curr = head.next;
/* 127 */       while (curr != null) {
/* 128 */         if ((!curr.removed) && (curr.key == key)) {
/* 129 */           return true;
/*     */         }
/* 131 */         curr = curr.next;
/*     */       }
/* 133 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   private static int index(AttributeKey<?> key) {
/* 138 */     return key.id() & 0x3;
/*     */   }
/*     */   
/*     */ 
/*     */   private static final class DefaultAttribute<T>
/*     */     extends AtomicReference<T>
/*     */     implements Attribute<T>
/*     */   {
/*     */     private static final long serialVersionUID = -2661411462200283011L;
/*     */     
/*     */     private final DefaultAttribute<?> head;
/*     */     
/*     */     private final AttributeKey<T> key;
/*     */     
/*     */     private DefaultAttribute<?> prev;
/*     */     private DefaultAttribute<?> next;
/*     */     private volatile boolean removed;
/*     */     
/*     */     DefaultAttribute(DefaultAttribute<?> head, AttributeKey<T> key)
/*     */     {
/* 158 */       this.head = head;
/* 159 */       this.key = key;
/*     */     }
/*     */     
/*     */     DefaultAttribute(AttributeKey<T> key) {
/* 163 */       this.head = this;
/* 164 */       this.key = key;
/*     */     }
/*     */     
/*     */     public AttributeKey<T> key()
/*     */     {
/* 169 */       return this.key;
/*     */     }
/*     */     
/*     */     public T setIfAbsent(T value)
/*     */     {
/* 174 */       while (!compareAndSet(null, value)) {
/* 175 */         T old = get();
/* 176 */         if (old != null) {
/* 177 */           return old;
/*     */         }
/*     */       }
/* 180 */       return null;
/*     */     }
/*     */     
/*     */     public T getAndRemove()
/*     */     {
/* 185 */       this.removed = true;
/* 186 */       T oldValue = getAndSet(null);
/* 187 */       remove0();
/* 188 */       return oldValue;
/*     */     }
/*     */     
/*     */     public void remove()
/*     */     {
/* 193 */       this.removed = true;
/* 194 */       set(null);
/* 195 */       remove0();
/*     */     }
/*     */     
/*     */     private void remove0() {
/* 199 */       synchronized (this.head)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 205 */         if (this.prev != null) {
/* 206 */           this.prev.next = this.next;
/*     */           
/* 208 */           if (this.next != null) {
/* 209 */             this.next.prev = this.prev;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\DefaultAttributeMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */