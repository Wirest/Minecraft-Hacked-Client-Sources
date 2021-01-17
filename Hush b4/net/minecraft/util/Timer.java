// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import net.minecraft.client.Minecraft;

public class Timer
{
    float ticksPerSecond;
    private double lastHRTime;
    public int elapsedTicks;
    public float renderPartialTicks;
    public float timerSpeed;
    public float elapsedPartialTicks;
    private long lastSyncSysClock;
    private long lastSyncHRClock;
    private long field_74285_i;
    private double timeSyncAdjustment;
    
    public Timer(final float p_i1018_1_) {
        this.timerSpeed = 1.0f;
        this.timeSyncAdjustment = 1.0;
        this.ticksPerSecond = p_i1018_1_;
        this.lastSyncSysClock = Minecraft.getSystemTime();
        this.lastSyncHRClock = System.nanoTime() / 1000000L;
    }
    
    public void updateTimer() {
        final long i = Minecraft.getSystemTime();
        final long j = i - this.lastSyncSysClock;
        final long k = System.nanoTime() / 1000000L;
        final double d0 = k / 1000.0;
        if (j <= 1000L && j >= 0L) {
            this.field_74285_i += j;
            if (this.field_74285_i > 1000L) {
                final long l = k - this.lastSyncHRClock;
                final double d2 = this.field_74285_i / (double)l;
                this.timeSyncAdjustment += (d2 - this.timeSyncAdjustment) * 0.20000000298023224;
                this.lastSyncHRClock = k;
                this.field_74285_i = 0L;
            }
            if (this.field_74285_i < 0L) {
                this.lastSyncHRClock = k;
            }
        }
        else {
            this.lastHRTime = d0;
        }
        this.lastSyncSysClock = i;
        double d3 = (d0 - this.lastHRTime) * this.timeSyncAdjustment;
        this.lastHRTime = d0;
        d3 = MathHelper.clamp_double(d3, 0.0, 1.0);
        this.elapsedPartialTicks += (float)(d3 * this.timerSpeed * this.ticksPerSecond);
        this.elapsedTicks = (int)this.elapsedPartialTicks;
        this.elapsedPartialTicks -= this.elapsedTicks;
        if (this.elapsedTicks > 10) {
            this.elapsedTicks = 10;
        }
        this.renderPartialTicks = this.elapsedPartialTicks;
    }
}
