package javax.vecmath;

import java.awt.Color;
import java.io.Serializable;

public class Color3b extends Tuple3b implements Serializable {
   static final long serialVersionUID = 6632576088353444794L;

   public Color3b(byte var1, byte var2, byte var3) {
      super(var1, var2, var3);
   }

   public Color3b(byte[] var1) {
      super(var1);
   }

   public Color3b(Color3b var1) {
      super((Tuple3b)var1);
   }

   public Color3b(Tuple3b var1) {
      super(var1);
   }

   public Color3b(Color var1) {
      super((byte)var1.getRed(), (byte)var1.getGreen(), (byte)var1.getBlue());
   }

   public Color3b() {
   }

   public final void set(Color var1) {
      this.x = (byte)var1.getRed();
      this.y = (byte)var1.getGreen();
      this.z = (byte)var1.getBlue();
   }

   public final Color get() {
      int var1 = this.x & 255;
      int var2 = this.y & 255;
      int var3 = this.z & 255;
      return new Color(var1, var2, var3);
   }
}
