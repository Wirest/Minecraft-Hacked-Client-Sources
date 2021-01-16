/*
 * Decompiled with CFR 0_122.
 */
package darkmagician6.events;

import darkmagician6.Event;
import net.minecraft.entity.Entity;

public class EventPostAttack
implements Event {
    private Entity attacker;
    private Entity target;

    public EventPostAttack(Entity attacker, Entity target) {
        this.attacker = attacker;
        this.target = target;
    }

    public Entity getAttacker() {
        return this.attacker;
    }

    public Entity getTarget() {
        return this.target;
    }
}

