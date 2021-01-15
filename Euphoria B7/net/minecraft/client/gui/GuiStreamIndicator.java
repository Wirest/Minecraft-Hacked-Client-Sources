package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.ResourceLocation;

public class GuiStreamIndicator
{
    private static final ResourceLocation locationStreamIndicator = new ResourceLocation("textures/gui/stream_indicator.png");
    private final Minecraft mc;
    private float field_152443_c = 1.0F;
    private int field_152444_d = 1;
    private static final String __OBFID = "CL_00001849";

    public GuiStreamIndicator(Minecraft mcIn)
    {
        this.mc = mcIn;
    }

    public void render(int p_152437_1_, int p_152437_2_)
    {
        if (this.mc.getTwitchStream().func_152934_n())
        {
            GlStateManager.enableBlend();
            int var3 = this.mc.getTwitchStream().func_152920_A();

            if (var3 > 0)
            {
                String var4 = "" + var3;
                int var5 = this.mc.fontRendererObj.getStringWidth(var4);
                boolean var6 = true;
                int var7 = p_152437_1_ - var5 - 1;
                int var8 = p_152437_2_ + 20 - 1;
                int var10 = p_152437_2_ + 20 + this.mc.fontRendererObj.FONT_HEIGHT - 1;
                GlStateManager.func_179090_x();
                Tessellator var11 = Tessellator.getInstance();
                WorldRenderer var12 = var11.getWorldRenderer();
                GlStateManager.color(0.0F, 0.0F, 0.0F, (0.65F + 0.35000002F * this.field_152443_c) / 2.0F);
                var12.startDrawingQuads();
                var12.addVertex((double)var7, (double)var10, 0.0D);
                var12.addVertex((double)p_152437_1_, (double)var10, 0.0D);
                var12.addVertex((double)p_152437_1_, (double)var8, 0.0D);
                var12.addVertex((double)var7, (double)var8, 0.0D);
                var11.draw();
                GlStateManager.func_179098_w();
                this.mc.fontRendererObj.drawString(var4, p_152437_1_ - var5, p_152437_2_ + 20, 16777215);
            }

            this.render(p_152437_1_, p_152437_2_, this.func_152440_b(), 0);
            this.render(p_152437_1_, p_152437_2_, this.func_152438_c(), 17);
        }
    }

    private void render(int p_152436_1_, int p_152436_2_, int p_152436_3_, int p_152436_4_)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.65F + 0.35000002F * this.field_152443_c);
        this.mc.getTextureManager().bindTexture(locationStreamIndicator);
        float var5 = 150.0F;
        float var6 = 0.0F;
        float var7 = (float)p_152436_3_ * 0.015625F;
        float var8 = 1.0F;
        float var9 = (float)(p_152436_3_ + 16) * 0.015625F;
        Tessellator var10 = Tessellator.getInstance();
        WorldRenderer var11 = var10.getWorldRenderer();
        var11.startDrawingQuads();
        var11.addVertexWithUV((double)(p_152436_1_ - 16 - p_152436_4_), (double)(p_152436_2_ + 16), (double)var5, (double)var6, (double)var9);
        var11.addVertexWithUV((double)(p_152436_1_ - p_152436_4_), (double)(p_152436_2_ + 16), (double)var5, (double)var8, (double)var9);
        var11.addVertexWithUV((double)(p_152436_1_ - p_152436_4_), (double)(p_152436_2_ + 0), (double)var5, (double)var8, (double)var7);
        var11.addVertexWithUV((double)(p_152436_1_ - 16 - p_152436_4_), (double)(p_152436_2_ + 0), (double)var5, (double)var6, (double)var7);
        var10.draw();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private int func_152440_b()
    {
        return this.mc.getTwitchStream().isPaused() ? 16 : 0;
    }

    private int func_152438_c()
    {
        return this.mc.getTwitchStream().func_152929_G() ? 48 : 32;
    }

    public void func_152439_a()
    {
        if (this.mc.getTwitchStream().func_152934_n())
        {
            this.field_152443_c += 0.025F * (float)this.field_152444_d;

            if (this.field_152443_c < 0.0F)
            {
                this.field_152444_d *= -1;
                this.field_152443_c = 0.0F;
            }
            else if (this.field_152443_c > 1.0F)
            {
                this.field_152444_d *= -1;
                this.field_152443_c = 1.0F;
            }
        }
        else
        {
            this.field_152443_c = 1.0F;
            this.field_152444_d = 1;
        }
    }
}
