package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

public class ServerListEntryLanScan implements GuiListExtended.IGuiListEntry
{
    private final Minecraft mc = Minecraft.getMinecraft();

    @Override
	public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected)
    {
        int var9 = y + slotHeight / 2 - this.mc.fontRendererObj.FONT_HEIGHT / 2;
        this.mc.fontRendererObj.drawString(I18n.format("lanServer.scanning", new Object[0]), this.mc.currentScreen.width / 2 - this.mc.fontRendererObj.getStringWidth(I18n.format("lanServer.scanning", new Object[0])) / 2, var9, 16777215);
        String var10;

        switch ((int)(Minecraft.getSystemTime() / 300L % 4L))
        {
            case 0:
            default:
                var10 = "O o o";
                break;

            case 1:
            case 3:
                var10 = "o O o";
                break;

            case 2:
                var10 = "o o O";
        }

        this.mc.fontRendererObj.drawString(var10, this.mc.currentScreen.width / 2 - this.mc.fontRendererObj.getStringWidth(var10) / 2, var9 + this.mc.fontRendererObj.FONT_HEIGHT, 8421504);
    }

    @Override
	public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_) {}

    /**
     * Returns true if the mouse has been pressed on this control.
     */
    @Override
	public boolean mousePressed(int slotIndex, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_, int p_148278_6_)
    {
        return false;
    }

    /**
     * Fired when the mouse button is released. Arguments: index, x, y, mouseEvent, relativeX, relativeY
     */
    @Override
	public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {}
}
