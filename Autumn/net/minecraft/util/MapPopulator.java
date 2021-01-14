package net.minecraft.util;

import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

public class MapPopulator {
   public static Map createMap(Iterable keys, Iterable values) {
      return populateMap(keys, values, Maps.newLinkedHashMap());
   }

   public static Map populateMap(Iterable keys, Iterable values, Map map) {
      Iterator iterator = values.iterator();
      Iterator var4 = keys.iterator();

      while(var4.hasNext()) {
         Object k = var4.next();
         map.put(k, iterator.next());
      }

      if (iterator.hasNext()) {
         throw new NoSuchElementException();
      } else {
         return map;
      }
   }
}
