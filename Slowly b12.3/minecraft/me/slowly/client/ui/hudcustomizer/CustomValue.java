package me.slowly.client.ui.hudcustomizer;

import java.awt.Color;
import java.util.ArrayList;

public class CustomValue {
   private Object value;
   private Object valueMin;
   private Object valueMax;
   private double step;
   private Object defaultValue;
   private String name;
   public boolean isValueBoolean = false;
   public boolean isValueInteger = false;
   public boolean isValueFloat = false;
   public boolean isValueDouble = false;
   public boolean isValueMode;
   public boolean isValueLong = false;
   public boolean isValueByte = false;
   public boolean isValueColor;
   private int current;
   public ArrayList mode;
   public double sliderX;
   public boolean set = false;
   public boolean isSettingMode;
   public boolean openMods;
   public double maxSliderSize;
   public int RADIUS = 4;
   public float currentRadius = 4.0F;
   public boolean disabled;
   private String modeTitle;
   private Object color;
   public String prefix;
   public CustomValue alphaValue;

   public CustomValue(String modeName) {
      this.defaultValue = this.value;
      this.isValueMode = true;
      this.step = 0.1D;
      this.mode = new ArrayList();
      this.current = 0;
      this.name = modeName;
   }

   public CustomValue(String name, Object defaultValue, Object valueMin, Object valueMax) {
      this.defaultValue = this.value;
      this.name = name;
      this.value = defaultValue;
      this.valueMin = valueMin;
      this.valueMax = valueMax;
      this.step = 0.1D;
      if (this.value instanceof Double) {
         this.isValueDouble = true;
      }

   }

   public CustomValue(String name, Object value, Object valueMin, Object valueMax, double steps, String prefix) {
      this.defaultValue = value;
      this.name = name;
      this.value = value;
      this.valueMin = valueMin;
      this.valueMax = valueMax;
      this.step = steps;
      this.prefix = prefix;
      if (value instanceof Double) {
         this.isValueDouble = true;
      }

   }

   public CustomValue(String name, Object value) {
      this.defaultValue = value;
      this.name = name;
      this.value = value;
      if (value instanceof Boolean) {
         this.isValueBoolean = true;
      } else if (value instanceof Integer) {
         this.isValueInteger = true;
      } else if (value instanceof Float) {
         this.isValueFloat = true;
      } else if (value instanceof Long) {
         this.isValueLong = true;
      } else if (value instanceof Byte) {
         this.isValueByte = true;
      } else if (value instanceof Color) {
         this.isValueColor = true;
      }

   }

   public CustomValue(String name, Object value, CustomValue alphaValue) {
      this.defaultValue = value;
      this.name = name;
      this.value = value;
      this.alphaValue = alphaValue;
      if (value instanceof Boolean) {
         this.isValueBoolean = true;
      } else if (value instanceof Integer) {
         this.isValueInteger = true;
      } else if (value instanceof Float) {
         this.isValueFloat = true;
      } else if (value instanceof Long) {
         this.isValueLong = true;
      } else if (value instanceof Byte) {
         this.isValueByte = true;
      } else if (value instanceof Color) {
         this.isValueColor = true;
      }

   }

   public CustomValue(String name, String name2, String nam3, Object value, Object value2, Object value3) {
      this.defaultValue = value;
      this.name = name;
      this.value = value;
      if (value instanceof Boolean) {
         this.isValueBoolean = true;
      } else if (value instanceof Integer) {
         this.isValueInteger = true;
      } else if (value instanceof Float) {
         this.isValueFloat = true;
      } else if (value instanceof Double) {
         this.isValueDouble = true;
      } else if (value instanceof Long) {
         this.isValueLong = true;
      } else if (value instanceof Byte) {
         this.isValueByte = true;
      }

   }

   public void setCurrentMode(int current) {
      if (current > this.mode.size() - 1) {
         System.out.println("Value is to big! Set to 0. (" + this.mode.size() + ")");
      } else {
         this.current = current;
      }
   }

   public int getCurrentMode() {
      return this.current;
   }

   public ArrayList listModes() {
      return this.mode;
   }

   public String getModeTitle() {
      return this.modeTitle;
   }

   public String getModeAt(int index) {
      return (String)this.mode.get(index);
   }

   public String getCurrentModeStr() {
      return this.getModeAt(this.getCurrentMode());
   }

   public String getModeAt(String modeName) {
      for(int i = 0; i < this.mode.size(); ++i) {
         if (((String)this.mode.get(i)).equalsIgnoreCase(modeName)) {
            return (String)this.mode.get(i);
         }
      }

      return "NULL";
   }

   public boolean isCurrentMode(String modeName) {
      return this.getModeAt(this.getCurrentMode()).equalsIgnoreCase(modeName);
   }

   public String getAllModes() {
      String all = "";

      for(int i = 0; i < this.mode.size(); ++i) {
         all = all + ((String)this.mode.get(i)).toString();
      }

      return all;
   }

   public String getValueName() {
      return this.name;
   }

   public Object getValueMin() {
      return this.value instanceof Double ? this.valueMin : null;
   }

   public double getSteps() {
      return this.step;
   }

   public Object getValueMax() {
      return this.value instanceof Double ? this.valueMax : null;
   }

   public Object getDefaultValue() {
      return this.defaultValue;
   }

   public Object getValueState() {
      return this.value;
   }

   public void setValueState(Object value) {
      this.value = value;
   }
}
