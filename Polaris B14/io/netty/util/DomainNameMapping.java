/*     */ package io.netty.util;
/*     */ 
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import java.net.IDN;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
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
/*     */ public class DomainNameMapping<V>
/*     */   implements Mapping<String, V>
/*     */ {
/*  36 */   private static final Pattern DNS_WILDCARD_PATTERN = Pattern.compile("^\\*\\..*");
/*     */   
/*     */ 
/*     */ 
/*     */   private final Map<String, V> map;
/*     */   
/*     */ 
/*     */   private final V defaultValue;
/*     */   
/*     */ 
/*     */ 
/*     */   public DomainNameMapping(V defaultValue)
/*     */   {
/*  49 */     this(4, defaultValue);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DomainNameMapping(int initialCapacity, V defaultValue)
/*     */   {
/*  60 */     if (defaultValue == null) {
/*  61 */       throw new NullPointerException("defaultValue");
/*     */     }
/*  63 */     this.map = new LinkedHashMap(initialCapacity);
/*  64 */     this.defaultValue = defaultValue;
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
/*     */   public DomainNameMapping<V> add(String hostname, V output)
/*     */   {
/*  79 */     if (hostname == null) {
/*  80 */       throw new NullPointerException("input");
/*     */     }
/*     */     
/*  83 */     if (output == null) {
/*  84 */       throw new NullPointerException("output");
/*     */     }
/*     */     
/*  87 */     this.map.put(normalizeHostname(hostname), output);
/*  88 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static boolean matches(String hostNameTemplate, String hostName)
/*     */   {
/*  96 */     if (DNS_WILDCARD_PATTERN.matcher(hostNameTemplate).matches()) {
/*  97 */       return (hostNameTemplate.substring(2).equals(hostName)) || (hostName.endsWith(hostNameTemplate.substring(1)));
/*     */     }
/*     */     
/* 100 */     return hostNameTemplate.equals(hostName);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static String normalizeHostname(String hostname)
/*     */   {
/* 108 */     if (needsNormalization(hostname)) {
/* 109 */       hostname = IDN.toASCII(hostname, 1);
/*     */     }
/* 111 */     return hostname.toLowerCase(Locale.US);
/*     */   }
/*     */   
/*     */   private static boolean needsNormalization(String hostname) {
/* 115 */     int length = hostname.length();
/* 116 */     for (int i = 0; i < length; i++) {
/* 117 */       int c = hostname.charAt(i);
/* 118 */       if (c > 127) {
/* 119 */         return true;
/*     */       }
/*     */     }
/* 122 */     return false;
/*     */   }
/*     */   
/*     */   public V map(String input)
/*     */   {
/* 127 */     if (input != null) {
/* 128 */       input = normalizeHostname(input);
/*     */       
/* 130 */       for (Map.Entry<String, V> entry : this.map.entrySet()) {
/* 131 */         if (matches((String)entry.getKey(), input)) {
/* 132 */           return (V)entry.getValue();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 137 */     return (V)this.defaultValue;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 142 */     return StringUtil.simpleClassName(this) + "(default: " + this.defaultValue + ", map: " + this.map + ')';
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\DomainNameMapping.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */