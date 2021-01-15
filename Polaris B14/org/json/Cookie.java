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
/*     */ public class Cookie
/*     */ {
/*     */   public static String escape(String string)
/*     */   {
/*  49 */     String s = string.trim();
/*  50 */     int length = s.length();
/*  51 */     StringBuilder sb = new StringBuilder(length);
/*  52 */     for (int i = 0; i < length; i++) {
/*  53 */       char c = s.charAt(i);
/*  54 */       if ((c < ' ') || (c == '+') || (c == '%') || (c == '=') || (c == ';')) {
/*  55 */         sb.append('%');
/*  56 */         sb.append(Character.forDigit((char)(c >>> '\004' & 0xF), 16));
/*  57 */         sb.append(Character.forDigit((char)(c & 0xF), 16));
/*     */       } else {
/*  59 */         sb.append(c);
/*     */       }
/*     */     }
/*  62 */     return sb.toString();
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
/*     */   public static JSONObject toJSONObject(String string)
/*     */     throws JSONException
/*     */   {
/*  83 */     JSONObject jo = new JSONObject();
/*     */     
/*  85 */     JSONTokener x = new JSONTokener(string);
/*  86 */     jo.put("name", x.nextTo('='));
/*  87 */     x.next('=');
/*  88 */     jo.put("value", x.nextTo(';'));
/*  89 */     x.next();
/*  90 */     while (x.more()) {
/*  91 */       String name = unescape(x.nextTo("=;"));
/*  92 */       Object value; if (x.next() != '=') { Object value;
/*  93 */         if (name.equals("secure")) {
/*  94 */           value = Boolean.TRUE;
/*     */         } else {
/*  96 */           throw x.syntaxError("Missing '=' in cookie parameter.");
/*     */         }
/*     */       } else {
/*  99 */         value = unescape(x.nextTo(';'));
/* 100 */         x.next();
/*     */       }
/* 102 */       jo.put(name, value);
/*     */     }
/* 104 */     return jo;
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
/*     */   public static String toString(JSONObject jo)
/*     */     throws JSONException
/*     */   {
/* 119 */     StringBuilder sb = new StringBuilder();
/*     */     
/* 121 */     sb.append(escape(jo.getString("name")));
/* 122 */     sb.append("=");
/* 123 */     sb.append(escape(jo.getString("value")));
/* 124 */     if (jo.has("expires")) {
/* 125 */       sb.append(";expires=");
/* 126 */       sb.append(jo.getString("expires"));
/*     */     }
/* 128 */     if (jo.has("domain")) {
/* 129 */       sb.append(";domain=");
/* 130 */       sb.append(escape(jo.getString("domain")));
/*     */     }
/* 132 */     if (jo.has("path")) {
/* 133 */       sb.append(";path=");
/* 134 */       sb.append(escape(jo.getString("path")));
/*     */     }
/* 136 */     if (jo.optBoolean("secure")) {
/* 137 */       sb.append(";secure");
/*     */     }
/* 139 */     return sb.toString();
/*     */   }
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
/* 151 */     int length = string.length();
/* 152 */     StringBuilder sb = new StringBuilder(length);
/* 153 */     for (int i = 0; i < length; i++) {
/* 154 */       char c = string.charAt(i);
/* 155 */       if (c == '+') {
/* 156 */         c = ' ';
/* 157 */       } else if ((c == '%') && (i + 2 < length)) {
/* 158 */         int d = JSONTokener.dehexchar(string.charAt(i + 1));
/* 159 */         int e = JSONTokener.dehexchar(string.charAt(i + 2));
/* 160 */         if ((d >= 0) && (e >= 0)) {
/* 161 */           c = (char)(d * 16 + e);
/* 162 */           i += 2;
/*     */         }
/*     */       }
/* 165 */       sb.append(c);
/*     */     }
/* 167 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\org\json\Cookie.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */