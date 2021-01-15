package dev.astroclient.client.ui.menu.click.component.components.buttons;

import dev.astroclient.client.Client;
import dev.astroclient.client.property.impl.number.NumberProperty;
import dev.astroclient.client.ui.menu.click.component.Category;
import dev.astroclient.client.ui.menu.click.component.components.Button;
import dev.astroclient.client.ui.menu.click.component.components.GroupBox;
import dev.astroclient.client.util.MouseUtil;
import dev.astroclient.client.util.render.Render2DUtil;
import net.minecraft.util.MathHelper;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * made by oHare for Ayyware
 *
 * @since 4/10/2019
 **/
public class SliderButton extends Button {
    private NumberProperty value;
    private boolean sliding;

    public SliderButton(Category parent, float posX, float posY, float width, float height, NumberProperty value) {
        super(parent, value.getName(), posX, posY, width, height);
        this.value = value;
    }


    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        float posX = getParent().getParent().getPosX() + getParent().getPosX() + 50 + getPosX();
        float posY = getParent().getParent().getPosY() + getParent().getPosY() + 15 + getPosY() + ((GroupBox) getParent()).getScrollY();
        float length = MathHelper.floor_double(((value.getValue()).floatValue() - value.getMinimumValue().floatValue()) / (value.getMaximumValue().floatValue() - value.getMinimumValue().floatValue()) * 80);
        Client.INSTANCE.smallFontRenderer.drawStringWithOutline(getLabel(), posX + 8, posY + 4.5, new Color(210, 210, 210, Math.min(getParent().getParent().getParent().getAlpha(), 255)).getRGB());
        Render2DUtil.drawGradientRect(posX + 8.5f, posY + 11.5F, posX + 88.5f, posY + 14f, new Color(52, 52, 52, Math.min(getParent().getParent().getParent().getAlpha(), 255)).getRGB(), new Color(68, 68, 68, Math.min(getParent().getParent().getParent().getAlpha(), 255)).getRGB());
        Render2DUtil.drawGradientRect(posX + 8.5f, posY + 11.5F, posX + 8.5f + length, posY + 14f, new Color(Client.INSTANCE.featureManager.clickGUI.guiColor.getColor().getRed(), Client.INSTANCE.featureManager.clickGUI.guiColor.getColor().getGreen(), Client.INSTANCE.featureManager.clickGUI.guiColor.getColor().getBlue(), Math.min(getParent().getParent().getParent().getAlpha(), 255)).getRGB(), new Color(Client.INSTANCE.featureManager.clickGUI.secondaryGuiColor.getColor().getRed(), Client.INSTANCE.featureManager.clickGUI.secondaryGuiColor.getColor().getGreen(), Client.INSTANCE.featureManager.clickGUI.secondaryGuiColor.getColor().getBlue(), Math.min(getParent().getParent().getParent().getAlpha(), 255)).getRGB());
        Client.INSTANCE.smallBoldFontRenderer.drawStringWithOutline(value.getValue() + value.getSuffix(), posX + 88.5f - Client.INSTANCE.smallBoldFontRenderer.getStringWidth(value.getValue() + value.getSuffix()), posY + 4.5f, new Color(210, 210, 210, Math.min(getParent().getParent().getParent().getAlpha(), 255)).getRGB());
        if (sliding) {
            if (value.getValue() instanceof Double) {
                value.setValue(round(((mouseX - (posX + 8.5f)) * (value.getMaximumValue().doubleValue() - value.getMinimumValue().doubleValue()) / 80 + value.getMinimumValue().doubleValue())));
            } else if (value.getValue() instanceof Float) {
                value.setValue((float) round(((mouseX - (posX + 8.5f)) * (value.getMaximumValue().floatValue() - value.getMinimumValue().floatValue()) / 80 + value.getMinimumValue().floatValue())));
            } else if (value.getValue() instanceof Long) {
                value.setValue((long) round(((mouseX - (posX + 8.5f)) * (value.getMaximumValue().longValue() - value.getMinimumValue().longValue()) / 80 + value.getMinimumValue().longValue())));
            } else if (value.getValue() instanceof Integer) {
                value.setValue((int) round(((mouseX - (posX + 8.5f)) * (value.getMaximumValue().intValue() - value.getMinimumValue().intValue()) / 80 + value.getMinimumValue().intValue())));
            } else if (value.getValue() instanceof Short) {
                value.setValue((short) round(((mouseX - (posX + 8.5f)) * (value.getMaximumValue().shortValue() - value.getMinimumValue().shortValue()) / 80 + value.getMinimumValue().shortValue())));
            } else if (value.getValue() instanceof Byte) {
                value.setValue((byte) round(((mouseX - (posX + 8.5f)) * (value.getMaximumValue().byteValue() - value.getMinimumValue().byteValue()) / 80 + value.getMinimumValue().byteValue())));
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        float posX = getParent().getParent().getPosX() + getParent().getPosX() + 50 + getPosX();
        float posY = getParent().getParent().getPosY() + getParent().getPosY() + 15 + getPosY() + ((GroupBox) getParent()).getScrollY();
        if (MouseUtil.mouseWithinBounds(mouseX, mouseY, posX + 8, posY + 10, posX + 88, posY + 15) && mouseButton == 0) {
            sliding = true;
            getParent().getParent().dragging = false;
        }
    }


    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
        if (sliding && mouseButton == 0) sliding = false;
    }

    private double round(final double val) {
        final double v = Math.round(val / value.getIncrement().doubleValue()) * value.getIncrement().doubleValue();
        BigDecimal bd = new BigDecimal(v);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
