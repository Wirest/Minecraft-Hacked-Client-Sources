package rip.autumn.module.option;

import java.util.function.Supplier;

public class Option {
   private final String label;
   private Object value;
   private final Supplier supplier;

   public Option(String label, Object defaultValue, Supplier supplier) {
      this.label = label;
      this.value = defaultValue;
      this.supplier = supplier;
   }

   public boolean check() {
      return (Boolean)this.supplier.get();
   }

   public String getLabel() {
      return this.label;
   }

   public Object getValue() {
      return this.value;
   }

   public void setValue(Object value) {
      this.value = value;
   }
}
