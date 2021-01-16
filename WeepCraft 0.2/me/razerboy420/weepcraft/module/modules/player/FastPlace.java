/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.module.modules.player;

import darkmagician6.EventTarget;
import darkmagician6.events.EventPreMotionUpdates;
import me.razerboy420.weepcraft.module.Module;
import me.razerboy420.weepcraft.util.Wrapper;
import net.minecraft.client.Minecraft;

@Module.Mod(category=Module.Category.PLAYER, description="Place blocks faster", key=0, name="FastPlace")
public class FastPlace
extends Module {
    @EventTarget
    public void onEvent(EventPreMotionUpdates event) {
        Wrapper.mc().rightClickDelayTimer = 0;
    }
}

