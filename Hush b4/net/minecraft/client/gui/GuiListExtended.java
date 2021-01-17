// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;

public abstract class GuiListExtended extends GuiSlot
{
    public GuiListExtended(final Minecraft mcIn, final int widthIn, final int heightIn, final int topIn, final int bottomIn, final int slotHeightIn) {
        super(mcIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
    }
    
    @Override
    protected void elementClicked(final int slotIndex, final boolean isDoubleClick, final int mouseX, final int mouseY) {
    }
    
    @Override
    protected boolean isSelected(final int slotIndex) {
        return false;
    }
    
    @Override
    protected void drawBackground() {
    }
    
    @Override
    protected void drawSlot(final int entryID, final int p_180791_2_, final int p_180791_3_, final int p_180791_4_, final int mouseXIn, final int mouseYIn) {
        this.getListEntry(entryID).drawEntry(entryID, p_180791_2_, p_180791_3_, this.getListWidth(), p_180791_4_, mouseXIn, mouseYIn, this.getSlotIndexFromScreenCoords(mouseXIn, mouseYIn) == entryID);
    }
    
    @Override
    protected void func_178040_a(final int p_178040_1_, final int p_178040_2_, final int p_178040_3_) {
        this.getListEntry(p_178040_1_).setSelected(p_178040_1_, p_178040_2_, p_178040_3_);
    }
    
    public boolean mouseClicked(final int mouseX, final int mouseY, final int mouseEvent) {
        if (this.isMouseYWithinSlotBounds(mouseY)) {
            final int i = this.getSlotIndexFromScreenCoords(mouseX, mouseY);
            if (i >= 0) {
                final int j = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
                final int k = this.top + 4 - this.getAmountScrolled() + i * this.slotHeight + this.headerPadding;
                final int l = mouseX - j;
                final int i2 = mouseY - k;
                if (this.getListEntry(i).mousePressed(i, mouseX, mouseY, mouseEvent, l, i2)) {
                    this.setEnabled(false);
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean mouseReleased(final int p_148181_1_, final int p_148181_2_, final int p_148181_3_) {
        for (int i = 0; i < this.getSize(); ++i) {
            final int j = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
            final int k = this.top + 4 - this.getAmountScrolled() + i * this.slotHeight + this.headerPadding;
            final int l = p_148181_1_ - j;
            final int i2 = p_148181_2_ - k;
            this.getListEntry(i).mouseReleased(i, p_148181_1_, p_148181_2_, p_148181_3_, l, i2);
        }
        this.setEnabled(true);
        return false;
    }
    
    public abstract IGuiListEntry getListEntry(final int p0);
    
    public interface IGuiListEntry
    {
        void setSelected(final int p0, final int p1, final int p2);
        
        void drawEntry(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final boolean p7);
        
        boolean mousePressed(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5);
        
        void mouseReleased(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5);
    }
}
