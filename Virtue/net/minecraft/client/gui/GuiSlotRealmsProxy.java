package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.realms.RealmsScrolledSelectionList;

public class GuiSlotRealmsProxy extends GuiSlot
{
    private final RealmsScrolledSelectionList selectionList;
    private static final String __OBFID = "CL_00001846";

    public GuiSlotRealmsProxy(RealmsScrolledSelectionList selectionListIn, int p_i1085_2_, int p_i1085_3_, int p_i1085_4_, int p_i1085_5_, int p_i1085_6_)
    {
        super(Minecraft.getMinecraft(), p_i1085_2_, p_i1085_3_, p_i1085_4_, p_i1085_5_, p_i1085_6_);
        this.selectionList = selectionListIn;
    }

    protected int getSize()
    {
        return this.selectionList.getItemCount();
    }

    /**
     * The element in the slot that was clicked, boolean for whether it was double clicked or not
     */
    protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY)
    {
        this.selectionList.selectItem(slotIndex, isDoubleClick, mouseX, mouseY);
    }

    /**
     * Returns true if the element passed in is currently selected
     */
    protected boolean isSelected(int slotIndex)
    {
        return this.selectionList.isSelectedItem(slotIndex);
    }

    protected void drawBackground()
    {
        this.selectionList.renderBackground();
    }

    protected void drawSlot(int p_180791_1_, int p_180791_2_, int p_180791_3_, int p_180791_4_, int p_180791_5_, int p_180791_6_)
    {
        this.selectionList.renderItem(p_180791_1_, p_180791_2_, p_180791_3_, p_180791_4_, p_180791_5_, p_180791_6_);
    }

    public int func_154338_k()
    {
        return super.width;
    }

    public int func_154339_l()
    {
        return super.mouseY;
    }

    public int func_154337_m()
    {
        return super.mouseX;
    }

    /**
     * Return the height of the content being scrolled
     */
    protected int getContentHeight()
    {
        return this.selectionList.getMaxPosition();
    }

    protected int getScrollBarX()
    {
        return this.selectionList.getScrollbarPosition();
    }

    public void func_178039_p()
    {
        super.func_178039_p();
    }
}
