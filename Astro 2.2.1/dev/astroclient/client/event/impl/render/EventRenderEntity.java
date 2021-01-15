package dev.astroclient.client.event.impl.render;

import net.minecraft.entity.EntityLivingBase;
import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.asdw.EventCancellable;
import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.asdw.EventType;

public class EventRenderEntity extends EventCancellable {

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public float getPartialTicks() {
        return partialTicks;
    }

    public void setPartialTicks(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public EntityLivingBase getEntityLivingBase() {
        return entityLivingBase;
    }

    public void setEntityLivingBase(EntityLivingBase entityLivingBase) {
        this.entityLivingBase = entityLivingBase;
    }

    private EventType eventType;

    public EventRenderEntity(EventType eventType, float partialTicks, EntityLivingBase entityLivingBase) {
        this.eventType = eventType;
        this.partialTicks = partialTicks;
        this.entityLivingBase = entityLivingBase;
    }

    private float partialTicks;

    private EntityLivingBase entityLivingBase;
}
