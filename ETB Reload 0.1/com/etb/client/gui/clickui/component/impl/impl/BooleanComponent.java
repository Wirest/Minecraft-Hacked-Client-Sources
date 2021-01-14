package com.etb.client.gui.clickui.component.impl.impl;

import com.etb.client.gui.clickui.component.Component;
import com.etb.client.gui.clickui.component.impl.ModButton;
import com.etb.client.utils.MouseUtil;
import com.etb.client.utils.RenderUtil;
import com.etb.client.utils.font.Fonts;
import com.etb.client.utils.value.impl.BooleanValue;
import net.minecraft.client.Minecraft;

import java.awt.*;

public class BooleanComponent extends Component {
    private BooleanValue booleanProperty;
    private ModButton parent;

    public BooleanComponent(ModButton modButton, BooleanValue booleanProperty, int posX, int posY, int width, int height) {
        super(booleanProperty.getLabel(), posX, posY, width, height);
        this.parent = modButton;
        this.booleanProperty = booleanProperty;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        int posX = parent.getPosX() + parent.parent.getPosX() + getPosX();
        int posY = parent.getPosY() + parent.getDefaultHeight() + parent.parent.getPosY() + parent.parent.getHeight() + getPosY() - parent.getValueY();

        boolean hovered = MouseUtil.mouseWithinBounds(mouseX, mouseY, posX, posY, posX + getWidth(), posY + getHeight());
        RenderUtil.drawRect(parent.parent.getPosX(), posY, parent.getWidth(), parent.getDefaultHeight() + 1, new Color(16, 16, 16).getRGB());
        RenderUtil.drawBorderedRect(posX, posY, getWidth(), getHeight(), 1, new Color(16, 16, 16).getRGB(), hovered ? new Color(31, 31, 31).darker().getRGB() : new Color(31, 31, 31).getRGB());
        RenderUtil.drawRect(posX + 1, posY + 1, 14, 14, booleanProperty.isEnabled() ? new Color(76, 153, 123).getRGB() : new Color(0xff4d4c).getRGB());
        RenderUtil.drawRect(posX + 14, posY + 14, getWidth() - 15, 1, booleanProperty.isEnabled() ? new Color(76, 153, 123).getRGB() : new Color(0xff4d4c).getRGB());
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(booleanProperty.getLabel(), posX + 18, posY + 4, new Color(131, 131, 131).getRGB());
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
                    booleanProperty.setValue(!booleanProperty.getValue());
                }
            }
        }
    }
}
