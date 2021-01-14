package net.minecraft.util;

import net.minecraft.client.Minecraft;

public class Timer {
    /**
     * The number of timer ticks per second of real time
     */
    float ticksPerSecond;

    /**
     * The time reported by the high-resolution clock at the last call of updateTimer(), in seconds
     */
    private double lastHRTime;

    /**
     * How many full ticks have turned over since the last call to updateTimer(), capped at 10.
     */
    public int elapsedTicks;

    /**
     * How much time has elapsed since the last tick, in ticks, for use by display rendering routines (range: 0.0 -
     * 1.0).  This field is frozen if the display is paused to eliminate jitter.
     */
    public float renderPartialTicks;

    /**
     * A multiplier to make the timer (and therefore the game) go faster or slower.  0.5 makes the game run at half-
     * speed.
     */
    public double timerSpeed = 1.0F;

    /**
     * How much time has elapsed since the last tick, in ticks (range: 0.0 - 1.0).
     */
    public float elapsedPartialTicks;

    /**
     * The time reported by the system clock at the last sync, in milliseconds
     */
    private long lastSyncSysClock;

    /**
     * The time reported by the high-resolution clock at the last sync, in milliseconds
     */
    private long lastSyncHRClock;
    private long field_74285_i;

    /**
     * A ratio used to sync the high-resolution clock to the system clock, updated once per second
     */
    private double timeSyncAdjustment = 1.0D;
    private static final String __OBFID = "CL_00000658";

    public Timer(float p_i1018_1_) {
        this.ticksPerSecond = p_i1018_1_;
        this.lastSyncSysClock = Minecraft.getSystemTime();
        this.lastSyncHRClock = System.nanoTime() / 1000000L;
    }

    /**
     * Updates all fields of the Timer using the current time
     */
    public void updateTimer() {
        long var1 = Minecraft.getSystemTime();
        long var3 = var1 - this.lastSyncSysClock;
        long var5 = System.nanoTime() / 1000000L;
        double var7 = (double) var5 / 1000.0D;

        if (var3 <= 1000L && var3 >= 0L) {
            this.field_74285_i += var3;

            if (this.field_74285_i > 1000L) {
                long var9 = var5 - this.lastSyncHRClock;
                double var11 = (double) this.field_74285_i / (double) var9;
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
        this.elapsedPartialTicks = (float) ((double) this.elapsedPartialTicks + var13 * (double) this.timerSpeed * (double) this.ticksPerSecond);
        this.elapsedTicks = (int) this.elapsedPartialTicks;
        this.elapsedPartialTicks -= (float) this.elapsedTicks;

        if (this.elapsedTicks > 10) {
            this.elapsedTicks = 10;
        }

        this.renderPartialTicks = this.elapsedPartialTicks;
    }
}
