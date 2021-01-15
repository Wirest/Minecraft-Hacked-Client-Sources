/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
/*    */ import java.util.NoSuchElementException;
/*    */ 
/*    */ public class MapPopulator
/*    */ {
/*    */   public static <K, V> Map<K, V> createMap(Iterable<K> keys, Iterable<V> values)
/*    */   {
/* 12 */     return populateMap(keys, values, Maps.newLinkedHashMap());
/*    */   }
/*    */   
/*    */   public static <K, V> Map<K, V> populateMap(Iterable<K> keys, Iterable<V> values, Map<K, V> map)
/*    */   {
/* 17 */     Iterator<V> iterator = values.iterator();
/*    */     
/* 19 */     for (K k : keys)
/*    */     {
/* 21 */       map.put(k, iterator.next());
/*    */     }
/*    */     
/* 24 */     if (iterator.hasNext())
/*    */     {
/* 26 */       throw new NoSuchElementException();
/*    */     }
/*    */     
/*    */ 
/* 30 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\util\MapPopulator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */