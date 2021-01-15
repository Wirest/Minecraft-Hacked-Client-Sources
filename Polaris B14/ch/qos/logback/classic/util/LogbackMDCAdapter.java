/*     */ package ch.qos.logback.classic.util;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.slf4j.spi.MDCAdapter;
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
/*     */ public final class LogbackMDCAdapter
/*     */   implements MDCAdapter
/*     */ {
/*  54 */   final InheritableThreadLocal<Map<String, String>> copyOnInheritThreadLocal = new InheritableThreadLocal();
/*     */   
/*     */   private static final int WRITE_OPERATION = 1;
/*     */   
/*     */   private static final int READ_OPERATION = 2;
/*     */   
/*  60 */   final ThreadLocal<Integer> lastOperation = new ThreadLocal();
/*     */   
/*     */   private Integer getAndSetLastOperation(int op) {
/*  63 */     Integer lastOp = (Integer)this.lastOperation.get();
/*  64 */     this.lastOperation.set(Integer.valueOf(op));
/*  65 */     return lastOp;
/*     */   }
/*     */   
/*     */   private boolean wasLastOpReadOrNull(Integer lastOp) {
/*  69 */     return (lastOp == null) || (lastOp.intValue() == 2);
/*     */   }
/*     */   
/*     */   private Map<String, String> duplicateAndInsertNewMap(Map<String, String> oldMap) {
/*  73 */     Map<String, String> newMap = Collections.synchronizedMap(new HashMap());
/*  74 */     if (oldMap != null)
/*     */     {
/*     */ 
/*  77 */       synchronized (oldMap) {
/*  78 */         newMap.putAll(oldMap);
/*     */       }
/*     */     }
/*     */     
/*  82 */     this.copyOnInheritThreadLocal.set(newMap);
/*  83 */     return newMap;
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
/*     */ 
/*     */   public void put(String key, String val)
/*     */     throws IllegalArgumentException
/*     */   {
/*  98 */     if (key == null) {
/*  99 */       throw new IllegalArgumentException("key cannot be null");
/*     */     }
/*     */     
/* 102 */     Map<String, String> oldMap = (Map)this.copyOnInheritThreadLocal.get();
/* 103 */     Integer lastOp = getAndSetLastOperation(1);
/*     */     
/* 105 */     if ((wasLastOpReadOrNull(lastOp)) || (oldMap == null)) {
/* 106 */       Map<String, String> newMap = duplicateAndInsertNewMap(oldMap);
/* 107 */       newMap.put(key, val);
/*     */     } else {
/* 109 */       oldMap.put(key, val);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void remove(String key)
/*     */   {
/* 118 */     if (key == null) {
/* 119 */       return;
/*     */     }
/* 121 */     Map<String, String> oldMap = (Map)this.copyOnInheritThreadLocal.get();
/* 122 */     if (oldMap == null) { return;
/*     */     }
/* 124 */     Integer lastOp = getAndSetLastOperation(1);
/*     */     
/* 126 */     if (wasLastOpReadOrNull(lastOp)) {
/* 127 */       Map<String, String> newMap = duplicateAndInsertNewMap(oldMap);
/* 128 */       newMap.remove(key);
/*     */     } else {
/* 130 */       oldMap.remove(key);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void clear()
/*     */   {
/* 139 */     this.lastOperation.set(Integer.valueOf(1));
/* 140 */     this.copyOnInheritThreadLocal.remove();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String get(String key)
/*     */   {
/* 148 */     Map<String, String> map = getPropertyMap();
/* 149 */     if ((map != null) && (key != null)) {
/* 150 */       return (String)map.get(key);
/*     */     }
/* 152 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Map<String, String> getPropertyMap()
/*     */   {
/* 161 */     this.lastOperation.set(Integer.valueOf(2));
/* 162 */     return (Map)this.copyOnInheritThreadLocal.get();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Set<String> getKeys()
/*     */   {
/* 170 */     Map<String, String> map = getPropertyMap();
/*     */     
/* 172 */     if (map != null) {
/* 173 */       return map.keySet();
/*     */     }
/* 175 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Map getCopyOfContextMap()
/*     */   {
/* 184 */     this.lastOperation.set(Integer.valueOf(2));
/* 185 */     Map<String, String> hashMap = (Map)this.copyOnInheritThreadLocal.get();
/* 186 */     if (hashMap == null) {
/* 187 */       return null;
/*     */     }
/* 189 */     return new HashMap(hashMap);
/*     */   }
/*     */   
/*     */ 
/*     */   public void setContextMap(Map contextMap)
/*     */   {
/* 195 */     this.lastOperation.set(Integer.valueOf(1));
/*     */     
/* 197 */     Map<String, String> newMap = Collections.synchronizedMap(new HashMap());
/* 198 */     newMap.putAll(contextMap);
/*     */     
/*     */ 
/* 201 */     this.copyOnInheritThreadLocal.set(newMap);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\util\LogbackMDCAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */