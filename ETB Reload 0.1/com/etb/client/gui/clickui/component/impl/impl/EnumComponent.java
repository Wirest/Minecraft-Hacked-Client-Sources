package com.etb.client.gui.clickui.component.impl.impl;

import com.etb.client.gui.clickui.component.Component;
import com.etb.client.gui.clickui.component.impl.ModButton;
import com.etb.client.utils.MathUtils;
import com.etb.client.utils.MouseUtil;
import com.etb.client.utils.RenderUtil;
import com.etb.client.utils.font.Fonts;
import com.etb.client.utils.value.impl.EnumValue;
import com.etb.client.utils.value.impl.NumberValue;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;

public class EnumComponent extends Component {
    private EnumValue enumProperty;
    private ModButton parent;
    private boolean dragging;

    public EnumComponent(ModButton modButton, EnumValue enumProperty, int posX, int posY, int width, int height) {
        super(enumProperty.getLabel(), posX, posY, width, height);
        this.parent = modButton;
        this.enumProperty = enumProperty;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        int posX = parent.getPosX() + parent.parent.getPosX() + getPosX();
        int posY = parent.getPosY() + parent.getDefaultHeight() + parent.parent.getPosY() + parent.parent.getHeight() + getPosY() - parent.getValueY();
        boolean hovered = MouseUtil.mouseWithinBounds(mouseX, mouseY, posX, posY, posX + getWidth(), posY + getHeight());
        RenderUtil.drawRect(parent.parent.getPosX(), posY, parent.getWidth(), parent.getDefaultHeight() + 1, new Color(16, 16, 16).getRGB());
        RenderUtil.drawBorderedRect(posX, posY, getWidth(), getHeight(), 1, new Color(16, 16, 16).getRGB(), hovered ? new Color(31, 31, 31).darker().getRGB() : new Color(31, 31, 31).getRGB());
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(enumProperty.getLabel(), posX + 4, posY + 5, new Color(131, 131, 131).getRGB());
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(StringUtils.capitalize(enumProperty.getValue().toString().toLowerCase()), posX + getWidth() - 4 - Minecraft.getMinecraft().fontRendererObj.getStringWidth(StringUtils.capitalize(enumProperty.getValue().toString().toLowerCase())), posY + 5, new Color(131, 131, 131).getRGB());
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
            if (MouseUtil.mouseWithinBounds(mouseX, mouseY, posX, posY, posX + getWidth(), posY + getHeight())) {
                if (mouseButton == 0) {
                    enumProperty.increment();
                } else if (mouseButton == 1) {
                    enumProperty.decrement();
                }
            }
        }
    }

    @Override
    public void keyTyped(char character, int key) {
        super.keyTyped(character, key);
    }
}
