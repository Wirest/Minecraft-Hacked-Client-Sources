package net.minecraft.util;

import net.minecraft.client.Minecraft;

public class Timer {
   float ticksPerSecond;
   private double lastHRTime;
   public int elapsedTicks;
   public float renderPartialTicks;
   public float timerSpeed = 1.0F;
   public float elapsedPartialTicks;
   private long lastSyncSysClock;
   private long lastSyncHRClock;
   private long field_74285_i;
   private double timeSyncAdjustment = 1.0D;
   private static final String __OBFID = "CL_00000658";

   public Timer(float p_i1018_1_) {
      this.ticksPerSecond = p_i1018_1_;
      this.lastSyncSysClock = Minecraft.getSystemTime();
      this.lastSyncHRClock = System.nanoTime() / 1000000L;
   }

   public void updateTimer() {
      long var1 = Minecraft.getSystemTime();
      long var3 = var1 - this.lastSyncSysClock;
      long var5 = System.nanoTime() / 1000000L;
      double var7 = (double)var5 / 1000.0D;
      if (var3 <= 1000L && var3 >= 0L) {
         this.field_74285_i += var3;
         if (this.field_74285_i > 1000L) {
            long var9 = var5 - this.lastSyncHRClock;
            double var11 = (double)this.field_74285_i / (double)var9;
            this.timeSyncAdjustment += (var11 - this.timeSyncAdjustment) * 0.20000000298023224D;
            this.lastSyncHRClock = var5;
            this.field_74285_i = 0L;
         }

         if (this.field_74285_i < 0L) {
            this.lastSyncHRClock = var5;
         }
      } else {
         this.lastHRTime = var7;
      }

      this.lastSyncSysClock = var1;
      double var13 = (var7 - this.lastHRTime) * this.timeSyncAdjustment;
      this.lastHRTime = var7;
      var13 = MathHelper.clamp_double(var13, 0.0D, 1.0D);
      this.elapsedPartialTicks = (float)((double)this.elapsedPartialTicks + var13 * (double)this.timerSpeed * (double)this.ticksPerSecond);
      this.elapsedTicks = (int)this.elapsedPartialTicks;
      this.elapsedPartialTicks -= (float)this.elapsedTicks;
      if (this.elapsedTicks > 10) {
         this.elapsedTicks = 10;
      }

      this.renderPartialTicks = this.elapsedPartialTicks;
   }
}
