/*     */ package org.json;
/*     */ 
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XML
/*     */ {
/*  41 */   public static final Character AMP = Character.valueOf('&');
/*     */   
/*     */ 
/*  44 */   public static final Character APOS = Character.valueOf('\'');
/*     */   
/*     */ 
/*  47 */   public static final Character BANG = Character.valueOf('!');
/*     */   
/*     */ 
/*  50 */   public static final Character EQ = Character.valueOf('=');
/*     */   
/*     */ 
/*  53 */   public static final Character GT = Character.valueOf('>');
/*     */   
/*     */ 
/*  56 */   public static final Character LT = Character.valueOf('<');
/*     */   
/*     */ 
/*  59 */   public static final Character QUEST = Character.valueOf('?');
/*     */   
/*     */ 
/*  62 */   public static final Character QUOT = Character.valueOf('"');
/*     */   
/*     */ 
/*  65 */   public static final Character SLASH = Character.valueOf('/');
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static Iterable<Integer> codePointIterator(String string)
/*     */   {
/*  79 */     new Iterable()
/*     */     {
/*     */       public Iterator<Integer> iterator() {
/*  82 */         new Iterator()
/*     */         {
/*     */           private int nextIndex;
/*     */           private int length;
/*     */           
/*     */           public boolean hasNext() {
/*  88 */             return this.nextIndex < this.length;
/*     */           }
/*     */           
/*     */           public Integer next()
/*     */           {
/*  93 */             int result = this.val$string.codePointAt(this.nextIndex);
/*  94 */             this.nextIndex += Character.charCount(result);
/*  95 */             return Integer.valueOf(result);
/*     */           }
/*     */           
/*     */           public void remove()
/*     */           {
/* 100 */             throw new UnsupportedOperationException();
/*     */           }
/*     */         };
/*     */       }
/*     */     };
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
/*     */   public static String escape(String string)
/*     */   {
/* 123 */     StringBuilder sb = new StringBuilder(string.length());
/* 124 */     for (Iterator localIterator = codePointIterator(string).iterator(); localIterator.hasNext();) { int cp = ((Integer)localIterator.next()).intValue();
/* 125 */       switch (cp) {
/*     */       case 38: 
/* 127 */         sb.append("&amp;");
/* 128 */         break;
/*     */       case 60: 
/* 130 */         sb.append("&lt;");
/* 131 */         break;
/*     */       case 62: 
/* 133 */         sb.append("&gt;");
/* 134 */         break;
/*     */       case 34: 
/* 136 */         sb.append("&quot;");
/* 137 */         break;
/*     */       case 39: 
/* 139 */         sb.append("&apos;");
/* 140 */         break;
/*     */       default: 
/* 142 */         if (mustEscape(cp)) {
/* 143 */           sb.append("&#x");
/* 144 */           sb.append(Integer.toHexString(cp));
/* 145 */           sb.append(';');
/*     */         } else {
/* 147 */           sb.appendCodePoint(cp);
/*     */         }
/*     */         break; }
/*     */     }
/* 151 */     return sb.toString();
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
/*     */   private static boolean mustEscape(int cp)
/*     */   {
/* 167 */     if ((!Character.isISOControl(cp)) || 
/* 168 */       (cp == 9) || 
/* 169 */       (cp == 10) || 
/* 170 */       (cp == 13))
/*     */     {
/*     */ 
/* 173 */       if (((cp >= 32) && (cp <= 55295)) || 
/* 174 */         ((cp >= 57344) && (cp <= 65533)) || (
/* 175 */         (cp >= 65536) && (cp <= 1114111)))
/* 167 */         return false; } return true;
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
/*     */   public static String unescape(String string)
/*     */   {
/* 188 */     StringBuilder sb = new StringBuilder(string.length());
/* 189 */     int i = 0; for (int length = string.length(); i < length; i++) {
/* 190 */       char c = string.charAt(i);
/* 191 */       if (c == '&') {
/* 192 */         int semic = string.indexOf(';', i);
/* 193 */         if (semic > i) {
/* 194 */           String entity = string.substring(i + 1, semic);
/* 195 */           sb.append(XMLTokener.unescapeEntity(entity));
/*     */           
/* 197 */           i += entity.length() + 1;
/*     */         }
/*     */         else
/*     */         {
/* 201 */           sb.append(c);
/*     */         }
/*     */       }
/*     */       else {
/* 205 */         sb.append(c);
/*     */       }
/*     */     }
/* 208 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void noSpace(String string)
/*     */     throws JSONException
/*     */   {
/* 220 */     int length = string.length();
/* 221 */     if (length == 0) {
/* 222 */       throw new JSONException("Empty string.");
/*     */     }
/* 224 */     for (int i = 0; i < length; i++) {
/* 225 */       if (Character.isWhitespace(string.charAt(i))) {
/* 226 */         throw new JSONException("'" + string + 
/* 227 */           "' contains a space character.");
/*     */       }
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
/*     */   private static boolean parse(XMLTokener x, JSONObject context, String name, boolean keepStrings)
/*     */     throws JSONException
/*     */   {
/* 248 */     JSONObject jsonobject = null;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 263 */     Object token = x.nextToken();
/*     */     
/*     */ 
/*     */ 
/* 267 */     if (token == BANG) {
/* 268 */       char c = x.next();
/* 269 */       if (c == '-') {
/* 270 */         if (x.next() == '-') {
/* 271 */           x.skipPast("-->");
/* 272 */           return false;
/*     */         }
/* 274 */         x.back();
/* 275 */       } else if (c == '[') {
/* 276 */         token = x.nextToken();
/* 277 */         if (("CDATA".equals(token)) && 
/* 278 */           (x.next() == '[')) {
/* 279 */           String string = x.nextCDATA();
/* 280 */           if (string.length() > 0) {
/* 281 */             context.accumulate("content", string);
/*     */           }
/* 283 */           return false;
/*     */         }
/*     */         
/* 286 */         throw x.syntaxError("Expected 'CDATA['");
/*     */       }
/* 288 */       int i = 1;
/*     */       do {
/* 290 */         token = x.nextMeta();
/* 291 */         if (token == null)
/* 292 */           throw x.syntaxError("Missing '>' after '<!'.");
/* 293 */         if (token == LT) {
/* 294 */           i++;
/* 295 */         } else if (token == GT) {
/* 296 */           i--;
/*     */         }
/* 289 */       } while (
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 298 */         i > 0);
/* 299 */       return false; }
/* 300 */     if (token == QUEST)
/*     */     {
/*     */ 
/* 303 */       x.skipPast("?>");
/* 304 */       return false; }
/* 305 */     if (token == SLASH)
/*     */     {
/*     */ 
/*     */ 
/* 309 */       token = x.nextToken();
/* 310 */       if (name == null) {
/* 311 */         throw x.syntaxError("Mismatched close tag " + token);
/*     */       }
/* 313 */       if (!token.equals(name)) {
/* 314 */         throw x.syntaxError("Mismatched " + name + " and " + token);
/*     */       }
/* 316 */       if (x.nextToken() != GT) {
/* 317 */         throw x.syntaxError("Misshaped close tag");
/*     */       }
/* 319 */       return true;
/*     */     }
/* 321 */     if ((token instanceof Character)) {
/* 322 */       throw x.syntaxError("Misshaped tag");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 327 */     String tagName = (String)token;
/* 328 */     token = null;
/* 329 */     jsonobject = new JSONObject();
/*     */     for (;;) {
/* 331 */       if (token == null) {
/* 332 */         token = x.nextToken();
/*     */       }
/*     */       
/* 335 */       if (!(token instanceof String)) break;
/* 336 */       String string = (String)token;
/* 337 */       token = x.nextToken();
/* 338 */       if (token == EQ) {
/* 339 */         token = x.nextToken();
/* 340 */         if (!(token instanceof String)) {
/* 341 */           throw x.syntaxError("Missing value");
/*     */         }
/* 343 */         jsonobject.accumulate(string, 
/* 344 */           keepStrings ? (String)token : stringToValue((String)token));
/* 345 */         token = null;
/*     */       } else {
/* 347 */         jsonobject.accumulate(string, "");
/*     */       }
/*     */     }
/*     */     
/* 351 */     if (token == SLASH)
/*     */     {
/* 353 */       if (x.nextToken() != GT) {
/* 354 */         throw x.syntaxError("Misshaped tag");
/*     */       }
/* 356 */       if (jsonobject.length() > 0) {
/* 357 */         context.accumulate(tagName, jsonobject);
/*     */       } else {
/* 359 */         context.accumulate(tagName, "");
/*     */       }
/* 361 */       return false;
/*     */     }
/* 363 */     if (token == GT) {
/*     */       do {
/*     */         for (;;) {
/* 366 */           token = x.nextContent();
/* 367 */           if (token == null) {
/* 368 */             if (tagName != null) {
/* 369 */               throw x.syntaxError("Unclosed tag " + tagName);
/*     */             }
/* 371 */             return false; }
/* 372 */           if (!(token instanceof String)) break;
/* 373 */           String string = (String)token;
/* 374 */           if (string.length() > 0) {
/* 375 */             jsonobject.accumulate("content", 
/* 376 */               keepStrings ? string : stringToValue(string));
/*     */           }
/*     */         }
/* 379 */       } while ((token != LT) || 
/*     */       
/* 381 */         (!parse(x, jsonobject, tagName, keepStrings)));
/* 382 */       if (jsonobject.length() == 0) {
/* 383 */         context.accumulate(tagName, "");
/* 384 */       } else if ((jsonobject.length() == 1) && 
/* 385 */         (jsonobject.opt("content") != null)) {
/* 386 */         context.accumulate(tagName, 
/* 387 */           jsonobject.opt("content"));
/*     */       } else {
/* 389 */         context.accumulate(tagName, jsonobject);
/*     */       }
/* 391 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 396 */     throw x.syntaxError("Misshaped tag");
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
/*     */   public static Object stringToValue(String string)
/*     */   {
/* 411 */     if (string.equals("")) {
/* 412 */       return string;
/*     */     }
/* 414 */     if (string.equalsIgnoreCase("true")) {
/* 415 */       return Boolean.TRUE;
/*     */     }
/* 417 */     if (string.equalsIgnoreCase("false")) {
/* 418 */       return Boolean.FALSE;
/*     */     }
/* 420 */     if (string.equalsIgnoreCase("null")) {
/* 421 */       return JSONObject.NULL;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 429 */     char initial = string.charAt(0);
/* 430 */     if (((initial >= '0') && (initial <= '9')) || (initial == '-'))
/*     */     {
/*     */       try
/*     */       {
/* 434 */         if ((string.indexOf('.') > -1) || (string.indexOf('e') > -1) || 
/* 435 */           (string.indexOf('E') > -1) || ("-0".equals(string))) {
/* 436 */           Double d = Double.valueOf(string);
/* 437 */           if ((!d.isInfinite()) && (!d.isNaN())) {
/* 438 */             return d;
/*     */           }
/*     */         } else {
/* 441 */           Long myLong = Long.valueOf(string);
/* 442 */           if (string.equals(myLong.toString())) {
/* 443 */             if (myLong.longValue() == myLong.intValue()) {
/* 444 */               return Integer.valueOf(myLong.intValue());
/*     */             }
/* 446 */             return myLong;
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (Exception localException) {}
/*     */     }
/* 452 */     return string;
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
/*     */   public static JSONObject toJSONObject(String string)
/*     */     throws JSONException
/*     */   {
/* 472 */     return toJSONObject(string, false);
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
/*     */   public static JSONObject toJSONObject(Reader reader)
/*     */     throws JSONException
/*     */   {
/* 491 */     return toJSONObject(reader, false);
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
/*     */   public static JSONObject toJSONObject(Reader reader, boolean keepStrings)
/*     */     throws JSONException
/*     */   {
/* 515 */     JSONObject jo = new JSONObject();
/* 516 */     XMLTokener x = new XMLTokener(reader);
/* 517 */     while (x.more()) {
/* 518 */       x.skipPast("<");
/* 519 */       if (x.more()) {
/* 520 */         parse(x, jo, null, keepStrings);
/*     */       }
/*     */     }
/* 523 */     return jo;
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
/*     */   public static JSONObject toJSONObject(String string, boolean keepStrings)
/*     */     throws JSONException
/*     */   {
/* 548 */     return toJSONObject(new StringReader(string), keepStrings);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String toString(Object object)
/*     */     throws JSONException
/*     */   {
/* 560 */     return toString(object, null);
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
/*     */   public static String toString(Object object, String tagName)
/*     */     throws JSONException
/*     */   {
/* 575 */     StringBuilder sb = new StringBuilder();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 580 */     if ((object instanceof JSONObject))
/*     */     {
/*     */ 
/* 583 */       if (tagName != null) {
/* 584 */         sb.append('<');
/* 585 */         sb.append(tagName);
/* 586 */         sb.append('>');
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 591 */       JSONObject jo = (JSONObject)object;
/* 592 */       for (String key : jo.keySet()) {
/* 593 */         Object value = jo.opt(key);
/* 594 */         if (value == null) {
/* 595 */           value = "";
/* 596 */         } else if (value.getClass().isArray()) {
/* 597 */           value = new JSONArray(value);
/*     */         }
/*     */         
/*     */ 
/* 601 */         if ("content".equals(key)) {
/* 602 */           if ((value instanceof JSONArray)) {
/* 603 */             JSONArray ja = (JSONArray)value;
/* 604 */             int jaLength = ja.length();
/*     */             
/* 606 */             for (int i = 0; i < jaLength; i++) {
/* 607 */               if (i > 0) {
/* 608 */                 sb.append('\n');
/*     */               }
/* 610 */               Object val = ja.opt(i);
/* 611 */               sb.append(escape(val.toString()));
/*     */             }
/*     */           } else {
/* 614 */             sb.append(escape(value.toString()));
/*     */           }
/*     */           
/*     */ 
/*     */         }
/* 619 */         else if ((value instanceof JSONArray)) {
/* 620 */           JSONArray ja = (JSONArray)value;
/* 621 */           int jaLength = ja.length();
/*     */           
/* 623 */           for (int i = 0; i < jaLength; i++) {
/* 624 */             Object val = ja.opt(i);
/* 625 */             if ((val instanceof JSONArray)) {
/* 626 */               sb.append('<');
/* 627 */               sb.append(key);
/* 628 */               sb.append('>');
/* 629 */               sb.append(toString(val));
/* 630 */               sb.append("</");
/* 631 */               sb.append(key);
/* 632 */               sb.append('>');
/*     */             } else {
/* 634 */               sb.append(toString(val, key));
/*     */             }
/*     */           }
/* 637 */         } else if ("".equals(value)) {
/* 638 */           sb.append('<');
/* 639 */           sb.append(key);
/* 640 */           sb.append("/>");
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 645 */           sb.append(toString(value, key));
/*     */         }
/*     */       }
/* 648 */       if (tagName != null)
/*     */       {
/*     */ 
/* 651 */         sb.append("</");
/* 652 */         sb.append(tagName);
/* 653 */         sb.append('>');
/*     */       }
/* 655 */       return sb.toString();
/*     */     }
/*     */     
/*     */ 
/* 659 */     if ((object != null) && (((object instanceof JSONArray)) || (object.getClass().isArray()))) { JSONArray ja;
/* 660 */       JSONArray ja; if (object.getClass().isArray()) {
/* 661 */         ja = new JSONArray(object);
/*     */       } else {
/* 663 */         ja = (JSONArray)object;
/*     */       }
/* 665 */       int jaLength = ja.length();
/*     */       
/* 667 */       for (int i = 0; i < jaLength; i++) {
/* 668 */         Object val = ja.opt(i);
/*     */         
/*     */ 
/*     */ 
/* 672 */         sb.append(toString(val, tagName == null ? "array" : tagName));
/*     */       }
/* 674 */       return sb.toString();
/*     */     }
/*     */     
/* 677 */     String string = object == null ? "null" : escape(object.toString());
/* 678 */     return 
/* 679 */       "<" + tagName + 
/* 680 */       ">" + string + "</" + tagName + ">";
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\org\json\XML.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */