package dev.astroclient.client.ui.menu.click.component.components.buttons;

import dev.astroclient.client.Client;
import dev.astroclient.client.ui.menu.click.component.Category;
import dev.astroclient.client.ui.menu.click.component.components.Button;
import dev.astroclient.client.ui.menu.click.component.components.GroupBox;
import dev.astroclient.client.util.render.Render2DUtil;

import java.awt.*;

public class ConfigButton extends Button {

    public boolean hovered;

    public ConfigButton(Category parent, String label, float posX, float posY, float width, float height) {
        super(parent, label, posX, posY, width, height);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        float posX = getParent().getParent().getPosX() + getParent().getPosX() + 50.5f + getPosX();
        float posY = getParent().getParent().getPosY() + getParent().getPosY() + 21.5f + getPosY() + ((GroupBox) getParent()).getScrollY();
        hovered = mouseX >= posX && mouseY >= posY && mouseX < posX + getWidth() && mouseY < posY + getHeight();
        int color = hovered ? -1 : new Color(112, 112, 112).getRGB();
        Render2DUtil.drawGradientRect(posX, posY, posX + getWidth(), posY + getHeight(), new Color(35, 35, 35).getRGB(), new Color(27, 27, 27).getRGB());
        Render2DUtil.drawOutline(posX, posY, getWidth(), getHeight(), .5, color);
        Client.INSTANCE.smallBoldFontRenderer.drawCenteredStringWithShadow(getLabel(), posX + getWidth() / 2, posY + getHeight() / 2 - 2, new Color(210, 210, 210).getRGB());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (hovered && mouseButton == 0) {
            getParent().getParent().dragging = false;
        }
    }

}
