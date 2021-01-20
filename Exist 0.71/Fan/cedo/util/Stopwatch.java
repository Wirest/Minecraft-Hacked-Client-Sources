package cedo.util;

public final class Stopwatch {

    private long ms;

    public Stopwatch() {
        ms = getCurrentMS();
    }

    private long getCurrentMS() {
        return System.currentTimeMillis();
    }

    public final long getElapsedTime() {
        return getCurrentMS() - ms;
    }

    public final boolean elapsed(final long milliseconds) {
        return (getCurrentMS() - ms) > milliseconds;
    }

    public final void reset() {
        ms = getCurrentMS();
    }
}
