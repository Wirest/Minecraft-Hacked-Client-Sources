/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.module.modules.movement;

import darkmagician6.EventTarget;
import darkmagician6.events.EventPreMotionUpdates;
import me.razerboy420.weepcraft.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

@Module.Mod(category=Module.Category.MOVEMENT, description="Go faster up ladders. Patched on latest NCP.", key=0, name="FastLadder")
public class SkipLadder
extends Module {
    @EventTarget
    public void onEvent(EventPreMotionUpdates pre) {
        if (Minecraft.getMinecraft().player.isOnLadder() && Minecraft.getMinecraft().player.isCollidedHorizontally) {
            Minecraft.getMinecraft().player.motionY = 0.2872999906539917;
        }
    }
}

