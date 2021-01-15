/*     */ package io.netty.util.internal;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Queue;
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
/*     */ final class MpscLinkedQueue<E>
/*     */   extends MpscLinkedQueueTailRef<E>
/*     */   implements Queue<E>
/*     */ {
/*     */   private static final long serialVersionUID = -1878402552271506449L;
/*     */   long p00;
/*     */   long p01;
/*     */   long p02;
/*     */   long p03;
/*     */   long p04;
/*     */   long p05;
/*     */   long p06;
/*     */   long p07;
/*     */   long p30;
/*     */   long p31;
/*     */   long p32;
/*     */   long p33;
/*     */   long p34;
/*     */   long p35;
/*     */   long p36;
/*     */   long p37;
/*     */   
/*     */   MpscLinkedQueue()
/*     */   {
/*  91 */     MpscLinkedQueueNode<E> tombstone = new DefaultNode(null);
/*  92 */     setHeadRef(tombstone);
/*  93 */     setTailRef(tombstone);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private MpscLinkedQueueNode<E> peekNode()
/*     */   {
/* 100 */     MpscLinkedQueueNode<E> head = headRef();
/* 101 */     MpscLinkedQueueNode<E> next = head.next();
/* 102 */     if ((next == null) && (head != tailRef()))
/*     */     {
/*     */ 
/*     */       do
/*     */       {
/*     */ 
/* 108 */         next = head.next();
/* 109 */       } while (next == null);
/*     */     }
/* 111 */     return next;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean offer(E value)
/*     */   {
/* 117 */     if (value == null) {
/* 118 */       throw new NullPointerException("value");
/*     */     }
/*     */     
/*     */     MpscLinkedQueueNode<E> newTail;
/* 122 */     if ((value instanceof MpscLinkedQueueNode)) {
/* 123 */       MpscLinkedQueueNode<E> newTail = (MpscLinkedQueueNode)value;
/* 124 */       newTail.setNext(null);
/*     */     } else {
/* 126 */       newTail = new DefaultNode(value);
/*     */     }
/*     */     
/* 129 */     MpscLinkedQueueNode<E> oldTail = getAndSetTailRef(newTail);
/* 130 */     oldTail.setNext(newTail);
/* 131 */     return true;
/*     */   }
/*     */   
/*     */   public E poll()
/*     */   {
/* 136 */     MpscLinkedQueueNode<E> next = peekNode();
/* 137 */     if (next == null) {
/* 138 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 142 */     MpscLinkedQueueNode<E> oldHead = headRef();
/*     */     
/*     */ 
/*     */ 
/* 146 */     lazySetHeadRef(next);
/*     */     
/*     */ 
/* 149 */     oldHead.unlink();
/*     */     
/* 151 */     return (E)next.clearMaybe();
/*     */   }
/*     */   
/*     */   public E peek()
/*     */   {
/* 156 */     MpscLinkedQueueNode<E> next = peekNode();
/* 157 */     if (next == null) {
/* 158 */       return null;
/*     */     }
/* 160 */     return (E)next.value();
/*     */   }
/*     */   
/*     */   public int size()
/*     */   {
/* 165 */     int count = 0;
/* 166 */     MpscLinkedQueueNode<E> n = peekNode();
/*     */     
/* 168 */     while (n != null)
/*     */     {
/*     */ 
/* 171 */       count++;
/* 172 */       n = n.next();
/*     */     }
/* 174 */     return count;
/*     */   }
/*     */   
/*     */   public boolean isEmpty()
/*     */   {
/* 179 */     return peekNode() == null;
/*     */   }
/*     */   
/*     */   public boolean contains(Object o)
/*     */   {
/* 184 */     MpscLinkedQueueNode<E> n = peekNode();
/*     */     
/* 186 */     while (n != null)
/*     */     {
/*     */ 
/* 189 */       if (n.value() == o) {
/* 190 */         return true;
/*     */       }
/* 192 */       n = n.next();
/*     */     }
/* 194 */     return false;
/*     */   }
/*     */   
/*     */   public Iterator<E> iterator()
/*     */   {
/* 199 */     new Iterator() {
/* 200 */       private MpscLinkedQueueNode<E> node = MpscLinkedQueue.this.peekNode();
/*     */       
/*     */       public boolean hasNext()
/*     */       {
/* 204 */         return this.node != null;
/*     */       }
/*     */       
/*     */       public E next()
/*     */       {
/* 209 */         MpscLinkedQueueNode<E> node = this.node;
/* 210 */         if (node == null) {
/* 211 */           throw new NoSuchElementException();
/*     */         }
/* 213 */         E value = node.value();
/* 214 */         this.node = node.next();
/* 215 */         return value;
/*     */       }
/*     */       
/*     */       public void remove()
/*     */       {
/* 220 */         throw new UnsupportedOperationException();
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */   public boolean add(E e)
/*     */   {
/* 227 */     if (offer(e)) {
/* 228 */       return true;
/*     */     }
/* 230 */     throw new IllegalStateException("queue full");
/*     */   }
/*     */   
/*     */   public E remove()
/*     */   {
/* 235 */     E e = poll();
/* 236 */     if (e != null) {
/* 237 */       return e;
/*     */     }
/* 239 */     throw new NoSuchElementException();
/*     */   }
/*     */   
/*     */   public E element()
/*     */   {
/* 244 */     E e = peek();
/* 245 */     if (e != null) {
/* 246 */       return e;
/*     */     }
/* 248 */     throw new NoSuchElementException();
/*     */   }
/*     */   
/*     */   public Object[] toArray()
/*     */   {
/* 253 */     Object[] array = new Object[size()];
/* 254 */     Iterator<E> it = iterator();
/* 255 */     for (int i = 0; i < array.length; i++) {
/* 256 */       if (it.hasNext()) {
/* 257 */         array[i] = it.next();
/*     */       } else {
/* 259 */         return Arrays.copyOf(array, i);
/*     */       }
/*     */     }
/* 262 */     return array;
/*     */   }
/*     */   
/*     */ 
/*     */   public <T> T[] toArray(T[] a)
/*     */   {
/* 268 */     int size = size();
/*     */     T[] array;
/* 270 */     T[] array; if (a.length >= size) {
/* 271 */       array = a;
/*     */     } else {
/* 273 */       array = (Object[])Array.newInstance(a.getClass().getComponentType(), size);
/*     */     }
/*     */     
/* 276 */     Iterator<E> it = iterator();
/* 277 */     for (int i = 0; i < array.length; i++) {
/* 278 */       if (it.hasNext()) {
/* 279 */         array[i] = it.next();
/*     */       } else {
/* 281 */         if (a == array) {
/* 282 */           array[i] = null;
/* 283 */           return array;
/*     */         }
/*     */         
/* 286 */         if (a.length < i) {
/* 287 */           return Arrays.copyOf(array, i);
/*     */         }
/*     */         
/* 290 */         System.arraycopy(array, 0, a, 0, i);
/* 291 */         if (a.length > i) {
/* 292 */           a[i] = null;
/*     */         }
/* 294 */         return a;
/*     */       }
/*     */     }
/* 297 */     return array;
/*     */   }
/*     */   
/*     */   public boolean remove(Object o)
/*     */   {
/* 302 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public boolean containsAll(Collection<?> c)
/*     */   {
/* 307 */     for (Object e : c) {
/* 308 */       if (!contains(e)) {
/* 309 */         return false;
/*     */       }
/*     */     }
/* 312 */     return true;
/*     */   }
/*     */   
/*     */   public boolean addAll(Collection<? extends E> c)
/*     */   {
/* 317 */     if (c == null) {
/* 318 */       throw new NullPointerException("c");
/*     */     }
/* 320 */     if (c == this) {
/* 321 */       throw new IllegalArgumentException("c == this");
/*     */     }
/*     */     
/* 324 */     boolean modified = false;
/* 325 */     for (E e : c) {
/* 326 */       add(e);
/* 327 */       modified = true;
/*     */     }
/* 329 */     return modified;
/*     */   }
/*     */   
/*     */   public boolean removeAll(Collection<?> c)
/*     */   {
/* 334 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public boolean retainAll(Collection<?> c)
/*     */   {
/* 339 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public void clear()
/*     */   {
/* 344 */     while (poll() != null) {}
/*     */   }
/*     */   
/*     */   private void writeObject(ObjectOutputStream out)
/*     */     throws IOException
/*     */   {
/* 350 */     out.defaultWriteObject();
/* 351 */     for (E e : this) {
/* 352 */       out.writeObject(e);
/*     */     }
/* 354 */     out.writeObject(null);
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 358 */     in.defaultReadObject();
/*     */     
/* 360 */     MpscLinkedQueueNode<E> tombstone = new DefaultNode(null);
/* 361 */     setHeadRef(tombstone);
/* 362 */     setTailRef(tombstone);
/*     */     
/*     */     for (;;)
/*     */     {
/* 366 */       E e = in.readObject();
/* 367 */       if (e == null) {
/*     */         break;
/*     */       }
/* 370 */       add(e);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class DefaultNode<T> extends MpscLinkedQueueNode<T>
/*     */   {
/*     */     private T value;
/*     */     
/*     */     DefaultNode(T value) {
/* 379 */       this.value = value;
/*     */     }
/*     */     
/*     */     public T value()
/*     */     {
/* 384 */       return (T)this.value;
/*     */     }
/*     */     
/*     */     protected T clearMaybe()
/*     */     {
/* 389 */       T value = this.value;
/* 390 */       this.value = null;
/* 391 */       return value;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\internal\MpscLinkedQueue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */