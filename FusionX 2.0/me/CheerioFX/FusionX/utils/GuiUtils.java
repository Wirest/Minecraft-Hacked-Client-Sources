// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.utils;

import net.minecraft.client.gui.GuiScreen;

public class GuiUtils
{
    static boolean inGui;
    static boolean willGlitch;
    
    static {
        GuiUtils.inGui = false;
        GuiUtils.willGlitch = false;
    }
    
    public static boolean isInGui() {
        return GuiUtils.inGui;
    }
    
    public static void setInGui(final boolean isInGui) {
        GuiUtils.inGui = isInGui;
    }
    
    public static GuiScreen getCurrentScreen() {
        return Wrapper.mc.currentScreen;
    }
}
