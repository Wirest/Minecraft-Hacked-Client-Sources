package javax.vecmath;

import java.awt.Color;
import java.io.Serializable;

public class Color3f extends Tuple3f implements Serializable {
   static final long serialVersionUID = -1861792981817493659L;

   public Color3f(float var1, float var2, float var3) {
      super(var1, var2, var3);
   }

   public Color3f(float[] var1) {
      super(var1);
   }

   public Color3f(Color3f var1) {
      super((Tuple3f)var1);
   }

   public Color3f(Tuple3f var1) {
      super(var1);
   }

   public Color3f(Tuple3d var1) {
      super(var1);
   }

   public Color3f(Color var1) {
      super((float)var1.getRed() / 255.0F, (float)var1.getGreen() / 255.0F, (float)var1.getBlue() / 255.0F);
   }

   public Color3f() {
   }

   public final void set(Color var1) {
      this.x = (float)var1.getRed() / 255.0F;
      this.y = (float)var1.getGreen() / 255.0F;
      this.z = (float)var1.getBlue() / 255.0F;
   }

   public final Color get() {
      int var1 = Math.round(this.x * 255.0F);
      int var2 = Math.round(this.y * 255.0F);
      int var3 = Math.round(this.z * 255.0F);
      return new Color(var1, var2, var3);
   }
}
