/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.module.modules.auto;

import darkmagician6.EventTarget;
import darkmagician6.events.EventPreMotionUpdates;
import me.razerboy420.weepcraft.module.Module;
import me.razerboy420.weepcraft.util.Wrapper;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;

@Module.Mod(category=Module.Category.AUTO, description="Walks for you", key=0, name="AutoWalk")
public class AutoWalk
extends Module {
    @EventTarget
    public void onUpdate(EventPreMotionUpdates event) {
        Wrapper.getSettings().keyBindForward.pressed = true;
    }

    @Override
    public void onDisable() {
        Wrapper.getSettings().keyBindForward.pressed = false;
        super.onDisable();
    }
}

