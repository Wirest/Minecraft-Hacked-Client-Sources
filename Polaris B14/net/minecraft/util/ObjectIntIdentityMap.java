/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.google.common.base.Predicates;
/*    */ import com.google.common.collect.Iterators;
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.IdentityHashMap;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ 
/*    */ public class ObjectIntIdentityMap implements IObjectIntIterable
/*    */ {
/* 12 */   private final IdentityHashMap identityMap = new IdentityHashMap(512);
/* 13 */   private final List objectList = Lists.newArrayList();
/*    */   private static final String __OBFID = "CL_00001203";
/*    */   
/*    */   public void put(Object key, int value)
/*    */   {
/* 18 */     this.identityMap.put(key, Integer.valueOf(value));
/*    */     
/* 20 */     while (this.objectList.size() <= value)
/*    */     {
/* 22 */       this.objectList.add(null);
/*    */     }
/*    */     
/* 25 */     this.objectList.set(value, key);
/*    */   }
/*    */   
/*    */   public int get(Object key)
/*    */   {
/* 30 */     Integer integer = (Integer)this.identityMap.get(key);
/* 31 */     return integer == null ? -1 : integer.intValue();
/*    */   }
/*    */   
/*    */   public final Object getByValue(int value)
/*    */   {
/* 36 */     return (value >= 0) && (value < this.objectList.size()) ? this.objectList.get(value) : null;
/*    */   }
/*    */   
/*    */   public Iterator iterator()
/*    */   {
/* 41 */     return Iterators.filter(this.objectList.iterator(), Predicates.notNull());
/*    */   }
/*    */   
/*    */   public List getObjectList()
/*    */   {
/* 46 */     return this.objectList;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\util\ObjectIntIdentityMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */