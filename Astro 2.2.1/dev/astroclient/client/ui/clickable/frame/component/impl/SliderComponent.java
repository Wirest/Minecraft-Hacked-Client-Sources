package dev.astroclient.client.ui.clickable.frame.component.impl;

import dev.astroclient.client.Client;
import dev.astroclient.client.property.impl.number.NumberProperty;
import dev.astroclient.client.ui.clickable.frame.component.Component;
import dev.astroclient.client.util.MouseUtil;
import dev.astroclient.client.util.render.Render2DUtil;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * made by Xen for Astro
 * at 12/19/2019
 **/
public class SliderComponent extends Component {
    private NumberProperty numberValue;
    private boolean sliding;

    private final int backgroundColor = new Color(47, 47, 47).getRGB();

    private final int lightBgColor = new Color(87, 87, 87).getRGB();


    public SliderComponent(NumberProperty numberValue, int parentX, int parentY, int offsetX, int offsetY, int width, int height) {
        super(numberValue.getName(), parentX, parentY, offsetX, offsetY, width, height);
        this.numberValue = numberValue;

    }

    public void updatePosition(int posX, int posY) {
        setParentX(posX);
        setParentY(posY);
        setFinishedX(posX + getOffsetX());
        setFinishedY(posY + getOffsetY());
    }

    @Override
    public void close() {
        sliding = false;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks, ScaledResolution scaledResolution) {
        super.drawScreen(mouseX, mouseY, partialTicks, scaledResolution);
        final boolean hovered = MouseUtil.mouseWithinBounds(mouseX, mouseY, getFinishedX() + 4, getFinishedY() + 5, getWidth() - 8, getHeight() - 8);
        final float startX = getFinishedX() + 4.5f;
        final float sliderwidth = getWidth() - 10;
        float length = MathHelper.floor_double(((numberValue.getValue()).floatValue() - numberValue.getMinimumValue().floatValue()) / (numberValue.getMaximumValue().floatValue() - numberValue.getMinimumValue().floatValue()) * sliderwidth);
        Render2DUtil.drawRectWH(getFinishedX(), getFinishedY(), getWidth(), getHeight(), backgroundColor);
        Render2DUtil.drawGradientRectWH(getFinishedX() + 5f, getFinishedY() + 6f, sliderwidth, getHeight() - 10, lightBgColor, lightBgColor);
        Render2DUtil.drawRectWH(getFinishedX() + 5f, getFinishedY() + 6f, length, getHeight() - 10, getColor().getRGB());
        Render2DUtil.drawRectWH(getFinishedX() + 5f + length, getFinishedY() + 5.5f, 2, getHeight() - 9, getColor().brighter().getRGB());
        Client.INSTANCE.smallFontRenderer.drawStringWithShadow(numberValue.getValue().toString(),
                (getFinishedX() + getWidth() - Client.INSTANCE.smallFontRenderer.getStringWidth(numberValue.getValue().toString())) - 6,
                (getFinishedY() + 10), -1);
        Client.INSTANCE.smallFontRenderer.drawStringWithShadow(getLabel(),
                getFinishedX() + 6,
                getFinishedY() + 10, -1);
        if (sliding) {
            if (numberValue.getValue() instanceof Double) {
                numberValue.setValue(round(((mouseX - startX) * (numberValue.getMaximumValue().doubleValue() - numberValue.getMinimumValue().doubleValue()) / sliderwidth + numberValue.getMinimumValue().doubleValue()), 2));
            }
            if (numberValue.getValue() instanceof Float) {
                numberValue.setValue((float) round(((mouseX - startX) * (numberValue.getMaximumValue().floatValue() - numberValue.getMinimumValue().floatValue()) / sliderwidth + numberValue.getMinimumValue().floatValue()), 2));
            }
            if (numberValue.getValue() instanceof Long) {
                numberValue.setValue((long) round(((mouseX - startX) * (numberValue.getMaximumValue().longValue() - numberValue.getMinimumValue().longValue()) / sliderwidth + numberValue.getMinimumValue().longValue()), 2));
            }
            if (numberValue.getValue() instanceof Integer) {
                numberValue.setValue((int) round(((mouseX - startX) * (numberValue.getMaximumValue().intValue() - numberValue.getMinimumValue().intValue()) / sliderwidth + numberValue.getMinimumValue().intValue()), 2));
            }
            if (numberValue.getValue() instanceof Short) {
                numberValue.setValue((short) round(((mouseX - startX) * (numberValue.getMaximumValue().shortValue() - numberValue.getMinimumValue().shortValue()) / sliderwidth + numberValue.getMinimumValue().shortValue()), 2));
            }
            if (numberValue.getValue() instanceof Byte) {
                numberValue.setValue((byte) round(((mouseX - startX) * (numberValue.getMaximumValue().byteValue() - numberValue.getMinimumValue().byteValue()) / sliderwidth + numberValue.getMinimumValue().byteValue()), 2));
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        final boolean hovered = MouseUtil.mouseWithinBounds(mouseX, mouseY, getFinishedX() + 4, getFinishedY() + 5, getWidth() - 8, getHeight() - 8);
        switch (mouseButton) {
            case 0:
                if (hovered) sliding = true;
                break;
            default:
                break;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
        switch (mouseButton) {
            case 0:
                if (sliding) sliding = false;
                break;
            default:
                break;
        }
    }

    private double round(final double val, final int places) {
        final double value = Math.round(val / numberValue.getIncrement().doubleValue()) * numberValue.getIncrement().doubleValue();
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}