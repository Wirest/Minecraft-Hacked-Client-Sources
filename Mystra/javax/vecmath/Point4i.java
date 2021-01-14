package javax.vecmath;

import java.io.Serializable;

public class Point4i extends Tuple4i implements Serializable {
   static final long serialVersionUID = 620124780244617983L;

   public Point4i(int var1, int var2, int var3, int var4) {
      super(var1, var2, var3, var4);
   }

   public Point4i(int[] var1) {
      super(var1);
   }

   public Point4i(Tuple4i var1) {
      super(var1);
   }

   public Point4i() {
   }
}
