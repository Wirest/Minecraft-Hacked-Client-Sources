package com.etb.client.gui.clickui.frame;

import com.etb.client.gui.clickui.component.Component;
import com.etb.client.utils.MouseUtil;
import com.etb.client.utils.RenderUtil;
import com.etb.client.utils.font.Fonts;
import net.minecraft.client.Minecraft;

import java.awt.*;
import java.util.ArrayList;

public abstract class Frame {
    private String name;
    private int posX, posY, lastPosX, lastPosY, width, height;
    private boolean dragging, extended;

    public ArrayList<Component> components = new ArrayList<>();

    public Frame(String name, int posX, int posY, int width, int height) {
        this.name = name;
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;

        init();
    }

    public abstract void init();

    public void keyTyped(char character, int key) {
        components.forEach(component -> component.keyTyped(character, key));
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (dragging) {
            this.posX = mouseX + this.lastPosX;
            this.posY = mouseY + this.lastPosY;
        }
        boolean hovered = MouseUtil.mouseWithinBounds(mouseX, mouseY, posX, posY, posX + width, posY + height);
        RenderUtil.drawRect(posX, posY,  width,  height,new Color(16,16,16).getRGB());
        Minecraft.getMinecraft().fontRendererObj.drawString(name, posX + 4, posY + 5, new Color(79,79,79,255).getRGB());
        if (extended) {
            int addition = 0;
            for (Component element : getComponents()) {
                element.setPosY(addition);
                addition += element.getHeight();
            }
            components.forEach(component -> component.drawScreen(mouseX, mouseY, partialTicks));
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (MouseUtil.mouseWithinBounds(mouseX, mouseY, posX, posY, posX + width, posY + height)) {
            if (mouseButton == 0) {
                dragging = true;
                this.lastPosX = (posX - mouseX);
                this.lastPosY = (posY - mouseY);
            } else if (mouseButton == 1) {
                extended = !extended;
            }
        }
        if (extended) {
            components.forEach(component -> component.mouseClicked(mouseX, mouseY, mouseButton));
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        dragging = false;

        if (extended) {
            components.forEach(component -> component.mouseReleased(mouseX, mouseY, mouseButton));
        }
    }

    public int getPosY() {
        return posY;
    }

    public int getPosX() {
        return posX;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public ArrayList<Component> getComponents() {
        return components;
    }
}
