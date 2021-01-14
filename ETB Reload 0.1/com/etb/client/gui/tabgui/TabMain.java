package com.etb.client.gui.tabgui;

import com.etb.client.Client;
import com.etb.client.gui.tabgui.components.Component;
import com.etb.client.gui.tabgui.components.impl.CategoryComponent;
import com.etb.client.module.Module;
import com.etb.client.utils.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;

/**
 * made by oHare for ETB Reloaded
 *
 * @since 6/28/2019
 **/
public class TabMain {
    public static Minecraft mc = Minecraft.getMinecraft();
    private float x, y;
    private ArrayList<Component> components = new ArrayList<>();
    private ArrayList<Module.Category> categories = new ArrayList();
    private Module.Category selectedCategory = Module.Category.COMBAT;
    private float largestString;
    private boolean extended, extendedvalue;

    public TabMain(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void init() {
        GL11.glColor4f(1,1,1,1);
        float categoryY = getY() + 1;
        for (Module.Category category : Module.Category.values()) {
            categories.add(category);
        }
        largestString = mc.fontRendererObj.getStringWidth(StringUtils.capitalize(categories.get(0).name().toLowerCase()));
        for (int i = 0; i < categories.size(); i++) {
            if (mc.fontRendererObj.getStringWidth(StringUtils.capitalize(categories.get(i).name().toLowerCase())) > largestString) {
                largestString = mc.fontRendererObj.getStringWidth(StringUtils.capitalize(categories.get(i).name().toLowerCase()));
            }
        }
        for (Module.Category category : categories) {
            components.add(new CategoryComponent(this, category, StringUtils.capitalize(category.name().toLowerCase()), getX() + 1, categoryY, largestString + 18, 12));
            categoryY += 12;
        }
        components.forEach(component -> component.init());
    }

    public void onRender(ScaledResolution sr) {
        RenderUtil.drawBorderedRect(x, y, largestString + 20, (Module.Category.values().length * 12) + 2, 1, new Color(0, 0, 0, 130).getRGB(), new Color(0, 0, 0, 180).getRGB());
        components.forEach(component -> component.onRender(sr));
    }

    public void onKeypress(int key) {
        switch (key) {
            case Keyboard.KEY_LEFT:
                if (extended && !extendedvalue) {
                    extended = false;
                }
                break;
        }
        components.forEach(component -> component.onKeyPress(key));
        switch (key) {
            case Keyboard.KEY_DOWN:
                if (!extended) {
                    if (categories.indexOf(selectedCategory) + 1 >= categories.size()) {
                        selectedCategory = categories.get(0);
                        return;
                    }
                    selectedCategory = categories.get(categories.indexOf(selectedCategory) + 1);
                }
                break;
            case Keyboard.KEY_UP:
                if (!extended) {
                    if (categories.indexOf(selectedCategory) <= 0) {
                        selectedCategory = categories.get(categories.size() - 1);
                        return;
                    }
                    selectedCategory = categories.get(categories.indexOf(selectedCategory) - 1);
                }
                break;
            case Keyboard.KEY_RIGHT:
                if (!extended && !extendedvalue& !extendedvalue) {
                    extended = true;
                }
                break;
        }
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Module.Category getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(Module.Category selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    public void setExtended(boolean extended) {
        this.extended = extended;
    }

    public void setExtendedvalue(boolean extendedvalue) {
        this.extendedvalue = extendedvalue;
    }

    public boolean isExtended() {
        return extended;
    }
    public boolean isExtendedValue() {
        return extendedvalue;
    }
}
