package org.m0jang.crystal.Utils;

public class TimeHelper {
   private long prevTime;

   public TimeHelper() {
      this.reset();
   }

   public boolean hasPassed(double milli) {
      return (double)(this.getTime() - this.prevTime) >= milli;
   }

   public double getPassed() {
      return (double)(this.getTime() - this.prevTime);
   }

   public boolean hasPassed(double milli, boolean reset) {
      boolean result = (double)(this.getTime() - this.prevTime) >= milli;
      if (reset) {
         this.reset();
      }

      return result;
   }

   public long getTime() {
      return System.nanoTime() / 1000000L;
   }

   public void reset() {
      this.prevTime = this.getTime();
   }

   public void setTime(long mili) {
      this.prevTime -= mili;
   }
}
