// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import net.minecraft.realms.Tezzelator;
import org.lwjgl.input.Mouse;
import net.minecraft.client.Minecraft;
import net.minecraft.realms.RealmsClickableScrolledSelectionList;

public class GuiClickableScrolledSelectionListProxy extends GuiSlot
{
    private final RealmsClickableScrolledSelectionList field_178046_u;
    
    public GuiClickableScrolledSelectionListProxy(final RealmsClickableScrolledSelectionList selectionList, final int p_i45526_2_, final int p_i45526_3_, final int p_i45526_4_, final int p_i45526_5_, final int p_i45526_6_) {
        super(Minecraft.getMinecraft(), p_i45526_2_, p_i45526_3_, p_i45526_4_, p_i45526_5_, p_i45526_6_);
        this.field_178046_u = selectionList;
    }
    
    @Override
    protected int getSize() {
        return this.field_178046_u.getItemCount();
    }
    
    @Override
    protected void elementClicked(final int slotIndex, final boolean isDoubleClick, final int mouseX, final int mouseY) {
        this.field_178046_u.selectItem(slotIndex, isDoubleClick, mouseX, mouseY);
    }
    
    @Override
    protected boolean isSelected(final int slotIndex) {
        return this.field_178046_u.isSelectedItem(slotIndex);
    }
    
    @Override
    protected void drawBackground() {
        this.field_178046_u.renderBackground();
    }
    
    @Override
    protected void drawSlot(final int entryID, final int p_180791_2_, final int p_180791_3_, final int p_180791_4_, final int mouseXIn, final int mouseYIn) {
        this.field_178046_u.renderItem(entryID, p_180791_2_, p_180791_3_, p_180791_4_, mouseXIn, mouseYIn);
    }
    
    public int func_178044_e() {
        return super.width;
    }
    
    public int func_178042_f() {
        return super.mouseY;
    }
    
    public int func_178045_g() {
        return super.mouseX;
    }
    
    @Override
    protected int getContentHeight() {
        return this.field_178046_u.getMaxPosition();
    }
    
    @Override
    protected int getScrollBarX() {
        return this.field_178046_u.getScrollbarPosition();
    }
    
    @Override
    public void handleMouseInput() {
        super.handleMouseInput();
        if (this.scrollMultiplier > 0.0f && Mouse.getEventButtonState()) {
            this.field_178046_u.customMouseEvent(this.top, this.bottom, this.headerPadding, this.amountScrolled, this.slotHeight);
        }
    }
    
    public void func_178043_a(final int p_178043_1_, final int p_178043_2_, final int p_178043_3_, final Tezzelator p_178043_4_) {
        this.field_178046_u.renderSelected(p_178043_1_, p_178043_2_, p_178043_3_, p_178043_4_);
    }
    
    @Override
    protected void drawSelectionBox(final int p_148120_1_, final int p_148120_2_, final int mouseXIn, final int mouseYIn) {
        for (int i = this.getSize(), j = 0; j < i; ++j) {
            final int k = p_148120_2_ + j * this.slotHeight + this.headerPadding;
            final int l = this.slotHeight - 4;
            if (k > this.bottom || k + l < this.top) {
                this.func_178040_a(j, p_148120_1_, k);
            }
            if (this.showSelectionBox && this.isSelected(j)) {
                this.func_178043_a(this.width, k, l, Tezzelator.instance);
            }
            this.drawSlot(j, p_148120_1_, k, l, mouseXIn, mouseYIn);
        }
    }
}
