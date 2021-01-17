/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.xtrm.atlaspluginloader.core.event.Event$State
 *  me.xtrm.atlaspluginloader.core.event.EventTarget
 *  me.xtrm.delta.api.event.events.move.EventMotion
 *  me.xtrm.delta.api.module.Category
 *  me.xtrm.delta.api.module.Module
 *  net.minecraft.item.ItemFood
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C03PacketPlayer
 */
package delta.module.modules;

import me.xtrm.atlaspluginloader.core.event.Event;
import me.xtrm.atlaspluginloader.core.event.EventTarget;
import me.xtrm.delta.api.event.events.move.EventMotion;
import me.xtrm.delta.api.module.Category;
import me.xtrm.delta.api.module.Module;
import net.minecraft.item.ItemFood;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

public class FastEat
extends Module {
    @EventTarget
    public void onMotion(EventMotion eventMotion) {
        if (eventMotion.getType() != Event.State.PRE) {
            return;
        }
        if (this.mc.thePlayer.onGround && this.mc.thePlayer.isEating() && this.mc.thePlayer.getItemInUse() != null && this.mc.thePlayer.getItemInUse().getItem() instanceof ItemFood && this.mc.thePlayer.fallDistance < 3.0f) {
            for (int i = 0; i < 10; ++i) {
                this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer(true));
            }
        }
    }

    public FastEat() {
        super("FastEat", Category.Player);
        this.setDescription("Permet de manger plus rapidement");
    }
}

