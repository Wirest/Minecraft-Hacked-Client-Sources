package moonx.ohare.client.event.impl.render;

import moonx.ohare.client.event.Event;
import net.minecraft.client.gui.ScaledResolution;

/**
 * made by oHare for eclipse
 *
 * @since 8/27/2019
 **/
public class Render2DEvent extends Event {
    private float partialTicks;
    private ScaledResolution scaledResolution;

    public Render2DEvent(float partialTicks, ScaledResolution scaledResolution) {
        this.partialTicks = partialTicks;
        this.scaledResolution = scaledResolution;
    }

    public float getPartialTicks() {
        return partialTicks;
    }

    public ScaledResolution getScaledResolution() {
        return scaledResolution;
    }
}
