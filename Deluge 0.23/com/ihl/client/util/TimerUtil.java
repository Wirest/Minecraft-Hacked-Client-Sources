package com.ihl.client.util;

public class TimerUtil {

    private long previousTime;

    public TimerUtil() {
        previousTime = 0;
    }

    public boolean isTime(double time) {
        return currentTime() >= time * 1000L;
    }

    public float currentTime() {
        return systemTime() - previousTime;
    }

    public void reset() {
        previousTime = systemTime();
    }

    public long systemTime() {
        return System.currentTimeMillis();
    }

}
