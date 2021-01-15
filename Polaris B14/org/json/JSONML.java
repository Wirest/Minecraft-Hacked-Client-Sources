/*     */ package org.json;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JSONML
/*     */ {
/*     */   private static Object parse(XMLTokener x, boolean arrayForm, JSONArray ja, boolean keepStrings)
/*     */     throws JSONException
/*     */   {
/*  54 */     String closeTag = null;
/*     */     
/*  56 */     JSONArray newja = null;
/*  57 */     JSONObject newjo = null;
/*     */     
/*  59 */     String tagName = null;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     for (;;)
/*     */     {
/*  68 */       if (!x.more()) {
/*  69 */         throw x.syntaxError("Bad XML");
/*     */       }
/*  71 */       Object token = x.nextContent();
/*  72 */       if (token == XML.LT) {
/*  73 */         token = x.nextToken();
/*  74 */         if ((token instanceof Character)) {
/*  75 */           if (token == XML.SLASH)
/*     */           {
/*     */ 
/*     */ 
/*  79 */             token = x.nextToken();
/*  80 */             if (!(token instanceof String)) {
/*  81 */               throw new JSONException(
/*  82 */                 "Expected a closing name instead of '" + 
/*  83 */                 token + "'.");
/*     */             }
/*  85 */             if (x.nextToken() != XML.GT) {
/*  86 */               throw x.syntaxError("Misshaped close tag");
/*     */             }
/*  88 */             return token; }
/*  89 */           if (token == XML.BANG)
/*     */           {
/*     */ 
/*     */ 
/*  93 */             char c = x.next();
/*  94 */             if (c == '-') {
/*  95 */               if (x.next() == '-') {
/*  96 */                 x.skipPast("-->");
/*     */               } else {
/*  98 */                 x.back();
/*     */               }
/* 100 */             } else if (c == '[') {
/* 101 */               token = x.nextToken();
/* 102 */               if ((token.equals("CDATA")) && (x.next() == '[')) {
/* 103 */                 if (ja != null) {
/* 104 */                   ja.put(x.nextCDATA());
/*     */                 }
/*     */               } else {
/* 107 */                 throw x.syntaxError("Expected 'CDATA['");
/*     */               }
/*     */             } else {
/* 110 */               int i = 1;
/*     */               do {
/* 112 */                 token = x.nextMeta();
/* 113 */                 if (token == null)
/* 114 */                   throw x.syntaxError("Missing '>' after '<!'.");
/* 115 */                 if (token == XML.LT) {
/* 116 */                   i++;
/* 117 */                 } else if (token == XML.GT) {
/* 118 */                   i--;
/*     */                 }
/* 111 */               } while (
/*     */               
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 120 */                 i > 0);
/*     */             }
/* 122 */           } else if (token == XML.QUEST)
/*     */           {
/*     */ 
/*     */ 
/* 126 */             x.skipPast("?>");
/*     */           } else {
/* 128 */             throw x.syntaxError("Misshaped tag");
/*     */           }
/*     */           
/*     */         }
/*     */         else
/*     */         {
/* 134 */           if (!(token instanceof String)) {
/* 135 */             throw x.syntaxError("Bad tagName '" + token + "'.");
/*     */           }
/* 137 */           tagName = (String)token;
/* 138 */           newja = new JSONArray();
/* 139 */           newjo = new JSONObject();
/* 140 */           if (arrayForm) {
/* 141 */             newja.put(tagName);
/* 142 */             if (ja != null) {
/* 143 */               ja.put(newja);
/*     */             }
/*     */           } else {
/* 146 */             newjo.put("tagName", tagName);
/* 147 */             if (ja != null) {
/* 148 */               ja.put(newjo);
/*     */             }
/*     */           }
/* 151 */           token = null;
/*     */           for (;;) {
/* 153 */             if (token == null) {
/* 154 */               token = x.nextToken();
/*     */             }
/* 156 */             if (token == null) {
/* 157 */               throw x.syntaxError("Misshaped tag");
/*     */             }
/* 159 */             if (!(token instanceof String)) {
/*     */               break;
/*     */             }
/*     */             
/*     */ 
/*     */ 
/* 165 */             String attribute = (String)token;
/* 166 */             if ((!arrayForm) && (("tagName".equals(attribute)) || ("childNode".equals(attribute)))) {
/* 167 */               throw x.syntaxError("Reserved attribute.");
/*     */             }
/* 169 */             token = x.nextToken();
/* 170 */             if (token == XML.EQ) {
/* 171 */               token = x.nextToken();
/* 172 */               if (!(token instanceof String)) {
/* 173 */                 throw x.syntaxError("Missing value");
/*     */               }
/* 175 */               newjo.accumulate(attribute, keepStrings ? (String)token : XML.stringToValue((String)token));
/* 176 */               token = null;
/*     */             } else {
/* 178 */               newjo.accumulate(attribute, "");
/*     */             }
/*     */           }
/* 181 */           if ((arrayForm) && (newjo.length() > 0)) {
/* 182 */             newja.put(newjo);
/*     */           }
/*     */           
/*     */ 
/*     */ 
/* 187 */           if (token == XML.SLASH) {
/* 188 */             if (x.nextToken() != XML.GT) {
/* 189 */               throw x.syntaxError("Misshaped tag");
/*     */             }
/* 191 */             if (ja == null) {
/* 192 */               if (arrayForm) {
/* 193 */                 return newja;
/*     */               }
/* 195 */               return newjo;
/*     */             }
/*     */             
/*     */           }
/*     */           else
/*     */           {
/* 201 */             if (token != XML.GT) {
/* 202 */               throw x.syntaxError("Misshaped tag");
/*     */             }
/* 204 */             closeTag = (String)parse(x, arrayForm, newja, keepStrings);
/* 205 */             if (closeTag != null) {
/* 206 */               if (!closeTag.equals(tagName)) {
/* 207 */                 throw x.syntaxError("Mismatched '" + tagName + 
/* 208 */                   "' and '" + closeTag + "'");
/*     */               }
/* 210 */               tagName = null;
/* 211 */               if ((!arrayForm) && (newja.length() > 0)) {
/* 212 */                 newjo.put("childNodes", newja);
/*     */               }
/* 214 */               if (ja == null) {
/* 215 */                 if (arrayForm) {
/* 216 */                   return newja;
/*     */                 }
/* 218 */                 return newjo;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 224 */       else if (ja != null) {
/* 225 */         ja.put((token instanceof String) ? 
/* 226 */           XML.stringToValue((String)token) : keepStrings ? XML.unescape((String)token) : 
/* 227 */           token);
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
/*     */   public static JSONArray toJSONArray(String string)
/*     */     throws JSONException
/*     */   {
/* 247 */     return (JSONArray)parse(new XMLTokener(string), true, null, false);
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
/*     */   public static JSONArray toJSONArray(String string, boolean keepStrings)
/*     */     throws JSONException
/*     */   {
/* 269 */     return (JSONArray)parse(new XMLTokener(string), true, null, keepStrings);
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
/*     */   public static JSONArray toJSONArray(XMLTokener x, boolean keepStrings)
/*     */     throws JSONException
/*     */   {
/* 291 */     return (JSONArray)parse(x, true, null, keepStrings);
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
/*     */   public static JSONArray toJSONArray(XMLTokener x)
/*     */     throws JSONException
/*     */   {
/* 308 */     return (JSONArray)parse(x, true, null, false);
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
/*     */   public static JSONObject toJSONObject(String string)
/*     */     throws JSONException
/*     */   {
/* 326 */     return (JSONObject)parse(new XMLTokener(string), false, null, false);
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
/*     */   public static JSONObject toJSONObject(String string, boolean keepStrings)
/*     */     throws JSONException
/*     */   {
/* 346 */     return (JSONObject)parse(new XMLTokener(string), false, null, keepStrings);
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
/*     */   public static JSONObject toJSONObject(XMLTokener x)
/*     */     throws JSONException
/*     */   {
/* 364 */     return (JSONObject)parse(x, false, null, false);
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
/*     */   public static JSONObject toJSONObject(XMLTokener x, boolean keepStrings)
/*     */     throws JSONException
/*     */   {
/* 384 */     return (JSONObject)parse(x, false, null, keepStrings);
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
/*     */   public static String toString(JSONArray ja)
/*     */     throws JSONException
/*     */   {
/* 399 */     StringBuilder sb = new StringBuilder();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 404 */     String tagName = ja.getString(0);
/* 405 */     XML.noSpace(tagName);
/* 406 */     tagName = XML.escape(tagName);
/* 407 */     sb.append('<');
/* 408 */     sb.append(tagName);
/*     */     
/* 410 */     Object object = ja.opt(1);
/* 411 */     int i; if ((object instanceof JSONObject)) {
/* 412 */       int i = 2;
/* 413 */       JSONObject jo = (JSONObject)object;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 418 */       for (String key : jo.keySet()) {
/* 419 */         Object value = jo.opt(key);
/* 420 */         XML.noSpace(key);
/* 421 */         if (value != null) {
/* 422 */           sb.append(' ');
/* 423 */           sb.append(XML.escape(key));
/* 424 */           sb.append('=');
/* 425 */           sb.append('"');
/* 426 */           sb.append(XML.escape(value.toString()));
/* 427 */           sb.append('"');
/*     */         }
/*     */       }
/*     */     } else {
/* 431 */       i = 1;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 436 */     int length = ja.length();
/* 437 */     if (i >= length) {
/* 438 */       sb.append('/');
/* 439 */       sb.append('>');
/*     */     } else {
/* 441 */       sb.append('>');
/*     */       do {
/* 443 */         object = ja.get(i);
/* 444 */         i++;
/* 445 */         if (object != null) {
/* 446 */           if ((object instanceof String)) {
/* 447 */             sb.append(XML.escape(object.toString()));
/* 448 */           } else if ((object instanceof JSONObject)) {
/* 449 */             sb.append(toString((JSONObject)object));
/* 450 */           } else if ((object instanceof JSONArray)) {
/* 451 */             sb.append(toString((JSONArray)object));
/*     */           } else {
/* 453 */             sb.append(object.toString());
/*     */           }
/*     */         }
/* 442 */       } while (
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 456 */         i < length);
/* 457 */       sb.append('<');
/* 458 */       sb.append('/');
/* 459 */       sb.append(tagName);
/* 460 */       sb.append('>');
/*     */     }
/* 462 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String toString(JSONObject jo)
/*     */     throws JSONException
/*     */   {
/* 475 */     StringBuilder sb = new StringBuilder();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 485 */     String tagName = jo.optString("tagName");
/* 486 */     if (tagName == null) {
/* 487 */       return XML.escape(jo.toString());
/*     */     }
/* 489 */     XML.noSpace(tagName);
/* 490 */     tagName = XML.escape(tagName);
/* 491 */     sb.append('<');
/* 492 */     sb.append(tagName);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 497 */     for (String key : jo.keySet()) {
/* 498 */       if ((!"tagName".equals(key)) && (!"childNodes".equals(key))) {
/* 499 */         XML.noSpace(key);
/* 500 */         Object value = jo.opt(key);
/* 501 */         if (value != null) {
/* 502 */           sb.append(' ');
/* 503 */           sb.append(XML.escape(key));
/* 504 */           sb.append('=');
/* 505 */           sb.append('"');
/* 506 */           sb.append(XML.escape(value.toString()));
/* 507 */           sb.append('"');
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 514 */     JSONArray ja = jo.optJSONArray("childNodes");
/* 515 */     if (ja == null) {
/* 516 */       sb.append('/');
/* 517 */       sb.append('>');
/*     */     } else {
/* 519 */       sb.append('>');
/* 520 */       int length = ja.length();
/* 521 */       for (int i = 0; i < length; i++) {
/* 522 */         Object object = ja.get(i);
/* 523 */         if (object != null) {
/* 524 */           if ((object instanceof String)) {
/* 525 */             sb.append(XML.escape(object.toString()));
/* 526 */           } else if ((object instanceof JSONObject)) {
/* 527 */             sb.append(toString((JSONObject)object));
/* 528 */           } else if ((object instanceof JSONArray)) {
/* 529 */             sb.append(toString((JSONArray)object));
/*     */           } else {
/* 531 */             sb.append(object.toString());
/*     */           }
/*     */         }
/*     */       }
/* 535 */       sb.append('<');
/* 536 */       sb.append('/');
/* 537 */       sb.append(tagName);
/* 538 */       sb.append('>');
/*     */     }
/* 540 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\org\json\JSONML.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */