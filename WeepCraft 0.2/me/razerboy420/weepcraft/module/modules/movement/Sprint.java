/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.module.modules.movement;

import darkmagician6.EventTarget;
import darkmagician6.events.EventPreMotionUpdates;
import me.razerboy420.weepcraft.module.Module;
import me.razerboy420.weepcraft.util.Wrapper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.FoodStats;

@Module.Mod(category=Module.Category.MOVEMENT, description="Automatically sprints for you.", key=47, name="Sprint")
public class Sprint
extends Module {
    @EventTarget
    public void onEvent(EventPreMotionUpdates event) {
        if (this.canSprint()) {
            Wrapper.getPlayer().setSprinting(true);
        }
    }

    public boolean canSprint() {
        if (!(Wrapper.getPlayer().isSneaking() || Wrapper.getPlayer().getFoodStats().getFoodLevel() <= 6 || Wrapper.getPlayer().isCollidedHorizontally || Wrapper.getPlayer().moveForward == 0.0f && Wrapper.getPlayer().moveStrafing == 0.0f)) {
            return true;
        }
        return false;
    }
}

