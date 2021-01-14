package net.minecraft.util;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.util.Iterator;
import java.util.Map;

public class RegistryNamespaced extends RegistrySimple implements IObjectIntIterable {
   protected final ObjectIntIdentityMap underlyingIntegerMap = new ObjectIntIdentityMap();
   protected final Map inverseObjectRegistry;

   public RegistryNamespaced() {
      this.inverseObjectRegistry = ((BiMap)this.registryObjects).inverse();
   }

   public void register(int id, Object p_177775_2_, Object p_177775_3_) {
      this.underlyingIntegerMap.put(p_177775_3_, id);
      this.putObject(p_177775_2_, p_177775_3_);
   }

   protected Map createUnderlyingMap() {
      return HashBiMap.create();
   }

   public Object getObject(Object name) {
      return super.getObject(name);
   }

   public Object getNameForObject(Object p_177774_1_) {
      return this.inverseObjectRegistry.get(p_177774_1_);
   }

   public boolean containsKey(Object p_148741_1_) {
      return super.containsKey(p_148741_1_);
   }

   public int getIDForObject(Object p_148757_1_) {
      return this.underlyingIntegerMap.get(p_148757_1_);
   }

   public Object getObjectById(int id) {
      return this.underlyingIntegerMap.getByValue(id);
   }

   public Iterator iterator() {
      return this.underlyingIntegerMap.iterator();
   }
}
