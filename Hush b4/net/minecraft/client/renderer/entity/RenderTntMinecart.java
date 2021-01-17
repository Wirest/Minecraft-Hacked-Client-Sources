// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.init.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityMinecartTNT;

public class RenderTntMinecart extends RenderMinecart<EntityMinecartTNT>
{
    public RenderTntMinecart(final RenderManager renderManagerIn) {
        super(renderManagerIn);
    }
    
    @Override
    protected void func_180560_a(final EntityMinecartTNT minecart, final float partialTicks, final IBlockState state) {
        final int i = minecart.getFuseTicks();
        if (i > -1 && i - partialTicks + 1.0f < 10.0f) {
            float f = 1.0f - (i - partialTicks + 1.0f) / 10.0f;
            f = MathHelper.clamp_float(f, 0.0f, 1.0f);
            f *= f;
            f *= f;
            final float f2 = 1.0f + f * 0.3f;
            GlStateManager.scale(f2, f2, f2);
        }
        super.func_180560_a(minecart, partialTicks, state);
        if (i > -1 && i / 5 % 2 == 0) {
            final BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
            GlStateManager.disableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 772);
            GlStateManager.color(1.0f, 1.0f, 1.0f, (1.0f - (i - partialTicks + 1.0f) / 100.0f) * 0.8f);
            GlStateManager.pushMatrix();
            blockrendererdispatcher.renderBlockBrightness(Blocks.tnt.getDefaultState(), 1.0f);
            GlStateManager.popMatrix();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.disableBlend();
            GlStateManager.enableLighting();
            GlStateManager.enableTexture2D();
        }
    }
}
