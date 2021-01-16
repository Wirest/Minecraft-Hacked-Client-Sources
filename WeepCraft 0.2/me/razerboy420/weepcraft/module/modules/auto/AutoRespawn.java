/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.module.modules.auto;

import darkmagician6.EventTarget;
import darkmagician6.events.EventPreMotionUpdates;
import me.razerboy420.weepcraft.module.Module;
import me.razerboy420.weepcraft.util.Wrapper;
import net.minecraft.client.entity.EntityPlayerSP;

@Module.Mod(category=Module.Category.AUTO, description="Respawn when you die", key=0, name="AutoRespawn")
public class AutoRespawn
extends Module {
    @EventTarget
    public void onEvent(EventPreMotionUpdates event) {
        if (Wrapper.getPlayer().isDead) {
            Wrapper.getPlayer().respawnPlayer();
        }
    }
}

