package saint.utilities;

import java.util.HashMap;
import org.lwjgl.opengl.GL11;

public final class Stencil {
   private static final Stencil INSTANCE = new Stencil();
   private final HashMap stencilFuncs = new HashMap();
   private int layers = 1;
   private boolean renderMask;

   private Stencil() {
   }

   public static Stencil getInstance() {
      return INSTANCE;
   }

   public void setRenderMask(boolean renderMask) {
      this.renderMask = renderMask;
   }

   public void startLayer() {
      if (this.layers == 1) {
         GL11.glClearStencil(0);
         GL11.glClear(1024);
      }

      GL11.glEnable(2960);
      ++this.layers;
      if (this.layers > this.getMaximumLayers()) {
         System.out.println("StencilUtil: Reached maximum amount of layers!");
         this.layers = 1;
      }
   }

   public void stopLayer() {
      if (this.layers == 1) {
         System.out.println("StencilUtil: No layers found!");
      } else {
         --this.layers;
         if (this.layers == 1) {
            GL11.glDisable(2960);
         } else {
            Stencil.StencilFunc lastStencilFunc = (Stencil.StencilFunc)this.stencilFuncs.remove(this.layers);
            if (lastStencilFunc != null) {
               lastStencilFunc.use();
            }
         }

      }
   }

   public void clear() {
      GL11.glClearStencil(0);
      GL11.glClear(1024);
      this.stencilFuncs.clear();
      this.layers = 1;
   }

   public void setBuffer() {
      this.setStencilFunc(new Stencil.StencilFunc(!this.renderMask ? 512 : 519, this.layers, this.getMaximumLayers(), 7681, 7680, 7680));
   }

   public void setBuffer(boolean set) {
      this.setStencilFunc(new Stencil.StencilFunc(!this.renderMask ? 512 : 519, set ? this.layers : this.layers - 1, this.getMaximumLayers(), 7681, 7681, 7681));
   }

   public void cropOutside() {
      this.setStencilFunc(new Stencil.StencilFunc(517, this.layers, this.getMaximumLayers(), 7680, 7680, 7680));
   }

   public void cropInside() {
      this.setStencilFunc(new Stencil.StencilFunc(514, this.layers, this.getMaximumLayers(), 7680, 7680, 7680));
   }

   public void setStencilFunc(Stencil.StencilFunc stencilFunc) {
      GL11.glStencilFunc(stencilFunc.func_func, stencilFunc.func_ref, stencilFunc.func_mask);
      GL11.glStencilOp(stencilFunc.op_fail, stencilFunc.op_zfail, stencilFunc.op_zpass);
      this.stencilFuncs.put(this.layers, stencilFunc);
   }

   public Stencil.StencilFunc getStencilFunc() {
      return (Stencil.StencilFunc)this.stencilFuncs.get(this.layers);
   }

   public int getLayer() {
      return this.layers;
   }

   public int getStencilBufferSize() {
      return GL11.glGetInteger(3415);
   }

   public int getMaximumLayers() {
      return (int)(Math.pow(2.0D, (double)this.getStencilBufferSize()) - 1.0D);
   }

   public void createCirlce(double x, double y, double radius) {
      GL11.glBegin(6);

      for(int i = 0; i <= 360; ++i) {
         double sin = Math.sin((double)i * 3.141592653589793D / 180.0D) * radius;
         double cos = Math.cos((double)i * 3.141592653589793D / 180.0D) * radius;
         GL11.glVertex2d(x + sin, y + cos);
      }

      GL11.glEnd();
   }

   public void createRect(double x, double y, double x2, double y2) {
      GL11.glBegin(7);
      GL11.glVertex2d(x, y2);
      GL11.glVertex2d(x2, y2);
      GL11.glVertex2d(x2, y);
      GL11.glVertex2d(x, y);
      GL11.glEnd();
   }

   public class StencilFunc {
      public final int func_func;
      public final int func_ref;
      public final int func_mask;
      public final int op_fail;
      public final int op_zfail;
      public final int op_zpass;

      public StencilFunc(int func_func, int func_ref, int func_mask, int op_fail, int op_zfail, int op_zpass) {
         this.func_func = func_func;
         this.func_ref = func_ref;
         this.func_mask = func_mask;
         this.op_fail = op_fail;
         this.op_zfail = op_zfail;
         this.op_zpass = op_zpass;
      }

      public void use() {
         GL11.glStencilFunc(this.func_func, this.func_ref, this.func_mask);
         GL11.glStencilOp(this.op_fail, this.op_zfail, this.op_zpass);
      }
   }
}
