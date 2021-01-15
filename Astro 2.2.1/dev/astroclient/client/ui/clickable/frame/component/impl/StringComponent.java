package dev.astroclient.client.ui.clickable.frame.component.impl;

import dev.astroclient.client.Client;
import dev.astroclient.client.property.impl.StringProperty;
import dev.astroclient.client.ui.clickable.frame.component.Component;
import dev.astroclient.client.util.MouseUtil;
import dev.astroclient.client.util.render.Render2DUtil;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;

/**
 * made by Xen for Astro
 * at 12/19/2019
 **/
public class StringComponent extends Component {
    private StringProperty enumValue;
    private boolean extended;
    private int defaultHeight;

    private final int backgroundColor = new Color(47, 47, 47).getRGB();

    private final int darkerBgColor = new Color(47, 47, 47).darker().getRGB();


    public StringComponent(StringProperty enumValue, int parentX, int parentY, int offsetX, int offsetY, int width, int height) {
        super(enumValue.getName(), parentX, parentY, offsetX, offsetY, width, height);
        this.enumValue = enumValue;
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

        Client.INSTANCE.smallFontRenderer.drawStringWithShadow(getLabel(),
                (int) ((getFinishedX() + 2)),
                (int) ((getFinishedY() + 2f)), -1);
        Client.INSTANCE.smallFontRenderer.drawStringWithShadow("\2477" + enumValue.getValue().toUpperCase(),
                (int) ((getFinishedX() + getWidth() - Client.INSTANCE.smallFontRenderer.getStringWidth(enumValue.getValue().toUpperCase()) - 2)),
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
                if (hoveredEnum)
                    this.enumValue.increment();
                break;
            default:
                break;
        }
    }
}
