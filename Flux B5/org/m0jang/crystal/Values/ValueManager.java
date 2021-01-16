package org.m0jang.crystal.Values;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ValueManager {
   public static ArrayList values = new ArrayList();

   public static Value getValue(String name) {
      Iterator var2 = values.iterator();

      while(var2.hasNext()) {
         Value value = (Value)var2.next();
         if (value.getName().equalsIgnoreCase(name)) {
            return value;
         }
      }

      return null;
   }

   public static List getValueByModName(String modName) {
      List result = new ArrayList();
      Iterator var3 = values.iterator();

      while(var3.hasNext()) {
         Value value = (Value)var3.next();
         if (value.getValType().equals(modName)) {
            result.add(value);
         }
      }

      return result;
   }
}
