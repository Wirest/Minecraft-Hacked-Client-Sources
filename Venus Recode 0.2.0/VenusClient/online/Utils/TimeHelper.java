package VenusClient.online.Utils;

public class TimeHelper {
    private long lastMS = 0L;

    public int convertToMS(int d) {
        return 1000 / d;
    }

    public long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }

    public boolean hasTimeElapsed(long time) {
        if (System.currentTimeMillis() - lastMS > time) {
            return true;
        }
        return false;
    }

    public boolean hasReached(long time) {
        if (System.currentTimeMillis() - lastMS > time) {
            return true;
        }
        return false;
    }


    public long getDelay() {
        return System.currentTimeMillis() - lastMS;
    }

    public void reset() {
        lastMS = getCurrentMS();
    }

    public void setLastMS() {
        lastMS = System.currentTimeMillis();
    }

    public void setLastMS(long lastMS) {
        this.lastMS = lastMS;
    }
    
    public void setTime(long time) {
    	this.lastMS = time;
    }
    
}
