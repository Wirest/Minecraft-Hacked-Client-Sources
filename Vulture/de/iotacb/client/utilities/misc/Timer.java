package de.iotacb.client.utilities.misc;

public class Timer {

	private long pastMS;
	
	public final boolean delay(long delay) {
		if (System.currentTimeMillis() - pastMS > delay) {
			reset();
			return true;
		}
		return false;
	}
	
	public final boolean delay2(long delay) {
		if (System.currentTimeMillis() - pastMS > delay) {
			return true;
		}
		return false;
	}
	
	public final void reset() {
		pastMS = System.currentTimeMillis();
	}
	
	public final long getPastMS() {
		return pastMS;
	}
	
}
