package net.minecraft.client.renderer.entity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartTNT;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;

public class RenderTntMinecart extends RenderMinecart
{

    public RenderTntMinecart(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
    }

    protected void func_180561_a(EntityMinecartTNT minecartTNT, float partialTicks, IBlockState state)
    {
        int var4 = minecartTNT.getFuseTicks();

        if (var4 > -1 && var4 - partialTicks + 1.0F < 10.0F)
        {
            float var5 = 1.0F - (var4 - partialTicks + 1.0F) / 10.0F;
            var5 = MathHelper.clamp_float(var5, 0.0F, 1.0F);
            var5 *= var5;
            var5 *= var5;
            float var6 = 1.0F + var5 * 0.3F;
            GlStateManager.scale(var6, var6, var6);
        }

        super.func_180560_a(minecartTNT, partialTicks, state);

        if (var4 > -1 && var4 / 5 % 2 == 0)
        {
            BlockRendererDispatcher var7 = Minecraft.getMinecraft().getBlockRendererDispatcher();
            GlStateManager.disableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 772);
            GlStateManager.color(1.0F, 1.0F, 1.0F, (1.0F - (var4 - partialTicks + 1.0F) / 100.0F) * 0.8F);
            GlStateManager.pushMatrix();
            var7.renderBlockBrightness(Blocks.tnt.getDefaultState(), 1.0F);
            GlStateManager.popMatrix();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.disableBlend();
            GlStateManager.enableLighting();
            GlStateManager.enableTexture2D();
        }
    }

    @Override
	protected void func_180560_a(EntityMinecart minecart, float partialTicks, IBlockState state)
    {
        this.func_180561_a((EntityMinecartTNT)minecart, partialTicks, state);
    }
}
