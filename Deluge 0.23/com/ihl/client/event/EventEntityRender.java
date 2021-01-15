package com.ihl.client.event;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.EntityLivingBase;

public class EventEntityRender extends Event {

    public Render render;
    public EntityLivingBase entity;
    public double x, y, z;

    public EventEntityRender(Type type, Render render, EntityLivingBase entity, double x, double y, double z) {
        super(type);
        this.render = render;
        this.entity = entity;
        this.x = x;
        this.y = y;
        this.z = z;
    }

}
