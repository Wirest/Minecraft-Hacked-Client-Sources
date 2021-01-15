/*    */ package io.netty.handler.codec.serialization;
/*    */ 
/*    */ import java.lang.ref.Reference;
/*    */ import java.lang.ref.SoftReference;
/*    */ import java.util.Map;
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
/*    */ final class SoftReferenceMap<K, V>
/*    */   extends ReferenceMap<K, V>
/*    */ {
/*    */   SoftReferenceMap(Map<K, Reference<V>> delegate)
/*    */   {
/* 25 */     super(delegate);
/*    */   }
/*    */   
/*    */   Reference<V> fold(V value)
/*    */   {
/* 30 */     return new SoftReference(value);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\serialization\SoftReferenceMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */