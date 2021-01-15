package dev.astroclient.client.event.impl.render;

import net.minecraft.client.gui.ScaledResolution;
import awfdd.ksksk.ap.zajkb.rgds.Event;

/**
* @author Zane for PublicBase
* @since 10/23/19
*/

public class EventRender2D extends Event {

    public ScaledResolution getScaledResolution() {
        return scaledResolution;
    }

    private ScaledResolution scaledResolution;

    public float getPartialTicks() {
        return partialTicks;
    }

    private float partialTicks;

    public EventRender2D(ScaledResolution scaledResolution, float partialTicks) {
        this.scaledResolution = scaledResolution;
        this.partialTicks = partialTicks;
    }
}
