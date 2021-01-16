package net.minecraft.client.gui;

import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.block.material.MapColor;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec4b;
import net.minecraft.world.storage.MapData;

public class MapItemRenderer
{
    private static final ResourceLocation mapIcons = new ResourceLocation("textures/map/map_icons.png");
    private final TextureManager textureManager;
    private final Map loadedMaps = Maps.newHashMap();
    private static final String __OBFID = "CL_00000663";

    public MapItemRenderer(TextureManager p_i45009_1_)
    {
        this.textureManager = p_i45009_1_;
    }

    public void func_148246_a(MapData p_148246_1_)
    {
        this.func_148248_b(p_148246_1_).func_148236_a();
    }

    public void func_148250_a(MapData p_148250_1_, boolean p_148250_2_)
    {
        this.func_148248_b(p_148250_1_).func_148237_a(p_148250_2_);
    }

    private MapItemRenderer.Instance func_148248_b(MapData p_148248_1_)
    {
        MapItemRenderer.Instance var2 = (MapItemRenderer.Instance)this.loadedMaps.get(p_148248_1_.mapName);

        if (var2 == null)
        {
            var2 = new MapItemRenderer.Instance(p_148248_1_, null);
            this.loadedMaps.put(p_148248_1_.mapName, var2);
        }

        return var2;
    }

    public void func_148249_a()
    {
        Iterator var1 = this.loadedMaps.values().iterator();

        while (var1.hasNext())
        {
            MapItemRenderer.Instance var2 = (MapItemRenderer.Instance)var1.next();
            this.textureManager.deleteTexture(var2.field_148240_d);
        }

        this.loadedMaps.clear();
    }

    class Instance
    {
        private final MapData field_148242_b;
        private final DynamicTexture field_148243_c;
        private final ResourceLocation field_148240_d;
        private final int[] field_148241_e;
        private static final String __OBFID = "CL_00000665";

        private Instance(MapData p_i45007_2_)
        {
            this.field_148242_b = p_i45007_2_;
            this.field_148243_c = new DynamicTexture(128, 128);
            this.field_148241_e = this.field_148243_c.getTextureData();
            this.field_148240_d = MapItemRenderer.this.textureManager.getDynamicTextureLocation("map/" + p_i45007_2_.mapName, this.field_148243_c);

            for (int var3 = 0; var3 < this.field_148241_e.length; ++var3)
            {
                this.field_148241_e[var3] = 0;
            }
        }

        private void func_148236_a()
        {
            for (int var1 = 0; var1 < 16384; ++var1)
            {
                int var2 = this.field_148242_b.colors[var1] & 255;

                if (var2 / 4 == 0)
                {
                    this.field_148241_e[var1] = (var1 + var1 / 128 & 1) * 8 + 16 << 24;
                }
                else
                {
                    this.field_148241_e[var1] = MapColor.mapColorArray[var2 / 4].func_151643_b(var2 & 3);
                }
            }

            this.field_148243_c.updateDynamicTexture();
        }

        private void func_148237_a(boolean p_148237_1_)
        {
            byte var2 = 0;
            byte var3 = 0;
            Tessellator var4 = Tessellator.getInstance();
            WorldRenderer var5 = var4.getWorldRenderer();
            float var6 = 0.0F;
            MapItemRenderer.this.textureManager.bindTexture(this.field_148240_d);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(1, 771, 0, 1);
            GlStateManager.disableAlpha();
            var5.startDrawingQuads();
            var5.addVertexWithUV((double)((float)(var2 + 0) + var6), (double)((float)(var3 + 128) - var6), -0.009999999776482582D, 0.0D, 1.0D);
            var5.addVertexWithUV((double)((float)(var2 + 128) - var6), (double)((float)(var3 + 128) - var6), -0.009999999776482582D, 1.0D, 1.0D);
            var5.addVertexWithUV((double)((float)(var2 + 128) - var6), (double)((float)(var3 + 0) + var6), -0.009999999776482582D, 1.0D, 0.0D);
            var5.addVertexWithUV((double)((float)(var2 + 0) + var6), (double)((float)(var3 + 0) + var6), -0.009999999776482582D, 0.0D, 0.0D);
            var4.draw();
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
            MapItemRenderer.this.textureManager.bindTexture(MapItemRenderer.mapIcons);
            int var7 = 0;
            Iterator var8 = this.field_148242_b.playersVisibleOnMap.values().iterator();

            while (var8.hasNext())
            {
                Vec4b var9 = (Vec4b)var8.next();

                if (!p_148237_1_ || var9.func_176110_a() == 1)
                {
                    GlStateManager.pushMatrix();
                    GlStateManager.translate((float)var2 + (float)var9.func_176112_b() / 2.0F + 64.0F, (float)var3 + (float)var9.func_176113_c() / 2.0F + 64.0F, -0.02F);
                    GlStateManager.rotate((float)(var9.func_176111_d() * 360) / 16.0F, 0.0F, 0.0F, 1.0F);
                    GlStateManager.scale(4.0F, 4.0F, 3.0F);
                    GlStateManager.translate(-0.125F, 0.125F, 0.0F);
                    byte var10 = var9.func_176110_a();
                    float var11 = (float)(var10 % 4 + 0) / 4.0F;
                    float var12 = (float)(var10 / 4 + 0) / 4.0F;
                    float var13 = (float)(var10 % 4 + 1) / 4.0F;
                    float var14 = (float)(var10 / 4 + 1) / 4.0F;
                    var5.startDrawingQuads();
                    var5.addVertexWithUV(-1.0D, 1.0D, (double)((float)var7 * 0.001F), (double)var11, (double)var12);
                    var5.addVertexWithUV(1.0D, 1.0D, (double)((float)var7 * 0.001F), (double)var13, (double)var12);
                    var5.addVertexWithUV(1.0D, -1.0D, (double)((float)var7 * 0.001F), (double)var13, (double)var14);
                    var5.addVertexWithUV(-1.0D, -1.0D, (double)((float)var7 * 0.001F), (double)var11, (double)var14);
                    var4.draw();
                    GlStateManager.popMatrix();
                    ++var7;
                }
            }

            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0F, 0.0F, -0.04F);
            GlStateManager.scale(1.0F, 1.0F, 1.0F);
            GlStateManager.popMatrix();
        }

        Instance(MapData p_i45008_2_, Object p_i45008_3_)
        {
            this(p_i45008_2_);
        }
    }
}
