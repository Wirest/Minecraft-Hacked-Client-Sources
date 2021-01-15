// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import java.util.Iterator;
import me.CheerioFX.FusionX.module.ModuleManager;
import me.CheerioFX.FusionX.GUI.clickgui.UI;
import org.lwjgl.opengl.Display;
import me.CheerioFX.FusionX.FusionX;
import me.CheerioFX.FusionX.ui.GuiIngameHook;
import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.module.Module;

public class GhostClient extends Module
{
    public static boolean disableAllHacks;
    public static boolean enabled;
    boolean firstStart;
    
    static {
        GhostClient.disableAllHacks = false;
        GhostClient.enabled = false;
    }
    
    public static boolean isDisableAllHacks() {
        return GhostClient.disableAllHacks;
    }
    
    public static void setDisableAllHacks(final boolean disableAllHacks) {
        GhostClient.disableAllHacks = disableAllHacks;
    }
    
    public GhostClient() {
        super("GhostClient", 210, Category.OTHER, false);
        this.firstStart = false;
    }
    
    @Override
    public void onDisable() {
        GhostClient.enabled = false;
        GuiIngameHook.renderGUI = true;
        GhostClient.disableAllHacks = false;
        Display.setTitle(String.valueOf(FusionX.theClient.Client_Name) + " " + FusionX.theClient.More_Info);
        if (UI.isTabGUI()) {
            FusionX.theClient.tabGui.register();
        }
        super.onDisable();
    }
    
    @Override
    public void onEnable() {
        GhostClient.enabled = true;
        GuiIngameHook.renderGUI = false;
        Display.setTitle("Minecraft 1.8");
        if (UI.isTabGUI()) {
            FusionX.theClient.tabGui.unregister();
        }
        this.firstStart = true;
        super.onEnable();
    }
    
    @Override
    public void onUpdate() {
        if (this.firstStart) {
            if (GhostClient.disableAllHacks) {
                final ModuleManager moduleManager = FusionX.theClient.moduleManager;
                for (final Module m : ModuleManager.activeModules) {
                    if (!m.getName().equalsIgnoreCase("GhostClient") && !m.getName().equalsIgnoreCase("GUI") && m.getState()) {
                        m.setState(false);
                    }
                }
            }
            this.firstStart = false;
        }
    }
}
