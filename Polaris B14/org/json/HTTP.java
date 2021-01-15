/*     */ package org.json;
/*     */ 
/*     */ import java.util.Locale;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HTTP
/*     */ {
/*     */   public static final String CRLF = "\r\n";
/*     */   
/*     */   public static JSONObject toJSONObject(String string)
/*     */     throws JSONException
/*     */   {
/*  72 */     JSONObject jo = new JSONObject();
/*  73 */     HTTPTokener x = new HTTPTokener(string);
/*     */     
/*     */ 
/*  76 */     String token = x.nextToken();
/*  77 */     if (token.toUpperCase(Locale.ROOT).startsWith("HTTP"))
/*     */     {
/*     */ 
/*     */ 
/*  81 */       jo.put("HTTP-Version", token);
/*  82 */       jo.put("Status-Code", x.nextToken());
/*  83 */       jo.put("Reason-Phrase", x.nextTo('\000'));
/*  84 */       x.next();
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/*     */ 
/*  90 */       jo.put("Method", token);
/*  91 */       jo.put("Request-URI", x.nextToken());
/*  92 */       jo.put("HTTP-Version", x.nextToken());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  97 */     while (x.more()) {
/*  98 */       String name = x.nextTo(':');
/*  99 */       x.next(':');
/* 100 */       jo.put(name, x.nextTo('\000'));
/* 101 */       x.next();
/*     */     }
/* 103 */     return jo;
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
/*     */   public static String toString(JSONObject jo)
/*     */     throws JSONException
/*     */   {
/* 128 */     StringBuilder sb = new StringBuilder();
/* 129 */     if ((jo.has("Status-Code")) && (jo.has("Reason-Phrase"))) {
/* 130 */       sb.append(jo.getString("HTTP-Version"));
/* 131 */       sb.append(' ');
/* 132 */       sb.append(jo.getString("Status-Code"));
/* 133 */       sb.append(' ');
/* 134 */       sb.append(jo.getString("Reason-Phrase"));
/* 135 */     } else if ((jo.has("Method")) && (jo.has("Request-URI"))) {
/* 136 */       sb.append(jo.getString("Method"));
/* 137 */       sb.append(' ');
/* 138 */       sb.append('"');
/* 139 */       sb.append(jo.getString("Request-URI"));
/* 140 */       sb.append('"');
/* 141 */       sb.append(' ');
/* 142 */       sb.append(jo.getString("HTTP-Version"));
/*     */     } else {
/* 144 */       throw new JSONException("Not enough material for an HTTP header.");
/*     */     }
/* 146 */     sb.append("\r\n");
/*     */     
/* 148 */     for (String key : jo.keySet()) {
/* 149 */       String value = jo.optString(key);
/* 150 */       if ((!"HTTP-Version".equals(key)) && (!"Status-Code".equals(key)) && 
/* 151 */         (!"Reason-Phrase".equals(key)) && (!"Method".equals(key)) && 
/* 152 */         (!"Request-URI".equals(key)) && (!JSONObject.NULL.equals(value))) {
/* 153 */         sb.append(key);
/* 154 */         sb.append(": ");
/* 155 */         sb.append(jo.optString(key));
/* 156 */         sb.append("\r\n");
/*     */       }
/*     */     }
/* 159 */     sb.append("\r\n");
/* 160 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\org\json\HTTP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */