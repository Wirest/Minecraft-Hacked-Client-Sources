package net.minecraft.client.renderer.tileentity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBanner;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.LayeredColorMaskTexture;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class TileEntityBannerRenderer extends TileEntitySpecialRenderer
{
    /** An array of all the patterns that are being currently rendered. */
    private static final Map DESIGNS = Maps.newHashMap();
    private static final ResourceLocation BANNERTEXTURES = new ResourceLocation("textures/entity/banner_base.png");
    private ModelBanner bannerModel = new ModelBanner();

    public void renderTileEntityBanner(TileEntityBanner entityBanner, double x, double y, double z, float p_180545_8_, int p_180545_9_)
    {
        boolean var10 = entityBanner.getWorld() != null;
        boolean var11 = !var10 || entityBanner.getBlockType() == Blocks.standing_banner;
        int var12 = var10 ? entityBanner.getBlockMetadata() : 0;
        long var13 = var10 ? entityBanner.getWorld().getTotalWorldTime() : 0L;
        GlStateManager.pushMatrix();
        float var15 = 0.6666667F;
        float var17;

        if (var11)
        {
            GlStateManager.translate((float)x + 0.5F, (float)y + 0.75F * var15, (float)z + 0.5F);
            float var16 = var12 * 360 / 16.0F;
            GlStateManager.rotate(-var16, 0.0F, 1.0F, 0.0F);
            this.bannerModel.bannerStand.showModel = true;
        }
        else
        {
            var17 = 0.0F;

            if (var12 == 2)
            {
                var17 = 180.0F;
            }

            if (var12 == 4)
            {
                var17 = 90.0F;
            }

            if (var12 == 5)
            {
                var17 = -90.0F;
            }

            GlStateManager.translate((float)x + 0.5F, (float)y - 0.25F * var15, (float)z + 0.5F);
            GlStateManager.rotate(-var17, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(0.0F, -0.3125F, -0.4375F);
            this.bannerModel.bannerStand.showModel = false;
        }

        BlockPos var19 = entityBanner.getPos();
        var17 = (float)(var19.getX() * 7 + var19.getY() * 9 + var19.getZ() * 13) + (float)var13 + p_180545_8_;
        this.bannerModel.bannerSlate.rotateAngleX = (-0.0125F + 0.01F * MathHelper.cos(var17 * (float)Math.PI * 0.02F)) * (float)Math.PI;
        GlStateManager.enableRescaleNormal();
        ResourceLocation var18 = this.func_178463_a(entityBanner);

        if (var18 != null)
        {
            this.bindTexture(var18);
            GlStateManager.pushMatrix();
            GlStateManager.scale(var15, -var15, -var15);
            this.bannerModel.renderBanner();
            GlStateManager.popMatrix();
        }

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }

    private ResourceLocation func_178463_a(TileEntityBanner bannerObj)
    {
        String var2 = bannerObj.func_175116_e();

        if (var2.isEmpty())
        {
            return null;
        }
        else
        {
            TileEntityBannerRenderer.TimedBannerTexture var3 = (TileEntityBannerRenderer.TimedBannerTexture)DESIGNS.get(var2);

            if (var3 == null)
            {
                if (DESIGNS.size() >= 256)
                {
                    long var4 = System.currentTimeMillis();
                    Iterator var6 = DESIGNS.keySet().iterator();

                    while (var6.hasNext())
                    {
                        String var7 = (String)var6.next();
                        TileEntityBannerRenderer.TimedBannerTexture var8 = (TileEntityBannerRenderer.TimedBannerTexture)DESIGNS.get(var7);

                        if (var4 - var8.systemTime > 60000L)
                        {
                            Minecraft.getMinecraft().getTextureManager().deleteTexture(var8.bannerTexture);
                            var6.remove();
                        }
                    }

                    if (DESIGNS.size() >= 256)
                    {
                        return null;
                    }
                }

                List var9 = bannerObj.getPatternList();
                List var5 = bannerObj.getColorList();
                ArrayList var10 = Lists.newArrayList();
                Iterator var11 = var9.iterator();

                while (var11.hasNext())
                {
                    TileEntityBanner.EnumBannerPattern var12 = (TileEntityBanner.EnumBannerPattern)var11.next();
                    var10.add("textures/entity/banner/" + var12.getPatternName() + ".png");
                }

                var3 = new TileEntityBannerRenderer.TimedBannerTexture(null);
                var3.bannerTexture = new ResourceLocation(var2);
                Minecraft.getMinecraft().getTextureManager().loadTexture(var3.bannerTexture, new LayeredColorMaskTexture(BANNERTEXTURES, var10, var5));
                DESIGNS.put(var2, var3);
            }

            var3.systemTime = System.currentTimeMillis();
            return var3.bannerTexture;
        }
    }

    @Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTicks, int destroyStage)
    {
        this.renderTileEntityBanner((TileEntityBanner)te, x, y, z, partialTicks, destroyStage);
    }

    static class TimedBannerTexture
    {
        public long systemTime;
        public ResourceLocation bannerTexture;

        private TimedBannerTexture() {}

        TimedBannerTexture(Object p_i46209_1_)
        {
            this();
        }
    }
}
