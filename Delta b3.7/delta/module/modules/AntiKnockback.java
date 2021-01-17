/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.xtrm.atlaspluginloader.core.event.Event$State
 *  me.xtrm.atlaspluginloader.core.event.EventTarget
 *  me.xtrm.delta.api.event.events.network.EventPacket
 *  me.xtrm.delta.api.module.Category
 *  me.xtrm.delta.api.module.Module
 *  net.minecraft.network.play.server.S12PacketEntityVelocity
 */
package delta.module.modules;

import me.xtrm.atlaspluginloader.core.event.Event;
import me.xtrm.atlaspluginloader.core.event.EventTarget;
import me.xtrm.delta.api.event.events.network.EventPacket;
import me.xtrm.delta.api.module.Category;
import me.xtrm.delta.api.module.Module;
import net.minecraft.network.play.server.S12PacketEntityVelocity;

public class AntiKnockback
extends Module {
    @EventTarget
    public void onPacket(EventPacket eventPacket) {
        if (eventPacket.getState() != Event.State.RECEIVE) {
            return;
        }
        if (eventPacket.getPacket() instanceof S12PacketEntityVelocity) {
            S12PacketEntityVelocity s12PacketEntityVelocity = (S12PacketEntityVelocity)eventPacket.getPacket();
            if (this.mc.theWorld == null) {
                return;
            }
            if (s12PacketEntityVelocity.func_149412_c() == -1) {
                return;
            }
            if (this.mc.theWorld.getEntityByID(s12PacketEntityVelocity.func_149412_c()) == this.mc.thePlayer) {
                eventPacket.setCancelled(true);
            }
        }
    }

    public AntiKnockback() {
        super("AntiKnockback", Category.Combat);
        this.setDescription("Annule le Knockback");
    }
}

