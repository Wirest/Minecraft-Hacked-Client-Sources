/*     */ package io.netty.util;
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
/*     */ public final class Signal
/*     */   extends Error
/*     */   implements Constant<Signal>
/*     */ {
/*     */   private static final long serialVersionUID = -221145131122459977L;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  27 */   private static final ConstantPool<Signal> pool = new ConstantPool()
/*     */   {
/*     */     protected Signal newConstant(int id, String name) {
/*  30 */       return new Signal(id, name, null);
/*     */     }
/*     */   };
/*     */   
/*     */   private final SignalConstant constant;
/*     */   
/*     */   public static Signal valueOf(String name)
/*     */   {
/*  38 */     return (Signal)pool.valueOf(name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static Signal valueOf(Class<?> firstNameComponent, String secondNameComponent)
/*     */   {
/*  45 */     return (Signal)pool.valueOf(firstNameComponent, secondNameComponent);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Signal(int id, String name)
/*     */   {
/*  54 */     this.constant = new SignalConstant(id, name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void expect(Signal signal)
/*     */   {
/*  62 */     if (this != signal) {
/*  63 */       throw new IllegalStateException("unexpected signal: " + signal);
/*     */     }
/*     */   }
/*     */   
/*     */   public Throwable initCause(Throwable cause)
/*     */   {
/*  69 */     return this;
/*     */   }
/*     */   
/*     */   public Throwable fillInStackTrace()
/*     */   {
/*  74 */     return this;
/*     */   }
/*     */   
/*     */   public int id()
/*     */   {
/*  79 */     return this.constant.id();
/*     */   }
/*     */   
/*     */   public String name()
/*     */   {
/*  84 */     return this.constant.name();
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj)
/*     */   {
/*  89 */     return this == obj;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/*  94 */     return System.identityHashCode(this);
/*     */   }
/*     */   
/*     */   public int compareTo(Signal other)
/*     */   {
/*  99 */     if (this == other) {
/* 100 */       return 0;
/*     */     }
/*     */     
/* 103 */     return this.constant.compareTo(other.constant);
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 108 */     return name();
/*     */   }
/*     */   
/*     */   private static final class SignalConstant extends AbstractConstant<SignalConstant> {
/*     */     SignalConstant(int id, String name) {
/* 113 */       super(name);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\Signal.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */