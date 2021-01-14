package rip.autumn.module.option;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import rip.autumn.module.option.impl.EnumOption;

public class Configurable {
   private final List options = new ArrayList();
   private EnumOption mode = null;

   public final void setMode(EnumOption mode) {
      this.mode = mode;
   }

   public final void addOptions(Option... options) {
      this.options.addAll(Arrays.asList(options));
   }

   public EnumOption getMode() {
      return this.mode;
   }

   public final List getOptions() {
      return this.options;
   }

   public final Option getOptionByLabel(String label) {
      Iterator var2 = this.options.iterator();

      Option option;
      do {
         if (!var2.hasNext()) {
            throw new NullPointerException();
         }

         option = (Option)var2.next();
      } while(!option.getLabel().equals(label));

      return option;
   }
}
