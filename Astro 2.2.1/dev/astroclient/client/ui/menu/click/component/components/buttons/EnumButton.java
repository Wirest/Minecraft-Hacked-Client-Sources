package dev.astroclient.client.ui.menu.click.component.components.buttons;

import dev.astroclient.client.Client;
import dev.astroclient.client.property.impl.StringProperty;
import dev.astroclient.client.ui.menu.click.component.Category;
import dev.astroclient.client.ui.menu.click.component.Component;
import dev.astroclient.client.ui.menu.click.component.components.Button;
import dev.astroclient.client.ui.menu.click.component.components.GroupBox;
import dev.astroclient.client.util.MouseUtil;
import dev.astroclient.client.util.render.Render2DUtil;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

/**
 * made by oHare for Ayyware
 *
 * @since 4/10/2019
 **/
public class EnumButton extends Button {
    private StringProperty enumValue;
    private boolean extended;

    public EnumButton(Category parent, float posX, float posY, float width, float height, StringProperty enumValue) {
        super(parent, enumValue.getName(), posX, posY, width, height);
        this.enumValue = enumValue;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        float posX = getParent().getParent().getPosX() + getParent().getPosX() + 50.5f + getPosX();
        float posY = getParent().getParent().getPosY() + getParent().getPosY() + 25 + getPosY() + ((GroupBox) getParent()).getScrollY();
        Render2DUtil.drawGradientRect(posX + 8.0f, posY + 1f, posX + 87.5f, posY + 10f, new Color(32, 32, 32, Math.min(getParent().getParent().getParent().getAlpha(), 255)).getRGB(), new Color(38, 38, 38, Math.min(getParent().getParent().getParent().getAlpha(), 255)).getRGB());
        Client.INSTANCE.smallFontRenderer.drawStringWithOutline(getLabel(), posX + 8, posY - 5, new Color(210, 210, 210, Math.min(getParent().getParent().getParent().getAlpha(), 255)).getRGB());
        Client.INSTANCE.smallFontRenderer.drawStringWithOutline(enumValue.getValue(), posX + 10, posY + 4f, new Color(210, 210, 210, Math.min(getParent().getParent().getParent().getAlpha(), 255)).getRGB());
        Render2DUtil.drawImg(new ResourceLocation("client/images/Arrow.png"), posX + 82, posY + 4, 3, 2);
        if (isExtended()) {
            setHeight(getDefaultHeight() + (9 * (enumValue.getValues().length - 1)));
            float y = posY + 10.5F;
            for (String enoom : enumValue.getValues()) {
                if (enoom == enumValue.getValue()) continue;
                Render2DUtil.drawRect(posX + 8f, y, posX + 87.5f, y + 8.5, new Color(32, 32, 32, Math.min(getParent().getParent().getParent().getAlpha(), 255)).getRGB());
                Client.INSTANCE.smallFontRenderer.drawStringWithOutline(enoom, posX + 10, y + 2.5, new Color(210, 210, 210, Math.min(getParent().getParent().getParent().getAlpha(), 255)).getRGB());
                y += 9;
            }
        } else setHeight(getDefaultHeight());
        rePosition();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        float posX = getParent().getParent().getPosX() + getParent().getPosX() + 50.5f + getPosX();
        float posY = getParent().getParent().getPosY() + getParent().getPosY() + 25 + getPosY() + ((GroupBox) getParent()).getScrollY();
        if (MouseUtil.mouseWithinBounds(mouseX, mouseY, posX + 8.0f, posY - 1.0f, posX + 87.4f, posY + 8.0f)) {
            if (mouseButton == 0) {
                setExtended(!isExtended());
                getParent().getParent().dragging = false;
            }
        }
        if (isExtended() && mouseButton == 0) {
            float y = posY + 10.5f;
            for (String enoom : enumValue.getValues()) {
                if (enoom == enumValue.getValue()) continue;
                if (MouseUtil.mouseWithinBounds(mouseX, mouseY, posX + 10, y, posX + 88.0f, y + 8)) {
                    enumValue.setValue(enoom);
                    setExtended(false);
                    getParent().getParent().dragging = false;
                }
                y += 9;
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
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
}
