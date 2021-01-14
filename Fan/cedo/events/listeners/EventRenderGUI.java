package cedo.events.listeners;

import cedo.events.Event;
import net.minecraft.client.gui.ScaledResolution;

public class EventRenderGUI extends Event<EventRenderGUI> {
    public ScaledResolution sr;

    public EventRenderGUI(ScaledResolution sr) {
        this.sr = sr;
    }
}
