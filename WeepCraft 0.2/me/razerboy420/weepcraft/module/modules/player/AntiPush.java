/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.module.modules.player;

import darkmagician6.EventTarget;
import darkmagician6.events.EventPreMotionUpdates;
import me.razerboy420.weepcraft.module.Module;
import me.razerboy420.weepcraft.util.Wrapper;
import net.minecraft.client.entity.EntityPlayerSP;

@Module.Mod(category=Module.Category.PLAYER, description="Don't get pushed around", key=0, name="AntiPush")
public class AntiPush
extends Module {
    public float saveReduction = 1.0E8f;

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (Wrapper.getWorld() != null) {
            Wrapper.getPlayer().entityCollisionReduction = this.saveReduction;
        }
    }

    @EventTarget
    public void onEvent(EventPreMotionUpdates event) {
        if (this.saveReduction == 1.0E8f) {
            this.saveReduction = Wrapper.getPlayer().entityCollisionReduction;
        }
        Wrapper.getPlayer().entityCollisionReduction = 1.0f;
    }
}

