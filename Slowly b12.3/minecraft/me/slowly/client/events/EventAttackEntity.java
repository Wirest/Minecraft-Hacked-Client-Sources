/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.events;

import com.darkmagician6.eventapi.events.Event;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class EventAttackEntity
implements Event {
    private EntityPlayer playerIn;
    public Entity target;

    public EventAttackEntity(EntityPlayer playerIn, Entity target) {
        this.playerIn = playerIn;
        this.target = target;
    }

    public EntityPlayer getPlayerIn() {
        return this.playerIn;
    }

    public Entity getTarget() {
        return this.target;
    }
}

