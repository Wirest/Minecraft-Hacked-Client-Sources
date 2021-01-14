package rip.autumn.module.option.impl;

import java.awt.Color;
import java.util.function.Supplier;
import rip.autumn.module.option.Option;

public class ColorOption extends Option {
   public ColorOption(String label, Color defaultValue, Supplier supplier) {
      super(label, defaultValue, supplier);
   }

   public ColorOption(String label, Color defaultValue) {
      super(label, defaultValue, () -> {
         return true;
      });
   }
}
