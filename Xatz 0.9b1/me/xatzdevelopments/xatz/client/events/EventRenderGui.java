package me.xatzdevelopments.xatz.client.events;


import net.minecraft.client.gui.ScaledResolution;

public class EventRenderGui extends Event {
    private ScaledResolution resolution;

    public void fire(ScaledResolution resolution) {
        this.resolution = resolution;
        
    }

    public ScaledResolution getResolution() {
        return resolution;
    }
}
