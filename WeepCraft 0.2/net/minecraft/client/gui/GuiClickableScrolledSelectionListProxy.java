package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.realms.RealmsClickableScrolledSelectionList;
import net.minecraft.realms.Tezzelator;
import org.lwjgl.input.Mouse;

public class GuiClickableScrolledSelectionListProxy extends GuiSlot
{
    private final RealmsClickableScrolledSelectionList proxy;

    public GuiClickableScrolledSelectionListProxy(RealmsClickableScrolledSelectionList selectionList, int p_i45526_2_, int p_i45526_3_, int p_i45526_4_, int p_i45526_5_, int p_i45526_6_)
    {
        super(Minecraft.getMinecraft(), p_i45526_2_, p_i45526_3_, p_i45526_4_, p_i45526_5_, p_i45526_6_);
        this.proxy = selectionList;
    }

    protected int getSize()
    {
        return this.proxy.getItemCount();
    }

    /**
     * The element in the slot that was clicked, boolean for whether it was double clicked or not
     */
    protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY)
    {
        this.proxy.selectItem(slotIndex, isDoubleClick, mouseX, mouseY);
    }

    /**
     * Returns true if the element passed in is currently selected
     */
    protected boolean isSelected(int slotIndex)
    {
        return this.proxy.isSelectedItem(slotIndex);
    }

    protected void drawBackground()
    {
        this.proxy.renderBackground();
    }

    protected void func_192637_a(int p_192637_1_, int p_192637_2_, int p_192637_3_, int p_192637_4_, int p_192637_5_, int p_192637_6_, float p_192637_7_)
    {
        this.proxy.renderItem(p_192637_1_, p_192637_2_, p_192637_3_, p_192637_4_, p_192637_5_, p_192637_6_);
    }

    public int width()
    {
        return this.width;
    }

    public int mouseY()
    {
        return this.mouseY;
    }

    public int mouseX()
    {
        return this.mouseX;
    }

    /**
     * Return the height of the content being scrolled
     */
    protected int getContentHeight()
    {
        return this.proxy.getMaxPosition();
    }

    protected int getScrollBarX()
    {
        return this.proxy.getScrollbarPosition();
    }

    public void handleMouseInput()
    {
        super.handleMouseInput();

        if (this.scrollMultiplier > 0.0F && Mouse.getEventButtonState())
        {
            this.proxy.customMouseEvent(this.top, this.bottom, this.headerPadding, this.amountScrolled, this.slotHeight);
        }
    }

    public void renderSelected(int p_178043_1_, int p_178043_2_, int p_178043_3_, Tezzelator p_178043_4_)
    {
        this.proxy.renderSelected(p_178043_1_, p_178043_2_, p_178043_3_, p_178043_4_);
    }

    protected void func_192638_a(int p_192638_1_, int p_192638_2_, int p_192638_3_, int p_192638_4_, float p_192638_5_)
    {
        int i = this.getSize();

        for (int j = 0; j < i; ++j)
        {
            int k = p_192638_2_ + j * this.slotHeight + this.headerPadding;
            int l = this.slotHeight - 4;

            if (k > this.bottom || k + l < this.top)
            {
                this.func_192639_a(j, p_192638_1_, k, p_192638_5_);
            }

            if (this.showSelectionBox && this.isSelected(j))
            {
                this.renderSelected(this.width, k, l, Tezzelator.instance);
            }

            this.func_192637_a(j, p_192638_1_, k, l, p_192638_3_, p_192638_4_, p_192638_5_);
        }
    }
}
