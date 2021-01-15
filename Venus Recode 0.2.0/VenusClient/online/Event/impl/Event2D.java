package VenusClient.online.Event.impl;


import VenusClient.online.Event.Event;
import net.minecraft.client.gui.ScaledResolution;

public class Event2D extends Event {
    private float width, height;
    private ScaledResolution scaledResolution;

    public Event2D(float width, float height, ScaledResolution scaledResolution) {
        this.width = width;
        this.height = height;
        this.scaledResolution = scaledResolution;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
    
    public ScaledResolution getScaledResolution() {
        return scaledResolution;
    }
    
}