/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.module.modules.movement;

import darkmagician6.EventTarget;
import darkmagician6.events.EventPostMotionUpdates;
import darkmagician6.events.EventPreMotionUpdates;
import me.razerboy420.weepcraft.module.Module;
import me.razerboy420.weepcraft.util.Wrapper;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;

@Module.Mod(category=Module.Category.MOVEMENT, description="Sneak for everyone else", key=0, name="Sneak")
public class Sneak
extends Module {
    @EventTarget
    public void onEvent(EventPreMotionUpdates pre) {
        Wrapper.sendPacket(new CPacketEntityAction(Wrapper.getPlayer(), CPacketEntityAction.Action.START_SNEAKING));
        Wrapper.sendPacket(new CPacketEntityAction(Wrapper.getPlayer(), CPacketEntityAction.Action.STOP_SNEAKING));
    }

    @EventTarget
    public void onPost(EventPostMotionUpdates post) {
        Wrapper.sendPacket(new CPacketEntityAction(Wrapper.getPlayer(), CPacketEntityAction.Action.STOP_SNEAKING));
        Wrapper.sendPacket(new CPacketEntityAction(Wrapper.getPlayer(), CPacketEntityAction.Action.START_SNEAKING));
    }

    @Override
    public void onDisable() {
        Wrapper.sendPacket(new CPacketEntityAction(Wrapper.getPlayer(), CPacketEntityAction.Action.STOP_SNEAKING));
        super.onDisable();
    }
}

