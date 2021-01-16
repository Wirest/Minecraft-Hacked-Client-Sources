package me.existdev.exist.utils;

public class TimerUtils2 {
   // $FF: synthetic field
   private long time;

   // $FF: synthetic method
   public boolean hasTimeElapsed(long time, boolean reset) {
      if(this.time() >= time) {
         if(reset) {
            this.reset();
         }

         return true;
      } else {
         return false;
      }
   }

   // $FF: synthetic method
   public boolean hasTimeElapsed(long time) {
      return this.time() >= time;
   }

   // $FF: synthetic method
   public boolean hasTicksElapsed(int ticks) {
      return this.time() >= (long)(1000 / ticks - 50);
   }

   // $FF: synthetic method
   public boolean hasTicksElapsed(int time, int ticks) {
      return this.time() >= (long)(time / ticks - 50);
   }

   // $FF: synthetic method
   public long time() {
      return System.nanoTime() / 1000000L - this.time;
   }

   // $FF: synthetic method
   public void reset() {
      this.time = System.nanoTime() / 1000000L;
   }
}
