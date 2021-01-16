/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.module.modules.movement;

import darkmagician6.EventTarget;
import darkmagician6.events.EventMove;
import me.razerboy420.weepcraft.module.Module;
import me.razerboy420.weepcraft.util.Wrapper;
import net.minecraft.client.entity.EntityPlayerSP;

@Module.Mod(category=Module.Category.MOVEMENT, description="Walk faster in water (Works even better with real DS enchant)", key=0, name="DepthStrider")
public class DepthStrider
extends Module {
    public int ticks = 0;

    @EventTarget
    public void onMove(EventMove event) {
        ++this.ticks;
        if (Wrapper.getPlayer().isInWater() || Wrapper.getPlayer().isInLava()) {
            EntityPlayerSP var10000;
            if (this.ticks == 3) {
                var10000 = Wrapper.getPlayer();
                var10000.motionX *= 4.300000190734863;
                var10000 = Wrapper.getPlayer();
                var10000.motionZ *= 4.300000190734863;
            }
            if (this.ticks == 4) {
                var10000 = Wrapper.getPlayer();
                var10000.motionX *= 1.100000023841858;
                var10000 = Wrapper.getPlayer();
                var10000.motionZ *= 1.100000023841858;
            }
            if (this.ticks >= 5) {
                var10000 = Wrapper.getPlayer();
                var10000.motionX /= 1.4500000476837158;
                var10000 = Wrapper.getPlayer();
                var10000.motionZ /= 1.4500000476837158;
            }
            if (this.ticks >= 7) {
                this.ticks = 0;
            }
        }
    }
}

