package net.minecraft.util;

public class TupleIntJsonSerializable {
   private int integerValue;
   private IJsonSerializable jsonSerializableValue;

   public int getIntegerValue() {
      return this.integerValue;
   }

   public void setIntegerValue(int integerValueIn) {
      this.integerValue = integerValueIn;
   }

   public IJsonSerializable getJsonSerializableValue() {
      return this.jsonSerializableValue;
   }

   public void setJsonSerializableValue(IJsonSerializable jsonSerializableValueIn) {
      this.jsonSerializableValue = jsonSerializableValueIn;
   }
}
