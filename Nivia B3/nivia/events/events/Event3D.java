package nivia.events.events;

import nivia.events.Event;
import shadersmod.client.Shaders;

public class Event3D implements Event{
    private float ticks;
    private boolean isUsingShaders;
    public Event3D(float ticks) {
        this.ticks = ticks;
        isUsingShaders = Shaders.getShaderPackName() != null;
    }
    public float getPartialTicks() {
        return ticks;
    }
    public boolean isUsingShaders() {return isUsingShaders;}


}
