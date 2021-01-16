/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.module.modules.graphics;

import me.razerboy420.weepcraft.Weepcraft;
import me.razerboy420.weepcraft.gui.click.Click;
import me.razerboy420.weepcraft.module.Module;
import me.razerboy420.weepcraft.util.Wrapper;

@Module.Mod(category=Module.Category.RENDER, description="The clickgui", key=54, name="GUI")
public class GUI
extends Module {
    @Override
    public void onEnable() {
        if (!(Wrapper.mc().currentScreen instanceof Click)) {
            Wrapper.mc().displayGuiScreen(Weepcraft.getClick());
        }
        this.setToggled(false);
    }
}

