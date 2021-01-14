package rip.autumn.module.option.impl;

import java.util.function.Supplier;
import rip.autumn.module.option.Option;

public final class EnumOption extends Option {
   private final Enum[] values = (Enum[])((Enum[])((Enum)this.getValue()).getClass().getEnumConstants());

   public EnumOption(String label, Enum defaultValue, Supplier supplier) {
      super(label, defaultValue, supplier);
   }

   public EnumOption(String label, Enum defaultValue) {
      super(label, defaultValue, () -> {
         return true;
      });
   }

   public Enum getValueOrNull(String constantName) {
      Enum[] var2 = this.values;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Enum value = var2[var4];
         if (value.name().equals(constantName)) {
            return value;
         }
      }

      return null;
   }

   public final Enum[] getValues() {
      return this.values;
   }
}
