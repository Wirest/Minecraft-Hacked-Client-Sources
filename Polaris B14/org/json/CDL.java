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
/*     */ public class CDL
/*     */ {
/*     */   private static String getValue(JSONTokener x)
/*     */     throws JSONException
/*     */   {
/*     */     char c;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     do
/*     */     {
/*  60 */       c = x.next();
/*  61 */     } while ((c == ' ') || (c == '\t'));
/*  62 */     switch (c) {
/*     */     case '\000': 
/*  64 */       return null;
/*     */     case '"': 
/*     */     case '\'': 
/*  67 */       char q = c;
/*  68 */       StringBuffer sb = new StringBuffer();
/*     */       for (;;) {
/*  70 */         c = x.next();
/*  71 */         if (c == q)
/*     */         {
/*  73 */           char nextC = x.next();
/*  74 */           if (nextC != '"')
/*     */           {
/*  76 */             if (nextC <= 0) break;
/*  77 */             x.back();
/*     */             
/*  79 */             break;
/*     */           }
/*     */         }
/*  82 */         if ((c == 0) || (c == '\n') || (c == '\r')) {
/*  83 */           throw x.syntaxError("Missing close quote '" + q + "'.");
/*     */         }
/*  85 */         sb.append(c);
/*     */       }
/*  87 */       return sb.toString();
/*     */     case ',': 
/*  89 */       x.back();
/*  90 */       return "";
/*     */     }
/*  92 */     x.back();
/*  93 */     return x.nextTo(',');
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static JSONArray rowToJSONArray(JSONTokener x)
/*     */     throws JSONException
/*     */   {
/* 104 */     JSONArray ja = new JSONArray();
/*     */     for (;;) {
/* 106 */       String value = getValue(x);
/* 107 */       char c = x.next();
/* 108 */       if ((value == null) || (
/* 109 */         (ja.length() == 0) && (value.length() == 0) && (c != ','))) {
/* 110 */         return null;
/*     */       }
/* 112 */       ja.put(value);
/*     */       
/* 114 */       while (c != ',')
/*     */       {
/*     */ 
/* 117 */         if (c != ' ') {
/* 118 */           if ((c == '\n') || (c == '\r') || (c == 0)) {
/* 119 */             return ja;
/*     */           }
/* 121 */           throw x.syntaxError("Bad character '" + c + "' (" + 
/* 122 */             c + ").");
/*     */         }
/* 124 */         c = x.next();
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
/*     */   public static JSONObject rowToJSONObject(JSONArray names, JSONTokener x)
/*     */     throws JSONException
/*     */   {
/* 141 */     JSONArray ja = rowToJSONArray(x);
/* 142 */     return ja != null ? ja.toJSONObject(names) : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String rowToString(JSONArray ja)
/*     */   {
/* 153 */     StringBuilder sb = new StringBuilder();
/* 154 */     for (int i = 0; i < ja.length(); i++) {
/* 155 */       if (i > 0) {
/* 156 */         sb.append(',');
/*     */       }
/* 158 */       Object object = ja.opt(i);
/* 159 */       if (object != null) {
/* 160 */         String string = object.toString();
/* 161 */         if ((string.length() > 0) && ((string.indexOf(',') >= 0) || 
/* 162 */           (string.indexOf('\n') >= 0) || (string.indexOf('\r') >= 0) || 
/* 163 */           (string.indexOf(0) >= 0) || (string.charAt(0) == '"'))) {
/* 164 */           sb.append('"');
/* 165 */           int length = string.length();
/* 166 */           for (int j = 0; j < length; j++) {
/* 167 */             char c = string.charAt(j);
/* 168 */             if ((c >= ' ') && (c != '"')) {
/* 169 */               sb.append(c);
/*     */             }
/*     */           }
/* 172 */           sb.append('"');
/*     */         } else {
/* 174 */           sb.append(string);
/*     */         }
/*     */       }
/*     */     }
/* 178 */     sb.append('\n');
/* 179 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static JSONArray toJSONArray(String string)
/*     */     throws JSONException
/*     */   {
/* 190 */     return toJSONArray(new JSONTokener(string));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static JSONArray toJSONArray(JSONTokener x)
/*     */     throws JSONException
/*     */   {
/* 201 */     return toJSONArray(rowToJSONArray(x), x);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static JSONArray toJSONArray(JSONArray names, String string)
/*     */     throws JSONException
/*     */   {
/* 214 */     return toJSONArray(names, new JSONTokener(string));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static JSONArray toJSONArray(JSONArray names, JSONTokener x)
/*     */     throws JSONException
/*     */   {
/* 227 */     if ((names == null) || (names.length() == 0)) {
/* 228 */       return null;
/*     */     }
/* 230 */     JSONArray ja = new JSONArray();
/*     */     for (;;) {
/* 232 */       JSONObject jo = rowToJSONObject(names, x);
/* 233 */       if (jo == null) {
/*     */         break;
/*     */       }
/* 236 */       ja.put(jo);
/*     */     }
/* 238 */     if (ja.length() == 0) {
/* 239 */       return null;
/*     */     }
/* 241 */     return ja;
/*     */   }
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
/* 254 */     JSONObject jo = ja.optJSONObject(0);
/* 255 */     if (jo != null) {
/* 256 */       JSONArray names = jo.names();
/* 257 */       if (names != null) {
/* 258 */         return rowToString(names) + toString(names, ja);
/*     */       }
/*     */     }
/* 261 */     return null;
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
/*     */   public static String toString(JSONArray names, JSONArray ja)
/*     */     throws JSONException
/*     */   {
/* 275 */     if ((names == null) || (names.length() == 0)) {
/* 276 */       return null;
/*     */     }
/* 278 */     StringBuffer sb = new StringBuffer();
/* 279 */     for (int i = 0; i < ja.length(); i++) {
/* 280 */       JSONObject jo = ja.optJSONObject(i);
/* 281 */       if (jo != null) {
/* 282 */         sb.append(rowToString(jo.toJSONArray(names)));
/*     */       }
/*     */     }
/* 285 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\org\json\CDL.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */