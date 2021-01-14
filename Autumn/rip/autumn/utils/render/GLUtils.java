package rip.autumn.utils.render;

import java.util.ArrayList;
import java.util.List;
import org.lwjgl.opengl.GL11;

public final class GLUtils {
   private static final List depth = new ArrayList();

   public static void pre() {
      if (depth.isEmpty()) {
         GL11.glClearDepth(1.0D);
         GL11.glClear(256);
      }

   }

   public static void mask() {
      depth.add(0, GL11.glGetInteger(2932));
      GL11.glEnable(6145);
      GL11.glDepthMask(true);
      GL11.glDepthFunc(513);
      GL11.glColorMask(false, false, false, true);
   }

   public static void render() {
      render(514);
   }

   public static void render(int gl) {
      GL11.glDepthFunc(gl);
      GL11.glColorMask(true, true, true, true);
   }

   public static void post() {
      GL11.glDepthFunc((Integer)depth.get(0));
      depth.remove(0);
   }

   public static double interpolate(double current, double old, double scale) {
      return old + (current - old) * scale;
   }

   public static void startSmooth() {
      GL11.glEnable(2848);
      GL11.glEnable(2881);
      GL11.glEnable(2832);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glHint(3154, 4354);
      GL11.glHint(3155, 4354);
      GL11.glHint(3153, 4354);
   }

   public static void endSmooth() {
      GL11.glDisable(2848);
      GL11.glDisable(2881);
      GL11.glEnable(2832);
   }
}
