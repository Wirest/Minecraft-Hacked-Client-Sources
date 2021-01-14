package cn.kody.debug.utils;

public final class ExhibitonTimer
{
    public double time;
    
    public ExhibitonTimer() {
        super();
        this.time = (double)(System.nanoTime() / (0xE9EFC3434B241CDAL ^ 0xE9EFC3434B2B5E9AL));
    }
    
    public boolean hasTimeElapsed(final double n) {
        if (this.getTime() >= n) {
            this.reset();
            return true;
        }
        return false;
    }
    
    public double getTime() {
        return System.nanoTime() / (0xB49A4C49B56FF9AFL ^ 0xB49A4C49B560BBEFL) - this.time;
    }
    
    public void reset() {
        this.time = (double)(System.nanoTime() / (0xAFCB4746C00894DAL ^ 0xAFCB4746C007D69AL));
    }
}
