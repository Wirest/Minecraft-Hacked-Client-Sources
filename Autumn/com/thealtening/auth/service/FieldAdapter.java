package com.thealtening.auth.service;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Optional;

public final class FieldAdapter {
   private final HashMap fields = new HashMap();
   private static final Lookup LOOKUP;
   private static Field MODIFIERS;

   public FieldAdapter(String parent) {
      try {
         Class cls = Class.forName(parent);
         Field modifiers = MODIFIERS;
         Field[] var4 = cls.getDeclaredFields();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Field field = var4[var6];
            field.setAccessible(true);
            int accessFlags = field.getModifiers();
            if (Modifier.isFinal(accessFlags)) {
               modifiers.setInt(field, accessFlags & -17);
            }

            MethodHandle handler = LOOKUP.unreflectSetter(field);
            handler = handler.asType(handler.type().generic().changeReturnType(Void.TYPE));
            this.fields.put(field.getName(), handler);
         }

      } catch (ClassNotFoundException var10) {
         throw new RuntimeException("Couldn't load/find the specified class");
      } catch (Exception var11) {
         throw new RuntimeException("Couldn't create a method handler for the field");
      }
   }

   public void updateFieldIfPresent(String name, Object newValue) {
      Optional.ofNullable(this.fields.get(name)).ifPresent((setter) -> {
         try {
            setter.invokeExact(newValue);
         } catch (Throwable var3) {
            var3.printStackTrace();
         }

      });
   }

   static {
      try {
         MODIFIERS = Field.class.getDeclaredField("modifiers");
         MODIFIERS.setAccessible(true);
      } catch (NoSuchFieldException var3) {
         var3.printStackTrace();
      }

      Lookup lookupObject;
      try {
         Field lookupImplField = Lookup.class.getDeclaredField("IMPL_LOOKUP");
         lookupImplField.setAccessible(true);
         lookupObject = (Lookup)lookupImplField.get((Object)null);
      } catch (ReflectiveOperationException var2) {
         lookupObject = MethodHandles.lookup();
      }

      LOOKUP = lookupObject;
   }
}
