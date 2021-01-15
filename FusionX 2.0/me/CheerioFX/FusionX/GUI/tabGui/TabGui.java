// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.GUI.tabGui;

import java.util.Iterator;
import me.CheerioFX.FusionX.module.ModuleManager;
import me.CheerioFX.FusionX.FusionX;
import java.util.ArrayList;
import java.util.List;
import com.darkmagician6.eventapi.EventTarget;
import me.CheerioFX.FusionX.events.KeyPressedEvent;
import me.CheerioFX.FusionX.module.Module;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import me.CheerioFX.FusionX.managers.GuiManager;
import me.CheerioFX.FusionX.module.Category;
import com.darkmagician6.eventapi.EventManager;
import me.CheerioFX.FusionX.GUI.clickgui.UI;
import net.minecraft.client.Minecraft;

public class TabGui
{
    public static int selected;
    public static int moduleSelected;
    private static boolean isOpened;
    private static final Minecraft mc;
    
    static {
        mc = Minecraft.getMinecraft();
    }
    
    public TabGui() {
        if (UI.isTabGUI()) {
            this.register();
        }
    }
    
    public void updateRegisteration() {
        if (UI.isTabGUI()) {
            this.register();
        }
        else {
            this.unregister();
        }
    }
    
    public void register() {
        EventManager.register(this);
    }
    
    public void unregister() {
        EventManager.unregister(this);
    }
    
    public static void drawTabGui() {
        for (int i = 0; i < Category.values().length - 2; ++i) {
            final Category category = Category.values()[i];
            if (category != Category.GUI) {
                if (category != Category.SETTINGS) {
                    final int y = 75;
                    final int xOffset = 5;
                    final int color = GuiManager.getRBG();
                    final int bg = new Color(0, 0, 0, GuiManager.getBGTransparency(true)).getRGB();
                    final int hightlighted = new Color(GuiManager.getHighlightDarkness(), GuiManager.getHighlightDarkness(), GuiManager.getHighlightDarkness()).getRGB();
                    Gui.drawRect(2 + xOffset, y + i * 10, 60 + xOffset, y + 10 + i * 10, (TabGui.selected == i) ? color : bg);
                    final String str = String.valueOf(category.name().substring(0, 1)) + category.name().substring(1).toLowerCase();
                    TabGui.mc.fontRendererObj.drawStringWithShadow(str, 4 + xOffset, y + i * 10 + 1, (TabGui.selected == i && TabGui.isOpened) ? hightlighted : 16777215);
                    if (TabGui.isOpened && i == TabGui.selected) {
                        for (int o = 0; o < getHacks(category).size(); ++o) {
                            final Module mod = getHacks(category).get(o);
                            Gui.drawRect(62 + xOffset, y + i * 10 + o * 10, 155 + xOffset, y + i * 10 + o * 10 + 10, (TabGui.moduleSelected == o) ? color : bg);
                            TabGui.mc.fontRendererObj.drawStringWithShadow(mod.getName(), 64 + xOffset, y + i * 10 + o * 10 + 1, mod.getState() ? hightlighted : 16777215);
                        }
                    }
                }
            }
        }
    }
    
    @EventTarget
    public static void onKeyPressed(final KeyPressedEvent event) {
        final int key = event.getEventKey();
        if (key == 200) {
            if (!TabGui.isOpened) {
                --TabGui.selected;
                if (TabGui.selected <= 0) {
                    TabGui.selected = 0;
                }
            }
            else {
                --TabGui.moduleSelected;
                if (TabGui.moduleSelected <= 0) {
                    TabGui.moduleSelected = 0;
                }
            }
        }
        if (key == 208) {
            if (!TabGui.isOpened) {
                ++TabGui.selected;
                if (TabGui.selected >= Category.values().length - 2) {
                    TabGui.selected = Category.values().length - 2 - 1;
                }
            }
            else {
                ++TabGui.moduleSelected;
                if (TabGui.moduleSelected >= getHacks(Category.values()[TabGui.selected]).size() - 1) {
                    TabGui.moduleSelected = getHacks(Category.values()[TabGui.selected]).size() - 1;
                }
            }
        }
        if (key == 205) {
            if (!TabGui.isOpened) {
                TabGui.isOpened = true;
            }
            else {
                getHacks(Category.values()[TabGui.selected]).get(TabGui.moduleSelected).toggleModule();
            }
        }
        if (key == 203) {
            TabGui.isOpened = false;
            TabGui.moduleSelected = 0;
        }
    }
    
    private static List<Module> getHacks(final Category category) {
        final List<Module> modules = new ArrayList<Module>();
        final ModuleManager moduleManager = FusionX.theClient.moduleManager;
        for (final Module m : ModuleManager.getModules()) {
            if (m.getCategory() == category) {
                modules.add(m);
            }
        }
        return modules;
    }
}
