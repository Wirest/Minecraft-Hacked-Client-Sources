/*     */ package org.slf4j;
/*     */ 
/*     */ import java.util.Map;
/*     */ import org.slf4j.helpers.NOPMDCAdapter;
/*     */ import org.slf4j.helpers.Util;
/*     */ import org.slf4j.impl.StaticMDCBinder;
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
/*     */ public class MDC
/*     */ {
/*     */   static final String NULL_MDCA_URL = "http://www.slf4j.org/codes.html#null_MDCA";
/*     */   static final String NO_STATIC_MDC_BINDER_URL = "http://www.slf4j.org/codes.html#no_static_mdc_binder";
/*     */   static MDCAdapter mdcAdapter;
/*     */   
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/*  74 */       mdcAdapter = StaticMDCBinder.SINGLETON.getMDCA();
/*     */     } catch (NoClassDefFoundError ncde) {
/*  76 */       mdcAdapter = new NOPMDCAdapter();
/*  77 */       String msg = ncde.getMessage();
/*  78 */       if ((msg != null) && (msg.indexOf("StaticMDCBinder") != -1)) {
/*  79 */         Util.report("Failed to load class \"org.slf4j.impl.StaticMDCBinder\".");
/*  80 */         Util.report("Defaulting to no-operation MDCAdapter implementation.");
/*  81 */         Util.report("See http://www.slf4j.org/codes.html#no_static_mdc_binder for further details.");
/*     */       }
/*     */       else {
/*  84 */         throw ncde;
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/*  88 */       Util.report("MDC binding unsuccessful.", e);
/*     */     }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void put(String key, String val)
/*     */     throws IllegalArgumentException
/*     */   {
/* 109 */     if (key == null) {
/* 110 */       throw new IllegalArgumentException("key parameter cannot be null");
/*     */     }
/* 112 */     if (mdcAdapter == null) {
/* 113 */       throw new IllegalStateException("MDCAdapter cannot be null. See also http://www.slf4j.org/codes.html#null_MDCA");
/*     */     }
/*     */     
/* 116 */     mdcAdapter.put(key, val);
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
/*     */ 
/*     */   public static String get(String key)
/*     */     throws IllegalArgumentException
/*     */   {
/* 132 */     if (key == null) {
/* 133 */       throw new IllegalArgumentException("key parameter cannot be null");
/*     */     }
/*     */     
/* 136 */     if (mdcAdapter == null) {
/* 137 */       throw new IllegalStateException("MDCAdapter cannot be null. See also http://www.slf4j.org/codes.html#null_MDCA");
/*     */     }
/*     */     
/* 140 */     return mdcAdapter.get(key);
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
/*     */   public static void remove(String key)
/*     */     throws IllegalArgumentException
/*     */   {
/* 154 */     if (key == null) {
/* 155 */       throw new IllegalArgumentException("key parameter cannot be null");
/*     */     }
/*     */     
/* 158 */     if (mdcAdapter == null) {
/* 159 */       throw new IllegalStateException("MDCAdapter cannot be null. See also http://www.slf4j.org/codes.html#null_MDCA");
/*     */     }
/*     */     
/* 162 */     mdcAdapter.remove(key);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void clear()
/*     */   {
/* 169 */     if (mdcAdapter == null) {
/* 170 */       throw new IllegalStateException("MDCAdapter cannot be null. See also http://www.slf4j.org/codes.html#null_MDCA");
/*     */     }
/*     */     
/* 173 */     mdcAdapter.clear();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Map<String, String> getCopyOfContextMap()
/*     */   {
/* 184 */     if (mdcAdapter == null) {
/* 185 */       throw new IllegalStateException("MDCAdapter cannot be null. See also http://www.slf4j.org/codes.html#null_MDCA");
/*     */     }
/*     */     
/* 188 */     return mdcAdapter.getCopyOfContextMap();
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
/*     */   public static void setContextMap(Map<String, String> contextMap)
/*     */   {
/* 201 */     if (mdcAdapter == null) {
/* 202 */       throw new IllegalStateException("MDCAdapter cannot be null. See also http://www.slf4j.org/codes.html#null_MDCA");
/*     */     }
/*     */     
/* 205 */     mdcAdapter.setContextMap(contextMap);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static MDCAdapter getMDCAdapter()
/*     */   {
/* 215 */     return mdcAdapter;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\org\slf4j\MDC.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */