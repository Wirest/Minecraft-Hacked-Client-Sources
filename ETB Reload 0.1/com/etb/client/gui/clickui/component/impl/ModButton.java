package com.etb.client.gui.clickui.component.impl;

import com.etb.client.gui.clickui.component.Component;
import com.etb.client.gui.clickui.frame.Frame;
import com.etb.client.module.Module;
import com.etb.client.utils.MouseUtil;
import com.etb.client.utils.Printer;
import com.etb.client.utils.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;

public abstract class ModButton extends Component {
    private Module module;
    public Frame parent;
    private int defaultHeight;
    private boolean extend, binding;
    private int valueY;
    private ArrayList<Component> components = new ArrayList<>();

    public ModButton(Frame parent, Module module, int posX, int posY, int width, int height, int defaultHeight) {
        super(module.getLabel(), posX, posY, width, height);
        this.parent = parent;
        this.module = module;
        this.defaultHeight = defaultHeight;
        init();
        valueY = 0;
    }

    public abstract void init();

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        int posX = parent.getPosX() + getPosX();
        int posY = parent.getPosY() + parent.getHeight() + getPosY();
        boolean hovered = MouseUtil.mouseWithinBounds(mouseX, mouseY, posX + 3, posY, posX + getWidth() - 3, posY + defaultHeight);
        RenderUtil.drawRect(posX, posY, getWidth(), defaultHeight + 1, new Color(16, 16, 16).getRGB());
        RenderUtil.drawBorderedRect(posX + 2, posY, getWidth() - 4, defaultHeight, 1, new Color(16, 16, 16).getRGB(), hovered ? new Color(31, 31, 31).darker().getRGB() : new Color(31, 31, 31).getRGB());
        RenderUtil.drawBorderedRect(posX + 5, posY + 3, 12, 12, 0.5f, module.isEnabled() ? new Color(68, 145, 115).getRGB() : new Color(16, 16, 16).getRGB(), module.isEnabled() ? new Color(76, 153, 123).getRGB() : new Color(16, 16, 16).getRGB());
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(binding ? "Press any key to bind":module.getLabel(), posX + 20, posY + 5, new Color(131, 131, 131).getRGB());
        if (!module.getValues().isEmpty())
            Minecraft.getMinecraft().fontRendererObj.drawString("..", posX + getWidth() - 8, posY + 5, new Color(131, 131, 131).getRGB());
        if (extend) {
            setHeight(defaultHeight + 1 + (components.size() > 6 ? 6 : components.size()) * 16);
            if (components.size() > 6) {
                if (MouseUtil.mouseWithinBounds(mouseX, mouseY, posX, posY + 16, posX + getWidth(), posY + 116)) {
                    int wheel = Mouse.getDWheel();
                    if (Mouse.hasWheel()) {
                        if (wheel > 16) {
                            valueY -= 4;
                        } else if (wheel < 0) {
                            if (valueY >= (components.size() * 16) - 94) {
                                valueY = (components.size() * 16) - 94;
                            } else {
                                valueY += 4;
                            }
                        }
                        if (valueY < 0) {
                            valueY = 0;
                        }
                    }
                }
                GL11.glPushMatrix();
                GL11.glEnable(GL11.GL_SCISSOR_TEST);
                ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
                RenderUtil.prepareScissorBox(sr, posX, posY + 19, getWidth(), 98);
                getComponents().forEach(component -> component.drawScreen(mouseX, mouseY, partialTicks));
                GL11.glDisable(GL11.GL_SCISSOR_TEST);
                GL11.glPopMatrix();
            } else {
                getComponents().forEach(component -> component.drawScreen(mouseX, mouseY, partialTicks));
            }
        } else {
            setHeight(defaultHeight);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        int posX = parent.getPosX() + getPosX();
        int posY = parent.getPosY() + parent.getHeight() + getPosY();

        if (MouseUtil.mouseWithinBounds(mouseX, mouseY, posX + 3, posY, posX + getWidth() - 3, posY + defaultHeight)) {
            if (mouseButton == 0) {
                module.toggle();
            } else if (mouseButton == 1 && !module.getValues().isEmpty()) {
                extend = !extend;
            } else if (mouseButton == 2) {
                binding = !binding;
            }
        }
        if (extend) {
            components.forEach(component -> component.mouseClicked(mouseX, mouseY, mouseButton));
        }
    }

    @Override
    public void keyTyped(char character, int key) {
        if (extend) {
            components.forEach(component -> component.keyTyped(character, key));
        }
        if (binding) {
            if (key == Keyboard.KEY_SPACE || key == Keyboard.KEY_BACK){
                module.setKeybind(Keyboard.KEY_NONE);
            } else {
                module.setKeybind(key);
            }
            Printer.print("Bound " + module.getLabel() + " to " + Keyboard.getKeyName(module.getKeybind()) + "!");
            binding = false;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
        if (extend) {
            components.forEach(component -> component.mouseReleased(mouseX, mouseY, mouseButton));
        }
    }

    public int getDefaultHeight() {
        return this.defaultHeight;
    }

    public ArrayList<Component> getComponents() {
        return components;
    }

    public int getValueY() {
        return valueY;
    }

    public void setValueY(int valueY) {
        this.valueY = valueY;
    }
}
