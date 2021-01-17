// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.realms.RealmsScrolledSelectionList;

public class GuiSlotRealmsProxy extends GuiSlot
{
    private final RealmsScrolledSelectionList selectionList;
    
    public GuiSlotRealmsProxy(final RealmsScrolledSelectionList selectionListIn, final int widthIn, final int heightIn, final int topIn, final int bottomIn, final int slotHeightIn) {
        super(Minecraft.getMinecraft(), widthIn, heightIn, topIn, bottomIn, slotHeightIn);
        this.selectionList = selectionListIn;
    }
    
    @Override
    protected int getSize() {
        return this.selectionList.getItemCount();
    }
    
    @Override
    protected void elementClicked(final int slotIndex, final boolean isDoubleClick, final int mouseX, final int mouseY) {
        this.selectionList.selectItem(slotIndex, isDoubleClick, mouseX, mouseY);
    }
    
    @Override
    protected boolean isSelected(final int slotIndex) {
        return this.selectionList.isSelectedItem(slotIndex);
    }
    
    @Override
    protected void drawBackground() {
        this.selectionList.renderBackground();
    }
    
    @Override
    protected void drawSlot(final int entryID, final int p_180791_2_, final int p_180791_3_, final int p_180791_4_, final int mouseXIn, final int mouseYIn) {
        this.selectionList.renderItem(entryID, p_180791_2_, p_180791_3_, p_180791_4_, mouseXIn, mouseYIn);
    }
    
    public int func_154338_k() {
        return super.width;
    }
    
    public int func_154339_l() {
        return super.mouseY;
    }
    
    public int func_154337_m() {
        return super.mouseX;
    }
    
    @Override
    protected int getContentHeight() {
        return this.selectionList.getMaxPosition();
    }
    
    @Override
    protected int getScrollBarX() {
        return this.selectionList.getScrollbarPosition();
    }
    
    @Override
    public void handleMouseInput() {
        super.handleMouseInput();
    }
}
