package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;

public abstract class GuiListExtended extends GuiSlot {
    private static final String __OBFID = "CL_00000674";

    public GuiListExtended(Minecraft mcIn, int p_i45010_2_, int p_i45010_3_, int p_i45010_4_, int p_i45010_5_, int p_i45010_6_) {
        super(mcIn, p_i45010_2_, p_i45010_3_, p_i45010_4_, p_i45010_5_, p_i45010_6_);
    }

    /**
     * The element in the slot that was clicked, boolean for whether it was double clicked or not
     */
    protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
    }

    /**
     * Returns true if the element passed in is currently selected
     */
    protected boolean isSelected(int slotIndex) {
        return false;
    }

    protected void drawBackground() {
    }

    protected void drawSlot(int p_180791_1_, int p_180791_2_, int p_180791_3_, int p_180791_4_, int p_180791_5_, int p_180791_6_) {
        this.getListEntry(p_180791_1_).drawEntry(p_180791_1_, p_180791_2_, p_180791_3_, this.getListWidth(), p_180791_4_, p_180791_5_, p_180791_6_, this.getSlotIndexFromScreenCoords(p_180791_5_, p_180791_6_) == p_180791_1_);
    }

    protected void func_178040_a(int p_178040_1_, int p_178040_2_, int p_178040_3_) {
        this.getListEntry(p_178040_1_).setSelected(p_178040_1_, p_178040_2_, p_178040_3_);
    }

    public boolean func_148179_a(int p_148179_1_, int p_148179_2_, int p_148179_3_) {
        if (this.isMouseYWithinSlotBounds(p_148179_2_)) {
            int var4 = this.getSlotIndexFromScreenCoords(p_148179_1_, p_148179_2_);

            if (var4 >= 0) {
                int var5 = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
                int var6 = this.top + 4 - this.getAmountScrolled() + var4 * this.slotHeight + this.headerPadding;
                int var7 = p_148179_1_ - var5;
                int var8 = p_148179_2_ - var6;

                if (this.getListEntry(var4).mousePressed(var4, p_148179_1_, p_148179_2_, p_148179_3_, var7, var8)) {
                    this.setEnabled(false);
                    return true;
                }
            }
        }

        return false;
    }

    public boolean func_148181_b(int p_148181_1_, int p_148181_2_, int p_148181_3_) {
        for (int var4 = 0; var4 < this.getSize(); ++var4) {
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

    public interface IGuiListEntry {
        void setSelected(int var1, int var2, int var3);

        void drawEntry(int var1, int var2, int var3, int var4, int var5, int var6, int var7, boolean var8);

        boolean mousePressed(int var1, int var2, int var3, int var4, int var5, int var6);

        void mouseReleased(int var1, int var2, int var3, int var4, int var5, int var6);
    }
}
