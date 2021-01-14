package moonx.ohare.client.gui.clickgui.frames;

import moonx.ohare.client.gui.clickgui.components.Component;
import moonx.ohare.client.utils.MouseUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.util.ArrayList;

public class Frame {
    private final String label;
    private float posX, posY, lastX, lastY, width, height;
    private boolean extended, dragging;
    private ArrayList<Component> components = new ArrayList<>();

    public Frame(String label, float posX, float posY, float width, float height) {
        this.label = label;
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
    }

    public void init() {
        components.forEach(Component::init);
    }

    public void onFrameMoved(float posX, float posY) {
        this.posX = posX;
        this.posY = posY;
        components.forEach(component -> component.onFrameMoved(posX, posY));
    }

    public void onDraw(int mouseX, int mouseY, float partialTicks) {
        if (isDragging()) {
            setPosX(mouseX + getLastX());
            setPosY(mouseY + getLastY());
            onFrameMoved(getPosX(), getPosY());
        }
        if (getPosX() < 0) {
            setPosX(0);
            onFrameMoved(getPosX(), getPosY());
        }
        if (getPosX() + getWidth() > new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth()) {
            setPosX(new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth() - getWidth());
            onFrameMoved(getPosX(), getPosY());
        }
        if (getPosY() < 0) {
            setPosY(0);
            onFrameMoved(getPosX(), getPosY());
        }
        if (getPosY() + getHeight() > new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight()) {
            setPosY(new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight() - getHeight());
            onFrameMoved(getPosX(), getPosY());
        }
        if (isExtended())
            components.forEach(component -> component.onDraw(mouseX, mouseY, partialTicks));
    }

    public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
        final boolean hovered = MouseUtil.mouseWithinBounds(mouseX, mouseY, getPosX(), getPosY(), getWidth(), getHeight());
        switch (mouseButton) {
            case 0:
                if (hovered) {
                    setDragging(true);
                    setLastX(getPosX() - mouseX);
                    setLastY(getPosY() - mouseY);
                }
                break;
            case 1:
                if (hovered)
                    setExtended(!isExtended());
                break;
            default:
                break;
        }
        if (isExtended())
            components.forEach(component -> component.onMouseClicked(mouseX, mouseY, mouseButton));
    }

    public void onMouseReleased(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0) {
            if (isDragging()) {
                setDragging(false);
            }
        }
        if (isExtended())
            components.forEach(component -> component.onMouseReleased(mouseX, mouseY, mouseButton));
    }

    public void onKeyTyped(int key, char character) {
        if (isExtended())
            components.forEach(component -> component.onKeyTyped(character, key));
    }

    public float getLastX() {
        return lastX;
    }

    public void setLastX(float lastX) {
        this.lastX = lastX;
    }

    public float getLastY() {
        return lastY;
    }

    public void setLastY(float lastY) {
        this.lastY = lastY;
    }

    public String getLabel() {
        return label;
    }

    public float getPosX() {
        return posX;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public float getPosY() {
        return posY;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public boolean isExtended() {
        return extended;
    }

    public void setExtended(boolean extended) {
        this.extended = extended;
    }

    public boolean isDragging() {
        return dragging;
    }

    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    public ArrayList<Component> getComponents() {
        return components;
    }
}
