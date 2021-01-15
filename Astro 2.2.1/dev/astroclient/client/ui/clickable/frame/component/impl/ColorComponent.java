package dev.astroclient.client.ui.clickable.frame.component.impl;

import dev.astroclient.client.Client;
import dev.astroclient.client.property.impl.ColorProperty;
import dev.astroclient.client.property.impl.number.NumberProperty;
import dev.astroclient.client.ui.clickable.frame.component.Component;
import dev.astroclient.client.util.ChatUtil;
import dev.astroclient.client.util.MouseUtil;
import dev.astroclient.client.util.render.Render2DUtil;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;

public class ColorComponent extends Component {

    private ColorProperty colorProperty;

    private boolean extended, sliding;

    private final int backgroundColor = new Color(47, 47, 47).getRGB();

    private final Color lightBgColor = new Color(87, 87, 87);

    public ColorComponent(ColorProperty colorProperty, int parentX, int parentY, int offsetX, int offsetY, int width, int height) {
        super(colorProperty.getName(), parentX, parentY, offsetX, offsetY, width, height);
        this.colorProperty = colorProperty;
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
        super.drawScreen(mouseX, mouseY, partialTicks, scaledResolution);
        Render2DUtil.drawRectWH(getFinishedX(), getFinishedY(), getWidth(), getHeight(), backgroundColor);
        Client.INSTANCE.smallFontRenderer.drawStringWithShadow(getLabel(), getFinishedX() + 2, (getFinishedY() + 2), -1);
        Render2DUtil.drawRectWH(getFinishedX() + getWidth() - 19, getFinishedY() + 2, 14, 7, colorProperty.getColor().getRGB());

        if (extended) {
            for (float i = 0; i < 44f; i += (43.5f / 50)) {
                Render2DUtil.drawRect(getFinishedX() + i + (60F / 50), getFinishedY() + 11, getFinishedX() + 1 + i + (60F / 50), getFinishedY() + 20, Color.getHSBColor(i / 50, 1, 1).getRGB());
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        /*final boolean hovered = MouseUtil.mouseWithinBounds(mouseX, mouseY, getFinishedX() + 2, getFinishedY() + 11, getFinishedX() + 50, getFinishedY() + 20);
        if (MouseUtil.mouseWithinBounds(mouseX, mouseY, getFinishedX() + getWidth() - 19, getFinishedY() + 1, 10, 10)) {
            if (extended) {
                if (hovered) sliding = true;
                this.setHeight(this.getHeight() - 40);
                extended = false;
            } else {
                extended = true;
                this.setHeight(this.getHeight() + 40);
            }
        }

         */

    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
        if (mouseButton == 0)
            sliding = false;
    }
}