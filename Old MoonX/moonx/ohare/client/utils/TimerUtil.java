package moonx.ohare.client.utils;

public final class TimerUtil
{
    private long time;

    public TimerUtil() {
        time = System.nanoTime() / 1000000L;
    }

    public boolean reach(final long time) {
        return time() >= time;
    }

    public void reset() {
        time = System.nanoTime() / 1000000L;
    }

    public boolean sleep(final long time) {
        if (time() >= time) {
            reset();
            return true;
        }
        return false;
    }
    public short convertToMS(float perSecond) {
        return (short) (int) (1000.0F / perSecond);
    }
    public long time() {
        return System.nanoTime() / 1000000L - time;
    }
}