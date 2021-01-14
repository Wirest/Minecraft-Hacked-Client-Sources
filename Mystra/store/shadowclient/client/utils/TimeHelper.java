package store.shadowclient.client.utils;

public class TimeHelper {
    private static long lastMS = 0L;
    private long pastMS;
    private long time;
    
    public int convertToMS(int d) {
        return 1000 / d;
    }

    public static long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }

    public boolean hasReached(double d) {
        return getCurrentMS() - lastMS >= d;
    }

    public boolean hasTimeReached(long delay) {
        return System.currentTimeMillis() - lastMS >= delay;
    }

    public long getDelay() {
        return System.currentTimeMillis() - lastMS;
    }

    public static void reset() {
        lastMS = getCurrentMS();
    }
    
    public boolean reach(final long time) {
        return time() >= time;
    }

    public void setLastMS() {
        lastMS = System.currentTimeMillis();
    }

    public void setLastMS(long lastMS) {
        this.lastMS = lastMS;
    }
    
    public long time() {
        return System.nanoTime() / 1000000L - time;
    }

    public boolean isDelayComplete(float f) {
        if(System.currentTimeMillis() - this.lastMS >= f) {
            return true;
        }
        return false;
    }

	public boolean delay(double d) {
		return false;
	}
	
	public final boolean delay(long delay) {
		if (System.currentTimeMillis() - pastMS > delay) {
			reset();
			return true;
		}
		return false;
	}
	
	public boolean sleep(final long time) {
        if (time() >= time) {
            reset();
            return true;
        }
        return false;
    }
	
	private long ms = this.getCurrentMS();

	public final long getElapsedTime() {
		return this.getCurrentMS() - this.ms;
	}

	public final boolean elapsed(long milliseconds) {
	    return this.getCurrentMS() - this.ms > milliseconds;
	}
}