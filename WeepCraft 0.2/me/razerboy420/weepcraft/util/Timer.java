/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.util;

public class Timer {
    public long currentMS = 0;
    public long lastMS = -1;

    public final void update() {
        this.currentMS = System.currentTimeMillis();
    }

    public final void reset() {
        this.lastMS = System.currentTimeMillis();
    }

    public final boolean hasPassed(long MS) {
        if (this.currentMS >= this.lastMS + MS) {
            return true;
        }
        return false;
    }
}

