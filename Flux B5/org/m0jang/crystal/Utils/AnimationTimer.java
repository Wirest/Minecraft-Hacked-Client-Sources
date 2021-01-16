package org.m0jang.crystal.Utils;

public class AnimationTimer {
   private final int delay;
   private int bottom;
   private int top;
   private int timer;
   private boolean wasRising;

   public AnimationTimer(int delay) {
      this.delay = delay;
      this.top = delay;
      this.bottom = 0;
   }

   public void update(boolean increment) {
      if (increment) {
         if (this.timer < this.delay) {
            if (!this.wasRising) {
               this.bottom = this.timer;
            }

            ++this.timer;
         }

         this.wasRising = true;
      } else {
         if (this.timer > 0) {
            if (this.wasRising) {
               this.top = this.timer;
            }

            --this.timer;
         }

         this.wasRising = false;
      }

   }

   public double getValue() {
      return this.wasRising ? Math.sin((double)(this.timer - this.bottom) / (double)(this.delay - this.bottom) * 3.141592653589793D / 2.0D) : 1.0D - Math.cos((double)this.timer / (double)this.top * 3.141592653589793D / 2.0D);
   }
}
