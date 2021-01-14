package splash.client.events.render;

import me.hippo.systems.lwjeb.event.MultiStage;
import me.hippo.systems.lwjeb.event.Stage;
import net.minecraft.entity.Entity;

public class EntityRenderEvent extends MultiStage {
    private float partialTicks;
    private Entity entity;

    public EntityRenderEvent(float partialTicks, Entity entity) {
        this.partialTicks = partialTicks;
        this.entity = entity;
    }

    public float getPartialTicks() {
        return partialTicks;
    }

    public void setPartialTicks(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }
}
