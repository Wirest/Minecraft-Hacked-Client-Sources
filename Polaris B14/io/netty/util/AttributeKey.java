/*    */ package io.netty.util;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class AttributeKey<T>
/*    */   extends AbstractConstant<AttributeKey<T>>
/*    */ {
/* 27 */   private static final ConstantPool<AttributeKey<Object>> pool = new ConstantPool()
/*    */   {
/*    */     protected AttributeKey<Object> newConstant(int id, String name) {
/* 30 */       return new AttributeKey(id, name, null);
/*    */     }
/*    */   };
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static <T> AttributeKey<T> valueOf(String name)
/*    */   {
/* 39 */     return (AttributeKey)pool.valueOf(name);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public static boolean exists(String name)
/*    */   {
/* 46 */     return pool.exists(name);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static <T> AttributeKey<T> newInstance(String name)
/*    */   {
/* 55 */     return (AttributeKey)pool.newInstance(name);
/*    */   }
/*    */   
/*    */   public static <T> AttributeKey<T> valueOf(Class<?> firstNameComponent, String secondNameComponent)
/*    */   {
/* 60 */     return (AttributeKey)pool.valueOf(firstNameComponent, secondNameComponent);
/*    */   }
/*    */   
/*    */   private AttributeKey(int id, String name) {
/* 64 */     super(id, name);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\AttributeKey.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */