package cn.kody.debug.events;

import com.darkmagician6.eventapi.events.Event;

import net.minecraft.client.gui.ScaledResolution;

public class EventRender2D
implements Event {
    float partialTicks;
    public ScaledResolution res;

    public EventRender2D(ScaledResolution res) {
	this.res = res;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }

    public void setPartialTicks(float partialTicks) {
        this.partialTicks = partialTicks;
    }
}

