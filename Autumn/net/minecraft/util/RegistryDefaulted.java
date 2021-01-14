package net.minecraft.util;

public class RegistryDefaulted extends RegistrySimple {
   private final Object defaultObject;

   public RegistryDefaulted(Object defaultObjectIn) {
      this.defaultObject = defaultObjectIn;
   }

   public Object getObject(Object name) {
      Object v = super.getObject(name);
      return v == null ? this.defaultObject : v;
   }
}
