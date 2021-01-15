package dev.astroclient.client.event.impl.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLivingBase;
import awfdd.ksksk.ap.zajkb.rgds.Event;
import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.asdw.EventType;

public class EventRenderModel extends Event {

    public EntityLivingBase getEntityLivingBase() {
        return entityLivingBase;
    }

    private EntityLivingBase entityLivingBase;

    private ModelBase model;

    public float getP_77036_2_() {
        return p_77036_2_;
    }

    public float getP_77036_3_() {
        return p_77036_3_;
    }

    public float getP_77036_4_() {
        return p_77036_4_;
    }

    public float getP_77036_5_() {
        return p_77036_5_;
    }

    public float getP_77036_6_() {
        return p_77036_6_;
    }

    public float getP_77036_7_() {
        return p_77036_7_;
    }

    private float p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, p_77036_7_;

    public ModelBase getModel() {
        return model;
    }

    public EventRenderModel(EventType eventType, EntityLivingBase entityLivingBase, ModelBase model, float p_77036_2_, float p_77036_3_, float p_77036_4_, float p_77036_5_, float p_77036_6_, float p_77036_7_) {
        super(eventType);
        this.entityLivingBase = entityLivingBase;
        this.model = model;
        this.p_77036_2_ = p_77036_2_;
        this.p_77036_3_ = p_77036_3_;
        this.p_77036_4_ = p_77036_4_;
        this.p_77036_5_ = p_77036_5_;
        this.p_77036_6_ = p_77036_6_;
        this.p_77036_7_ = p_77036_7_;
    }

}
