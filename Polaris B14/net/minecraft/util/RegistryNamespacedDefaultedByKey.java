/*    */ package net.minecraft.util;
/*    */ 
/*    */ import org.apache.commons.lang3.Validate;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RegistryNamespacedDefaultedByKey<K, V>
/*    */   extends RegistryNamespaced<K, V>
/*    */ {
/*    */   private final K defaultValueKey;
/*    */   private V defaultValue;
/*    */   
/*    */   public RegistryNamespacedDefaultedByKey(K p_i46017_1_)
/*    */   {
/* 17 */     this.defaultValueKey = p_i46017_1_;
/*    */   }
/*    */   
/*    */   public void register(int id, K p_177775_2_, V p_177775_3_)
/*    */   {
/* 22 */     if (this.defaultValueKey.equals(p_177775_2_))
/*    */     {
/* 24 */       this.defaultValue = p_177775_3_;
/*    */     }
/*    */     
/* 27 */     super.register(id, p_177775_2_, p_177775_3_);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void validateKey()
/*    */   {
/* 35 */     Validate.notNull(this.defaultValueKey);
/*    */   }
/*    */   
/*    */   public V getObject(K name)
/*    */   {
/* 40 */     V v = super.getObject(name);
/* 41 */     return (V)(v == null ? this.defaultValue : v);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public V getObjectById(int id)
/*    */   {
/* 49 */     V v = super.getObjectById(id);
/* 50 */     return (V)(v == null ? this.defaultValue : v);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\util\RegistryNamespacedDefaultedByKey.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */