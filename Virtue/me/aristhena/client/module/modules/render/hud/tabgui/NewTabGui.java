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

public class NewTabGui extends TabGui
{
    private static final int NO_COLOR = 0;
    private static final int INSIDE_COLOR = -1610612736;
    private static final int BORDER_COLOR = 2013265920;
    private static final int COMPONENT_HEIGHT = 18;
    private static final double BUFFER_HEIGHT = 1.0;
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
        NewTabGui.section = Section.CATEGORY;
        NewTabGui.categoryTab = 0;
        NewTabGui.modTab = 0;
        NewTabGui.categoryPosition = 15;
        NewTabGui.categoryTargetPosition = 15;
        NewTabGui.modPosition = 15;
        NewTabGui.modTargetPosition = 15;
    }
    
    public NewTabGui(final String name, final boolean value, final Module module) {
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
        NewTabGui.baseCategoryWidth = highestWidth + 5;
        NewTabGui.baseCategoryHeight = Module.Category.values().length * 18 + 2;
        return true;
    }
    
    @Override
    public boolean onRender(final Render2DEvent event) {
        if (super.onRender(event)) {
            final int baseYPosition = 14;
            updateBars();
            double yPosition = baseYPosition;
            for (int i = 0; i < Module.Category.values().length; ++i) {
                RenderUtils.rectangleBordered(2.0, yPosition, 2 + NewTabGui.baseCategoryWidth, yPosition + 18.0, 0.6000000238418579, (NewTabGui.categoryTab == i) ? -14935012 : -13421773, -16777216);
                yPosition += 19.0;
            }
            double yPos = 15.0;
            double yPosBottom = 33.0;
            for (int j = 0; j < Module.Category.values().length; ++j) {
                final Module.Category category = Module.Category.values()[j];
                final String name = StringUtils.capitalize(category.name().toLowerCase());
                ClientUtils.clientFont().drawStringWithShadow(name, NewTabGui.baseCategoryWidth / 2 - ClientUtils.clientFont().getStringWidth(name) / 2 + 2, yPos + 5.0, (NewTabGui.categoryTab == j) ? -1 : -6052957);
                yPos += 19.0;
                yPosBottom += 19.0;
            }
            if (NewTabGui.section == Section.MODS) {
                yPosition = baseYPosition;
                for (int i = 0; i < getModsInCategory(Module.Category.values()[NewTabGui.categoryTab]).size(); ++i) {
                    RenderUtils.rectangleBordered(NewTabGui.baseCategoryWidth + 4, yPosition, 2 + NewTabGui.baseCategoryWidth + NewTabGui.baseModWidth + 2, yPosition + 18.0, 0.6000000238418579, (NewTabGui.modTab == i) ? -14935012 : -13421773, -16777216);
                    yPosition += 19.0;
                }
                yPos = baseYPosition;
                yPosBottom = baseYPosition + 18;
                for (int j = 0; j < getModsInCategory(Module.Category.values()[NewTabGui.categoryTab]).size(); ++j) {
                    final Module mod = getModsInCategory(Module.Category.values()[NewTabGui.categoryTab]).get(j);
                    final String name = mod.getDisplayName();
                    ClientUtils.clientFont().drawStringWithShadow(name, NewTabGui.baseCategoryWidth + NewTabGui.baseModWidth / 2 - ClientUtils.clientFont().getStringWidth(name) / 2 + 4, yPos + 6.0, (NewTabGui.modTab == j) ? (mod.isEnabled() ? -1 : -3026479) : (mod.isEnabled() ? -2171170 : -6052957));
                    yPos += 19.0;
                    yPosBottom += 19.0;
                }
            }
        }
        return true;
    }
    
    @Override
    public boolean onKeypress(final KeyPressEvent event) {
        if (super.onKeypress(event)) {
            final int key = event.getKey();
            if (NewTabGui.section == Section.CATEGORY) {
                switch (key) {
                    case 205: {
                        int highestWidth = 0;
                        for (final Module module : getModsInCategory(Module.Category.values()[NewTabGui.categoryTab])) {
                            final String name = StringUtils.capitalize(module.getDisplayName().toLowerCase());
                            final int stringWidth = ClientUtils.clientFont().getStringWidth(name);
                            highestWidth = Math.max(stringWidth, highestWidth);
                        }
                        NewTabGui.baseModWidth = highestWidth + 6;
                        NewTabGui.baseModHeight = getModsInCategory(Module.Category.values()[NewTabGui.categoryTab]).size() * 18 + 2;
                        NewTabGui.modTargetPosition = (NewTabGui.modPosition = NewTabGui.categoryTargetPosition);
                        NewTabGui.modTab = 0;
                        NewTabGui.section = Section.MODS;
                        break;
                    }
                    case 208: {
                        ++NewTabGui.categoryTab;
                        NewTabGui.categoryTargetPosition += 18;
                        if (NewTabGui.categoryTab > Module.Category.values().length - 1) {
                            NewTabGui.transitionQuickly = true;
                            NewTabGui.categoryTargetPosition = 15;
                            NewTabGui.categoryTab = 0;
                            break;
                        }
                        break;
                    }
                    case 200: {
                        --NewTabGui.categoryTab;
                        NewTabGui.categoryTargetPosition -= 18;
                        if (NewTabGui.categoryTab < 0) {
                            NewTabGui.transitionQuickly = true;
                            NewTabGui.categoryTargetPosition = 15 + (Module.Category.values().length - 1) * 18;
                            NewTabGui.categoryTab = Module.Category.values().length - 1;
                            break;
                        }
                        break;
                    }
                }
            }
            else if (NewTabGui.section == Section.MODS) {
                switch (key) {
                    case 203: {
                        NewTabGui.section = Section.CATEGORY;
                        break;
                    }
                    case 205: {
                        final Module mod = getModsInCategory(Module.Category.values()[NewTabGui.categoryTab]).get(NewTabGui.modTab);
                        mod.toggle();
                        break;
                    }
                    case 208: {
                        ++NewTabGui.modTab;
                        NewTabGui.modTargetPosition += 18;
                        if (NewTabGui.modTab > getModsInCategory(Module.Category.values()[NewTabGui.categoryTab]).size() - 1) {
                            NewTabGui.transitionQuickly = true;
                            NewTabGui.modTargetPosition = NewTabGui.categoryTargetPosition;
                            NewTabGui.modTab = 0;
                            break;
                        }
                        break;
                    }
                    case 200: {
                        --NewTabGui.modTab;
                        NewTabGui.modTargetPosition -= 18;
                        if (NewTabGui.modTab < 0) {
                            NewTabGui.transitionQuickly = true;
                            NewTabGui.modTargetPosition = NewTabGui.categoryTargetPosition + (getModsInCategory(Module.Category.values()[NewTabGui.categoryTab]).size() - 1) * 18;
                            NewTabGui.modTab = getModsInCategory(Module.Category.values()[NewTabGui.categoryTab]).size() - 1;
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
        final long timeDifference = System.currentTimeMillis() - NewTabGui.lastUpdateTime;
        NewTabGui.lastUpdateTime = System.currentTimeMillis();
        int increment = NewTabGui.transitionQuickly ? 100 : 20;
        increment = Math.max(1, Math.round(increment * timeDifference / 100L));
        if (NewTabGui.categoryPosition < NewTabGui.categoryTargetPosition) {
            NewTabGui.categoryPosition += increment;
            if (NewTabGui.categoryPosition >= NewTabGui.categoryTargetPosition) {
                NewTabGui.categoryPosition = NewTabGui.categoryTargetPosition;
                NewTabGui.transitionQuickly = false;
            }
        }
        else if (NewTabGui.categoryPosition > NewTabGui.categoryTargetPosition) {
            NewTabGui.categoryPosition -= increment;
            if (NewTabGui.categoryPosition <= NewTabGui.categoryTargetPosition) {
                NewTabGui.categoryPosition = NewTabGui.categoryTargetPosition;
                NewTabGui.transitionQuickly = false;
            }
        }
        if (NewTabGui.modPosition < NewTabGui.modTargetPosition) {
            NewTabGui.modPosition += increment;
            if (NewTabGui.modPosition >= NewTabGui.modTargetPosition) {
                NewTabGui.modPosition = NewTabGui.modTargetPosition;
                NewTabGui.transitionQuickly = false;
            }
        }
        else if (NewTabGui.modPosition > NewTabGui.modTargetPosition) {
            NewTabGui.modPosition -= increment;
            if (NewTabGui.modPosition <= NewTabGui.modTargetPosition) {
                NewTabGui.modPosition = NewTabGui.modTargetPosition;
                NewTabGui.transitionQuickly = false;
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
