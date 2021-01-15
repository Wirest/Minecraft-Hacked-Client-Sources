package dev.astroclient.client.ui.clickable.frame.component.impl;

import dev.astroclient.client.Client;
import dev.astroclient.client.property.impl.BooleanProperty;
import dev.astroclient.client.ui.clickable.frame.component.Component;
import dev.astroclient.client.util.MouseUtil;
import dev.astroclient.client.util.render.Render2DUtil;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;

/**
 * made by Xen for Astro
 * at 12/19/2019
 **/
public class BooleanComponent extends Component {
    private BooleanProperty booleanValue;

    private final int backgroundColor = new Color(47, 47, 47).getRGB();

    private final Color lightBgColor = new Color(87, 87, 87);

    public BooleanComponent(BooleanProperty booleanValue, int parentX, int parentY, int offsetX, int offsetY, int width, int height) {
        super(booleanValue.getName(), parentX, parentY, offsetX, offsetY, width, height);
        this.booleanValue = booleanValue;
    }

    @Override
    public void updatePosition(int posX, int posY) {
        setParentX(posX);
        setParentY(posY);
        setFinishedX(posX + getOffsetX());
        setFinishedY(posY + getOffsetY());
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks, ScaledResolution scaledResolution) {
        final boolean hovered = MouseUtil.mouseWithinBounds(mouseX, mouseY, getFinishedX() + getWidth() - 12, getFinishedY() + 1, 10, 10);
        Render2DUtil.drawRectWH(getFinishedX(), getFinishedY(), getWidth(), getHeight(), backgroundColor);
        Client.INSTANCE.smallFontRenderer.drawStringWithShadow(getLabel(), getFinishedX() + 2, (getFinishedY() + getHeight() / 2 -Client.INSTANCE.smallFontRenderer.getHeight() / 2), -1);
        Render2DUtil.drawRectWH(getFinishedX() + getWidth() - 19, getFinishedY() + 2, 14, 5, new Color(27, 27, 27).getRGB());
        Render2DUtil.drawRectWH(getFinishedX() + getWidth() - (booleanValue.getValue() ? 12 : 19), getFinishedY() + 1, 7, 7, booleanValue.getValue() ? (hovered ? getColor().darker().getRGB() : getColor().getRGB()) : lightBgColor.brighter().getRGB());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        final boolean hovered = MouseUtil.mouseWithinBounds(mouseX, mouseY, getFinishedX() + getWidth() - 19, getFinishedY() + 1, 17, 10);
        switch (mouseButton) {
            case 0:
                if (hovered) booleanValue.setValue(!booleanValue.getValue());
                break;
            default:
                break;
        }
    }
}