package dev.astroclient.client.ui.menu;

import dev.astroclient.client.Client;
import dev.astroclient.client.util.render.Render2DUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

import java.awt.*;

/**
* @author Zane for PublicBase
* @since 10/27/19
*/

public class MenuButton extends GuiButton {

    public boolean hovered;

    public MenuButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
        this.buttonId = buttonId;
        this.x = x;
        this.y = y;
        this.widthIn = widthIn;
        this.heightIn = heightIn;
        this.buttonText = buttonText;
    }

    private int buttonId, x, y, widthIn, heightIn;

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    private String buttonText;

    @Override
    public boolean isMouseOver() {
        return hovered;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        hovered = mouseX >= this.xPosition + 30 && mouseY >= this.yPosition && mouseX < this.xPosition + this.width - 30 && mouseY < this.yPosition + this.height;
        int color = hovered ? new Color(112, 112, 112).getRGB() : new Color(65, 65, 65).getRGB();
        Render2DUtil.drawGradientRect(x + 30, y, x + widthIn - 30, y + heightIn, new Color(35, 35, 35).getRGB(), new Color(27, 27, 27).getRGB());
        Render2DUtil.drawOutline(x + 30, y, widthIn - 60, heightIn, .5, color);
        Client.INSTANCE.buttonFontRenderer.drawCenteredStringWithShadow(buttonText, x + widthIn / 2, y + heightIn / 2 - 2, new Color(166, 166, 166).getRGB());
    }
}
