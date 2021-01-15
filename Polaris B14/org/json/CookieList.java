/*    */ package org.json;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CookieList
/*    */ {
/*    */   public static JSONObject toJSONObject(String string)
/*    */     throws JSONException
/*    */   {
/* 48 */     JSONObject jo = new JSONObject();
/* 49 */     JSONTokener x = new JSONTokener(string);
/* 50 */     while (x.more()) {
/* 51 */       String name = Cookie.unescape(x.nextTo('='));
/* 52 */       x.next('=');
/* 53 */       jo.put(name, Cookie.unescape(x.nextTo(';')));
/* 54 */       x.next();
/*    */     }
/* 56 */     return jo;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static String toString(JSONObject jo)
/*    */     throws JSONException
/*    */   {
/* 69 */     boolean b = false;
/* 70 */     StringBuilder sb = new StringBuilder();
/*    */     
/* 72 */     for (String key : jo.keySet()) {
/* 73 */       Object value = jo.opt(key);
/* 74 */       if (!JSONObject.NULL.equals(value)) {
/* 75 */         if (b) {
/* 76 */           sb.append(';');
/*    */         }
/* 78 */         sb.append(Cookie.escape(key));
/* 79 */         sb.append("=");
/* 80 */         sb.append(Cookie.escape(value.toString()));
/* 81 */         b = true;
/*    */       }
/*    */     }
/* 84 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\org\json\CookieList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */