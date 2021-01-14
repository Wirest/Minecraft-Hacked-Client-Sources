package cn.kody.debug.utils;

public class HBTimer
{
    public long lastMs;
    
    public HBTimer() {
        super();
        this.lastMs = (0x5A6A4E0E1AD3B7C5L ^ 0x5A6A4E0E1AD3B7C5L);
    }
    
    public boolean isDelayComplete(final long n) {
        boolean b;
        if (System.currentTimeMillis() - this.lastMs > n) {
            b = true;
        }
        else {
            b = false;
        }
        return b;
    }
    
    public long getCurrentMS() {
        return System.nanoTime() / (0xEB53852F942C83B6L ^ 0xEB53852F9423C1F6L);
    }
    
    public void reset() {
        this.lastMs = System.currentTimeMillis();
    }
    
    public long getLastMs() {
        return this.lastMs;
    }
    
    public void setLastMs(final int n) {
        this.lastMs = System.currentTimeMillis() + n;
    }
    
    public boolean hasReached(final long n) {
        boolean b;
        if (this.getCurrentMS() - this.lastMs >= n) {
            b = true;
        }
        else {
            b = false;
        }
        return b;
    }
    
    public boolean isDelayComplete(final float n) {
        boolean b;
        if (System.currentTimeMillis() - this.lastMs > n) {
            b = true;
        }
        else {
            b = false;
        }
        return b;
    }
    
    public boolean isDelayComplete(final Double n) {
        boolean b;
        if (System.currentTimeMillis() - this.lastMs > n) {
            b = true;
        }
        else {
            b = false;
        }
        return b;
    }
}
