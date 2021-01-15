package dev.astroclient.client.event.impl.render;

import net.minecraft.entity.EntityLivingBase;
import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.asdw.EventCancellable;

public class EventRenderTag extends EventCancellable {

    public EventRenderTag(EntityLivingBase entityLivingBase) {
        this.entityLivingBase = entityLivingBase;
    }

    public EntityLivingBase getEntityLivingBase() {
        return entityLivingBase;
    }

    private EntityLivingBase entityLivingBase;
}
