/*
 * Decompiled with CFR 0.150.
 */
package delta.utils;

public class TimeHelper {
    private long lastMS = 0L;

    public void isDelayComplete(long l) {
        this.lastMS = this.getCurrentMS() - l;
    }

    public long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }

    public void setLastMS() {
        this.lastMS = this.getCurrentMS();
    }

    public void convertToMS(long l) {
        this.lastMS = System.currentTimeMillis();
    }

    public long subMS() {
        return this.getCurrentMS() - this.lastMS;
    }

    public boolean hasReached(long l) {
        return this.subMS() >= l;
    }

    public int reset(int n) {
        return 1000 / n;
    }
}

