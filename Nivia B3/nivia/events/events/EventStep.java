package nivia.events.events;

import nivia.events.Event;

public class EventStep implements Event {
	   private float stepHeight;
	    private Time time;
	    public EventStep(Time time, float stepHeight) {
	        this.time = time;
	        this.stepHeight = stepHeight;
	    }
	    public float getStepHeight() {
	        return stepHeight;
	    }
	    public void setStepHeight(float stepHeight) {
	        this.stepHeight = stepHeight;
	    }
	    public Time getTime() {
	        return time;
	    }
	    public enum Time {
	        PRE, POST
	    }
}
