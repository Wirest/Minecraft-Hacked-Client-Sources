/*     */ package org.slf4j.impl;
/*     */ 
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
/*     */ public class LogbackMDCAdapter
/*     */   implements MDCAdapter
/*     */ {
/*  27 */   final CopyOnInheritThreadLocal copyOnInheritThreadLocal = new CopyOnInheritThreadLocal();
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
/*     */   public void put(String key, String val)
/*     */     throws IllegalArgumentException
/*     */   {
/*  49 */     if (key == null) {
/*  50 */       throw new IllegalArgumentException("key cannot be null");
/*     */     }
/*     */     
/*  53 */     HashMap<String, String> oldMap = (HashMap)this.copyOnInheritThreadLocal.get();
/*     */     
/*  55 */     HashMap<String, String> newMap = new HashMap();
/*  56 */     if (oldMap != null) {
/*  57 */       newMap.putAll(oldMap);
/*     */     }
/*     */     
/*  60 */     this.copyOnInheritThreadLocal.set(newMap);
/*  61 */     newMap.put(key, val);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String get(String key)
/*     */   {
/*  70 */     HashMap<String, String> hashMap = (HashMap)this.copyOnInheritThreadLocal.get();
/*     */     
/*  72 */     if ((hashMap != null) && (key != null)) {
/*  73 */       return (String)hashMap.get(key);
/*     */     }
/*  75 */     return null;
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
/*     */   public void remove(String key)
/*     */   {
/*  88 */     HashMap<String, String> oldMap = (HashMap)this.copyOnInheritThreadLocal.get();
/*     */     
/*  90 */     HashMap<String, String> newMap = new HashMap();
/*  91 */     if (oldMap != null) {
/*  92 */       newMap.putAll(oldMap);
/*     */     }
/*     */     
/*  95 */     this.copyOnInheritThreadLocal.set(newMap);
/*  96 */     newMap.remove(key);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void clear()
/*     */   {
/* 103 */     HashMap<String, String> hashMap = (HashMap)this.copyOnInheritThreadLocal.get();
/*     */     
/* 105 */     if (hashMap != null) {
/* 106 */       hashMap.clear();
/* 107 */       this.copyOnInheritThreadLocal.remove();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Map<String, String> getPropertyMap()
/*     */   {
/* 116 */     return (Map)this.copyOnInheritThreadLocal.get();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Map getCopyOfContextMap()
/*     */   {
/* 124 */     HashMap<String, String> hashMap = (HashMap)this.copyOnInheritThreadLocal.get();
/* 125 */     if (hashMap == null) {
/* 126 */       return null;
/*     */     }
/* 128 */     return new HashMap(hashMap);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Set<String> getKeys()
/*     */   {
/* 137 */     HashMap<String, String> hashMap = (HashMap)this.copyOnInheritThreadLocal.get();
/*     */     
/* 139 */     if (hashMap != null) {
/* 140 */       return hashMap.keySet();
/*     */     }
/* 142 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setContextMap(Map contextMap)
/*     */   {
/* 148 */     HashMap<String, String> oldMap = (HashMap)this.copyOnInheritThreadLocal.get();
/*     */     
/* 150 */     HashMap<String, String> newMap = new HashMap();
/* 151 */     newMap.putAll(contextMap);
/*     */     
/*     */ 
/* 154 */     this.copyOnInheritThreadLocal.set(newMap);
/*     */     
/*     */ 
/* 157 */     if (oldMap != null) {
/* 158 */       oldMap.clear();
/* 159 */       oldMap = null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\org\slf4j\impl\LogbackMDCAdapter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */