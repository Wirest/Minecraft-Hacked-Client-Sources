/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiScreen
 */
package me.aristhena.lucid.modules.render;

import me.aristhena.lucid.management.module.Mod;
import me.aristhena.lucid.management.module.Module;
import me.aristhena.lucid.management.option.Op;
import me.aristhena.lucid.ui.click.ClickGui;
import me.aristhena.lucid.ui.clickgui.Gui;
import me.aristhena.lucid.ui.clickgui.GuiGrabber;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

@Mod(keybind=54)
public class GUI
extends Module {
    @Op
    private boolean panel;

    @Override
    public void onEnable() {
        if (this.panel) {
            this.mc.displayGuiScreen((GuiScreen)new ClickGui());
        } else {
            Gui.instance.reloadOptions();
            this.mc.displayGuiScreen((GuiScreen)new GuiGrabber());
        }
    }
}

