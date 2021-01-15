package dev.astroclient.client.ui.clickable.frame.component;

import dev.astroclient.client.Client;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;

/**
 * made by Xen for Astro
 * at 12/19/2019
 **/
public class Component {
    private int parentX, parentY, offsetX, offsetY, finishedX, finishedY, width, height, defaultOffsetY;
    private String label;
    public static Minecraft mc = Minecraft.getMinecraft();

    public Component(String label, int parentX, int parentY, int offsetX, int offsetY, int width, int height) {
        this.label = label;
        this.parentX = parentX;
        this.parentY = parentY;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.finishedX = parentX + offsetX;
        this.finishedY = parentY + offsetY;
        this.width = width;
        this.height = height;
        this.defaultOffsetY = offsetY;
    }

    public Color getColor() {
        return Client.INSTANCE.featureManager.clickGUI.guiColor.getColor();
    }


    public void updatePosition(int posX, int posY) {
    }

    public void close() {
    }

    public void init() {
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks, ScaledResolution scaledResolution) {
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
    }

    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
    }

    public void keyTyped(char typedChar, int keyCode) {
    }

    public int getParentX() {
        return parentX;
    }

    public int getParentY() {
        return parentY;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public int getFinishedX() {
        return finishedX;
    }

    public int getFinishedY() {
        return finishedY;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getLabel() {
        return label;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setParentX(int parentX) {
        this.parentX = parentX;
    }

    public void setParentY(int parentY) {
        this.parentY = parentY;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    public void setFinishedX(int finishedX) {
        this.finishedX = finishedX;
    }

    public void setFinishedY(int finishedY) {
        this.finishedY = finishedY;
    }

    public int getDefaultOffsetY() {
        return defaultOffsetY;
    }
}