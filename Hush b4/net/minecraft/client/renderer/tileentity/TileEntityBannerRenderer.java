// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.item.EnumDyeColor;
import java.util.List;
import java.util.Iterator;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.LayeredColorMaskTexture;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Blocks;
import com.google.common.collect.Maps;
import net.minecraft.client.model.ModelBanner;
import net.minecraft.util.ResourceLocation;
import java.util.Map;
import net.minecraft.tileentity.TileEntityBanner;

public class TileEntityBannerRenderer extends TileEntitySpecialRenderer<TileEntityBanner>
{
    private static final Map<String, TimedBannerTexture> DESIGNS;
    private static final ResourceLocation BANNERTEXTURES;
    private ModelBanner bannerModel;
    
    static {
        DESIGNS = Maps.newHashMap();
        BANNERTEXTURES = new ResourceLocation("textures/entity/banner_base.png");
    }
    
    public TileEntityBannerRenderer() {
        this.bannerModel = new ModelBanner();
    }
    
    @Override
    public void renderTileEntityAt(final TileEntityBanner te, final double x, final double y, final double z, final float partialTicks, final int destroyStage) {
        final boolean flag = te.getWorld() != null;
        final boolean flag2 = !flag || te.getBlockType() == Blocks.standing_banner;
        final int i = flag ? te.getBlockMetadata() : 0;
        final long j = flag ? te.getWorld().getTotalWorldTime() : 0L;
        GlStateManager.pushMatrix();
        final float f = 0.6666667f;
        if (flag2) {
            GlStateManager.translate((float)x + 0.5f, (float)y + 0.75f * f, (float)z + 0.5f);
            final float f2 = i * 360 / 16.0f;
            GlStateManager.rotate(-f2, 0.0f, 1.0f, 0.0f);
            this.bannerModel.bannerStand.showModel = true;
        }
        else {
            float f3 = 0.0f;
            if (i == 2) {
                f3 = 180.0f;
            }
            if (i == 4) {
                f3 = 90.0f;
            }
            if (i == 5) {
                f3 = -90.0f;
            }
            GlStateManager.translate((float)x + 0.5f, (float)y - 0.25f * f, (float)z + 0.5f);
            GlStateManager.rotate(-f3, 0.0f, 1.0f, 0.0f);
            GlStateManager.translate(0.0f, -0.3125f, -0.4375f);
            this.bannerModel.bannerStand.showModel = false;
        }
        final BlockPos blockpos = te.getPos();
        final float f4 = blockpos.getX() * 7 + blockpos.getY() * 9 + blockpos.getZ() * 13 + (float)j + partialTicks;
        this.bannerModel.bannerSlate.rotateAngleX = (-0.0125f + 0.01f * MathHelper.cos(f4 * 3.1415927f * 0.02f)) * 3.1415927f;
        GlStateManager.enableRescaleNormal();
        final ResourceLocation resourcelocation = this.func_178463_a(te);
        if (resourcelocation != null) {
            this.bindTexture(resourcelocation);
            GlStateManager.pushMatrix();
            GlStateManager.scale(f, -f, -f);
            this.bannerModel.renderBanner();
            GlStateManager.popMatrix();
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.popMatrix();
    }
    
    private ResourceLocation func_178463_a(final TileEntityBanner bannerObj) {
        final String s = bannerObj.func_175116_e();
        if (s.isEmpty()) {
            return null;
        }
        TimedBannerTexture tileentitybannerrenderer$timedbannertexture = TileEntityBannerRenderer.DESIGNS.get(s);
        if (tileentitybannerrenderer$timedbannertexture == null) {
            if (TileEntityBannerRenderer.DESIGNS.size() >= 256) {
                final long i = System.currentTimeMillis();
                final Iterator<String> iterator = TileEntityBannerRenderer.DESIGNS.keySet().iterator();
                while (iterator.hasNext()) {
                    final String s2 = iterator.next();
                    final TimedBannerTexture tileentitybannerrenderer$timedbannertexture2 = TileEntityBannerRenderer.DESIGNS.get(s2);
                    if (i - tileentitybannerrenderer$timedbannertexture2.systemTime > 60000L) {
                        Minecraft.getMinecraft().getTextureManager().deleteTexture(tileentitybannerrenderer$timedbannertexture2.bannerTexture);
                        iterator.remove();
                    }
                }
                if (TileEntityBannerRenderer.DESIGNS.size() >= 256) {
                    return null;
                }
            }
            final List<TileEntityBanner.EnumBannerPattern> list1 = bannerObj.getPatternList();
            final List<EnumDyeColor> list2 = bannerObj.getColorList();
            final List<String> list3 = (List<String>)Lists.newArrayList();
            for (final TileEntityBanner.EnumBannerPattern tileentitybanner$enumbannerpattern : list1) {
                list3.add("textures/entity/banner/" + tileentitybanner$enumbannerpattern.getPatternName() + ".png");
            }
            tileentitybannerrenderer$timedbannertexture = new TimedBannerTexture(null);
            tileentitybannerrenderer$timedbannertexture.bannerTexture = new ResourceLocation(s);
            Minecraft.getMinecraft().getTextureManager().loadTexture(tileentitybannerrenderer$timedbannertexture.bannerTexture, new LayeredColorMaskTexture(TileEntityBannerRenderer.BANNERTEXTURES, list3, list2));
            TileEntityBannerRenderer.DESIGNS.put(s, tileentitybannerrenderer$timedbannertexture);
        }
        tileentitybannerrenderer$timedbannertexture.systemTime = System.currentTimeMillis();
        return tileentitybannerrenderer$timedbannertexture.bannerTexture;
    }
    
    static class TimedBannerTexture
    {
        public long systemTime;
        public ResourceLocation bannerTexture;
        
        private TimedBannerTexture() {
        }
    }
}
