package net.minecraft.util;

public class Tuple {
   private Object a;
   private Object b;

   public Tuple(Object aIn, Object bIn) {
      this.a = aIn;
      this.b = bIn;
   }

   public Object getFirst() {
      return this.a;
   }

   public Object getSecond() {
      return this.b;
   }
}
