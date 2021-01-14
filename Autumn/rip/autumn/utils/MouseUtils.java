package rip.autumn.utils;

public final class MouseUtils {
   public static boolean mouseWithinBounds(int mouseX, int mouseY, double x, double y, double right, double bottom) {
      return (double)mouseX >= x && (double)mouseX <= right && (double)mouseY >= y && (double)mouseY <= bottom;
   }
}
