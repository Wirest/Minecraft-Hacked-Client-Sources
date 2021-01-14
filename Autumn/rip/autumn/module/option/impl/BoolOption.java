package rip.autumn.module.option.impl;

import java.util.function.Supplier;
import rip.autumn.module.option.Option;

public final class BoolOption extends Option {
   public BoolOption(String label, Boolean defaultValue, Supplier supplier) {
      super(label, defaultValue, supplier);
   }

   public BoolOption(String label, Boolean defaultValue) {
      super(label, defaultValue, () -> {
         return true;
      });
   }

   public Boolean getValue() {
      return (Boolean)super.getValue() && this.check();
   }
}
