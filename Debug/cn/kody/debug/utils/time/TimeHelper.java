/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package cn.kody.debug.utils.time;

public class TimeHelper {
    public long lastMs = 0L;

    public boolean isDelayComplete(long delay) {
        if (System.currentTimeMillis() - this.lastMs > delay) {
            return true;
        }
        return false;
    }

    public long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }

    public void reset() {
        this.lastMs = System.currentTimeMillis();
    }

    public long getLastMs() {
        return this.lastMs;
    }

    public void setLastMs(int i) {
        this.lastMs = System.currentTimeMillis() + (long)i;
    }

    public boolean hasReached(long milliseconds) {
        return this.getCurrentMS() - this.lastMs >= milliseconds;
    }

    public boolean hasReached(float timeLeft) {
        return (float)(this.getCurrentMS() - this.lastMs) >= timeLeft;
    }

    public boolean delay(double nextDelay) {
        return (double)(System.currentTimeMillis() - this.lastMs) >= nextDelay;
    }
}

