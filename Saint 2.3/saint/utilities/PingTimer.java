package saint.utilities;

public final class PingTimer {
   private static float startTime;
   private static float endTime;
   private static float time;

   private PingTimer() {
   }

   private static void update() {
      startTime = (float)System.nanoTime();
      time = (startTime - endTime) / 1.0E9F;
   }

   public static float getTime() {
      update();
      return time;
   }

   public static void reset() {
      endTime = (float)System.nanoTime();
      update();
   }
}
