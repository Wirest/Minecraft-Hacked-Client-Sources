// 
// Decompiled by Procyon v0.5.30
// 

package cf.euphoria.euphorical.UI;

import java.util.Comparator;
import cf.euphoria.euphorical.Euphoria;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import cf.euphoria.euphorical.Mod.Mod;
import cf.euphoria.euphorical.Utils.RenderUtils;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import cf.euphoria.euphorical.Mod.Category;

public class TabGUI
{
    private static int baseCategoryWidth;
    private static int baseCategoryHeight;
    private static int baseModWidth;
    private static int baseModHeight;
    private static Section section;
    private static int categoryTab;
    private static int modTab;
    private static int categoryPosition;
    private static int categoryTargetPosition;
    private static int modPosition;
    private static int modTargetPosition;
    private static long lastUpdateTime;
    private static boolean transitionQuickly;
    private static float hue;
    
    static {
        TabGUI.section = Section.CATEGORY;
        TabGUI.categoryTab = 0;
        TabGUI.modTab = 0;
        TabGUI.categoryPosition = 22;
        TabGUI.categoryTargetPosition = 22;
        TabGUI.modPosition = 22;
        TabGUI.modTargetPosition = 22;
        TabGUI.hue = 0.0f;
    }
    
    public static void init() {
        int highestWidth = 0;
        Category[] values;
        for (int length = (values = Category.values()).length, i = 0; i < length; ++i) {
            final Category category = values[i];
            final String name = String.valueOf(Character.toUpperCase(category.name().charAt(0))) + category.name().substring(1);
            final int stringWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(name);
            highestWidth = Math.max(stringWidth, highestWidth);
        }
        TabGUI.baseCategoryWidth = highestWidth + 25;
        TabGUI.baseCategoryHeight = Category.values().length * 14 + 2;
    }
    
    public static void render(final int c) {
        updateBars();
        RenderUtils.drawBorderRect(1.0, 21.0, 2 + TabGUI.baseCategoryWidth, 21 + TabGUI.baseCategoryHeight, c, new Color(40, 40, 40, 150).getRGB(), 1);
        RenderUtils.drawRect(2, TabGUI.categoryPosition, 2 + TabGUI.baseCategoryWidth - 1, TabGUI.categoryPosition + 14, c);
        int yPos = 22;
        int yPosBottom = 29;
        for (int i = 0; i < Category.values().length; ++i) {
            final Category category = Category.values()[i];
            final String name = String.valueOf(Character.toUpperCase(category.name().toLowerCase().charAt(0))) + category.name().toLowerCase().substring(1);
            Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(name, TabGUI.baseCategoryWidth / 2 - Minecraft.getMinecraft().fontRendererObj.getStringWidth(name) / 2 + 3, yPos + 3, -1);
            yPos += 14;
            yPosBottom += 14;
        }
        if (TabGUI.section == Section.MODS) {
            RenderUtils.drawBorderRect(TabGUI.baseCategoryWidth + 4, TabGUI.categoryPosition - 1, TabGUI.baseCategoryWidth + 2 + TabGUI.baseModWidth, TabGUI.categoryPosition + getModsInCategory(Category.values()[TabGUI.categoryTab]).size() * 14 + 1, c, new Color(40, 40, 40, 150).getRGB(), 1);
            RenderUtils.drawRect(TabGUI.baseCategoryWidth + 5, TabGUI.modPosition, TabGUI.baseCategoryWidth + TabGUI.baseModWidth + 1, TabGUI.modPosition + 14, c);
            yPos = TabGUI.categoryPosition;
            yPosBottom = TabGUI.categoryPosition + 14;
            for (int i = 0; i < getModsInCategory(Category.values()[TabGUI.categoryTab]).size(); ++i) {
                final Mod mod = getModsInCategory(Category.values()[TabGUI.categoryTab]).get(i);
                final String name = mod.getModName();
                Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(name, TabGUI.baseCategoryWidth + TabGUI.baseModWidth / 2 - Minecraft.getMinecraft().fontRendererObj.getStringWidth(name) / 2 + 3, yPos + 3, mod.isEnabled() ? 10066329 : -1);
                yPos += 14;
                yPosBottom += 14;
            }
        }
    }
    
    public static void keyPress(final int key) {
        if (TabGUI.section == Section.CATEGORY) {
            switch (key) {
                case 205: {
                    int highestWidth = 0;
                    for (final Mod module : getModsInCategory(Category.values()[TabGUI.categoryTab])) {
                        final String name = String.valueOf(String.valueOf(String.valueOf(Character.toUpperCase(module.getModName().charAt(0))))) + module.getModName().substring(1);
                        final int stringWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(name);
                        highestWidth = Math.max(stringWidth, highestWidth);
                    }
                    TabGUI.baseModWidth = highestWidth + 25;
                    TabGUI.baseModHeight = getModsInCategory(Category.values()[TabGUI.categoryTab]).size() * 14 + 2;
                    TabGUI.modTargetPosition = (TabGUI.modPosition = TabGUI.categoryTargetPosition);
                    TabGUI.modTab = 0;
                    TabGUI.section = Section.MODS;
                    break;
                }
                case 28: {
                    int highestWidth = 0;
                    for (final Mod module : getModsInCategory(Category.values()[TabGUI.categoryTab])) {
                        final String name = String.valueOf(String.valueOf(String.valueOf(Character.toUpperCase(module.getModName().charAt(0))))) + module.getModName().substring(1);
                        final int stringWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(name);
                        highestWidth = Math.max(stringWidth, highestWidth);
                    }
                    TabGUI.baseModWidth = highestWidth + 25;
                    TabGUI.baseModHeight = getModsInCategory(Category.values()[TabGUI.categoryTab]).size() * 14 + 2;
                    TabGUI.modTargetPosition = (TabGUI.modPosition = TabGUI.categoryTargetPosition);
                    TabGUI.modTab = 0;
                    TabGUI.section = Section.MODS;
                    break;
                }
                case 208: {
                    ++TabGUI.categoryTab;
                    TabGUI.categoryTargetPosition += 14;
                    if (TabGUI.categoryTab > Category.values().length - 1) {
                        TabGUI.transitionQuickly = true;
                        TabGUI.categoryTargetPosition = 22;
                        TabGUI.categoryTab = 0;
                        break;
                    }
                    break;
                }
                case 200: {
                    --TabGUI.categoryTab;
                    TabGUI.categoryTargetPosition -= 14;
                    if (TabGUI.categoryTab < 0) {
                        TabGUI.transitionQuickly = true;
                        TabGUI.categoryTargetPosition = 22 + (Category.values().length - 1) * 14;
                        TabGUI.categoryTab = Category.values().length - 1;
                        break;
                    }
                    break;
                }
            }
        }
        else if (TabGUI.section == Section.MODS) {
            switch (key) {
                case 203: {
                    TabGUI.section = Section.CATEGORY;
                    break;
                }
                case 205: {
                    final Mod mod = getModsInCategory(Category.values()[TabGUI.categoryTab]).get(TabGUI.modTab);
                    mod.toggle();
                    break;
                }
                case 28: {
                    final Mod mod = getModsInCategory(Category.values()[TabGUI.categoryTab]).get(TabGUI.modTab);
                    mod.toggle();
                    break;
                }
                case 208: {
                    ++TabGUI.modTab;
                    TabGUI.modTargetPosition += 14;
                    if (TabGUI.modTab > getModsInCategory(Category.values()[TabGUI.categoryTab]).size() - 1) {
                        TabGUI.transitionQuickly = true;
                        TabGUI.modTargetPosition = TabGUI.categoryTargetPosition;
                        TabGUI.modTab = 0;
                        break;
                    }
                    break;
                }
                case 200: {
                    --TabGUI.modTab;
                    TabGUI.modTargetPosition -= 14;
                    if (TabGUI.modTab < 0) {
                        TabGUI.transitionQuickly = true;
                        TabGUI.modTargetPosition = TabGUI.categoryTargetPosition + (getModsInCategory(Category.values()[TabGUI.categoryTab]).size() - 1) * 14;
                        TabGUI.modTab = getModsInCategory(Category.values()[TabGUI.categoryTab]).size() - 1;
                        break;
                    }
                    break;
                }
            }
        }
    }
    
    private static void updateBars() {
        final long timeDifference = System.nanoTime() / 1000000L - TabGUI.lastUpdateTime;
        TabGUI.lastUpdateTime = System.nanoTime() / 1000000L;
        int increment = TabGUI.transitionQuickly ? 100 : 20;
        increment = Math.max(1, Math.round(increment * timeDifference / 100L));
        if (TabGUI.categoryPosition < TabGUI.categoryTargetPosition) {
            TabGUI.categoryPosition += increment;
            if (TabGUI.categoryPosition >= TabGUI.categoryTargetPosition) {
                TabGUI.categoryPosition = TabGUI.categoryTargetPosition;
                TabGUI.transitionQuickly = false;
            }
        }
        else if (TabGUI.categoryPosition > TabGUI.categoryTargetPosition) {
            TabGUI.categoryPosition -= increment;
            if (TabGUI.categoryPosition <= TabGUI.categoryTargetPosition) {
                TabGUI.categoryPosition = TabGUI.categoryTargetPosition;
                TabGUI.transitionQuickly = false;
            }
        }
        if (TabGUI.modPosition < TabGUI.modTargetPosition) {
            TabGUI.modPosition += increment;
            if (TabGUI.modPosition >= TabGUI.modTargetPosition) {
                TabGUI.modPosition = TabGUI.modTargetPosition;
                TabGUI.transitionQuickly = false;
            }
        }
        else if (TabGUI.modPosition > TabGUI.modTargetPosition) {
            TabGUI.modPosition -= increment;
            if (TabGUI.modPosition <= TabGUI.modTargetPosition) {
                TabGUI.modPosition = TabGUI.modTargetPosition;
                TabGUI.transitionQuickly = false;
            }
        }
    }
    
    private static List<Mod> getModsInCategory(final Category category) {
        final List<Mod> modList = new ArrayList<Mod>();
        for (final Mod mod : Euphoria.getEuphoria().theMods.getMods()) {
            if (mod.getCategory() == category) {
                modList.add(mod);
            }
        }
        modList.sort(new Comparator<Mod>() {
            @Override
            public int compare(final Mod o1, final Mod o2) {
                return o1.getModName().length() - o2.getModName().length();
            }
        });
        return modList;
    }
    
    private enum Section
    {
        CATEGORY("CATEGORY", 0), 
        MODS("MODS", 1);
        
        private Section(final String s, final int n) {
        }
    }
}
