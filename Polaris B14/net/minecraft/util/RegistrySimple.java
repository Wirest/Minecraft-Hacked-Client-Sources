/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import org.apache.commons.lang3.Validate;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ public class RegistrySimple<K, V> implements IRegistry<K, V>
/*    */ {
/* 14 */   private static final Logger logger = ;
/* 15 */   protected final Map<K, V> registryObjects = createUnderlyingMap();
/*    */   
/*    */   protected Map<K, V> createUnderlyingMap()
/*    */   {
/* 19 */     return Maps.newHashMap();
/*    */   }
/*    */   
/*    */   public V getObject(K name)
/*    */   {
/* 24 */     return (V)this.registryObjects.get(name);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void putObject(K p_82595_1_, V p_82595_2_)
/*    */   {
/* 32 */     Validate.notNull(p_82595_1_);
/* 33 */     Validate.notNull(p_82595_2_);
/*    */     
/* 35 */     if (this.registryObjects.containsKey(p_82595_1_))
/*    */     {
/* 37 */       logger.debug("Adding duplicate key '" + p_82595_1_ + "' to registry");
/*    */     }
/*    */     
/* 40 */     this.registryObjects.put(p_82595_1_, p_82595_2_);
/*    */   }
/*    */   
/*    */   public Set<K> getKeys()
/*    */   {
/* 45 */     return Collections.unmodifiableSet(this.registryObjects.keySet());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean containsKey(K p_148741_1_)
/*    */   {
/* 53 */     return this.registryObjects.containsKey(p_148741_1_);
/*    */   }
/*    */   
/*    */   public Iterator<V> iterator()
/*    */   {
/* 58 */     return this.registryObjects.values().iterator();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\util\RegistrySimple.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */