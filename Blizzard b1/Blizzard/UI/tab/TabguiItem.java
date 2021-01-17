/*
 * Decompiled with CFR 0_122.
 */
package Blizzard.UI.tab;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;

import Blizzard.Mod.Mod;
import Blizzard.UI.tab.TabGui;
import Blizzard.Utils.R2DUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public final class TabguiItem {
    private final TabGui gui;
    private final ArrayList<GuiItem> Mods = new ArrayList();
    private int menuHeight = 0;
    private int menuWidth = 0;
    private String tabName;

    public TabguiItem(TabGui gui, String tabName) {
        this.gui = gui;
        this.tabName = tabName;
    }

    public void drawTabMenu(Minecraft mc2, int x2, int y2) {
        this.countMenuSize(mc2);
        int boxY = y2;
        R2DUtils.drawBorderedRect(x2, y2, x2 + this.menuWidth + 2, y2 + this.menuHeight, this.gui.getColourBody(), Integer.MIN_VALUE);
        int i2 = 0;
        while (i2 < this.Mods.size()) {
            if (this.gui.getSelectedItem() == i2) {
                int transitionTop = this.gui.getTransition() + (this.gui.getSelectedItem() == 0 && this.gui.getTransition() < 0 ? - this.gui.getTransition() : 0);
                int transitionBottom = this.gui.getTransition() + (this.gui.getSelectedItem() == this.Mods.size() - 1 && this.gui.getTransition() > 0 ? - this.gui.getTransition() : 0);
                R2DUtils.drawBorderedRect(x2, boxY + transitionTop, x2 + this.menuWidth + 2, boxY + 12 + transitionBottom, this.gui.getColourBox(), Integer.MIN_VALUE);
            }
            Collator collator = Collator.getInstance(Locale.US);
            this.Mods.sort((mod1, mod2) -> collator.compare(mod1.getMod().getName(), mod2.getMod().getName()));
            Minecraft.getMinecraft().fontRendererObj.drawString(String.valueOf(String.valueOf(String.valueOf(this.gui.getColourNormal()))) + this.Mods.get(i2).getMod().getName(), x2 + 2, y2 + this.gui.getTabHeight() * i2 - 1 + 2, -1);
            boxY += 12;
            ++i2;
        }
    }

    private void countMenuSize(Minecraft mc2) {
        int maxWidth = 0;
        for (GuiItem Mod2 : this.Mods) {
            if (Minecraft.getMinecraft().fontRendererObj.getStringWidth(Mod2.getMod().getName()) <= maxWidth) continue;
            maxWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(Mod2.getMod().getName()) + 4;
        }
        this.menuWidth = maxWidth;
        this.menuHeight = this.Mods.size() * this.gui.getTabHeight();
    }

    public String getTabName() {
        return this.tabName;
    }

    public ArrayList<GuiItem> getMods() {
        return this.Mods;
    }

    public static class GuiItem {
        private final Mod mod;

        public GuiItem(Mod mod) {
            this.mod = mod;
        }

        public Mod getMod() {
            return this.mod;
        }
    }

}

