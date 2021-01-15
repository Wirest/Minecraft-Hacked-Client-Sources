package me.xatzdevelopments.xatz.utils;

public class ctimer {
	
    private long prevMS;
    
    public ctimer() {
        this.prevMS = 0L;
    }
    
    public boolean delay(final float milliSec) {
        return this.getTime() - this.prevMS >= milliSec;
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
    
    public void setDifference(final long difference) {
        this.prevMS = this.getTime() - difference;
    }
}


