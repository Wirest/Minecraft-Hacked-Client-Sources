package net.minecraft.client.gui;

import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.MathHelper;

public abstract class GuiSlot
{
    protected final Minecraft mc;
    protected int width;
    protected int height;

    /** The top of the slot container. Affects the overlays and scrolling. */
    protected int top;

    /** The bottom of the slot container. Affects the overlays and scrolling. */
    protected int bottom;
    protected int right;
    protected int left;

    /** The height of a slot. */
    protected final int slotHeight;

    /** The buttonID of the button used to scroll up */
    private int scrollUpButtonID;

    /** The buttonID of the button used to scroll down */
    private int scrollDownButtonID;
    protected int mouseX;
    protected int mouseY;
    protected boolean field_148163_i = true;

    /** Where the mouse was in the window when you first clicked to scroll */
    protected float initialClickY = -2.0F;

    /**
     * What to multiply the amount you moved your mouse by (used for slowing down scrolling when over the items and not
     * on the scroll bar)
     */
    protected float scrollMultiplier;

    /** How far down this slot has been scrolled */
    protected float amountScrolled;

    /** The element in the list that was selected */
    protected int selectedElement = -1;

    /** The time when this button was last clicked. */
    protected long lastClicked;
    protected boolean field_178041_q = true;

    /**
     * Set to true if a selected element in this gui will show an outline box
     */
    protected boolean showSelectionBox = true;
    protected boolean hasListHeader;
    protected int headerPadding;
    private boolean enabled = true;

    public GuiSlot(Minecraft mcIn, int width, int height, int topIn, int bottomIn, int slotHeightIn)
    {
        this.mc = mcIn;
        this.width = width;
        this.height = height;
        this.top = topIn;
        this.bottom = bottomIn;
        this.slotHeight = slotHeightIn;
        this.left = 0;
        this.right = width;
    }

    public void setDimensions(int widthIn, int heightIn, int topIn, int bottomIn)
    {
        this.width = widthIn;
        this.height = heightIn;
        this.top = topIn;
        this.bottom = bottomIn;
        this.left = 0;
        this.right = widthIn;
    }

    public void setShowSelectionBox(boolean showSelectionBoxIn)
    {
        this.showSelectionBox = showSelectionBoxIn;
    }

    /**
     * Sets hasListHeader and headerHeight. Params: hasListHeader, headerHeight. If hasListHeader is false headerHeight
     * is set to 0.
     */
    protected void setHasListHeader(boolean hasListHeaderIn, int headerPaddingIn)
    {
        this.hasListHeader = hasListHeaderIn;
        this.headerPadding = headerPaddingIn;

        if (!hasListHeaderIn)
        {
            this.headerPadding = 0;
        }
    }

    protected abstract int getSize();

    /**
     * The element in the slot that was clicked, boolean for whether it was double clicked or not
     */
    protected abstract void elementClicked(int var1, boolean var2, int var3, int var4);

    /**
     * Returns true if the element passed in is currently selected
     */
    protected abstract boolean isSelected(int var1);

    /**
     * Return the height of the content being scrolled
     */
    protected int getContentHeight()
    {
        return this.getSize() * this.slotHeight + this.headerPadding;
    }

    protected abstract void drawBackground();

    protected void func_178040_a(int p_178040_1_, int p_178040_2_, int p_178040_3_) {}

    protected abstract void drawSlot(int var1, int var2, int var3, int var4, int var5, int var6);

    /**
     * Handles drawing a list's header row.
     */
    protected void drawListHeader(int p_148129_1_, int p_148129_2_, Tessellator p_148129_3_) {}

    protected void func_148132_a(int p_148132_1_, int p_148132_2_) {}

    protected void func_148142_b(int p_148142_1_, int p_148142_2_) {}

    public int getSlotIndexFromScreenCoords(int p_148124_1_, int p_148124_2_)
    {
        int var3 = this.left + this.width / 2 - this.getListWidth() / 2;
        int var4 = this.left + this.width / 2 + this.getListWidth() / 2;
        int var5 = p_148124_2_ - this.top - this.headerPadding + (int)this.amountScrolled - 4;
        int var6 = var5 / this.slotHeight;
        return p_148124_1_ < this.getScrollBarX() && p_148124_1_ >= var3 && p_148124_1_ <= var4 && var6 >= 0 && var5 >= 0 && var6 < this.getSize() ? var6 : -1;
    }

    /**
     * Registers the IDs that can be used for the scrollbar's up/down buttons.
     */
    public void registerScrollButtons(int scrollUpButtonIDIn, int scrollDownButtonIDIn)
    {
        this.scrollUpButtonID = scrollUpButtonIDIn;
        this.scrollDownButtonID = scrollDownButtonIDIn;
    }

    /**
     * Stop the thing from scrolling out of bounds
     */
    protected void bindAmountScrolled()
    {
        int var1 = this.func_148135_f();

        if (var1 < 0)
        {
            var1 /= 2;
        }

        if (!this.field_148163_i && var1 < 0)
        {
            var1 = 0;
        }

        this.amountScrolled = MathHelper.clamp_float(this.amountScrolled, 0.0F, var1);
    }

    public int func_148135_f()
    {
        return Math.max(0, this.getContentHeight() - (this.bottom - this.top - 4));
    }

    /**
     * Returns the amountScrolled field as an integer.
     */
    public int getAmountScrolled()
    {
        return (int)this.amountScrolled;
    }

    public boolean isMouseYWithinSlotBounds(int p_148141_1_)
    {
        return p_148141_1_ >= this.top && p_148141_1_ <= this.bottom && this.mouseX >= this.left && this.mouseX <= this.right;
    }

    /**
     * Scrolls the slot by the given amount. A positive value scrolls down, and a negative value scrolls up.
     */
    public void scrollBy(int amount)
    {
        this.amountScrolled += amount;
        this.bindAmountScrolled();
        this.initialClickY = -2.0F;
    }

    public void actionPerformed(GuiButton button)
    {
        if (button.enabled)
        {
            if (button.id == this.scrollUpButtonID)
            {
                this.amountScrolled -= this.slotHeight * 2 / 3;
                this.initialClickY = -2.0F;
                this.bindAmountScrolled();
            }
            else if (button.id == this.scrollDownButtonID)
            {
                this.amountScrolled += this.slotHeight * 2 / 3;
                this.initialClickY = -2.0F;
                this.bindAmountScrolled();
            }
        }
    }

    public void drawScreen(int mouseXIn, int mouseYIn, float p_148128_3_)
    {
        if (this.field_178041_q)
        {
            this.mouseX = mouseXIn;
            this.mouseY = mouseYIn;
            this.drawBackground();
            int var4 = this.getScrollBarX();
            int var5 = var4 + 6;
            this.bindAmountScrolled();
            GlStateManager.disableLighting();
            GlStateManager.disableFog();
            Tessellator var6 = Tessellator.getInstance();
            WorldRenderer var7 = var6.getWorldRenderer();
            this.mc.getTextureManager().bindTexture(Gui.optionsBackground);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            float var8 = 32.0F;
            var7.startDrawingQuads();
            var7.setColorOpaque_I(2105376);
            var7.addVertexWithUV(this.left, this.bottom, 0.0D, this.left / var8, (this.bottom + (int)this.amountScrolled) / var8);
            var7.addVertexWithUV(this.right, this.bottom, 0.0D, this.right / var8, (this.bottom + (int)this.amountScrolled) / var8);
            var7.addVertexWithUV(this.right, this.top, 0.0D, this.right / var8, (this.top + (int)this.amountScrolled) / var8);
            var7.addVertexWithUV(this.left, this.top, 0.0D, this.left / var8, (this.top + (int)this.amountScrolled) / var8);
            var6.draw();
            int var9 = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
            int var10 = this.top + 4 - (int)this.amountScrolled;

            if (this.hasListHeader)
            {
                this.drawListHeader(var9, var10, var6);
            }

            this.drawSelectionBox(var9, var10, mouseXIn, mouseYIn);
            GlStateManager.disableDepth();
            byte var11 = 4;
            this.overlayBackground(0, this.top, 255, 255);
            this.overlayBackground(this.bottom, this.height, 255, 255);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
            GlStateManager.disableAlpha();
            GlStateManager.shadeModel(7425);
            GlStateManager.disableTexture2D();
            var7.startDrawingQuads();
            var7.setColorRGBA_I(0, 0);
            var7.addVertexWithUV(this.left, this.top + var11, 0.0D, 0.0D, 1.0D);
            var7.addVertexWithUV(this.right, this.top + var11, 0.0D, 1.0D, 1.0D);
            var7.setColorRGBA_I(0, 255);
            var7.addVertexWithUV(this.right, this.top, 0.0D, 1.0D, 0.0D);
            var7.addVertexWithUV(this.left, this.top, 0.0D, 0.0D, 0.0D);
            var6.draw();
            var7.startDrawingQuads();
            var7.setColorRGBA_I(0, 255);
            var7.addVertexWithUV(this.left, this.bottom, 0.0D, 0.0D, 1.0D);
            var7.addVertexWithUV(this.right, this.bottom, 0.0D, 1.0D, 1.0D);
            var7.setColorRGBA_I(0, 0);
            var7.addVertexWithUV(this.right, this.bottom - var11, 0.0D, 1.0D, 0.0D);
            var7.addVertexWithUV(this.left, this.bottom - var11, 0.0D, 0.0D, 0.0D);
            var6.draw();
            int var12 = this.func_148135_f();

            if (var12 > 0)
            {
                int var13 = (this.bottom - this.top) * (this.bottom - this.top) / this.getContentHeight();
                var13 = MathHelper.clamp_int(var13, 32, this.bottom - this.top - 8);
                int var14 = (int)this.amountScrolled * (this.bottom - this.top - var13) / var12 + this.top;

                if (var14 < this.top)
                {
                    var14 = this.top;
                }

                var7.startDrawingQuads();
                var7.setColorRGBA_I(0, 255);
                var7.addVertexWithUV(var4, this.bottom, 0.0D, 0.0D, 1.0D);
                var7.addVertexWithUV(var5, this.bottom, 0.0D, 1.0D, 1.0D);
                var7.addVertexWithUV(var5, this.top, 0.0D, 1.0D, 0.0D);
                var7.addVertexWithUV(var4, this.top, 0.0D, 0.0D, 0.0D);
                var6.draw();
                var7.startDrawingQuads();
                var7.setColorRGBA_I(8421504, 255);
                var7.addVertexWithUV(var4, var14 + var13, 0.0D, 0.0D, 1.0D);
                var7.addVertexWithUV(var5, var14 + var13, 0.0D, 1.0D, 1.0D);
                var7.addVertexWithUV(var5, var14, 0.0D, 1.0D, 0.0D);
                var7.addVertexWithUV(var4, var14, 0.0D, 0.0D, 0.0D);
                var6.draw();
                var7.startDrawingQuads();
                var7.setColorRGBA_I(12632256, 255);
                var7.addVertexWithUV(var4, var14 + var13 - 1, 0.0D, 0.0D, 1.0D);
                var7.addVertexWithUV(var5 - 1, var14 + var13 - 1, 0.0D, 1.0D, 1.0D);
                var7.addVertexWithUV(var5 - 1, var14, 0.0D, 1.0D, 0.0D);
                var7.addVertexWithUV(var4, var14, 0.0D, 0.0D, 0.0D);
                var6.draw();
            }

            this.func_148142_b(mouseXIn, mouseYIn);
            GlStateManager.enableTexture2D();
            GlStateManager.shadeModel(7424);
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
        }
    }

