/*
 * Decompiled with CFR 0_114.
 */
package me.aristhena.lucid.modules.render;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import me.aristhena.lucid.Lucid;
import me.aristhena.lucid.eventapi.EventTarget;
import me.aristhena.lucid.eventapi.events.KeyboardEvent;
import me.aristhena.lucid.eventapi.events.Render2DEvent;
import me.aristhena.lucid.management.module.Mod;
import me.aristhena.lucid.management.module.Module;
import me.aristhena.lucid.management.module.ModuleManager;
import me.aristhena.lucid.management.option.Op;
import me.aristhena.lucid.management.value.Val;
import me.aristhena.lucid.ui.TabGui;
import me.aristhena.lucid.util.FontRenderer;

@Mod(shown = false, enabled = true)
public class HUD extends Module
{
    @Val(min = 10.0, max = 200.0, increment = 10.0)
    public static double transitionDelay;
    @Op
    private boolean hyphen;
    public static int color;
    
    static {
        HUD.transitionDelay = 50.0;
        HUD.color = -9764816;
    }
    
    public HUD() {
        this.hyphen = true;
    }
    
    @Override
    public void onEnable() {
        TabGui.init();
        super.onEnable();
    }
    
    @EventTarget
    private void onKeypress(final KeyboardEvent event) {
        TabGui.keyPress(event.key);
    }
    
    @EventTarget(4)
    private void onRender2D(final Render2DEvent event) {
        Lucid.font.drawStringWithShadow(Lucid.NAME, 3.0f, 3.0f, -1);
        Lucid.font.drawStringWithShadow(Lucid.VERSION + "", 2 + Lucid.font.getStringWidth(Lucid.NAME) + 4, 3.0f, HUD.color);
        int yPos = 2;
        for (final Module module : getSortedModuleArray()) {
            final String name = String.valueOf(module.name) + module.suffix;
            Lucid.font.drawStringWithShadow(name, event.width - Lucid.font.getStringWidth(name) - 2, yPos, module.color);
            yPos += 10;
        }
        TabGui.render();
    }
    
    private static List<Module> getSortedModuleArray() {
        final List<Module> list = new ArrayList<Module>();
        for (final Module mod : ModuleManager.moduleList) {
            if (mod.enabled) {
                if (!mod.shown) {
                    continue;
                }
                list.add(mod);
            }
        }
        list.sort(new Comparator<Module>() {
            @Override
            public int compare(final Module m1, final Module m2) {
                final String s1 = String.valueOf(m1.name) + ((m1.suffix == null) ? "" : m1.suffix);
                final String s2 = String.valueOf(m2.name) + ((m2.suffix == null) ? "" : m2.suffix);
                final int cmp = Lucid.font.getStringWidth(s2) - Lucid.font.getStringWidth(s1);
                return (cmp != 0) ? cmp : s2.compareTo(s1);
            }
        });
        return list;
    }
}
