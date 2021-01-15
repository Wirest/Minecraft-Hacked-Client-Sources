/*    */ package net.minecraft.util;
/*    */ 
/*    */ 
/*    */ public class TupleIntJsonSerializable
/*    */ {
/*    */   private int integerValue;
/*    */   
/*    */   private IJsonSerializable jsonSerializableValue;
/*    */   
/*    */ 
/*    */   public int getIntegerValue()
/*    */   {
/* 13 */     return this.integerValue;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setIntegerValue(int integerValueIn)
/*    */   {
/* 21 */     this.integerValue = integerValueIn;
/*    */   }
/*    */   
/*    */   public <T extends IJsonSerializable> T getJsonSerializableValue()
/*    */   {
/* 26 */     return this.jsonSerializableValue;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setJsonSerializableValue(IJsonSerializable jsonSerializableValueIn)
/*    */   {
/* 34 */     this.jsonSerializableValue = jsonSerializableValueIn;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\util\TupleIntJsonSerializable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */