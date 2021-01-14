package cheatware.utils;

import java.util.concurrent.TimeUnit;

public class TimerUtil {
	public long lastMS = System.currentTimeMillis();
	
	public void reset() {
		lastMS = System.currentTimeMillis();
	}
	
	public boolean hasTimeElapsed(long time, boolean reset) {
		if(System.currentTimeMillis()-lastMS > time) {
			if(reset)
				reset();
			
			return true;
		}
		
		return false;
	}
	
	public boolean sleep(long time)
    {
        return sleep(time, TimeUnit.MILLISECONDS);
    }

    public boolean sleep(long time, TimeUnit timeUnit)
    {
        long convert = timeUnit.convert(System.nanoTime() - lastMS, TimeUnit.NANOSECONDS);
        if (convert >= time)
            reset();
        return convert >= time;
    }
}
