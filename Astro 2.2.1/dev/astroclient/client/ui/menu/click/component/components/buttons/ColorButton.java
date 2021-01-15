package dev.astroclient.client.ui.menu.click.component.components.buttons;

import dev.astroclient.client.Client;
import dev.astroclient.client.property.impl.ColorProperty;
import dev.astroclient.client.ui.menu.click.component.Category;
import dev.astroclient.client.ui.menu.click.component.Component;
import dev.astroclient.client.ui.menu.click.component.components.Button;
import dev.astroclient.client.ui.menu.click.component.components.GroupBox;
import dev.astroclient.client.util.MouseUtil;
import dev.astroclient.client.util.render.Render2DUtil;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * made by oHare for Ayyware
 *
 * @since 4/10/2019
 **/
public class ColorButton extends Button {
    private ColorProperty colorValue;
    private boolean extended, slidingAlpha;

    public ColorButton(Category parent, float posX, float posY, float width, float height, ColorProperty colorValue) {
        super(parent, colorValue.getName(), posX, posY, width, height);
        this.colorValue = colorValue;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        float posX = getParent().getParent().getPosX() + getParent().getPosX() + 50.5f + getPosX();
        float posY = getParent().getParent().getPosY() + getParent().getPosY() + 19 + getPosY() + ((GroupBox) getParent()).getScrollY();
        Client.INSTANCE.smallFontRenderer.drawStringWithOutline(getLabel(), posX + 8, posY + 1, new Color(210, 210, 210, Math.min(getParent().getParent().getParent().getAlpha(), 255)).getRGB());
        Render2DUtil.drawBorderedRect(posX - 34 + getParent().getWidth(), posY - .5, posX - 22 + getParent().getWidth(), posY + 6, 0.5f, colorValue.getColor().getRGB(), new Color(0, 0, 0, Math.min(getParent().getParent().getParent().getAlpha(), 255)).getRGB(), true);
        if (isExtended()) {
            Render2DUtil.drawRect(posX - 80 + getParent().getWidth(), posY + 6.5, posX - 27 + getParent().getWidth(), posY + 59.5, new Color(0, 0, 0, Math.min(getParent().getParent().getParent().getAlpha(), 255)).getRGB());
            Render2DUtil.drawBorderedRect(posX - 79.5 + getParent().getWidth(), posY + 7, posX - 27.5 + getParent().getWidth(), posY + 59, 0.5f, 0xff282828, 0xff3C3C3C, true);
            Render2DUtil.drawRect(posX - 77.5 + getParent().getWidth(), posY + 9, posX - 36.5 + getParent().getWidth(), posY + 49, new Color(0, 0, 0, Math.min(getParent().getParent().getParent().getAlpha(), 255)).getRGB());
            Render2DUtil.drawRect(posX - 77.5 + getParent().getWidth(), posY + 51, posX - 36.5 + getParent().getWidth(), posY + 57, new Color(0, 0, 0, Math.min(getParent().getParent().getParent().getAlpha(), 255)).getRGB());
            Render2DUtil.drawRect(posX - 77 + getParent().getWidth(), posY + 51.5, posX - 37 + getParent().getWidth(), posY + 56.5, colorValue.getColor().getRGB());
            Render2DUtil.drawRect(posX - 35.5 + getParent().getWidth(), posY + 9, posX - 29.5 + getParent().getWidth(), posY + 54.5, new Color(0, 0, 0, Math.min(getParent().getParent().getParent().getAlpha(), 255)).getRGB());
            for (float i = 0; i < 44f; i += (43.5f / 50)) {
                Render2DUtil.drawRect(posX - 35 + getParent().getWidth(), posY + 9.5 + i, posX - 30 + getParent().getWidth(), posY + 9.5 + i + (44.5f / 50), Color.getHSBColor(i / 50, 1, 1).getRGB());
            }
            for (float i = 0; i < 44f; i += (43.5f / 50)) {
                if (round(colorValue.getHSB()[0]) == round(i / 50)) {
                      Render2DUtil.drawBorderedRect(posX - 35 + getParent().getWidth(), posY + 9 + i, posX - 30 + getParent().getWidth(), posY + 10 + i + (44.5f / 50), 0.5f, 0, 0xff000000, true);
                }
            }
            for (float s = 40f; s > 0f; s -= (40f / 50)) {
                for (float b = 40f; b > 0f; b -= (40f / 50)) {
                    Render2DUtil.drawRect(posX - 77.5 + s + getParent().getWidth(), posY + 49 - b, posX - 76.5 + s + getParent().getWidth(), posY + 50 - b, Color.getHSBColor(colorValue.getHSB()[0], s / 50, b / 50).getRGB());
                }
            }
            for (float s = 40f; s > 0f; s -= (40f / 50)) {
                for (float b = 40f; b > 0f; b -= (40f / 50)) {
                    if (colorValue.getColor().getRGB() == Color.getHSBColor(colorValue.getHSB()[0], s / 50, b / 50).getRGB()) {
                        Render2DUtil.drawBorderedRect(posX - 78 + s + getParent().getWidth(), posY + 48.5 - b, posX - 76 + s + getParent().getWidth(), posY + 50.5 - b, 0.5f, 0, new Color(0, 0, 0, 255).getRGB(), true);
                    }
                }
            }
            //Render2DUtil.drawBorderedRect(posX - 77.5 + getParent().getWidth(), posY + 9, posX - 36.5 + getParent().getWidth(), posY + 49, 0.5, 0, new Color(0, 0, 0).getRGB(), true);
            setHeight(getDefaultHeight() + 60);
        } else setHeight(getDefaultHeight());
        rePosition();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        float posX = getParent().getParent().getPosX() + getParent().getPosX() + 50.5f + getPosX();
        float posY = getParent().getParent().getPosY() + getParent().getPosY() + 19 + getPosY() + ((GroupBox) getParent()).getScrollY();
        if (MouseUtil.mouseWithinBounds(mouseX, mouseY, posX - 34 + getParent().getWidth(), posY + 0.5f, posX - 22 + getParent().getWidth(), posY + 6)) {
            if (mouseButton == 0) {
                setExtended(!isExtended());
                getParent().getParent().dragging = false;
            }
        }
        if (isExtended()) {
            if (MouseUtil.mouseWithinBounds(mouseX, mouseY, posX - 35.5f + getParent().getWidth(), posY + 9, posX - 29.5f + getParent().getWidth(), posY + 54.5f)) {
                if (mouseButton == 0) {
                    for (float i = 0; i < 44f; i += (43.5f / 50)) {
                        if (MouseUtil.mouseWithinBounds(mouseX, mouseY, posX - 35 + getParent().getWidth(), posY + 9 + i, posX - 30 + getParent().getWidth(), posY + 10 + i + (44.5f / 50))) {
                            colorValue.setColor(Color.getHSBColor(i / 50, colorValue.getHSB()[1], colorValue.getHSB()[2]));
                        }
                    }
                    getParent().getParent().dragging = false;
                }
            }
            if (MouseUtil.mouseWithinBounds(mouseX, mouseY, posX - 77.5f + getParent().getWidth(), posY + 9, posX - 36.5f + getParent().getWidth(), posY + 49)) {
                if (mouseButton == 0) {
                    for (float s = 40f; s > 0f; s -= (40f / 50)) {
                        for (float b = 40f; b > 0f; b -= (40f / 50)) {
                            if (MouseUtil.mouseWithinBounds(mouseX, mouseY, posX - 77.5f + s + getParent().getWidth(), posY + 49 - b, posX - 76.5f + s + getParent().getWidth(), posY + 50 - b))
                                colorValue.setColor(Color.getHSBColor(colorValue.getHSB()[0], s / 50, b / 50));
                        }
                    }
                    getParent().getParent().dragging = false;
                }
            }
            if (MouseUtil.mouseWithinBounds(mouseX, mouseY, posX - 77 + getParent().getWidth(), posY + 51.5f, posX - 37 + getParent().getWidth(), posY + 56.5f)) {
                if (mouseButton == 0) {
                    int alpha2 = (int) ((mouseX - (posX - 77 + getParent().getWidth())) * (255 / 40));
                    colorValue.setColor(new Color(colorValue.getColor().getRed(), colorValue.getColor().getGreen(), colorValue.getColor().getBlue(), Math.min(Math.max(alpha2, 0), 255)));
                    getParent().getParent().dragging = false;
                }
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
        if (mouseButton == 0) {
            slidingAlpha = false;
        }
    }

    public boolean isExtended() {
        return extended;
    }

    public void setExtended(boolean extended) {
        this.extended = extended;
    }

    private void rePosition() {
        for (int i = ((GroupBox) getParent()).getComponents().indexOf(this); i < ((GroupBox) getParent()).getComponents().size(); i++) {
            final Component component = ((GroupBox) getParent()).getComponents().get(i);
            component.setPosY(((GroupBox) getParent()).getComponents().get(i - 1).getPosY() + ((GroupBox) getParent()).getComponents().get(i - 1).getHeight());
        }
    }

    private double round(final double val) {
        BigDecimal bd = new BigDecimal(val);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
