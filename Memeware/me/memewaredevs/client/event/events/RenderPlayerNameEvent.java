
package me.memewaredevs.client.event.events;

import net.minecraft.entity.Entity;
import me.memewaredevs.client.event.Event;

public class RenderPlayerNameEvent extends Event {
    public Entity p;

    public RenderPlayerNameEvent(final Entity p2) {
        this.p = p2;
    }
}
