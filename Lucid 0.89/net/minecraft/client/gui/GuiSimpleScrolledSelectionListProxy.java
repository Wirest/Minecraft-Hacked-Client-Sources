package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.realms.RealmsSimpleScrolledSelectionList;
import net.minecraft.util.MathHelper;

public class GuiSimpleScrolledSelectionListProxy extends GuiSlot
{
    private final RealmsSimpleScrolledSelectionList field_178050_u;

    public GuiSimpleScrolledSelectionListProxy(RealmsSimpleScrolledSelectionList p_i45525_1_, int widthIn, int heightIn, int topIn, int bottomIn, int slotHeightIn)
    {
        super(Minecraft.getMinecraft(), widthIn, heightIn, topIn, bottomIn, slotHeightIn);
        this.field_178050_u = p_i45525_1_;
    }

    @Override
	protected int getSize()
    {
        return this.field_178050_u.getItemCount();
    }

    /**
     * The element in the slot that was clicked, boolean for whether it was double clicked or not
     */
    @Override
	protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY)
    {
        this.field_178050_u.selectItem(slotIndex, isDoubleClick, mouseX, mouseY);
    }

    /**
     * Returns true if the element passed in is currently selected
     */
    @Override
	protected boolean isSelected(int slotIndex)
    {
        return this.field_178050_u.isSelectedItem(slotIndex);
    }

    @Override
	protected void drawBackground()
    {
        this.field_178050_u.renderBackground();
    }

    @Override
	protected void drawSlot(int entryID, int p_180791_2_, int p_180791_3_, int p_180791_4_, int mouseXIn, int mouseYIn)
    {
        this.field_178050_u.renderItem(entryID, p_180791_2_, p_180791_3_, p_180791_4_, mouseXIn, mouseYIn);
    }

    public int getWidth()
    {
        return super.width;
    }

    public int getMouseY()
    {
        return super.mouseY;
    }

    public int getMouseX()
    {
        return super.mouseX;
    }

    /**
     * Return the height of the content being scrolled
     */
    @Override
	protected int getContentHeight()
    {
        return this.field_178050_u.getMaxPosition();
    }

    @Override
	protected int getScrollBarX()
    {
        return this.field_178050_u.getScrollbarPosition();
    }

    @Override
	public void handleMouseInput()
    {
        super.handleMouseInput();
    }

    @Override
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
            int var8 = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
            int var9 = this.top + 4 - (int)this.amountScrolled;

            if (this.hasListHeader)
            {
                this.drawListHeader(var8, var9, var6);
            }

            this.drawSelectionBox(var8, var9, mouseXIn, mouseYIn);
            GlStateManager.disableDepth();
            this.overlayBackground(0, this.top, 255, 255);
            this.overlayBackground(this.bottom, this.height, 255, 255);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
            GlStateManager.disableAlpha();
            GlStateManager.shadeModel(7425);
            GlStateManager.disableTexture2D();
            int var11 = this.func_148135_f();

            if (var11 > 0)
            {
                int var12 = (this.bottom - this.top) * (this.bottom - this.top) / this.getContentHeight();
                var12 = MathHelper.clamp_int(var12, 32, this.bottom - this.top - 8);
                int var13 = (int)this.amountScrolled * (this.bottom - this.top - var12) / var11 + this.top;

                if (var13 < this.top)
                {
                    var13 = this.top;
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
                var7.addVertexWithUV(var4, var13 + var12, 0.0D, 0.0D, 1.0D);
                var7.addVertexWithUV(var5, var13 + var12, 0.0D, 1.0D, 1.0D);
                var7.addVertexWithUV(var5, var13, 0.0D, 1.0D, 0.0D);
                var7.addVertexWithUV(var4, var13, 0.0D, 0.0D, 0.0D);
                var6.draw();
                var7.startDrawingQuads();
                var7.setColorRGBA_I(12632256, 255);
                var7.addVertexWithUV(var4, var13 + var12 - 1, 0.0D, 0.0D, 1.0D);
                var7.addVertexWithUV(var5 - 1, var13 + var12 - 1, 0.0D, 1.0D, 1.0D);
                var7.addVertexWithUV(var5 - 1, var13, 0.0D, 1.0D, 0.0D);
                var7.addVertexWithUV(var4, var13, 0.0D, 0.0D, 0.0D);
                var6.draw();
            }

            this.func_148142_b(mouseXIn, mouseYIn);
            GlStateManager.enableTexture2D();
            GlStateManager.shadeModel(7424);
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
        }
    }
}
