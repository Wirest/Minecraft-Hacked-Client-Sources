package net.optifine.gui;

import java.io.IOException;
import java.util.List;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiVideoSettings;

public class GuiScreenOF extends GuiScreen
{
    protected void actionPerformedRightClick(GuiButton button) throws IOException
    {
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (mouseButton == 1)
        {
            GuiButton guibutton = getSelectedButton(mouseX, mouseY, this.buttonList);

            if (guibutton != null && guibutton.enabled)
            {
                guibutton.playPressSound(this.mc.getSoundHandler());
                this.actionPerformedRightClick(guibutton);
            }
        }
    }

    public static GuiButton getSelectedButton(int x, int y, List<GuiButton> listButtons)
    {
        for (GuiButton listButton : listButtons) {
            GuiButton guibutton = (GuiButton) listButton;

            if (guibutton.visible) {
                int j = GuiVideoSettings.getButtonWidth(guibutton);
                int k = GuiVideoSettings.getButtonHeight(guibutton);

                if (x >= guibutton.xPosition && y >= guibutton.yPosition && x < guibutton.xPosition + j && y < guibutton.yPosition + k) {
                    return guibutton;
                }
            }
        }

        return null;
    }
}
