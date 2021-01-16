/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.module.modules.misc;

import darkmagician6.EventTarget;
import darkmagician6.events.EventPreMotionUpdates;
import me.razerboy420.weepcraft.module.Module;
import me.razerboy420.weepcraft.util.Wrapper;
import net.minecraft.client.entity.EntityPlayerSP;

@Module.Mod(category=Module.Category.MISC, description="Fly up when you die (Looks very strange)", key=0, name="DeathDerp")
public class DeathDerp
extends Module {
    @EventTarget
    public void onEvent(EventPreMotionUpdates event) {
        if (Wrapper.getPlayer().getHealth() <= 0.0f) {
            Wrapper.getPlayer().addVelocity(Wrapper.getPlayer().motionX / 2.0, 0.6, Wrapper.getPlayer().motionZ / 2.0);
        }
    }
}

