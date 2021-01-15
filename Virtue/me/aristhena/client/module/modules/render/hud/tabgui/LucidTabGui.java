// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.module.modules.render.hud.tabgui;

import java.util.Comparator;
import me.aristhena.client.module.ModuleManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import me.aristhena.event.events.KeyPressEvent;
import me.aristhena.utils.RenderUtils;
import me.aristhena.event.events.Render2DEvent;
import me.aristhena.utils.ClientUtils;
import org.apache.commons.lang3.StringUtils;
import me.aristhena.client.module.Module;

public class LucidTabGui extends TabGui
{
    private static final int NO_COLOR = 0;
    private static final int INSIDE_COLOR = -1610612736;
    private static final int BORDER_COLOR = 2013265920;
    private static final int COMPONENT_HEIGHT = 14;
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
    private static boolean transitionQuickly;
    private static long lastUpdateTime;
    
    static {
        LucidTabGui.section = Section.CATEGORY;
        LucidTabGui.categoryTab = 0;
        LucidTabGui.modTab = 0;
        LucidTabGui.categoryPosition = 15;
        LucidTabGui.categoryTargetPosition = 15;
        LucidTabGui.modPosition = 15;
        LucidTabGui.modTargetPosition = 15;
    }
    
    public LucidTabGui(final String name, final boolean value, final Module module) {
        super(name, value, module);
    }
    
    @Override
    public boolean setupSizes() {
        int highestWidth = 0;
        Module.Category[] values;
        for (int length = (values = Module.Category.values()).length, i = 0; i < length; ++i) {
            final Module.Category category = values[i];
            final String name = StringUtils.capitalize(category.name().toLowerCase());
            final int stringWidth = ClientUtils.clientFont().getStringWidth(name);
            highestWidth = Math.max(stringWidth, highestWidth);
        }
        LucidTabGui.baseCategoryWidth = highestWidth + 6;
        LucidTabGui.baseCategoryHeight = Module.Category.values().length * 14 + 2;
        return true;
    }
    
    @Override
    public boolean onRender(final Render2DEvent event) {
        if (super.onRender(event)) {
            updateBars();
            RenderUtils.rectangle(2.0, 14.0, 2 + LucidTabGui.baseCategoryWidth, 14 + LucidTabGui.baseCategoryHeight, -1610612736);
            RenderUtils.rectangle(3.0, LucidTabGui.categoryPosition, 2 + LucidTabGui.baseCategoryWidth - 1, LucidTabGui.categoryPosition + 14, -1610612736);
            int yPos = 15;
            int yPosBottom = 29;
            for (int i = 0; i < Module.Category.values().length; ++i) {
                final Module.Category category = Module.Category.values()[i];
                final String name = StringUtils.capitalize(category.name().toLowerCase());
                ClientUtils.clientFont().drawStringWithShadow(name, LucidTabGui.baseCategoryWidth / 2 - ClientUtils.clientFont().getStringWidth(name) / 2 + 3, yPos + 3, (LucidTabGui.categoryTab == i) ? -1 : -6052957);
                yPos += 14;
                yPosBottom += 14;
            }
            if (LucidTabGui.section == Section.MODS) {
                RenderUtils.rectangle(LucidTabGui.baseCategoryWidth + 4, LucidTabGui.categoryPosition - 1, LucidTabGui.baseCategoryWidth + 2 + LucidTabGui.baseModWidth, LucidTabGui.categoryPosition + getModsInCategory(Module.Category.values()[LucidTabGui.categoryTab]).size() * 14 + 1, -1610612736);
                RenderUtils.rectangle(LucidTabGui.baseCategoryWidth + 5, LucidTabGui.modPosition, LucidTabGui.baseCategoryWidth + LucidTabGui.baseModWidth + 1, LucidTabGui.modPosition + 14, -1610612736);
                yPos = LucidTabGui.categoryPosition;
                yPosBottom = LucidTabGui.categoryPosition + 14;
                for (int i = 0; i < getModsInCategory(Module.Category.values()[LucidTabGui.categoryTab]).size(); ++i) {
                    final Module mod = getModsInCategory(Module.Category.values()[LucidTabGui.categoryTab]).get(i);
                    final String name = mod.getDisplayName();
                    ClientUtils.clientFont().drawStringWithShadow(name, LucidTabGui.baseCategoryWidth + LucidTabGui.baseModWidth / 2 - ClientUtils.clientFont().getStringWidth(name) / 2 + 3, yPos + 3, (LucidTabGui.modTab == i) ? (mod.isEnabled() ? -1 : -3026479) : (mod.isEnabled() ? -2171170 : -6052957));
                    yPos += 14;
                    yPosBottom += 14;
                }
            }
        }
        return true;
    }
    
