package Blizzard.Utils;

public class Time {
	long sys_ms;
	private long currentMS = 0L;
	protected long lastMS = -1L;
	private long a = 0L;
	private long prevMS;

	public Time() {
		this.prevMS = 0L;
		this.time = (System.nanoTime() / 1000000l);
		update();
	}

	void update() {
		this.sys_ms = System.currentTimeMillis();
	}

	public void begin() {
		update();
	}

	public boolean over(long ms) {
		return System.currentTimeMillis() - this.sys_ms > ms;
	}

	public boolean a(double a1) {
		return this.b() - this.c() >= a1;
	}

	public long b() {
		return System.nanoTime() / 1000000L;
	}

	public long c() {
		return this.a;
	}

	public boolean hasTimeElapsed(long time, boolean reset) {
		if (getTime() >= time) {
			if (reset) {
				reset();
			}
			return true;
		}
		return false;
	}

	public long getPrevMS() {
		return this.prevMS;
	}

	public boolean delay(final double d) {
		if (this.getTime() - this.getPrevMS() >= d) {
			this.reset();
			return true;
		}
		return false;
	}

	public long getTime() {
		return System.nanoTime() / 1000000l - this.time;
	}

	public final boolean hasPassed(long MS) {
		return this.currentMS >= this.lastMS + MS;
	}

	public void reset() {
		this.time = (System.nanoTime() / 1000000l);
	}

	public long time;

}
