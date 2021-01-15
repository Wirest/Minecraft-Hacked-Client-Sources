/*    */ package optfine;
/*    */ 
/*    */ import com.google.gson.JsonArray;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonParseException;
/*    */ 
/*    */ public class Json
/*    */ {
/*    */   public static float getFloat(JsonObject p_getFloat_0_, String p_getFloat_1_, float p_getFloat_2_)
/*    */   {
/* 12 */     JsonElement jsonelement = p_getFloat_0_.get(p_getFloat_1_);
/* 13 */     return jsonelement == null ? p_getFloat_2_ : jsonelement.getAsFloat();
/*    */   }
/*    */   
/*    */   public static boolean getBoolean(JsonObject p_getBoolean_0_, String p_getBoolean_1_, boolean p_getBoolean_2_)
/*    */   {
/* 18 */     JsonElement jsonelement = p_getBoolean_0_.get(p_getBoolean_1_);
/* 19 */     return jsonelement == null ? p_getBoolean_2_ : jsonelement.getAsBoolean();
/*    */   }
/*    */   
/*    */   public static String getString(JsonObject p_getString_0_, String p_getString_1_)
/*    */   {
/* 24 */     return getString(p_getString_0_, p_getString_1_, null);
/*    */   }
/*    */   
/*    */   public static String getString(JsonObject p_getString_0_, String p_getString_1_, String p_getString_2_)
/*    */   {
/* 29 */     JsonElement jsonelement = p_getString_0_.get(p_getString_1_);
/* 30 */     return jsonelement == null ? p_getString_2_ : jsonelement.getAsString();
/*    */   }
/*    */   
/*    */   public static float[] parseFloatArray(JsonElement p_parseFloatArray_0_, int p_parseFloatArray_1_)
/*    */   {
/* 35 */     return parseFloatArray(p_parseFloatArray_0_, p_parseFloatArray_1_, null);
/*    */   }
/*    */   
/*    */   public static float[] parseFloatArray(JsonElement p_parseFloatArray_0_, int p_parseFloatArray_1_, float[] p_parseFloatArray_2_)
/*    */   {
/* 40 */     if (p_parseFloatArray_0_ == null)
/*    */     {
/* 42 */       return p_parseFloatArray_2_;
/*    */     }
/*    */     
/*    */ 
/* 46 */     JsonArray jsonarray = p_parseFloatArray_0_.getAsJsonArray();
/*    */     
/* 48 */     if (jsonarray.size() != p_parseFloatArray_1_)
/*    */     {
/* 50 */       throw new JsonParseException("Wrong array length: " + jsonarray.size() + ", should be: " + p_parseFloatArray_1_ + ", array: " + jsonarray);
/*    */     }
/*    */     
/*    */ 
/* 54 */     float[] afloat = new float[jsonarray.size()];
/*    */     
/* 56 */     for (int i = 0; i < afloat.length; i++)
/*    */     {
/* 58 */       afloat[i] = jsonarray.get(i).getAsFloat();
/*    */     }
/*    */     
/* 61 */     return afloat;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public static int[] parseIntArray(JsonElement p_parseIntArray_0_, int p_parseIntArray_1_)
/*    */   {
/* 68 */     return parseIntArray(p_parseIntArray_0_, p_parseIntArray_1_, null);
/*    */   }
/*    */   
/*    */   public static int[] parseIntArray(JsonElement p_parseIntArray_0_, int p_parseIntArray_1_, int[] p_parseIntArray_2_)
/*    */   {
/* 73 */     if (p_parseIntArray_0_ == null)
/*    */     {
/* 75 */       return p_parseIntArray_2_;
/*    */     }
/*    */     
/*    */ 
/* 79 */     JsonArray jsonarray = p_parseIntArray_0_.getAsJsonArray();
/*    */     
/* 81 */     if (jsonarray.size() != p_parseIntArray_1_)
/*    */     {
/* 83 */       throw new JsonParseException("Wrong array length: " + jsonarray.size() + ", should be: " + p_parseIntArray_1_ + ", array: " + jsonarray);
/*    */     }
/*    */     
/*    */ 
/* 87 */     int[] aint = new int[jsonarray.size()];
/*    */     
/* 89 */     for (int i = 0; i < aint.length; i++)
/*    */     {
/* 91 */       aint[i] = jsonarray.get(i).getAsInt();
/*    */     }
/*    */     
/* 94 */     return aint;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\optfine\Json.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */