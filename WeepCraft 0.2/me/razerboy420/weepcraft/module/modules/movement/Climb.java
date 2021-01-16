/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.module.modules.movement;

import darkmagician6.EventTarget;
import darkmagician6.events.EventPreMotionUpdates;
import me.razerboy420.weepcraft.module.Module;
import me.razerboy420.weepcraft.util.Wrapper;
import net.minecraft.client.entity.EntityPlayerSP;

@Module.Mod(category=Module.Category.MOVEMENT, description="Climb like a spider", key=0, name="Climb")
public class Climb
extends Module {
    @EventTarget
    public void onEvent(EventPreMotionUpdates event) {
        if (Wrapper.getPlayer().isCollidedHorizontally) {
            Wrapper.getPlayer().motionY = 0.2;
        }
    }
}

