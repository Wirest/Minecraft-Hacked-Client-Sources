package javax.vecmath;

import java.io.Serializable;

public class TexCoord2f extends Tuple2f implements Serializable {
   static final long serialVersionUID = 7998248474800032487L;

   public TexCoord2f(float var1, float var2) {
      super(var1, var2);
   }

   public TexCoord2f(float[] var1) {
      super(var1);
   }

   public TexCoord2f(TexCoord2f var1) {
      super((Tuple2f)var1);
   }

   public TexCoord2f(Tuple2f var1) {
      super(var1);
   }

   public TexCoord2f() {
   }
}
