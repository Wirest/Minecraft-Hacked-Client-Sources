/*     */ package io.netty.handler.ssl;
/*     */ 
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentMap;
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
/*     */ 
/*     */ final class CipherSuiteConverter
/*     */ {
/*  36 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(CipherSuiteConverter.class);
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
/*  51 */   private static final Pattern JAVA_CIPHERSUITE_PATTERN = Pattern.compile("^(?:TLS|SSL)_((?:(?!_WITH_).)+)_WITH_(.*)_(.*)$");
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
/*  66 */   private static final Pattern OPENSSL_CIPHERSUITE_PATTERN = Pattern.compile("^(?:((?:(?:EXP-)?(?:(?:DHE|EDH|ECDH|ECDHE|SRP)-(?:DSS|RSA|ECDSA)|(?:ADH|AECDH|KRB5|PSK|SRP)))|EXP)-)?(.*)-(.*)$");
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
/*  80 */   private static final Pattern JAVA_AES_CBC_PATTERN = Pattern.compile("^(AES)_([0-9]+)_CBC$");
/*  81 */   private static final Pattern JAVA_AES_PATTERN = Pattern.compile("^(AES)_([0-9]+)_(.*)$");
/*  82 */   private static final Pattern OPENSSL_AES_CBC_PATTERN = Pattern.compile("^(AES)([0-9]+)$");
/*  83 */   private static final Pattern OPENSSL_AES_PATTERN = Pattern.compile("^(AES)([0-9]+)-(.*)$");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  89 */   private static final ConcurrentMap<String, String> j2o = PlatformDependent.newConcurrentHashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  96 */   private static final ConcurrentMap<String, Map<String, String>> o2j = PlatformDependent.newConcurrentHashMap();
/*     */   
/*     */ 
/*     */ 
/*     */   static void clearCache()
/*     */   {
/* 102 */     j2o.clear();
/* 103 */     o2j.clear();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static boolean isJ2OCached(String key, String value)
/*     */   {
/* 110 */     return value.equals(j2o.get(key));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static boolean isO2JCached(String key, String protocol, String value)
/*     */   {
/* 117 */     Map<String, String> p2j = (Map)o2j.get(key);
/* 118 */     if (p2j == null) {
/* 119 */       return false;
/*     */     }
/* 121 */     return value.equals(p2j.get(protocol));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static String toOpenSsl(Iterable<String> javaCipherSuites)
/*     */   {
/* 129 */     StringBuilder buf = new StringBuilder();
/* 130 */     for (String c : javaCipherSuites) {
/* 131 */       if (c == null) {
/*     */         break;
/*     */       }
/*     */       
/* 135 */       String converted = toOpenSsl(c);
/* 136 */       if (converted != null) {
/* 137 */         c = converted;
/*     */       }
/*     */       
/* 140 */       buf.append(c);
/* 141 */       buf.append(':');
/*     */     }
/*     */     
/* 144 */     if (buf.length() > 0) {
/* 145 */       buf.setLength(buf.length() - 1);
/* 146 */       return buf.toString();
/*     */     }
/* 148 */     return "";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static String toOpenSsl(String javaCipherSuite)
/*     */   {
/* 158 */     String converted = (String)j2o.get(javaCipherSuite);
/* 159 */     if (converted != null) {
/* 160 */       return converted;
/*     */     }
/* 162 */     return cacheFromJava(javaCipherSuite);
/*     */   }
/*     */   
/*     */   private static String cacheFromJava(String javaCipherSuite)
/*     */   {
/* 167 */     String openSslCipherSuite = toOpenSslUncached(javaCipherSuite);
/* 168 */     if (openSslCipherSuite == null) {
/* 169 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 173 */     j2o.putIfAbsent(javaCipherSuite, openSslCipherSuite);
/*     */     
/*     */ 
/* 176 */     String javaCipherSuiteSuffix = javaCipherSuite.substring(4);
/* 177 */     Map<String, String> p2j = new HashMap(4);
/* 178 */     p2j.put("", javaCipherSuiteSuffix);
/* 179 */     p2j.put("SSL", "SSL_" + javaCipherSuiteSuffix);
/* 180 */     p2j.put("TLS", "TLS_" + javaCipherSuiteSuffix);
/* 181 */     o2j.put(openSslCipherSuite, p2j);
/*     */     
/* 183 */     logger.debug("Cipher suite mapping: {} => {}", javaCipherSuite, openSslCipherSuite);
/*     */     
/* 185 */     return openSslCipherSuite;
/*     */   }
/*     */   
/*     */   static String toOpenSslUncached(String javaCipherSuite) {
/* 189 */     Matcher m = JAVA_CIPHERSUITE_PATTERN.matcher(javaCipherSuite);
/* 190 */     if (!m.matches()) {
/* 191 */       return null;
/*     */     }
/*     */     
/* 194 */     String handshakeAlgo = toOpenSslHandshakeAlgo(m.group(1));
/* 195 */     String bulkCipher = toOpenSslBulkCipher(m.group(2));
/* 196 */     String hmacAlgo = toOpenSslHmacAlgo(m.group(3));
/* 197 */     if (handshakeAlgo.length() == 0) {
/* 198 */       return bulkCipher + '-' + hmacAlgo;
/*     */     }
/* 200 */     return handshakeAlgo + '-' + bulkCipher + '-' + hmacAlgo;
/*     */   }
/*     */   
/*     */   private static String toOpenSslHandshakeAlgo(String handshakeAlgo)
/*     */   {
/* 205 */     boolean export = handshakeAlgo.endsWith("_EXPORT");
/* 206 */     if (export) {
/* 207 */       handshakeAlgo = handshakeAlgo.substring(0, handshakeAlgo.length() - 7);
/*     */     }
/*     */     
/* 210 */     if ("RSA".equals(handshakeAlgo)) {
/* 211 */       handshakeAlgo = "";
/* 212 */     } else if (handshakeAlgo.endsWith("_anon")) {
/* 213 */       handshakeAlgo = 'A' + handshakeAlgo.substring(0, handshakeAlgo.length() - 5);
/*     */     }
/*     */     
/* 216 */     if (export) {
/* 217 */       if (handshakeAlgo.length() == 0) {
/* 218 */         handshakeAlgo = "EXP";
/*     */       } else {
/* 220 */         handshakeAlgo = "EXP-" + handshakeAlgo;
/*     */       }
/*     */     }
/*     */     
/* 224 */     return handshakeAlgo.replace('_', '-');
/*     */   }
/*     */   
/*     */   private static String toOpenSslBulkCipher(String bulkCipher) {
/* 228 */     if (bulkCipher.startsWith("AES_")) {
/* 229 */       Matcher m = JAVA_AES_CBC_PATTERN.matcher(bulkCipher);
/* 230 */       if (m.matches()) {
/* 231 */         return m.replaceFirst("$1$2");
/*     */       }
/*     */       
/* 234 */       m = JAVA_AES_PATTERN.matcher(bulkCipher);
/* 235 */       if (m.matches()) {
/* 236 */         return m.replaceFirst("$1$2-$3");
/*     */       }
/*     */     }
/*     */     
/* 240 */     if ("3DES_EDE_CBC".equals(bulkCipher)) {
/* 241 */       return "DES-CBC3";
/*     */     }
/*     */     
/* 244 */     if (("RC4_128".equals(bulkCipher)) || ("RC4_40".equals(bulkCipher))) {
/* 245 */       return "RC4";
/*     */     }
/*     */     
/* 248 */     if (("DES40_CBC".equals(bulkCipher)) || ("DES_CBC_40".equals(bulkCipher))) {
/* 249 */       return "DES-CBC";
/*     */     }
/*     */     
/* 252 */     if ("RC2_CBC_40".equals(bulkCipher)) {
/* 253 */       return "RC2-CBC";
/*     */     }
/*     */     
/* 256 */     return bulkCipher.replace('_', '-');
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static String toOpenSslHmacAlgo(String hmacAlgo)
/*     */   {
/* 266 */     return hmacAlgo;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static String toJava(String openSslCipherSuite, String protocol)
/*     */   {
/* 276 */     Map<String, String> p2j = (Map)o2j.get(openSslCipherSuite);
/* 277 */     if (p2j == null) {
/* 278 */       p2j = cacheFromOpenSsl(openSslCipherSuite);
/*     */     }
/*     */     
/* 281 */     String javaCipherSuite = (String)p2j.get(protocol);
/* 282 */     if (javaCipherSuite == null) {
/* 283 */       javaCipherSuite = protocol + '_' + (String)p2j.get("");
/*     */     }
/*     */     
/* 286 */     return javaCipherSuite;
/*     */   }
/*     */   
/*     */   private static Map<String, String> cacheFromOpenSsl(String openSslCipherSuite) {
/* 290 */     String javaCipherSuiteSuffix = toJavaUncached(openSslCipherSuite);
/* 291 */     if (javaCipherSuiteSuffix == null) {
/* 292 */       return null;
/*     */     }
/*     */     
/* 295 */     String javaCipherSuiteSsl = "SSL_" + javaCipherSuiteSuffix;
/* 296 */     String javaCipherSuiteTls = "TLS_" + javaCipherSuiteSuffix;
/*     */     
/*     */ 
/* 299 */     Map<String, String> p2j = new HashMap(4);
/* 300 */     p2j.put("", javaCipherSuiteSuffix);
/* 301 */     p2j.put("SSL", javaCipherSuiteSsl);
/* 302 */     p2j.put("TLS", javaCipherSuiteTls);
/* 303 */     o2j.putIfAbsent(openSslCipherSuite, p2j);
/*     */     
/*     */ 
/* 306 */     j2o.putIfAbsent(javaCipherSuiteTls, openSslCipherSuite);
/* 307 */     j2o.putIfAbsent(javaCipherSuiteSsl, openSslCipherSuite);
/*     */     
/* 309 */     logger.debug("Cipher suite mapping: {} => {}", javaCipherSuiteTls, openSslCipherSuite);
/* 310 */     logger.debug("Cipher suite mapping: {} => {}", javaCipherSuiteSsl, openSslCipherSuite);
/*     */     
/* 312 */     return p2j;
/*     */   }
/*     */   
/*     */   static String toJavaUncached(String openSslCipherSuite) {
/* 316 */     Matcher m = OPENSSL_CIPHERSUITE_PATTERN.matcher(openSslCipherSuite);
/* 317 */     if (!m.matches()) {
/* 318 */       return null;
/*     */     }
/*     */     
/* 321 */     String handshakeAlgo = m.group(1);
/*     */     boolean export;
/* 323 */     boolean export; if (handshakeAlgo == null) {
/* 324 */       handshakeAlgo = "";
/* 325 */       export = false; } else { boolean export;
/* 326 */       if (handshakeAlgo.startsWith("EXP-")) {
/* 327 */         handshakeAlgo = handshakeAlgo.substring(4);
/* 328 */         export = true; } else { boolean export;
/* 329 */         if ("EXP".equals(handshakeAlgo)) {
/* 330 */           handshakeAlgo = "";
/* 331 */           export = true;
/*     */         } else {
/* 333 */           export = false;
/*     */         }
/*     */       } }
/* 336 */     handshakeAlgo = toJavaHandshakeAlgo(handshakeAlgo, export);
/* 337 */     String bulkCipher = toJavaBulkCipher(m.group(2), export);
/* 338 */     String hmacAlgo = toJavaHmacAlgo(m.group(3));
/*     */     
/* 340 */     return handshakeAlgo + "_WITH_" + bulkCipher + '_' + hmacAlgo;
/*     */   }
/*     */   
/*     */   private static String toJavaHandshakeAlgo(String handshakeAlgo, boolean export) {
/* 344 */     if (handshakeAlgo.length() == 0) {
/* 345 */       handshakeAlgo = "RSA";
/* 346 */     } else if ("ADH".equals(handshakeAlgo)) {
/* 347 */       handshakeAlgo = "DH_anon";
/* 348 */     } else if ("AECDH".equals(handshakeAlgo)) {
/* 349 */       handshakeAlgo = "ECDH_anon";
/*     */     }
/*     */     
/* 352 */     handshakeAlgo = handshakeAlgo.replace('-', '_');
/* 353 */     if (export) {
/* 354 */       return handshakeAlgo + "_EXPORT";
/*     */     }
/* 356 */     return handshakeAlgo;
/*     */   }
/*     */   
/*     */   private static String toJavaBulkCipher(String bulkCipher, boolean export)
/*     */   {
/* 361 */     if (bulkCipher.startsWith("AES")) {
/* 362 */       Matcher m = OPENSSL_AES_CBC_PATTERN.matcher(bulkCipher);
/* 363 */       if (m.matches()) {
/* 364 */         return m.replaceFirst("$1_$2_CBC");
/*     */       }
/*     */       
/* 367 */       m = OPENSSL_AES_PATTERN.matcher(bulkCipher);
/* 368 */       if (m.matches()) {
/* 369 */         return m.replaceFirst("$1_$2_$3");
/*     */       }
/*     */     }
/*     */     
/* 373 */     if ("DES-CBC3".equals(bulkCipher)) {
/* 374 */       return "3DES_EDE_CBC";
/*     */     }
/*     */     
/* 377 */     if ("RC4".equals(bulkCipher)) {
/* 378 */       if (export) {
/* 379 */         return "RC4_40";
/*     */       }
/* 381 */       return "RC4_128";
/*     */     }
/*     */     
/*     */ 
/* 385 */     if ("DES-CBC".equals(bulkCipher)) {
/* 386 */       if (export) {
/* 387 */         return "DES_CBC_40";
/*     */       }
/* 389 */       return "DES_CBC";
/*     */     }
/*     */     
/*     */ 
/* 393 */     if ("RC2-CBC".equals(bulkCipher)) {
/* 394 */       if (export) {
/* 395 */         return "RC2_CBC_40";
/*     */       }
/* 397 */       return "RC2_CBC";
/*     */     }
/*     */     
/*     */ 
/* 401 */     return bulkCipher.replace('-', '_');
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static String toJavaHmacAlgo(String hmacAlgo)
/*     */   {
/* 411 */     return hmacAlgo;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\ssl\CipherSuiteConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */