package info.sigmaclient.event.impl;

import info.sigmaclient.event.Event;
import net.minecraft.client.gui.ScaledResolution;

/**
 * Created by Arithmo on 6/13/2017 at 5:25 PM.
 */
public class EventRenderHUD extends Event {
    private ScaledResolution resolution;

    public void fire(ScaledResolution resolution) {
        this.resolution = resolution;
        super.fire();
    }

    public ScaledResolution getResolution() {
        return resolution;
    }
}
