package dev.astroclient.client.ui.menu.click.component.components.buttons;

import dev.astroclient.client.Client;
import dev.astroclient.client.property.impl.BooleanProperty;
import dev.astroclient.client.ui.menu.click.component.Category;
import dev.astroclient.client.ui.menu.click.component.components.Button;
import dev.astroclient.client.ui.menu.click.component.components.GroupBox;
import dev.astroclient.client.util.MouseUtil;
import dev.astroclient.client.util.render.Render2DUtil;

import java.awt.*;

/**
 * made by oHare for Ayyware
 *
 * @since 4/10/2019
 **/
public class BooleanButton extends Button {
    private BooleanProperty value;

    public BooleanButton(Category parent, float posX, float posY, float width, float height, BooleanProperty value) {
        super(parent, value.getName(), posX, posY, width, height);
        this.value = value;
    }


    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        float posX = getParent().getParent().getPosX() + getParent().getPosX() + 50 + getPosX();
        float posY = getParent().getParent().getPosY() + getParent().getPosY() + 18 + getPosY() + ((GroupBox)getParent()).getScrollY();
        Render2DUtil.drawGradientRect(posX - 0.5f, posY + .5, posX + 3f, posY + 4f, value.getValue() ? new Color(Client.INSTANCE.featureManager.clickGUI.guiColor.getColor().getRed(), Client.INSTANCE.featureManager.clickGUI.guiColor.getColor().getGreen(), Client.INSTANCE.featureManager.clickGUI.guiColor.getColor().getBlue(), Math.min(getParent().getParent().getParent().getAlpha(), 255)).getRGB() : new Color(53, 53, 53, Math.min(getParent().getParent().getParent().getAlpha(), 255)).getRGB(), value.getValue() ? new Color(Client.INSTANCE.featureManager.clickGUI.secondaryGuiColor.getColor().getRed(), Client.INSTANCE.featureManager.clickGUI.secondaryGuiColor.getColor().getGreen(), Client.INSTANCE.featureManager.clickGUI.secondaryGuiColor.getColor().getBlue(), Math.min(getParent().getParent().getParent().getAlpha(), 255)).getRGB() : new Color(74, 74, 74, Math.min(getParent().getParent().getParent().getAlpha(), 255)).getRGB());
        Client.INSTANCE.smallFontRenderer.drawStringWithOutline(getLabel(), posX + 8, posY + 1.5, new Color(210, 210, 210, Math.min(getParent().getParent().getParent().getAlpha(), 255)).getRGB());

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        float posX = getParent().getParent().getPosX() + getParent().getPosX() + 50 + getPosX();
        float posY = getParent().getParent().getPosY() + getParent().getPosY() + 18 + getPosY() + ((GroupBox)getParent()).getScrollY();
        if (MouseUtil.mouseWithinBounds(mouseX, mouseY, posX - 1, posY - 1, posX + 5, posY + 5) && mouseButton == 0) {
            value.setValue(!value.getValue());
            getParent().getParent().dragging = false;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);

    }
}
