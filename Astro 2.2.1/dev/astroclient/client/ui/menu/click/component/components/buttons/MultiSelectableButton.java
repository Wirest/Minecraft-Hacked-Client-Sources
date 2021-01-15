package dev.astroclient.client.ui.menu.click.component.components.buttons;

import dev.astroclient.client.Client;
import dev.astroclient.client.property.impl.MultiSelectableProperty;
import dev.astroclient.client.ui.menu.click.component.Category;
import dev.astroclient.client.ui.menu.click.component.Component;
import dev.astroclient.client.ui.menu.click.component.components.Button;
import dev.astroclient.client.ui.menu.click.component.components.GroupBox;
import dev.astroclient.client.util.MouseUtil;
import dev.astroclient.client.util.render.Render2DUtil;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class MultiSelectableButton extends Button {
    private MultiSelectableProperty enumValue;
    private boolean extended;

    public MultiSelectableButton(Category parent, float posX, float posY, float width, float height, MultiSelectableProperty enumValue) {
        super(parent, enumValue.getName(), posX, posY, width, height);
        this.enumValue = enumValue;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        float posX = getParent().getParent().getPosX() + getParent().getPosX() + 50.5f + getPosX();
        float posY = getParent().getParent().getPosY() + getParent().getPosY() + 25 + getPosY() + ((GroupBox) getParent()).getScrollY();
        super.drawScreen(mouseX, mouseY, partialTicks);
        Render2DUtil.drawGradientRect(posX + 8.0f, posY + 1f, posX + 87.5f, posY + 10f, new Color(32, 32, 32, Math.min(getParent().getParent().getParent().getAlpha(), 255)).getRGB(), new Color(38, 38, 38, Math.min(getParent().getParent().getParent().getAlpha(), 255)).getRGB());
        Client.INSTANCE.smallFontRenderer.drawString(getLabel(), posX + 8, posY - 5, new Color(210, 210, 210, Math.min(getParent().getParent().getParent().getAlpha(), 255)).getRGB());
        Client.INSTANCE.smallFontRenderer.drawString(getSelectedObjects(), posX + 10, posY + 4f, new Color(210, 210, 210, Math.min(getParent().getParent().getParent().getAlpha(), 255)).getRGB());
        Render2DUtil.drawImg(new ResourceLocation("client/images/Arrow.png"), posX + 82, posY + 4, 3, 2);
        if (isExtended()) {
            setHeight(getDefaultHeight() + (9 * (enumValue.getObjects().size())));
            float y = posY + 10.5F;
            for (String enoom : enumValue.getObjects()) {
                Render2DUtil.drawRect(posX + 8f, y, posX + 87.5f, y + 8.5, new Color(enumValue.isSelected(enoom) ? 25 : 32, enumValue.isSelected(enoom) ? 25 : 32, enumValue.isSelected(enoom) ? 25 : 32, Math.min(getParent().getParent().getParent().getAlpha(), 255)).getRGB());
                Client.INSTANCE.smallFontRenderer.drawString(enoom, posX + 10, y + 2.5, new Color(210, 210, 210, Math.min(getParent().getParent().getParent().getAlpha(), 255)).getRGB());
                y += 9;
            }
        } else setHeight(getDefaultHeight());
        rePosition();
    }

    public String getSelectedObjects() {
        StringBuilder stringBuilder = new StringBuilder();
        int size = enumValue.getSelectedObjects().size();
        for (int i = 0; i < size; i++)
            stringBuilder.append(enumValue.getSelectedObjects().get(i)).append(i == size - 1 ? "" : ", ");
        if (Client.INSTANCE.smallFontRenderer.getStringWidth(stringBuilder.toString()) > 72)
            stringBuilder.delete(15, stringBuilder.toString().length());
        return stringBuilder.append(size > 2 ? ". . ." : "").toString();
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
            for (String enoom : enumValue.getObjects()) {
                if (MouseUtil.mouseWithinBounds(mouseX, mouseY, posX + 10, y, posX + 88.0f, y + 8)) {
                    enumValue.addObject(enoom);
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
