package net.minecraft.client.renderer.entity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartTNT;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;

public class RenderTntMinecart extends RenderMinecart {
    private static final String __OBFID = "CL_00001029";

    public RenderTntMinecart(RenderManager p_i46135_1_) {
        super(p_i46135_1_);
    }

    protected void func_180561_a(EntityMinecartTNT p_180561_1_, float p_180561_2_, IBlockState p_180561_3_) {
        int var4 = p_180561_1_.func_94104_d();

        if (var4 > -1 && (float) var4 - p_180561_2_ + 1.0F < 10.0F) {
            float var5 = 1.0F - ((float) var4 - p_180561_2_ + 1.0F) / 10.0F;
            var5 = MathHelper.clamp_float(var5, 0.0F, 1.0F);
            var5 *= var5;
            var5 *= var5;
            float var6 = 1.0F + var5 * 0.3F;
            GlStateManager.scale(var6, var6, var6);
        }

        super.func_180560_a(p_180561_1_, p_180561_2_, p_180561_3_);

        if (var4 > -1 && var4 / 5 % 2 == 0) {
            BlockRendererDispatcher var7 = Minecraft.getMinecraft().getBlockRendererDispatcher();
            GlStateManager.func_179090_x();
            GlStateManager.disableLighting();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 772);
            GlStateManager.color(1.0F, 1.0F, 1.0F, (1.0F - ((float) var4 - p_180561_2_ + 1.0F) / 100.0F) * 0.8F);
            GlStateManager.pushMatrix();
            var7.func_175016_a(Blocks.tnt.getDefaultState(), 1.0F);
            GlStateManager.popMatrix();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.disableBlend();
            GlStateManager.enableLighting();
            GlStateManager.func_179098_w();
        }
    }

    protected void func_180560_a(EntityMinecart p_180560_1_, float p_180560_2_, IBlockState p_180560_3_) {
        this.func_180561_a((EntityMinecartTNT) p_180560_1_, p_180560_2_, p_180560_3_);
    }
}
