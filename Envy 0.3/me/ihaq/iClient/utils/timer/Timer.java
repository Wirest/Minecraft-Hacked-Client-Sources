package me.ihaq.iClient.utils.timer;

public class Timer {
	private long lastMS;
	private long currentMS;

	public final void updateMS() {
		currentMS = System.currentTimeMillis();
	}

	public final void updateLastMS() {
		lastMS = System.currentTimeMillis();
	}

	public final boolean hasTimePassedM(final long MS) {
		return currentMS >= lastMS + MS;
	}

	public final boolean hasTimePassedS(final float speed) {
		return currentMS >= lastMS + (long) (1000.0f / speed);
	}

	public long getCurrentMS() {
		return System.nanoTime() / 1000000L;
	}

	public long getLastMS() {
		return this.lastMS;
	}

	public boolean hasReached(long milliseconds) {
		return getCurrentMS() - this.lastMS >= milliseconds;
	}

	public long getTime() {
		return getCurrentMS() - this.lastMS;
	}

	public void reset() {
		this.lastMS = getCurrentMS();
	}

	public void setLastMS(long currentMS) {
		this.lastMS = currentMS;
	}

	public boolean hasTimeElapsed(long time, boolean reset) {
		if (time() >= time) {
			if (reset) {
				reset();
			}
			return true;
		}
		return false;
	}

	public long time() {
		return System.nanoTime() / 1000000L - this.lastMS;
	}
}
