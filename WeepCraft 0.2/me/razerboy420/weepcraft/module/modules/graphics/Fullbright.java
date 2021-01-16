/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.module.modules.graphics;

import me.razerboy420.weepcraft.module.Module;
import me.razerboy420.weepcraft.util.Wrapper;
import net.minecraft.client.settings.GameSettings;

@Module.Mod(category=Module.Category.RENDER, description="Makes the world brighter", key=0, name="Fullbright")
public class Fullbright
extends Module {
    public float saved;

    @Override
    public void onEnable() {
        this.saved = Wrapper.getSettings().gammaSetting;
        Wrapper.getSettings().gammaSetting = 1000.0f;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        Wrapper.getSettings().gammaSetting = this.saved;
        super.onDisable();
    }
}