    public void handleMouseInput()
    {
        if (this.isMouseYWithinSlotBounds(this.mouseY))
        {
            if (Mouse.isButtonDown(0) && this.getEnabled())
            {
                if (this.initialClickY == -1.0F)
                {
                    boolean var1 = true;

                    if (this.mouseY >= this.top && this.mouseY <= this.bottom)
                    {
                        int var2 = this.width / 2 - this.getListWidth() / 2;
                        int var3 = this.width / 2 + this.getListWidth() / 2;
                        int var4 = this.mouseY - this.top - this.headerPadding + (int)this.amountScrolled - 4;
                        int var5 = var4 / this.slotHeight;

                        if (this.mouseX >= var2 && this.mouseX <= var3 && var5 >= 0 && var4 >= 0 && var5 < this.getSize())
                        {
                            boolean var6 = var5 == this.selectedElement && Minecraft.getSystemTime() - this.lastClicked < 250L;
                            this.elementClicked(var5, var6, this.mouseX, this.mouseY);
                            this.selectedElement = var5;
                            this.lastClicked = Minecraft.getSystemTime();
                        }
                        else if (this.mouseX >= var2 && this.mouseX <= var3 && var4 < 0)
                        {
                            this.func_148132_a(this.mouseX - var2, this.mouseY - this.top + (int)this.amountScrolled - 4);
                            var1 = false;
                        }

                        int var11 = this.getScrollBarX();
                        int var7 = var11 + 6;

                        if (this.mouseX >= var11 && this.mouseX <= var7)
                        {
                            this.scrollMultiplier = -1.0F;
                            int var8 = this.func_148135_f();

                            if (var8 < 1)
                            {
                                var8 = 1;
                            }

                            int var9 = (int)((float)((this.bottom - this.top) * (this.bottom - this.top)) / (float)this.getContentHeight());
                            var9 = MathHelper.clamp_int(var9, 32, this.bottom - this.top - 8);
                            this.scrollMultiplier /= (float)(this.bottom - this.top - var9) / (float)var8;
                        }
                        else
                        {
                            this.scrollMultiplier = 1.0F;
                        }

                        if (var1)
                        {
                            this.initialClickY = this.mouseY;
                        }
                        else
                        {
                            this.initialClickY = -2.0F;
                        }
                    }
                    else
                    {
                        this.initialClickY = -2.0F;
                    }
                }
                else if (this.initialClickY >= 0.0F)
                {
                    this.amountScrolled -= (this.mouseY - this.initialClickY) * this.scrollMultiplier;
                    this.initialClickY = this.mouseY;
                }
            }
            else
            {
                this.initialClickY = -1.0F;
            }

            int var10 = Mouse.getEventDWheel();

            if (var10 != 0)
            {
                if (var10 > 0)
                {
                    var10 = -1;
                }
                else if (var10 < 0)
                {
                    var10 = 1;
                }

                this.amountScrolled += var10 * this.slotHeight / 2;
            }
        }
    }

