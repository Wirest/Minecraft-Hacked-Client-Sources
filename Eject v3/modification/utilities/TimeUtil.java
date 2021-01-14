package modification.utilities;

public final class TimeUtil {
    private long last;

    public final boolean reached(double paramDouble) {
        return System.currentTimeMillis() - this.last >= paramDouble;
    }

    public final void reset() {
        this.last = System.currentTimeMillis();
    }

    public int convertToMs(int paramInt) {
        return -paramInt * 50;
    }
}




