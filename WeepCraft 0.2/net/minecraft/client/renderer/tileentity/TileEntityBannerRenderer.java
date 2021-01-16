package net.minecraft.client.renderer.tileentity;

import javax.annotation.Nullable;
import net.minecraft.client.model.ModelBanner;
import net.minecraft.client.renderer.BannerTextures;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class TileEntityBannerRenderer extends TileEntitySpecialRenderer<TileEntityBanner>
{
    private final ModelBanner bannerModel = new ModelBanner();

    public void func_192841_a(TileEntityBanner p_192841_1_, double p_192841_2_, double p_192841_4_, double p_192841_6_, float p_192841_8_, int p_192841_9_, float p_192841_10_)
    {
        boolean flag = p_192841_1_.getWorld() != null;
        boolean flag1 = !flag || p_192841_1_.getBlockType() == Blocks.STANDING_BANNER;
        int i = flag ? p_192841_1_.getBlockMetadata() : 0;
        long j = flag ? p_192841_1_.getWorld().getTotalWorldTime() : 0L;
        GlStateManager.pushMatrix();
        float f = 0.6666667F;

        if (flag1)
        {
            GlStateManager.translate((float)p_192841_2_ + 0.5F, (float)p_192841_4_ + 0.5F, (float)p_192841_6_ + 0.5F);
            float f1 = (float)(i * 360) / 16.0F;
            GlStateManager.rotate(-f1, 0.0F, 1.0F, 0.0F);
            this.bannerModel.bannerStand.showModel = true;
        }
        else
        {
            float f2 = 0.0F;

            if (i == 2)
            {
                f2 = 180.0F;
            }

            if (i == 4)
            {
                f2 = 90.0F;
            }

            if (i == 5)
            {
                f2 = -90.0F;
            }

            GlStateManager.translate((float)p_192841_2_ + 0.5F, (float)p_192841_4_ - 0.16666667F, (float)p_192841_6_ + 0.5F);
            GlStateManager.rotate(-f2, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(0.0F, -0.3125F, -0.4375F);
            this.bannerModel.bannerStand.showModel = false;
        }

        BlockPos blockpos = p_192841_1_.getPos();
        float f3 = (float)(blockpos.getX() * 7 + blockpos.getY() * 9 + blockpos.getZ() * 13) + (float)j + p_192841_8_;
        this.bannerModel.bannerSlate.rotateAngleX = (-0.0125F + 0.01F * MathHelper.cos(f3 * (float)Math.PI * 0.02F)) * (float)Math.PI;
        GlStateManager.enableRescaleNormal();
        ResourceLocation resourcelocation = this.getBannerResourceLocation(p_192841_1_);

        if (resourcelocation != null)
        {
            this.bindTexture(resourcelocation);
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.6666667F, -0.6666667F, -0.6666667F);
            this.bannerModel.renderBanner();
            GlStateManager.popMatrix();
        }

        GlStateManager.color(1.0F, 1.0F, 1.0F, p_192841_10_);
        GlStateManager.popMatrix();
    }

    @Nullable
    private ResourceLocation getBannerResourceLocation(TileEntityBanner bannerObj)
    {
        return BannerTextures.BANNER_DESIGNS.getResourceLocation(bannerObj.getPatternResourceLocation(), bannerObj.getPatternList(), bannerObj.getColorList());
    }
}
