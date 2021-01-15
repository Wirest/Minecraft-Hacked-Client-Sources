package nivia.utils;

public class TimeHelper {
	private long lastMS;

	public TimeHelper() {
		this.lastMS = 0L;
	}

	public boolean isDelayComplete(final long delay) {
		return System.currentTimeMillis() - this.lastMS >= delay;
	}

	public boolean isDelayComplete(final float delay) {
		return System.currentTimeMillis() - this.lastMS >= delay;
	}

	public boolean isDelayComplete(final int delay) {
		return System.currentTimeMillis() - this.lastMS >= delay;
	}

	public boolean isDelayComplete(final double delay) {
		return System.currentTimeMillis() - this.lastMS >= delay;
	}

	public long getLastMs() {
		return System.currentTimeMillis() - this.lastMS;
	}

	public void setLastMS() {
		this.lastMS = System.currentTimeMillis();
	}
}
