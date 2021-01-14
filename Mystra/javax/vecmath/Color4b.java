package javax.vecmath;

import java.awt.Color;
import java.io.Serializable;

public class Color4b extends Tuple4b implements Serializable {
   static final long serialVersionUID = -105080578052502155L;

   public Color4b(byte var1, byte var2, byte var3, byte var4) {
      super(var1, var2, var3, var4);
   }

   public Color4b(byte[] var1) {
      super(var1);
   }

   public Color4b(Color4b var1) {
      super((Tuple4b)var1);
   }

   public Color4b(Tuple4b var1) {
      super(var1);
   }

   public Color4b(Color var1) {
      super((byte)var1.getRed(), (byte)var1.getGreen(), (byte)var1.getBlue(), (byte)var1.getAlpha());
   }

   public Color4b() {
   }

   public final void set(Color var1) {
      this.x = (byte)var1.getRed();
      this.y = (byte)var1.getGreen();
      this.z = (byte)var1.getBlue();
      this.w = (byte)var1.getAlpha();
   }

   public final Color get() {
      int var1 = this.x & 255;
      int var2 = this.y & 255;
      int var3 = this.z & 255;
      int var4 = this.w & 255;
      return new Color(var1, var2, var3, var4);
   }
}
