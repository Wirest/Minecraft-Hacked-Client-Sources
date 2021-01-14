package moonx.ohare.client.event.impl.render;

import moonx.ohare.client.event.cancelable.CancelableEvent;
import net.minecraft.entity.Entity;

/**
 * made by oHare for eclipse
 *
 * @since 8/30/2019
 **/
public class NameplateEvent extends CancelableEvent {
    private Entity entity;

    public NameplateEvent(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }
}
