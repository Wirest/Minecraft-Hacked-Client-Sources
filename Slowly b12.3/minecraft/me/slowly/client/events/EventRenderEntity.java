/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.events;

import com.darkmagician6.eventapi.events.Event;
import net.minecraft.client.entity.AbstractClientPlayer;

public class EventRenderEntity
implements Event {
    private AbstractClientPlayer entity = null;
    double x = -1.0;
    double y = -1.0;
    double z = -1.0;
    float entityYaw = -1.0f;
    float partialTicks = -1.0f;
    public Entity entityData;

    public EventRenderEntity(AbstractClientPlayer entity, double x, double y, double z, float entityYaw, float partialTicks) {
        this.entity = entity;
        this.x = x;
        this.y = y;
        this.z = z;
        this.entityYaw = entityYaw;
        this.partialTicks = partialTicks;
        this.entityData = new Entity(entity, x, y, z, entityYaw, partialTicks);
    }

    public class Entity {
        private AbstractClientPlayer entity = null;
        double x = -1.0;
        double y = -1.0;
        double z = -1.0;
        float entityYaw = -1.0f;
        float partialTicks = -1.0f;

        public Entity(AbstractClientPlayer entity, double x, double y, double z, float entityYaw, float partialTicks) {
            this.entity = entity;
            this.x = x;
            this.y = y;
            this.z = z;
            this.entityYaw = entityYaw;
            this.partialTicks = partialTicks;
        }

        public AbstractClientPlayer getEntity() {
            return this.entity;
        }

        public double getX() {
            return this.x;
        }

        public double getY() {
            return this.y;
        }

        public double getZ() {
            return this.z;
        }

        public float getEntityYaw() {
            return this.entityYaw;
        }

        public float getPartialTicks() {
            return this.partialTicks;
        }
    }

}

