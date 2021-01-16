/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.module.modules.movement;

import darkmagician6.EventTarget;
import darkmagician6.events.EventPreMotionUpdates;
import me.razerboy420.weepcraft.module.Module;
import me.razerboy420.weepcraft.util.Wrapper;
import net.minecraft.client.entity.EntityPlayerSP;

@Module.Mod(category=Module.Category.MOVEMENT, description="Press space to jump", key=0, name="JetPack")
public class JetPack
extends Module {
    @EventTarget
    public void onEvent(EventPreMotionUpdates event) {
        Wrapper.getPlayer().onGround = true;
    }
}

