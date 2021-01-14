
package me.memewaredevs.client.util.misc;

public class Timer {
    private long prevMS = 0L;

    public Timer() {
        this.reset();
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

    public boolean sleep(long time) {
	    if (getTime() >= time) {
	      reset();
	      return true;
	    } 
	    return false;
	  }

}