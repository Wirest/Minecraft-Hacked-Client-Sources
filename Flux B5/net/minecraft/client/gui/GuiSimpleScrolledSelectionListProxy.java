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
    private static final String __OBFID = "CL_00001938";

    public GuiSimpleScrolledSelectionListProxy(RealmsSimpleScrolledSelectionList p_i45525_1_, int p_i45525_2_, int p_i45525_3_, int p_i45525_4_, int p_i45525_5_, int p_i45525_6_)
    {
        super(Minecraft.getMinecraft(), p_i45525_2_, p_i45525_3_, p_i45525_4_, p_i45525_5_, p_i45525_6_);
        this.field_178050_u = p_i45525_1_;
    }

    protected int getSize()
    {
        return this.field_178050_u.getItemCount();
    }

    /**
     * The element in the slot that was clicked, boolean for whether it was double clicked or not
     */
    protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY)
    {
        this.field_178050_u.selectItem(slotIndex, isDoubleClick, mouseX, mouseY);
    }

    /**
     * Returns true if the element passed in is currently selected
     */
    protected boolean isSelected(int slotIndex)
    {
        return this.field_178050_u.isSelectedItem(slotIndex);
    }

    protected void drawBackground()
    {
        this.field_178050_u.renderBackground();
    }

    protected void drawSlot(int p_180791_1_, int p_180791_2_, int p_180791_3_, int p_180791_4_, int p_180791_5_, int p_180791_6_)
    {
        this.field_178050_u.renderItem(p_180791_1_, p_180791_2_, p_180791_3_, p_180791_4_, p_180791_5_, p_180791_6_);
    }

    public int func_178048_e()
    {
        return super.width;
    }

    public int func_178047_f()
    {
        return super.mouseY;
    }

    public int func_178049_g()
    {
        return super.mouseX;
    }

    /**
     * Return the height of the content being scrolled
     */
    protected int getContentHeight()
    {
        return this.field_178050_u.getMaxPosition();
    }

    protected int getScrollBarX()
    {
        return this.field_178050_u.getScrollbarPosition();
    }

    public void func_178039_p()
    {
        super.func_178039_p();
    }

    public void drawScreen(int p_148128_1_, int p_148128_2_, float p_148128_3_)
    {
        if (this.field_178041_q)
        {
            this.mouseX = p_148128_1_;
            this.mouseY = p_148128_2_;
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

            this.drawSelectionBox(var8, var9, p_148128_1_, p_148128_2_);
            GlStateManager.disableDepth();
            boolean var10 = true;
            this.overlayBackground(0, this.top, 255, 255);
            this.overlayBackground(this.bottom, this.height, 255, 255);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
            GlStateManager.disableAlpha();
            GlStateManager.shadeModel(7425);
            GlStateManager.func_179090_x();
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
                var7.func_178974_a(0, 255);
                var7.addVertexWithUV((double)var4, (double)this.bottom, 0.0D, 0.0D, 1.0D);
                var7.addVertexWithUV((double)var5, (double)this.bottom, 0.0D, 1.0D, 1.0D);
                var7.addVertexWithUV((double)var5, (double)this.top, 0.0D, 1.0D, 0.0D);
                var7.addVertexWithUV((double)var4, (double)this.top, 0.0D, 0.0D, 0.0D);
                var6.draw();
                var7.startDrawingQuads();
                var7.func_178974_a(8421504, 255);
                var7.addVertexWithUV((double)var4, (double)(var13 + var12), 0.0D, 0.0D, 1.0D);
                var7.addVertexWithUV((double)var5, (double)(var13 + var12), 0.0D, 1.0D, 1.0D);
                var7.addVertexWithUV((double)var5, (double)var13, 0.0D, 1.0D, 0.0D);
                var7.addVertexWithUV((double)var4, (double)var13, 0.0D, 0.0D, 0.0D);
                var6.draw();
                var7.startDrawingQuads();
                var7.func_178974_a(12632256, 255);
                var7.addVertexWithUV((double)var4, (double)(var13 + var12 - 1), 0.0D, 0.0D, 1.0D);
                var7.addVertexWithUV((double)(var5 - 1), (double)(var13 + var12 - 1), 0.0D, 1.0D, 1.0D);
                var7.addVertexWithUV((double)(var5 - 1), (double)var13, 0.0D, 1.0D, 0.0D);
                var7.addVertexWithUV((double)var4, (double)var13, 0.0D, 0.0D, 0.0D);
                var6.draw();
            }

            this.func_148142_b(p_148128_1_, p_148128_2_);
            GlStateManager.func_179098_w();
            GlStateManager.shadeModel(7424);
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
        }
    }
}
