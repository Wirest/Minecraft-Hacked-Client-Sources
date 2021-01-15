package dev.astroclient.client.feature.impl.hud.hud.settings.impl.impl;

import dev.astroclient.client.Client;
import dev.astroclient.client.feature.impl.hud.hud.settings.impl.Setting;
import dev.astroclient.client.property.impl.number.NumberProperty;
import dev.astroclient.client.util.render.Render2DUtil;
import net.minecraft.util.MathHelper;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * made by Xen for Astro
 * at 12/18/2019
 **/
public class NumberSetting extends Setting {
    private NumberProperty numberValue;
    private boolean dragging;

    public NumberSetting(NumberProperty numberProperty, float posX, float posY) {
        super(numberProperty.getName(), posX, posY);
        this.numberValue = numberProperty;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        float sliderwidth = 74;
        float length = MathHelper.floor_double(((numberValue.getValue()).floatValue() - numberValue.getMinimumValue().floatValue()) / (numberValue.getMaximumValue().floatValue() - numberValue.getMinimumValue().floatValue()) * sliderwidth);
        boolean isHovered = mouseWithinBounds(mouseX, mouseY, getPosX(), getPosY(), 80, 10);
        Render2DUtil.drawBorderedRect(getPosX(), getPosY(), 80, 10, 0.5, new Color(36, 41, 51, 255).getRGB(), isHovered ? new Color(0x505760).getRGB() : new Color(0xFF3b4149).getRGB(), false);
        Render2DUtil.drawRect(getPosX() + length + 1f, getPosY() + 1, 4f, 8, new Color(174, 58, 67, 255).getRGB());
        Client.INSTANCE.fontRenderer.drawString(numberValue.getName() + ": " + numberValue.getValue().toString(), getPosX() + 40 -  Client.INSTANCE.fontRenderer.getStringWidth(numberValue.getName() + ": " + numberValue.getValue().toString()) / 2, getPosY() + 4, -1);

        if (dragging) {
            if (numberValue.getValue() instanceof Double) {
                numberValue.setValue(round(((mouseX - getPosX()) * (numberValue.getMaximumValue().doubleValue() - numberValue.getMinimumValue().doubleValue()) / sliderwidth + numberValue.getMinimumValue().doubleValue())));
            }
            if (numberValue.getValue() instanceof Float) {
                numberValue.setValue((float) round(((mouseX - getPosX()) * (numberValue.getMaximumValue().floatValue() - numberValue.getMinimumValue().floatValue()) / sliderwidth + numberValue.getMinimumValue().floatValue())));
            }
            if (numberValue.getValue() instanceof Long) {
                numberValue.setValue((long) round(((mouseX - getPosX()) * (numberValue.getMaximumValue().longValue() - numberValue.getMinimumValue().longValue()) / sliderwidth + numberValue.getMinimumValue().longValue())));
            }
            if (numberValue.getValue() instanceof Integer) {
                numberValue.setValue((int) round(((mouseX - getPosX()) * (numberValue.getMaximumValue().intValue() - numberValue.getMinimumValue().intValue()) / sliderwidth + numberValue.getMinimumValue().intValue())));
            }
            if (numberValue.getValue() instanceof Short) {
                numberValue.setValue((short) round(((mouseX - getPosX()) * (numberValue.getMaximumValue().shortValue() - numberValue.getMinimumValue().shortValue()) / sliderwidth + numberValue.getMinimumValue().shortValue())));
            }
        }

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        boolean isHovered = mouseWithinBounds(mouseX, mouseY, getPosX(), getPosY(), 80, 10);
        if (isHovered) {
            dragging = true;
        }

    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
        if (dragging) {
            dragging = false;
        }

    }

    private double round(final double val) {
        final double v = Math.round(val / numberValue.getIncrement().doubleValue()) * numberValue.getIncrement().doubleValue();
        BigDecimal bd = new BigDecimal(v);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
