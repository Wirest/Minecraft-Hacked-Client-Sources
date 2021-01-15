package nivia.events;

import nivia.utils.Logger;
import nivia.utils.utils.Timer;

/**
 * The most basic form of an stoppable Event.
 * Stoppable events are called seperate from other events and the calling of methods is stopped
 * as soon as the EventStoppable is stopped.
 *
 * @author DarkMagician6
 * @since 26-9-13
 */
public abstract class EventDelayable implements Event, Delayable {

	
	
    private long delay;
    private Timer timer = new Timer();

    public boolean canCall(){   		
    	if(delay == 0) 
			return true;
		if(timer.hasTimeElapsed(getDelay(), true)) {
			Logger.logChat("lel");
			return true;
		}
		
		return false;
	}
    
    protected EventDelayable() { 
    }

    @Override
    public void setDelay(long delay) {	
        this.delay = delay;
    }
    @Override
    public long getDelay() {
        return delay;
    }
    
    

}