    public void setEnabled(boolean enabledIn)
    {
        this.enabled = enabledIn;
    }

    public boolean getEnabled()
    {
        return this.enabled;
    }

    /**
     * Gets the width of the list
     */
    public int getListWidth()
    {
        return 220;
    }

    /**
     * Draws the selection box around the selected slot element.
     */
    protected void drawSelectionBox(int p_148120_1_, int p_148120_2_, int mouseXIn, int mouseYIn)
    {
        int var5 = this.getSize();
        Tessellator var6 = Tessellator.getInstance();
        WorldRenderer var7 = var6.getWorldRenderer();

        for (int var8 = 0; var8 < var5; ++var8)
        {
            int var9 = p_148120_2_ + var8 * this.slotHeight + this.headerPadding;
            int var10 = this.slotHeight - 4;

            if (var9 > this.bottom || var9 + var10 < this.top)
            {
                this.func_178040_a(var8, p_148120_1_, var9);
            }

            if (this.showSelectionBox && this.isSelected(var8))
            {
                int var11 = this.left + (this.width / 2 - this.getListWidth() / 2);
                int var12 = this.left + this.width / 2 + this.getListWidth() / 2;
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.disableTexture2D();
                var7.startDrawingQuads();
                var7.setColorOpaque_I(8421504);
                var7.addVertexWithUV(var11, var9 + var10 + 2, 0.0D, 0.0D, 1.0D);
                var7.addVertexWithUV(var12, var9 + var10 + 2, 0.0D, 1.0D, 1.0D);
                var7.addVertexWithUV(var12, var9 - 2, 0.0D, 1.0D, 0.0D);
                var7.addVertexWithUV(var11, var9 - 2, 0.0D, 0.0D, 0.0D);
                var7.setColorOpaque_I(0);
                var7.addVertexWithUV(var11 + 1, var9 + var10 + 1, 0.0D, 0.0D, 1.0D);
                var7.addVertexWithUV(var12 - 1, var9 + var10 + 1, 0.0D, 1.0D, 1.0D);
                var7.addVertexWithUV(var12 - 1, var9 - 1, 0.0D, 1.0D, 0.0D);
                var7.addVertexWithUV(var11 + 1, var9 - 1, 0.0D, 0.0D, 0.0D);
                var6.draw();
                GlStateManager.enableTexture2D();
            }

            this.drawSlot(var8, p_148120_1_, var9, var10, mouseXIn, mouseYIn);
        }
    }

    protected int getScrollBarX()
    {
        return this.width / 2 + 124;
    }

    /**
     * Overlays the background to hide scrolled items
     */
    protected void overlayBackground(int startY, int endY, int startAlpha, int endAlpha)
    {
        Tessellator var5 = Tessellator.getInstance();
        WorldRenderer var6 = var5.getWorldRenderer();
        this.mc.getTextureManager().bindTexture(Gui.optionsBackground);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        float var7 = 32.0F;
        var6.startDrawingQuads();
        var6.setColorRGBA_I(4210752, endAlpha);
        var6.addVertexWithUV(this.left, endY, 0.0D, 0.0D, endY / var7);
        var6.addVertexWithUV(this.left + this.width, endY, 0.0D, this.width / var7, endY / var7);
        var6.setColorRGBA_I(4210752, startAlpha);
        var6.addVertexWithUV(this.left + this.width, startY, 0.0D, this.width / var7, startY / var7);
        var6.addVertexWithUV(this.left, startY, 0.0D, 0.0D, startY / var7);
        var5.draw();
    }

    /**
     * Sets the left and right bounds of the slot. Param is the left bound, right is calculated as left + width.
     */
    public void setSlotXBoundsFromLeft(int leftIn)
    {
        this.left = leftIn;
        this.right = leftIn + this.width;
    }

    public int getSlotHeight()
    {
        return this.slotHeight;
    }
}
