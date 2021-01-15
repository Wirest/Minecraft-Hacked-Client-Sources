/*     */ package org.slf4j.helpers;
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
/*     */ public class BasicMDCAdapter
/*     */   implements MDCAdapter
/*     */ {
/*  46 */   private InheritableThreadLocal<Map<String, String>> inheritableThreadLocal = new InheritableThreadLocal();
/*     */   
/*     */   static boolean isJDK14()
/*     */   {
/*     */     try {
/*  51 */       String javaVersion = System.getProperty("java.version");
/*  52 */       return javaVersion.startsWith("1.4");
/*     */     }
/*     */     catch (SecurityException se) {}
/*  55 */     return false;
/*     */   }
/*     */   
/*     */ 
/*  59 */   static boolean IS_JDK14 = ;
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
/*     */   public void put(String key, String val)
/*     */   {
/*  75 */     if (key == null) {
/*  76 */       throw new IllegalArgumentException("key cannot be null");
/*     */     }
/*  78 */     Map<String, String> map = (Map)this.inheritableThreadLocal.get();
/*  79 */     if (map == null) {
/*  80 */       map = Collections.synchronizedMap(new HashMap());
/*  81 */       this.inheritableThreadLocal.set(map);
/*     */     }
/*  83 */     map.put(key, val);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String get(String key)
/*     */   {
/*  90 */     Map<String, String> Map = (Map)this.inheritableThreadLocal.get();
/*  91 */     if ((Map != null) && (key != null)) {
/*  92 */       return (String)Map.get(key);
/*     */     }
/*  94 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void remove(String key)
/*     */   {
/* 102 */     Map<String, String> map = (Map)this.inheritableThreadLocal.get();
/* 103 */     if (map != null) {
/* 104 */       map.remove(key);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void clear()
/*     */   {
/* 112 */     Map<String, String> map = (Map)this.inheritableThreadLocal.get();
/* 113 */     if (map != null) {
/* 114 */       map.clear();
/*     */       
/*     */ 
/* 117 */       if (isJDK14()) {
/* 118 */         this.inheritableThreadLocal.set(null);
/*     */       } else {
/* 120 */         this.inheritableThreadLocal.remove();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Set<String> getKeys()
/*     */   {
/* 132 */     Map<String, String> map = (Map)this.inheritableThreadLocal.get();
/* 133 */     if (map != null) {
/* 134 */       return map.keySet();
/*     */     }
/* 136 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Map<String, String> getCopyOfContextMap()
/*     */   {
/* 145 */     Map<String, String> oldMap = (Map)this.inheritableThreadLocal.get();
/* 146 */     if (oldMap != null) {
/* 147 */       Map<String, String> newMap = Collections.synchronizedMap(new HashMap());
/* 148 */       synchronized (oldMap) {
/* 149 */         newMap.putAll(oldMap);
/*     */       }
/* 151 */       return newMap;
/*     */     }
/* 153 */     return null;
/*     */   }
/*     */   
/*     */   public void setContextMap(Map<String, String> contextMap)
/*     */   {
/* 158 */     Map<String, String> map = Collections.synchronizedMap(new HashMap(contextMap));
/* 159 */     this.inheritableThreadLocal.set(map);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\org\slf4j\helpers\BasicMDCAdapter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */