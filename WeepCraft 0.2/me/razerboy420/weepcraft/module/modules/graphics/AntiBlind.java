/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.module.modules.graphics;

import darkmagician6.EventTarget;
import darkmagician6.events.EventPreMotionUpdates;
import me.razerboy420.weepcraft.module.Module;
import me.razerboy420.weepcraft.util.Wrapper;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;

@Module.Mod(category=Module.Category.RENDER, description="Takes away blindness and nausea", key=0, name="AntiBlind")
public class AntiBlind
extends Module {
    @EventTarget
    public void onEvent(EventPreMotionUpdates event) {
        Wrapper.getPlayer().removePotionEffect(MobEffects.NAUSEA);
        Wrapper.getPlayer().removePotionEffect(MobEffects.BLINDNESS);
    }
}

