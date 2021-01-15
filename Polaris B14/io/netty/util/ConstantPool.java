/*     */ package io.netty.util;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ public abstract class ConstantPool<T extends Constant<T>>
/*     */ {
/*  31 */   private final Map<String, T> constants = new HashMap();
/*     */   
/*  33 */   private int nextId = 1;
/*     */   
/*     */ 
/*     */ 
/*     */   public T valueOf(Class<?> firstNameComponent, String secondNameComponent)
/*     */   {
/*  39 */     if (firstNameComponent == null) {
/*  40 */       throw new NullPointerException("firstNameComponent");
/*     */     }
/*  42 */     if (secondNameComponent == null) {
/*  43 */       throw new NullPointerException("secondNameComponent");
/*     */     }
/*     */     
/*  46 */     return valueOf(firstNameComponent.getName() + '#' + secondNameComponent);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public T valueOf(String name)
/*     */   {
/*  58 */     if (name == null) {
/*  59 */       throw new NullPointerException("name");
/*     */     }
/*     */     
/*  62 */     if (name.isEmpty()) {
/*  63 */       throw new IllegalArgumentException("empty name");
/*     */     }
/*     */     
/*  66 */     synchronized (this.constants) {
/*  67 */       T c = (Constant)this.constants.get(name);
/*  68 */       if (c == null) {
/*  69 */         c = newConstant(this.nextId, name);
/*  70 */         this.constants.put(name, c);
/*  71 */         this.nextId += 1;
/*     */       }
/*     */       
/*  74 */       return c;
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public boolean exists(String name)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: ldc 63
/*     */     //   3: invokestatic 103	io/netty/util/internal/ObjectUtil:checkNotNull	(Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;
/*     */     //   6: pop
/*     */     //   7: aload_0
/*     */     //   8: getfield 20	io/netty/util/ConstantPool:constants	Ljava/util/Map;
/*     */     //   11: dup
/*     */     //   12: astore_2
/*     */     //   13: monitorenter
/*     */     //   14: aload_0
/*     */     //   15: getfield 20	io/netty/util/ConstantPool:constants	Ljava/util/Map;
/*     */     //   18: aload_1
/*     */     //   19: invokeinterface 107 2 0
/*     */     //   24: aload_2
/*     */     //   25: monitorexit
/*     */     //   26: ireturn
/*     */     //   27: astore_3
/*     */     //   28: aload_2
/*     */     //   29: monitorexit
/*     */     //   30: aload_3
/*     */     //   31: athrow
/*     */     // Line number table:
/*     */     //   Java source line #82	-> byte code offset #0
/*     */     //   Java source line #83	-> byte code offset #7
/*     */     //   Java source line #84	-> byte code offset #14
/*     */     //   Java source line #85	-> byte code offset #27
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	32	0	this	ConstantPool<T>
/*     */     //   0	32	1	name	String
/*     */     //   12	17	2	Ljava/lang/Object;	Object
/*     */     //   27	4	3	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   14	26	27	finally
/*     */     //   27	30	27	finally
/*     */   }
/*     */   
/*     */   public T newInstance(String name)
/*     */   {
/*  94 */     if (name == null) {
/*  95 */       throw new NullPointerException("name");
/*     */     }
/*     */     
/*  98 */     if (name.isEmpty()) {
/*  99 */       throw new IllegalArgumentException("empty name");
/*     */     }
/*     */     
/* 102 */     synchronized (this.constants) {
/* 103 */       T c = (Constant)this.constants.get(name);
/* 104 */       if (c == null) {
/* 105 */         c = newConstant(this.nextId, name);
/* 106 */         this.constants.put(name, c);
/* 107 */         this.nextId += 1;
/*     */       } else {
/* 109 */         throw new IllegalArgumentException(String.format("'%s' is already in use", new Object[] { name }));
/*     */       }
/* 111 */       return c;
/*     */     }
/*     */   }
/*     */   
/*     */   protected abstract T newConstant(int paramInt, String paramString);
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\ConstantPool.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */