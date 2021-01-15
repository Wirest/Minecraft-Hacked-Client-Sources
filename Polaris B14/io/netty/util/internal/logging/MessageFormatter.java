/*     */ package io.netty.util.internal.logging;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class MessageFormatter
/*     */ {
/*     */   static final char DELIM_START = '{';
/*     */   static final char DELIM_STOP = '}';
/*     */   static final String DELIM_STR = "{}";
/*     */   private static final char ESCAPE_CHAR = '\\';
/*     */   
/*     */   static FormattingTuple format(String messagePattern, Object arg)
/*     */   {
/* 135 */     return arrayFormat(messagePattern, new Object[] { arg });
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static FormattingTuple format(String messagePattern, Object argA, Object argB)
/*     */   {
/* 159 */     return arrayFormat(messagePattern, new Object[] { argA, argB });
/*     */   }
/*     */   
/*     */   static Throwable getThrowableCandidate(Object[] argArray) {
/* 163 */     if ((argArray == null) || (argArray.length == 0)) {
/* 164 */       return null;
/*     */     }
/*     */     
/* 167 */     Object lastEntry = argArray[(argArray.length - 1)];
/* 168 */     if ((lastEntry instanceof Throwable)) {
/* 169 */       return (Throwable)lastEntry;
/*     */     }
/* 171 */     return null;
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
/*     */   static FormattingTuple arrayFormat(String messagePattern, Object[] argArray)
/*     */   {
/* 187 */     Throwable throwableCandidate = getThrowableCandidate(argArray);
/*     */     
/* 189 */     if (messagePattern == null) {
/* 190 */       return new FormattingTuple(null, argArray, throwableCandidate);
/*     */     }
/*     */     
/* 193 */     if (argArray == null) {
/* 194 */       return new FormattingTuple(messagePattern);
/*     */     }
/*     */     
/* 197 */     int i = 0;
/*     */     
/* 199 */     StringBuffer sbuf = new StringBuffer(messagePattern.length() + 50);
/*     */     
/*     */ 
/* 202 */     for (int L = 0; L < argArray.length; L++)
/*     */     {
/* 204 */       int j = messagePattern.indexOf("{}", i);
/*     */       
/* 206 */       if (j == -1)
/*     */       {
/* 208 */         if (i == 0) {
/* 209 */           return new FormattingTuple(messagePattern, argArray, throwableCandidate);
/*     */         }
/*     */         
/*     */ 
/* 213 */         sbuf.append(messagePattern.substring(i, messagePattern.length()));
/* 214 */         return new FormattingTuple(sbuf.toString(), argArray, throwableCandidate);
/*     */       }
/*     */       
/*     */ 
/* 218 */       if (isEscapedDelimeter(messagePattern, j)) {
/* 219 */         if (!isDoubleEscaped(messagePattern, j)) {
/* 220 */           L--;
/* 221 */           sbuf.append(messagePattern.substring(i, j - 1));
/* 222 */           sbuf.append('{');
/* 223 */           i = j + 1;
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 228 */           sbuf.append(messagePattern.substring(i, j - 1));
/* 229 */           deeplyAppendParameter(sbuf, argArray[L], new HashMap());
/* 230 */           i = j + 2;
/*     */         }
/*     */       }
/*     */       else {
/* 234 */         sbuf.append(messagePattern.substring(i, j));
/* 235 */         deeplyAppendParameter(sbuf, argArray[L], new HashMap());
/* 236 */         i = j + 2;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 241 */     sbuf.append(messagePattern.substring(i, messagePattern.length()));
/* 242 */     if (L < argArray.length - 1) {
/* 243 */       return new FormattingTuple(sbuf.toString(), argArray, throwableCandidate);
/*     */     }
/* 245 */     return new FormattingTuple(sbuf.toString(), argArray, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static boolean isEscapedDelimeter(String messagePattern, int delimeterStartIndex)
/*     */   {
/* 252 */     if (delimeterStartIndex == 0) {
/* 253 */       return false;
/*     */     }
/* 255 */     return messagePattern.charAt(delimeterStartIndex - 1) == '\\';
/*     */   }
/*     */   
/*     */   static boolean isDoubleEscaped(String messagePattern, int delimeterStartIndex)
/*     */   {
/* 260 */     return (delimeterStartIndex >= 2) && (messagePattern.charAt(delimeterStartIndex - 2) == '\\');
/*     */   }
/*     */   
/*     */ 
/*     */   private static void deeplyAppendParameter(StringBuffer sbuf, Object o, Map<Object[], Void> seenMap)
/*     */   {
/* 266 */     if (o == null) {
/* 267 */       sbuf.append("null");
/* 268 */       return;
/*     */     }
/* 270 */     if (!o.getClass().isArray()) {
/* 271 */       safeObjectAppend(sbuf, o);
/*     */ 
/*     */ 
/*     */     }
/* 275 */     else if ((o instanceof boolean[])) {
/* 276 */       booleanArrayAppend(sbuf, (boolean[])o);
/* 277 */     } else if ((o instanceof byte[])) {
/* 278 */       byteArrayAppend(sbuf, (byte[])o);
/* 279 */     } else if ((o instanceof char[])) {
/* 280 */       charArrayAppend(sbuf, (char[])o);
/* 281 */     } else if ((o instanceof short[])) {
/* 282 */       shortArrayAppend(sbuf, (short[])o);
/* 283 */     } else if ((o instanceof int[])) {
/* 284 */       intArrayAppend(sbuf, (int[])o);
/* 285 */     } else if ((o instanceof long[])) {
/* 286 */       longArrayAppend(sbuf, (long[])o);
/* 287 */     } else if ((o instanceof float[])) {
/* 288 */       floatArrayAppend(sbuf, (float[])o);
/* 289 */     } else if ((o instanceof double[])) {
/* 290 */       doubleArrayAppend(sbuf, (double[])o);
/*     */     } else {
/* 292 */       objectArrayAppend(sbuf, (Object[])o, seenMap);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void safeObjectAppend(StringBuffer sbuf, Object o)
/*     */   {
/*     */     try {
/* 299 */       String oAsString = o.toString();
/* 300 */       sbuf.append(oAsString);
/*     */     } catch (Throwable t) {
/* 302 */       System.err.println("SLF4J: Failed toString() invocation on an object of type [" + o.getClass().getName() + ']');
/*     */       
/*     */ 
/* 305 */       t.printStackTrace();
/* 306 */       sbuf.append("[FAILED toString()]");
/*     */     }
/*     */   }
/*     */   
/*     */   private static void objectArrayAppend(StringBuffer sbuf, Object[] a, Map<Object[], Void> seenMap)
/*     */   {
/* 312 */     sbuf.append('[');
/* 313 */     if (!seenMap.containsKey(a)) {
/* 314 */       seenMap.put(a, null);
/* 315 */       int len = a.length;
/* 316 */       for (int i = 0; i < len; i++) {
/* 317 */         deeplyAppendParameter(sbuf, a[i], seenMap);
/* 318 */         if (i != len - 1) {
/* 319 */           sbuf.append(", ");
/*     */         }
/*     */       }
/*     */       
/* 323 */       seenMap.remove(a);
/*     */     } else {
/* 325 */       sbuf.append("...");
/*     */     }
/* 327 */     sbuf.append(']');
/*     */   }
/*     */   
/*     */   private static void booleanArrayAppend(StringBuffer sbuf, boolean[] a) {
/* 331 */     sbuf.append('[');
/* 332 */     int len = a.length;
/* 333 */     for (int i = 0; i < len; i++) {
/* 334 */       sbuf.append(a[i]);
/* 335 */       if (i != len - 1) {
/* 336 */         sbuf.append(", ");
/*     */       }
/*     */     }
/* 339 */     sbuf.append(']');
/*     */   }
/*     */   
/*     */   private static void byteArrayAppend(StringBuffer sbuf, byte[] a) {
/* 343 */     sbuf.append('[');
/* 344 */     int len = a.length;
/* 345 */     for (int i = 0; i < len; i++) {
/* 346 */       sbuf.append(a[i]);
/* 347 */       if (i != len - 1) {
/* 348 */         sbuf.append(", ");
/*     */       }
/*     */     }
/* 351 */     sbuf.append(']');
/*     */   }
/*     */   
/*     */   private static void charArrayAppend(StringBuffer sbuf, char[] a) {
/* 355 */     sbuf.append('[');
/* 356 */     int len = a.length;
/* 357 */     for (int i = 0; i < len; i++) {
/* 358 */       sbuf.append(a[i]);
/* 359 */       if (i != len - 1) {
/* 360 */         sbuf.append(", ");
/*     */       }
/*     */     }
/* 363 */     sbuf.append(']');
/*     */   }
/*     */   
/*     */   private static void shortArrayAppend(StringBuffer sbuf, short[] a) {
/* 367 */     sbuf.append('[');
/* 368 */     int len = a.length;
/* 369 */     for (int i = 0; i < len; i++) {
/* 370 */       sbuf.append(a[i]);
/* 371 */       if (i != len - 1) {
/* 372 */         sbuf.append(", ");
/*     */       }
/*     */     }
/* 375 */     sbuf.append(']');
/*     */   }
/*     */   
/*     */   private static void intArrayAppend(StringBuffer sbuf, int[] a) {
/* 379 */     sbuf.append('[');
/* 380 */     int len = a.length;
/* 381 */     for (int i = 0; i < len; i++) {
/* 382 */       sbuf.append(a[i]);
/* 383 */       if (i != len - 1) {
/* 384 */         sbuf.append(", ");
/*     */       }
/*     */     }
/* 387 */     sbuf.append(']');
/*     */   }
/*     */   
/*     */   private static void longArrayAppend(StringBuffer sbuf, long[] a) {
/* 391 */     sbuf.append('[');
/* 392 */     int len = a.length;
/* 393 */     for (int i = 0; i < len; i++) {
/* 394 */       sbuf.append(a[i]);
/* 395 */       if (i != len - 1) {
/* 396 */         sbuf.append(", ");
/*     */       }
/*     */     }
/* 399 */     sbuf.append(']');
/*     */   }
/*     */   
/*     */   private static void floatArrayAppend(StringBuffer sbuf, float[] a) {
/* 403 */     sbuf.append('[');
/* 404 */     int len = a.length;
/* 405 */     for (int i = 0; i < len; i++) {
/* 406 */       sbuf.append(a[i]);
/* 407 */       if (i != len - 1) {
/* 408 */         sbuf.append(", ");
/*     */       }
/*     */     }
/* 411 */     sbuf.append(']');
/*     */   }
/*     */   
/*     */   private static void doubleArrayAppend(StringBuffer sbuf, double[] a) {
/* 415 */     sbuf.append('[');
/* 416 */     int len = a.length;
/* 417 */     for (int i = 0; i < len; i++) {
/* 418 */       sbuf.append(a[i]);
/* 419 */       if (i != len - 1) {
/* 420 */         sbuf.append(", ");
/*     */       }
/*     */     }
/* 423 */     sbuf.append(']');
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\internal\logging\MessageFormatter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */