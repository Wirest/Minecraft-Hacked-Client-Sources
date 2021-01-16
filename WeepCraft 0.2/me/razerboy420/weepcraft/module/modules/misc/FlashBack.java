/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.module.modules.misc;

import darkmagician6.EventTarget;
import darkmagician6.events.EventBoundingBox;
import darkmagician6.events.EventPacketRecieve;
import darkmagician6.events.EventPacketSent;
import darkmagician6.events.EventPostMotionUpdates;
import darkmagician6.events.EventPreMotionUpdates;
import me.razerboy420.weepcraft.module.Module;
import me.razerboy420.weepcraft.util.Timer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;

@Module.Mod(category=Module.Category.MISC, description="Turn this on when on 1.8 servers", key=0, name="FlashBack")
public class FlashBack
extends Module {
    Timer timer = new Timer();

    @EventTarget
    public void onEvent(EventPreMotionUpdates event) {
    }

    @EventTarget
    public void onPost(EventPostMotionUpdates event) {
    }

    @EventTarget
    public void onPacket(EventPacketSent event) {
        if (event.getPacket() instanceof CPacketPlayer) {
            this.timer.reset();
        }
    }

    @EventTarget
    public void onRecieve(EventPacketRecieve event) {
    }

    @EventTarget
    public void onBB(EventBoundingBox event) {
    }
}

