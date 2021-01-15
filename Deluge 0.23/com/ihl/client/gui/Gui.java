package com.ihl.client.gui;

import com.ihl.client.Helper;
import com.ihl.client.comparator.StringLengthComparator;
import com.ihl.client.module.Category;
import com.ihl.client.module.Module;
import com.ihl.client.module.option.Option;
import com.ihl.client.util.ColorUtil;
import com.ihl.client.util.RenderUtil;
import com.ihl.client.util.RenderUtil2D;
import com.ihl.client.util.part.Settings;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Gui {

    public static Map<String, Component> components = new HashMap();

    public static void init() {
        components.put("ring", new RingCategory(Arrays.asList(Category.values())));
    }

    public static void tick() {
        for(Component component : components.values()) {
            component.tick();
        }
    }

    public static void keyPress(int k, char c) {
        for(Component component : components.values()) {
            component.keyPress(k, c);
        }
    }

    public static void keyRelease(int k, char c) {
        for(Component component : components.values()) {
            component.keyRelease(k, c);
        }
    }

    public static void mouseClicked(int button) {
        for(Component component : components.values()) {
            component.mouseClicked(button);
        }
    }

    public static void mouseReleased(int button) {
        for(Component component : components.values()) {
            component.mouseReleased(button);
        }
    }

    public static void changeRing(Ring ring) {
        components.put("ring", ring);
    }

    public static void render() {
        GlStateManager.pushMatrix();
        double scale = Helper.scaled().getScaleFactor() / Math.pow(Helper.scaled().getScaleFactor(), 2);
        GlStateManager.scale(scale, scale, scale);

        RenderUtil2D.rect(-1, -1, -1, -1, 0, 1, 0);

        for(Component component : components.values()) {
            component.render();
        }

        GlStateManager.scale(2, 2, 2);

        if (!Helper.mc().gameSettings.showDebugInfo) {
            boolean arrayList = Option.get(Settings.options, "arraylist").BOOLEAN();
            String arrayListColor = Option.get(Settings.options, "arraylist", "color").CHOICE();
            String arrayListSort = Option.get(Settings.options, "arraylist", "sort").CHOICE();

            if (arrayList) {
                List<String> list = Module.enabled();
                switch (arrayListSort) {
                    case "length":
                        Collections.sort(list, new StringLengthComparator(StringLengthComparator.Direction.LARGETOSMALL, Helper.mc().fontRendererObj));
                        break;
                    case "alphabetical":
                        Collections.sort(list);
                        break;
                }
                for (String key : list) {
                    Module module = Module.modules.get(key);
                    int index = list.indexOf(key);
                    int color = -1;
                    switch (arrayListColor) {
                        case "rainbow":
                            color = ColorUtil.rainbow(index * 200000000L, 1f).getRGB();
                            break;
                        case "categorical":
                            color = module.category.color;
                            break;
                    }
                    Helper.mc().fontRendererObj.drawStringWithShadow(module.getDisplay(), (Display.getWidth()/2)-2-Helper.mc().fontRendererObj.getStringWidth(module.getDisplay()), 2 + (index * Helper.mc().fontRendererObj.FONT_HEIGHT), color);
                }
            }
        }

        GlStateManager.popMatrix();
    }
    
    public static void dispose() {
    }
}
