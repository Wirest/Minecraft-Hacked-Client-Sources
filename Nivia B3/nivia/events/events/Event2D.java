package nivia.events.events;

import net.minecraft.client.gui.ScaledResolution;
import nivia.events.Event;

public class Event2D implements Event {
    private ScaledResolution scaledRes;

    public Event2D(ScaledResolution sr) {
	this.scaledRes = sr;
    }

    public ScaledResolution getScaledRes() {
	return this.scaledRes;
    }
}
