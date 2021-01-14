package javax.vecmath;

import java.io.Serializable;

public class Point2i extends Tuple2i implements Serializable {
   static final long serialVersionUID = 9208072376494084954L;

   public Point2i(int var1, int var2) {
      super(var1, var2);
   }

   public Point2i(int[] var1) {
      super(var1);
   }

   public Point2i(Tuple2i var1) {
      super(var1);
   }

   public Point2i() {
   }
}