    @Override
    public boolean onKeypress(final KeyPressEvent event) {
        if (super.onKeypress(event)) {
            final int key = event.getKey();
            if (LucidTabGui.section == Section.CATEGORY) {
                switch (key) {
                    case 205: {
                        int highestWidth = 0;
                        for (final Module module : getModsInCategory(Module.Category.values()[LucidTabGui.categoryTab])) {
                            final String name = StringUtils.capitalize(module.getDisplayName().toLowerCase());
                            final int stringWidth = ClientUtils.clientFont().getStringWidth(name);
                            highestWidth = Math.max(stringWidth, highestWidth);
                        }
                        LucidTabGui.baseModWidth = highestWidth + 6;
                        LucidTabGui.baseModHeight = getModsInCategory(Module.Category.values()[LucidTabGui.categoryTab]).size() * 14 + 2;
                        LucidTabGui.modTargetPosition = (LucidTabGui.modPosition = LucidTabGui.categoryTargetPosition);
                        LucidTabGui.modTab = 0;
                        LucidTabGui.section = Section.MODS;
                        break;
                    }
                    case 208: {
                        ++LucidTabGui.categoryTab;
                        LucidTabGui.categoryTargetPosition += 14;
                        if (LucidTabGui.categoryTab > Module.Category.values().length - 1) {
                            LucidTabGui.transitionQuickly = true;
                            LucidTabGui.categoryTargetPosition = 15;
                            LucidTabGui.categoryTab = 0;
                            break;
                        }
                        break;
                    }
                    case 200: {
                        --LucidTabGui.categoryTab;
                        LucidTabGui.categoryTargetPosition -= 14;
                        if (LucidTabGui.categoryTab < 0) {
                            LucidTabGui.transitionQuickly = true;
                            LucidTabGui.categoryTargetPosition = 15 + (Module.Category.values().length - 1) * 14;
                            LucidTabGui.categoryTab = Module.Category.values().length - 1;
                            break;
                        }
                        break;
                    }
                }
            }
            else if (LucidTabGui.section == Section.MODS) {
                switch (key) {
                    case 203: {
                        LucidTabGui.section = Section.CATEGORY;
                        break;
                    }
                    case 205: {
                        final Module mod = getModsInCategory(Module.Category.values()[LucidTabGui.categoryTab]).get(LucidTabGui.modTab);
                        mod.toggle();
                        break;
                    }
                    case 208: {
                        ++LucidTabGui.modTab;
                        LucidTabGui.modTargetPosition += 14;
                        if (LucidTabGui.modTab > getModsInCategory(Module.Category.values()[LucidTabGui.categoryTab]).size() - 1) {
                            LucidTabGui.transitionQuickly = true;
                            LucidTabGui.modTargetPosition = LucidTabGui.categoryTargetPosition;
                            LucidTabGui.modTab = 0;
                            break;
                        }
                        break;
                    }
                    case 200: {
                        --LucidTabGui.modTab;
                        LucidTabGui.modTargetPosition -= 14;
                        if (LucidTabGui.modTab < 0) {
                            LucidTabGui.transitionQuickly = true;
                            LucidTabGui.modTargetPosition = LucidTabGui.categoryTargetPosition + (getModsInCategory(Module.Category.values()[LucidTabGui.categoryTab]).size() - 1) * 14;
                            LucidTabGui.modTab = getModsInCategory(Module.Category.values()[LucidTabGui.categoryTab]).size() - 1;
                            break;
                        }
                        break;
                    }
                }
            }
        }
        return true;
    }
    
    private static void updateBars() {
        final long timeDifference = System.currentTimeMillis() - LucidTabGui.lastUpdateTime;
        LucidTabGui.lastUpdateTime = System.currentTimeMillis();
        int increment = LucidTabGui.transitionQuickly ? 100 : 20;
        increment = Math.max(1, Math.round(increment * timeDifference / 100L));
        if (LucidTabGui.categoryPosition < LucidTabGui.categoryTargetPosition) {
            LucidTabGui.categoryPosition += increment;
            if (LucidTabGui.categoryPosition >= LucidTabGui.categoryTargetPosition) {
                LucidTabGui.categoryPosition = LucidTabGui.categoryTargetPosition;
                LucidTabGui.transitionQuickly = false;
            }
        }
        else if (LucidTabGui.categoryPosition > LucidTabGui.categoryTargetPosition) {
            LucidTabGui.categoryPosition -= increment;
            if (LucidTabGui.categoryPosition <= LucidTabGui.categoryTargetPosition) {
                LucidTabGui.categoryPosition = LucidTabGui.categoryTargetPosition;
                LucidTabGui.transitionQuickly = false;
            }
        }
        if (LucidTabGui.modPosition < LucidTabGui.modTargetPosition) {
            LucidTabGui.modPosition += increment;
            if (LucidTabGui.modPosition >= LucidTabGui.modTargetPosition) {
                LucidTabGui.modPosition = LucidTabGui.modTargetPosition;
                LucidTabGui.transitionQuickly = false;
            }
        }
        else if (LucidTabGui.modPosition > LucidTabGui.modTargetPosition) {
            LucidTabGui.modPosition -= increment;
            if (LucidTabGui.modPosition <= LucidTabGui.modTargetPosition) {
                LucidTabGui.modPosition = LucidTabGui.modTargetPosition;
                LucidTabGui.transitionQuickly = false;
            }
        }
    }
    
    private static List<Module> getModsInCategory(final Module.Category category) {
        final List<Module> modList = new ArrayList<Module>();
        for (final Module mod : ModuleManager.getModules()) {
            if (mod.getCategory() == category) {
                modList.add(mod);
            }
        }
        modList.sort(new Comparator<Module>() {
            @Override
            public int compare(final Module m1, final Module m2) {
                final String s1 = m1.getDisplayName();
                final String s2 = m2.getDisplayName();
                return s1.compareTo(s2);
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
