package dev.astroclient.client.ui.clickable.frame;

import dev.astroclient.client.Client;
import dev.astroclient.client.util.MouseUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

import java.awt.*;

/**
 * made by Xen for Astro
 * at 12/19/2019
 **/
public abstract class Frame {
    private int posX, posY, lastPosX, lastPosY, width, height;
    private String label;
    private boolean extended, dragging, pinnable, pinned,visible;
    public static Minecraft mc = Minecraft.getMinecraft();

    public Frame(String label, int posX, int posY, int width, int height, boolean pinnable) {
        this.label = label;
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.pinnable = pinnable;
    }

    public abstract void init();
    public abstract void close();
    public abstract void updatePosition(int posX, int posY);

    public Color getColor() {
        return Client.INSTANCE.featureManager.clickGUI.guiColor.getColor();
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks, ScaledResolution scaledResolution) {
        if(isVisible()) {
            if (isDragging()) {
                setPosX(mouseX + getLastPosX());
                setPosY(mouseY + getLastPosY());
                if (getPosX() < 0) setPosX(0);
                if (getPosX() + getWidth() > scaledResolution.getScaledWidth())
                    setPosX(scaledResolution.getScaledWidth() - getWidth());
                if (getPosY() < 0) setPosY(0);
                if (getPosY() + getHeight() > scaledResolution.getScaledHeight())
                    setPosY(scaledResolution.getScaledHeight() - getHeight());
                updatePosition(getPosX(), getPosY());
            }
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        final boolean hovered = MouseUtil.mouseWithinBounds(mouseX, mouseY, getPosX(), getPosY(), getWidth(), getHeight());
        if(isVisible()) {
            switch (mouseButton) {
                case 0:
                    if (hovered) {
                        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                            if (isPinnable()) setPinned(!isPinned());
                        } else {
                            setDragging(true);
                            setLastPosX(getPosX() - mouseX);
                            setLastPosY(getPosY() - mouseY);
                        }
                    }
                    break;
                case 1:
                    if (hovered) {
                        setExtended(!isExtended());
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if(isVisible()) {
            switch (mouseButton) {
                case 0:
                    if (isDragging()) {
                        setDragging(false);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public void keyTyped(char typedChar, int keyCode) {

    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
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

    public boolean isExtended() {
        return extended;
    }

    public boolean isDragging() {
        return dragging;
    }

    public int getLastPosX() {
        return lastPosX;
    }

    public int getLastPosY() {
        return lastPosY;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public boolean isPinnable() {
        return pinnable;
    }

    public boolean isPinned() {
        return pinned;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }

    public void setExtended(boolean extended) {
        this.extended = extended;
    }

    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    public void setLastPosX(int lastPosX) {
        this.lastPosX = lastPosX;
    }

    public void setLastPosY(int lastPosY) {
        this.lastPosY = lastPosY;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

}
