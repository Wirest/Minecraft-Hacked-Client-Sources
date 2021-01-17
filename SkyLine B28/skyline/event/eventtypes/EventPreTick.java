package skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.eventtypes;

import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.Event;

public class EventPreTick extends Event{

	public double posX;
	public double minY;
	public double posY;
	public double posZ;
	
	 public EventPreTick(final double posX, final double minY, final double posY, final double posZ) {
	        this.posX = posX;
	        this.minY = minY;
	        this.posY = posY;
	        this.posZ = posZ;
	    }
	}
