package org.m0jang.crystal.Values;

public class Value {
   private String name;
   private float minFloatValue;
   private float floatValue;
   private float maxFloat;
   private boolean booleanValue;
   private String stringValue;
   private int minIntValue;
   private int intValue;
   private int maxInt;
   private String[] options;
   private String selectedOption;
   private String valType;
   private Object type;
   private float increment;

   public Value(String valType, Object type, String name, float value, float minValue, float maxValue, float increment) {
      this.setName(name);
      this.setType(type);
      this.setValType(valType);
      this.setFloatValue(value);
      this.setMinFloatValue(minValue);
      this.setMaxFloat(maxValue);
      this.setIncrement(increment);
      ValueManager.values.add(this);
   }

   public Value(String valType, Object type, String name, String selected, String[] options) {
      this.setName(name);
      this.setSelectedOption(selected);
      this.setOptions(options);
      this.setType(type);
      this.setValType(valType);
      ValueManager.values.add(this);
   }

   public Value(String valType, Object type, String name, int value, int minValue, int maxValue, float increment) {
      this.setName(name);
      this.setIntValue(value);
      this.setMinIntValue(minValue);
      this.setMaxInt(maxValue);
      this.setType(type);
      this.setValType(valType);
      this.setIncrement(increment);
      ValueManager.values.add(this);
   }

   public Value(String valType, Object type, String name, boolean value) {
      this.setName(name);
      this.setBooleanValue(value);
      this.setType(type);
      this.setValType(valType);
      ValueManager.values.add(this);
   }

   public Value(Object type, String name, String value) {
      this.setName(name);
      this.setStringValue(value);
      this.setType(type);
      this.setValType(this.valType);
      ValueManager.values.add(this);
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public float getFloatValue() {
      return this.floatValue;
   }

   public void setFloatValue(float floatValue) {
      this.floatValue = floatValue;
   }

   public boolean getBooleanValue() {
      return this.booleanValue;
   }

   public void setBooleanValue(boolean booleanValue) {
      this.booleanValue = booleanValue;
   }

   public String getStringValue() {
      return this.stringValue;
   }

   public void setStringValue(String stringValue) {
      this.stringValue = stringValue;
   }

   public float getMaxFloat() {
      return this.maxFloat;
   }

   public void setMaxFloat(float maxFloat) {
      this.maxFloat = maxFloat;
   }

   public int getIntValue() {
      return this.intValue;
   }

   public void setIntValue(int intValue) {
      this.intValue = intValue;
   }

   public int getMaxInt() {
      return this.maxInt;
   }

   public void setMaxInt(int maxInt) {
      this.maxInt = maxInt;
   }

   public float getMinFloatValue() {
      return this.minFloatValue;
   }

   public void setMinFloatValue(float minFloatValue) {
      this.minFloatValue = minFloatValue;
   }

   public int getMinIntValue() {
      return this.minIntValue;
   }

   public void setMinIntValue(int minIntValue) {
      this.minIntValue = minIntValue;
   }

   public String[] getOptions() {
      return this.options;
   }

   public void setOptions(String[] options) {
      this.options = options;
   }

   public String getSelectedOption() {
      return this.selectedOption;
   }

   public void setSelectedOption(String selectedOption) {
      this.selectedOption = selectedOption;
   }

   public Object getType() {
      return this.type;
   }

   public void setType(Object type) {
      this.type = type;
   }

   public String getValType() {
      return this.valType;
   }

   public void setValType(String valType) {
      this.valType = valType;
   }

   public void setIncrement(float increment) {
      this.increment = increment;
   }

   public float getIncrement() {
      return this.increment;
   }
}
