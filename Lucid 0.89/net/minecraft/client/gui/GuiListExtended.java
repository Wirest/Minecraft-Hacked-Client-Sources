package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;

public abstract class GuiListExtended extends GuiSlot
{

    public GuiListExtended(Minecraft mcIn, int widthIn, int heightIn, int topIn, int bottomIn, int slotHeightIn)
    {
        super(mcIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
    }

    /**
     * The element in the slot that was clicked, boolean for whether it was double clicked or not
     */
    @Override
	protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {}

    /**
     * Returns true if the element passed in is currently selected
     */
    @Override
	protected boolean isSelected(int slotIndex)
    {
        return false;
    }

    @Override
	protected void drawBackground() {}

    @Override
	protected void drawSlot(int entryID, int p_180791_2_, int p_180791_3_, int p_180791_4_, int mouseXIn, int mouseYIn)
    {
        this.getListEntry(entryID).drawEntry(entryID, p_180791_2_, p_180791_3_, this.getListWidth(), p_180791_4_, mouseXIn, mouseYIn, this.getSlotIndexFromScreenCoords(mouseXIn, mouseYIn) == entryID);
    }

    @Override
	protected void func_178040_a(int p_178040_1_, int p_178040_2_, int p_178040_3_)
    {
        this.getListEntry(p_178040_1_).setSelected(p_178040_1_, p_178040_2_, p_178040_3_);
    }

    public boolean mouseClicked(int mouseX, int mouseY, int mouseEvent)
    {
        if (this.isMouseYWithinSlotBounds(mouseY))
        {
            int var4 = this.getSlotIndexFromScreenCoords(mouseX, mouseY);

            if (var4 >= 0)
            {
                int var5 = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
                int var6 = this.top + 4 - this.getAmountScrolled() + var4 * this.slotHeight + this.headerPadding;
                int var7 = mouseX - var5;
                int var8 = mouseY - var6;

                if (this.getListEntry(var4).mousePressed(var4, mouseX, mouseY, mouseEvent, var7, var8))
                {
                    this.setEnabled(false);
                    return true;
                }
            }
        }

        return false;
    }

    public boolean mouseReleased(int p_148181_1_, int p_148181_2_, int p_148181_3_)
    {
        for (int var4 = 0; var4 < this.getSize(); ++var4)
        {
            int var5 = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
            int var6 = this.top + 4 - this.getAmountScrolled() + var4 * this.slotHeight + this.headerPadding;
            int var7 = p_148181_1_ - var5;
            int var8 = p_148181_2_ - var6;
            this.getListEntry(var4).mouseReleased(var4, p_148181_1_, p_148181_2_, p_148181_3_, var7, var8);
        }

        this.setEnabled(true);
        return false;
    }

    /**
     * Gets the IGuiListEntry object for the given index
     */
    public abstract GuiListExtended.IGuiListEntry getListEntry(int var1);

    public interface IGuiListEntry
    {
        void setSelected(int var1, int var2, int var3);

        void drawEntry(int var1, int var2, int var3, int var4, int var5, int var6, int var7, boolean var8);

        boolean mousePressed(int var1, int var2, int var3, int var4, int var5, int var6);

        void mouseReleased(int var1, int var2, int var3, int var4, int var5, int var6);
    }
}
