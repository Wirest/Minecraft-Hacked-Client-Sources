package dev.astroclient.client.ui.clickable.frame.component.impl;

import dev.astroclient.client.Client;
import dev.astroclient.client.property.impl.MultiSelectableProperty;
import dev.astroclient.client.ui.clickable.frame.component.Component;
import dev.astroclient.client.util.MouseUtil;
import dev.astroclient.client.util.render.Render2DUtil;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;

/**
 * made by Xen for Astro
 * at 12/19/2019
 **/
public class StringMultiSelectable extends Component {
    private MultiSelectableProperty property;
    private boolean extended;
    private int defaultHeight;

    private final int backgroundColor = new Color(47, 47, 47).getRGB();

    private final int darkerBgColor = new Color(47, 47, 47).darker().getRGB();


    public StringMultiSelectable(MultiSelectableProperty property, int parentX, int parentY, int offsetX, int offsetY, int width, int height) {
        super(property.getName(), parentX, parentY, offsetX, offsetY, width, height);
        this.property = property;
        this.defaultHeight = height;
    }

    public void updatePosition(int posX, int posY) {
        setParentX(posX);
        setParentY(posY);
        setFinishedX(posX + getOffsetX());
        setFinishedY(posY + getOffsetY());
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks, ScaledResolution scaledResolution) {
        super.drawScreen(mouseX, mouseY, partialTicks, scaledResolution);
        Render2DUtil.drawRectWH(getFinishedX(), getFinishedY(), getWidth(), getHeight(), backgroundColor);
        Render2DUtil.drawRectWH(getFinishedX() + 4, getFinishedY() + 5, getWidth() - 8, (float) (getHeight() - 8.25), 0x00000000);

        Client.INSTANCE.smallFontRenderer.drawStringWithShadow(getLabel() + "...",
                (int) ((getFinishedX() + 2)),
                (int) ((getFinishedY() + 2f)), -1);
        if (getHeight() != defaultHeight) {
            setHeight(defaultHeight);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        switch (mouseButton) {
            case 0:
                boolean hoveredEnum = MouseUtil.mouseWithinBounds(mouseX, mouseY, getFinishedX() , getFinishedY(), getWidth() - 10, 10);
                break;
            default:
                break;
        }
    }
}
