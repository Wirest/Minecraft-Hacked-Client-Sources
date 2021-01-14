package net.minecraft.client.gui;

import java.awt.Color;

import de.iotacb.client.Client;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

public class ServerListEntryLanScan implements GuiListExtended.IGuiListEntry
{
    private final Minecraft mc = Minecraft.getMinecraft();

    public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected)
    {
//        int i = y + slotHeight / 2 - this.mc.fontRendererObj.FONT_HEIGHT / 2;
    	final String text = I18n.format("lanServer.scanning", new Object[0]);
    	int i = (int) (y + slotHeight / 2 - Client.INSTANCE.getFontManager().getDefaultFont().getHeight(text) / 2);
    	Client.INSTANCE.getFontManager().getDefaultFont().drawStringWithShadow(text, (mc.currentScreen.width - Client.INSTANCE.getFontManager().getDefaultFont().getWidth(text)) / 2, i, Color.lightGray);
//        this.mc.fontRendererObj.drawString(I18n.format("lanServer.scanning", new Object[0]), this.mc.currentScreen.width / 2 - this.mc.fontRendererObj.getStringWidth(I18n.format("lanServer.scanning", new Object[0])) / 2, i, 16777215);
        String s;

        switch ((int)(Minecraft.getSystemTime() / 300L % 4L))
        {
            case 0:
            default:
                s = ".   ";
                break;

            case 1:
            case 3:
                s = ". . ";
                break;

            case 2:
                s = ". . .";
        }

//        this.mc.fontRendererObj.drawString(s, this.mc.currentScreen.width / 2 - this.mc.fontRendererObj.getStringWidth(s) / 2, i + this.mc.fontRendererObj.FONT_HEIGHT, 8421504);
        Client.INSTANCE.getFontManager().getDefaultFont().drawStringWithShadow(s, (mc.currentScreen.width - Client.INSTANCE.getFontManager().getDefaultFont().getWidth(s)) / 2, i + Client.INSTANCE.getFontManager().getDefaultFont().getHeight(text), Color.gray);
    }

    public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_)
    {
    }

    /**
     * Returns true if the mouse has been pressed on this control.
     */
    public boolean mousePressed(int slotIndex, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_, int p_148278_6_)
    {
        return false;
    }

    /**
     * Fired when the mouse button is released. Arguments: index, x, y, mouseEvent, relativeX, relativeY
     */
    public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY)
    {
    }
}
