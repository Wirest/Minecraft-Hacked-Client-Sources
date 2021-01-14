package com.etb.client.gui.tabgui.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

/**
 * made by oHare for ETB Reloaded
 *
 * @since 6/28/2019
 **/
public class Component {
    private float x, y, width, height;
    private String label;
    public static Minecraft mc = Minecraft.getMinecraft();
    public Component(String label, float x, float y, float width, float height) {
        this.label = label;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    public void init() {

    }
    public void onRender(ScaledResolution sr) {
    }

    public void onKeyPress(int key) {
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
