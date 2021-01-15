/*     */ package org.slf4j.helpers;
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
/*     */ public final class MessageFormatter
/*     */ {
/*     */   static final char DELIM_START = '{';
/*     */   static final char DELIM_STOP = '}';
/*     */   static final String DELIM_STR = "{}";
/*     */   private static final char ESCAPE_CHAR = '\\';
/*     */   
/*     */   public static final FormattingTuple format(String messagePattern, Object arg)
/*     */   {
/* 124 */     return arrayFormat(messagePattern, new Object[] { arg });
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final FormattingTuple format(String messagePattern, Object arg1, Object arg2)
/*     */   {
/* 152 */     return arrayFormat(messagePattern, new Object[] { arg1, arg2 });
/*     */   }
/*     */   
/*     */   static final Throwable getThrowableCandidate(Object[] argArray) {
/* 156 */     if ((argArray == null) || (argArray.length == 0)) {
/* 157 */       return null;
/*     */     }
/*     */     
/* 160 */     Object lastEntry = argArray[(argArray.length - 1)];
/* 161 */     if ((lastEntry instanceof Throwable)) {
/* 162 */       return (Throwable)lastEntry;
/*     */     }
/* 164 */     return null;
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
/*     */   public static final FormattingTuple arrayFormat(String messagePattern, Object[] argArray)
/*     */   {
/* 182 */     Throwable throwableCandidate = getThrowableCandidate(argArray);
/*     */     
/* 184 */     if (messagePattern == null) {
/* 185 */       return new FormattingTuple(null, argArray, throwableCandidate);
/*     */     }
/*     */     
/* 188 */     if (argArray == null) {
/* 189 */       return new FormattingTuple(messagePattern);
/*     */     }
/*     */     
/* 192 */     int i = 0;
/*     */     
/*     */ 
/* 195 */     StringBuilder sbuf = new StringBuilder(messagePattern.length() + 50);
/*     */     
/*     */ 
/* 198 */     for (int L = 0; L < argArray.length; L++)
/*     */     {
/* 200 */       int j = messagePattern.indexOf("{}", i);
/*     */       
/* 202 */       if (j == -1)
/*     */       {
/* 204 */         if (i == 0) {
/* 205 */           return new FormattingTuple(messagePattern, argArray, throwableCandidate);
/*     */         }
/*     */         
/*     */ 
/* 209 */         sbuf.append(messagePattern.substring(i, messagePattern.length()));
/* 210 */         return new FormattingTuple(sbuf.toString(), argArray, throwableCandidate);
/*     */       }
/*     */       
/*     */ 
/* 214 */       if (isEscapedDelimeter(messagePattern, j)) {
/* 215 */         if (!isDoubleEscaped(messagePattern, j)) {
/* 216 */           L--;
/* 217 */           sbuf.append(messagePattern.substring(i, j - 1));
/* 218 */           sbuf.append('{');
/* 219 */           i = j + 1;
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 224 */           sbuf.append(messagePattern.substring(i, j - 1));
/* 225 */           deeplyAppendParameter(sbuf, argArray[L], new HashMap());
/* 226 */           i = j + 2;
/*     */         }
/*     */       }
/*     */       else {
/* 230 */         sbuf.append(messagePattern.substring(i, j));
/* 231 */         deeplyAppendParameter(sbuf, argArray[L], new HashMap());
/* 232 */         i = j + 2;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 237 */     sbuf.append(messagePattern.substring(i, messagePattern.length()));
/* 238 */     if (L < argArray.length - 1) {
/* 239 */       return new FormattingTuple(sbuf.toString(), argArray, throwableCandidate);
/*     */     }
/* 241 */     return new FormattingTuple(sbuf.toString(), argArray, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static final boolean isEscapedDelimeter(String messagePattern, int delimeterStartIndex)
/*     */   {
/* 248 */     if (delimeterStartIndex == 0) {
/* 249 */       return false;
/*     */     }
/* 251 */     char potentialEscape = messagePattern.charAt(delimeterStartIndex - 1);
/* 252 */     if (potentialEscape == '\\') {
/* 253 */       return true;
/*     */     }
/* 255 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   static final boolean isDoubleEscaped(String messagePattern, int delimeterStartIndex)
/*     */   {
/* 261 */     if ((delimeterStartIndex >= 2) && (messagePattern.charAt(delimeterStartIndex - 2) == '\\'))
/*     */     {
/* 263 */       return true;
/*     */     }
/* 265 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static void deeplyAppendParameter(StringBuilder sbuf, Object o, Map<Object[], Object> seenMap)
/*     */   {
/* 272 */     if (o == null) {
/* 273 */       sbuf.append("null");
/* 274 */       return;
/*     */     }
/* 276 */     if (!o.getClass().isArray()) {
/* 277 */       safeObjectAppend(sbuf, o);
/*     */ 
/*     */ 
/*     */     }
/* 281 */     else if ((o instanceof boolean[])) {
/* 282 */       booleanArrayAppend(sbuf, (boolean[])o);
/* 283 */     } else if ((o instanceof byte[])) {
/* 284 */       byteArrayAppend(sbuf, (byte[])o);
/* 285 */     } else if ((o instanceof char[])) {
/* 286 */       charArrayAppend(sbuf, (char[])o);
/* 287 */     } else if ((o instanceof short[])) {
/* 288 */       shortArrayAppend(sbuf, (short[])o);
/* 289 */     } else if ((o instanceof int[])) {
/* 290 */       intArrayAppend(sbuf, (int[])o);
/* 291 */     } else if ((o instanceof long[])) {
/* 292 */       longArrayAppend(sbuf, (long[])o);
/* 293 */     } else if ((o instanceof float[])) {
/* 294 */       floatArrayAppend(sbuf, (float[])o);
/* 295 */     } else if ((o instanceof double[])) {
/* 296 */       doubleArrayAppend(sbuf, (double[])o);
/*     */     } else {
/* 298 */       objectArrayAppend(sbuf, (Object[])o, seenMap);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void safeObjectAppend(StringBuilder sbuf, Object o)
/*     */   {
/*     */     try {
/* 305 */       String oAsString = o.toString();
/* 306 */       sbuf.append(oAsString);
/*     */     } catch (Throwable t) {
/* 308 */       System.err.println("SLF4J: Failed toString() invocation on an object of type [" + o.getClass().getName() + "]");
/*     */       
/*     */ 
/* 311 */       t.printStackTrace();
/* 312 */       sbuf.append("[FAILED toString()]");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static void objectArrayAppend(StringBuilder sbuf, Object[] a, Map<Object[], Object> seenMap)
/*     */   {
/* 319 */     sbuf.append('[');
/* 320 */     if (!seenMap.containsKey(a)) {
/* 321 */       seenMap.put(a, null);
/* 322 */       int len = a.length;
/* 323 */       for (int i = 0; i < len; i++) {
/* 324 */         deeplyAppendParameter(sbuf, a[i], seenMap);
/* 325 */         if (i != len - 1) {
/* 326 */           sbuf.append(", ");
/*     */         }
/*     */       }
/* 329 */       seenMap.remove(a);
/*     */     } else {
/* 331 */       sbuf.append("...");
/*     */     }
/* 333 */     sbuf.append(']');
/*     */   }
/*     */   
/*     */   private static void booleanArrayAppend(StringBuilder sbuf, boolean[] a) {
/* 337 */     sbuf.append('[');
/* 338 */     int len = a.length;
/* 339 */     for (int i = 0; i < len; i++) {
/* 340 */       sbuf.append(a[i]);
/* 341 */       if (i != len - 1)
/* 342 */         sbuf.append(", ");
/*     */     }
/* 344 */     sbuf.append(']');
/*     */   }
/*     */   
/*     */   private static void byteArrayAppend(StringBuilder sbuf, byte[] a) {
/* 348 */     sbuf.append('[');
/* 349 */     int len = a.length;
/* 350 */     for (int i = 0; i < len; i++) {
/* 351 */       sbuf.append(a[i]);
/* 352 */       if (i != len - 1)
/* 353 */         sbuf.append(", ");
/*     */     }
/* 355 */     sbuf.append(']');
/*     */   }
/*     */   
/*     */   private static void charArrayAppend(StringBuilder sbuf, char[] a) {
/* 359 */     sbuf.append('[');
/* 360 */     int len = a.length;
/* 361 */     for (int i = 0; i < len; i++) {
/* 362 */       sbuf.append(a[i]);
/* 363 */       if (i != len - 1)
/* 364 */         sbuf.append(", ");
/*     */     }
/* 366 */     sbuf.append(']');
/*     */   }
/*     */   
/*     */   private static void shortArrayAppend(StringBuilder sbuf, short[] a) {
/* 370 */     sbuf.append('[');
/* 371 */     int len = a.length;
/* 372 */     for (int i = 0; i < len; i++) {
/* 373 */       sbuf.append(a[i]);
/* 374 */       if (i != len - 1)
/* 375 */         sbuf.append(", ");
/*     */     }
/* 377 */     sbuf.append(']');
/*     */   }
/*     */   
/*     */   private static void intArrayAppend(StringBuilder sbuf, int[] a) {
/* 381 */     sbuf.append('[');
/* 382 */     int len = a.length;
/* 383 */     for (int i = 0; i < len; i++) {
/* 384 */       sbuf.append(a[i]);
/* 385 */       if (i != len - 1)
/* 386 */         sbuf.append(", ");
/*     */     }
/* 388 */     sbuf.append(']');
/*     */   }
/*     */   
/*     */   private static void longArrayAppend(StringBuilder sbuf, long[] a) {
/* 392 */     sbuf.append('[');
/* 393 */     int len = a.length;
/* 394 */     for (int i = 0; i < len; i++) {
/* 395 */       sbuf.append(a[i]);
/* 396 */       if (i != len - 1)
/* 397 */         sbuf.append(", ");
/*     */     }
/* 399 */     sbuf.append(']');
/*     */   }
/*     */   
/*     */   private static void floatArrayAppend(StringBuilder sbuf, float[] a) {
/* 403 */     sbuf.append('[');
/* 404 */     int len = a.length;
/* 405 */     for (int i = 0; i < len; i++) {
/* 406 */       sbuf.append(a[i]);
/* 407 */       if (i != len - 1)
/* 408 */         sbuf.append(", ");
/*     */     }
/* 410 */     sbuf.append(']');
/*     */   }
/*     */   
/*     */   private static void doubleArrayAppend(StringBuilder sbuf, double[] a) {
/* 414 */     sbuf.append('[');
/* 415 */     int len = a.length;
/* 416 */     for (int i = 0; i < len; i++) {
/* 417 */       sbuf.append(a[i]);
/* 418 */       if (i != len - 1)
/* 419 */         sbuf.append(", ");
/*     */     }
/* 421 */     sbuf.append(']');
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\org\slf4j\helpers\MessageFormatter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */