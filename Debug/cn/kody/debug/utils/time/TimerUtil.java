/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package cn.kody.debug.utils.time;

public class TimerUtil {
    private long time = System.nanoTime() / 1000000L;

    public boolean reach(long time) {
        return this.time() >= time;
    }

    public void reset() {
        this.time = System.nanoTime() / 1000000L;
    }

    public boolean sleep(long time) {
        if (this.time() >= time) {
            this.reset();
            return true;
        }
        return false;
    }

    public long time() {
        return System.nanoTime() / 1000000L - this.time;
    }
}

