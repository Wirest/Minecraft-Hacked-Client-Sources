package javax.vecmath;

import java.io.Serializable;

public class TexCoord3f extends Tuple3f implements Serializable {
   static final long serialVersionUID = -3517736544731446513L;

   public TexCoord3f(float var1, float var2, float var3) {
      super(var1, var2, var3);
   }

   public TexCoord3f(float[] var1) {
      super(var1);
   }

   public TexCoord3f(TexCoord3f var1) {
      super((Tuple3f)var1);
   }

   public TexCoord3f(Tuple3f var1) {
      super(var1);
   }

   public TexCoord3f(Tuple3d var1) {
      super(var1);
   }

   public TexCoord3f() {
   }
}
