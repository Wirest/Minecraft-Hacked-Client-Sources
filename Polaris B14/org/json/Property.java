/*    */ package org.json;
/*    */ 
/*    */ import java.util.Enumeration;
/*    */ import java.util.Properties;
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
/*    */ public class Property
/*    */ {
/*    */   public static JSONObject toJSONObject(Properties properties)
/*    */     throws JSONException
/*    */   {
/* 45 */     JSONObject jo = new JSONObject();
/* 46 */     if ((properties != null) && (!properties.isEmpty())) {
/* 47 */       Enumeration<?> enumProperties = properties.propertyNames();
/* 48 */       while (enumProperties.hasMoreElements()) {
/* 49 */         String name = (String)enumProperties.nextElement();
/* 50 */         jo.put(name, properties.getProperty(name));
/*    */       }
/*    */     }
/* 53 */     return jo;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static Properties toProperties(JSONObject jo)
/*    */     throws JSONException
/*    */   {
/* 63 */     Properties properties = new Properties();
/* 64 */     if (jo != null)
/*    */     {
/* 66 */       for (String key : jo.keySet()) {
/* 67 */         Object value = jo.opt(key);
/* 68 */         if (!JSONObject.NULL.equals(value)) {
/* 69 */           properties.put(key, value.toString());
/*    */         }
/*    */       }
/*    */     }
/* 73 */     return properties;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\org\json\Property.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */