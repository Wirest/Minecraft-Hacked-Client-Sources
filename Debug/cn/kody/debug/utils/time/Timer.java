/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package cn.kody.debug.utils.time;

public class Timer {
    private long prevMS = 0L;

    public boolean delay(float milliSec) {
        return (float)(this.getTime() - this.prevMS) >= milliSec;
    }

    public void reset() {
        this.prevMS = this.getTime();
    }

    public long getTime() {
        return System.nanoTime() / 1000000L;
    }

    public long getDifference() {
        return this.getTime() - this.prevMS;
    }

    public void setDifference(long difference) {
        this.prevMS = this.getTime() - difference;
    }
}

