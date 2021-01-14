package javax.vecmath;

import java.io.Serializable;

public class Point3i extends Tuple3i implements Serializable {
   static final long serialVersionUID = 6149289077348153921L;

   public Point3i(int var1, int var2, int var3) {
      super(var1, var2, var3);
   }

   public Point3i(int[] var1) {
      super(var1);
   }

   public Point3i(Tuple3i var1) {
      super(var1);
   }

   public Point3i() {
   }
}
