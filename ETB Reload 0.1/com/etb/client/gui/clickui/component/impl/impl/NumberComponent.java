package com.etb.client.gui.clickui.component.impl.impl;

import com.etb.client.gui.clickui.component.Component;
import com.etb.client.gui.clickui.component.impl.ModButton;
import com.etb.client.utils.MathUtils;
import com.etb.client.utils.MouseUtil;
import com.etb.client.utils.RenderUtil;
import com.etb.client.utils.font.Fonts;
import com.etb.client.utils.value.impl.NumberValue;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class NumberComponent extends Component {
    private NumberValue numberProperty;
    private ModButton parent;
    private boolean dragging;

    public NumberComponent(ModButton modButton, NumberValue numberProperty, int posX, int posY, int width, int height) {
        super(numberProperty.getLabel(), posX, posY, width, height);
        this.parent = modButton;
        this.numberProperty = numberProperty;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        int posX = parent.getPosX() + parent.parent.getPosX() + getPosX();
        int posY = parent.getPosY() + parent.getDefaultHeight() + parent.parent.getPosY() + parent.parent.getHeight() + getPosY() - parent.getValueY();
        float length = MathHelper.floor_double(((numberProperty.getValue()).floatValue() - numberProperty.getMinimum().floatValue()) / (numberProperty.getMaximum().floatValue() - numberProperty.getMinimum().floatValue()) * (getWidth() - 8));
        boolean hovered = MouseUtil.mouseWithinBounds(mouseX, mouseY, posX, posY, posX + getWidth(), posY + getHeight());
        RenderUtil.drawRect(parent.parent.getPosX(), posY, parent.getWidth(), parent.getDefaultHeight() + 1, new Color(16, 16, 16).getRGB());
        RenderUtil.drawBorderedRect(posX, posY, getWidth(), getHeight(), 1, new Color(16, 16, 16).getRGB(), hovered ? new Color(31, 31, 31).darker().getRGB() : new Color(31, 31, 31).getRGB());
        RenderUtil.drawRect(posX + length + 1, posY + 1, 6, getHeight() - 2, new Color(76, 153, 123).getRGB());
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(numberProperty.getLabel(), posX + 4, posY + 5, new Color(131, 131, 131).getRGB());
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(numberProperty.getValue().toString(), posX + getWidth() - 4 - Minecraft.getMinecraft().fontRendererObj.getStringWidth(numberProperty.getValue().toString()), posY + 5, new Color(131, 131, 131).getRGB());

        if (dragging) {
            if (numberProperty.getValue() instanceof Float) {
                float preval = ((mouseX - posX) * (numberProperty.getMaximum().floatValue() - numberProperty.getMinimum().floatValue()) / getWidth() + numberProperty.getMinimum().floatValue());
                numberProperty.setValue(MathUtils.roundFloat(preval, 2));
            } else if (numberProperty.getValue() instanceof Integer) {
                int preval = ((mouseX - posX) * (numberProperty.getMaximum().intValue() - numberProperty.getMinimum().intValue()) / getWidth() + numberProperty.getMinimum().intValue());
                numberProperty.setValue(preval);
            } else if (numberProperty.getValue() instanceof Double) {
                double preval = ((mouseX - posX) * (numberProperty.getMaximum().doubleValue() - numberProperty.getMinimum().doubleValue()) / getWidth() + numberProperty.getMinimum().doubleValue());
                numberProperty.setValue(MathUtils.roundDouble(preval, 2));
            } else if (numberProperty.getValue() instanceof Long) {
                long preval = ((mouseX - posX) * (numberProperty.getMaximum().longValue() - numberProperty.getMinimum().longValue()) / getWidth() + numberProperty.getMinimum().longValue());
                numberProperty.setValue(MathUtils.roundDouble(preval, 2));
            }
        }
    }


    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && dragging) {
            dragging = false;
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        int parentposY = parent.parent.getPosY() + parent.parent.getHeight() + parent.getPosY();
        int posX = parent.getPosX() + parent.parent.getPosX() + getPosX();
        int posY = parent.getPosY() + parent.getDefaultHeight() + parent.parent.getPosY() + parent.parent.getHeight() + getPosY() - parent.getValueY();
        if (MouseUtil.mouseWithinBounds(mouseX, mouseY, posX, parentposY + 16, posX + getWidth(), parentposY + 116)) {
            if (mouseButton == 0 && MouseUtil.mouseWithinBounds(mouseX, mouseY, posX, posY, posX + getWidth(), posY + getHeight())) {
                dragging = true;
            }
        }
    }

    @Override
    public void keyTyped(char character, int key) {
        super.keyTyped(character, key);
    }
}
