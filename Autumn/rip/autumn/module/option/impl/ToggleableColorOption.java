package rip.autumn.module.option.impl;

import java.awt.Color;
import java.util.function.Supplier;

public final class ToggleableColorOption extends ColorOption {
   private boolean toggled;

   public ToggleableColorOption(String label, boolean toggled, Color defaultValue, Supplier supplier) {
      super(label, defaultValue, supplier);
      this.toggled = toggled;
   }

   public ToggleableColorOption(String label, boolean toggled, Color defaultValue) {
      super(label, defaultValue);
      this.toggled = toggled;
   }

   public boolean isToggled() {
      return this.toggled;
   }

   public void setToggled(boolean toggled) {
      this.toggled = toggled;
   }
}
