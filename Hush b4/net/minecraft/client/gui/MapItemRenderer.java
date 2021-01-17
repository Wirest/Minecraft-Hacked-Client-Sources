// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.Vec4b;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.block.material.MapColor;
import net.minecraft.client.renderer.texture.DynamicTexture;
import java.util.Iterator;
import net.minecraft.world.storage.MapData;
import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

public class MapItemRenderer
{
    private static final ResourceLocation mapIcons;
    private final TextureManager textureManager;
    private final Map<String, Instance> loadedMaps;
    
    static {
        mapIcons = new ResourceLocation("textures/map/map_icons.png");
    }
    
    public MapItemRenderer(final TextureManager textureManagerIn) {
        this.loadedMaps = (Map<String, Instance>)Maps.newHashMap();
        this.textureManager = textureManagerIn;
    }
    
    public void updateMapTexture(final MapData mapdataIn) {
        this.getMapRendererInstance(mapdataIn).updateMapTexture();
    }
    
    public void renderMap(final MapData mapdataIn, final boolean p_148250_2_) {
        this.getMapRendererInstance(mapdataIn).render(p_148250_2_);
    }
    
    private Instance getMapRendererInstance(final MapData mapdataIn) {
        Instance mapitemrenderer$instance = this.loadedMaps.get(mapdataIn.mapName);
        if (mapitemrenderer$instance == null) {
            mapitemrenderer$instance = new Instance(mapdataIn, (Instance)null);
            this.loadedMaps.put(mapdataIn.mapName, mapitemrenderer$instance);
        }
        return mapitemrenderer$instance;
    }
    
    public void clearLoadedMaps() {
        for (final Instance mapitemrenderer$instance : this.loadedMaps.values()) {
            this.textureManager.deleteTexture(mapitemrenderer$instance.location);
        }
        this.loadedMaps.clear();
    }
    
    class Instance
    {
        private final MapData mapData;
        private final DynamicTexture mapTexture;
        private final ResourceLocation location;
        private final int[] mapTextureData;
        
        private Instance(final MapData mapdataIn) {
            this.mapData = mapdataIn;
            this.mapTexture = new DynamicTexture(128, 128);
            this.mapTextureData = this.mapTexture.getTextureData();
            this.location = MapItemRenderer.this.textureManager.getDynamicTextureLocation("map/" + mapdataIn.mapName, this.mapTexture);
            for (int i = 0; i < this.mapTextureData.length; ++i) {
                this.mapTextureData[i] = 0;
            }
        }
        
        private void updateMapTexture() {
            for (int i = 0; i < 16384; ++i) {
                final int j = this.mapData.colors[i] & 0xFF;
                if (j / 4 == 0) {
                    this.mapTextureData[i] = (i + i / 128 & 0x1) * 8 + 16 << 24;
                }
                else {
                    this.mapTextureData[i] = MapColor.mapColorArray[j / 4].func_151643_b(j & 0x3);
                }
            }
            this.mapTexture.updateDynamicTexture();
        }
        
        private void render(final boolean noOverlayRendering) {
            final int i = 0;
            final int j = 0;
            final Tessellator tessellator = Tessellator.getInstance();
            final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            final float f = 0.0f;
            MapItemRenderer.this.textureManager.bindTexture(this.location);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(1, 771, 0, 1);
            GlStateManager.disableAlpha();
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
            worldrenderer.pos(i + 0 + f, j + 128 - f, -0.009999999776482582).tex(0.0, 1.0).endVertex();
            worldrenderer.pos(i + 128 - f, j + 128 - f, -0.009999999776482582).tex(1.0, 1.0).endVertex();
            worldrenderer.pos(i + 128 - f, j + 0 + f, -0.009999999776482582).tex(1.0, 0.0).endVertex();
            worldrenderer.pos(i + 0 + f, j + 0 + f, -0.009999999776482582).tex(0.0, 0.0).endVertex();
            tessellator.draw();
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
            MapItemRenderer.this.textureManager.bindTexture(MapItemRenderer.mapIcons);
            int k = 0;
            for (final Vec4b vec4b : this.mapData.mapDecorations.values()) {
                if (!noOverlayRendering || vec4b.func_176110_a() == 1) {
                    GlStateManager.pushMatrix();
                    GlStateManager.translate(i + vec4b.func_176112_b() / 2.0f + 64.0f, j + vec4b.func_176113_c() / 2.0f + 64.0f, -0.02f);
                    GlStateManager.rotate(vec4b.func_176111_d() * 360 / 16.0f, 0.0f, 0.0f, 1.0f);
                    GlStateManager.scale(4.0f, 4.0f, 3.0f);
                    GlStateManager.translate(-0.125f, 0.125f, 0.0f);
                    final byte b0 = vec4b.func_176110_a();
                    final float f2 = (b0 % 4 + 0) / 4.0f;
                    final float f3 = (b0 / 4 + 0) / 4.0f;
                    final float f4 = (b0 % 4 + 1) / 4.0f;
                    final float f5 = (b0 / 4 + 1) / 4.0f;
                    worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
                    final float f6 = -0.001f;
                    worldrenderer.pos(-1.0, 1.0, k * -0.001f).tex(f2, f3).endVertex();
                    worldrenderer.pos(1.0, 1.0, k * -0.001f).tex(f4, f3).endVertex();
                    worldrenderer.pos(1.0, -1.0, k * -0.001f).tex(f4, f5).endVertex();
                    worldrenderer.pos(-1.0, -1.0, k * -0.001f).tex(f2, f5).endVertex();
                    tessellator.draw();
                    GlStateManager.popMatrix();
                    ++k;
                }
            }
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0f, 0.0f, -0.04f);
            GlStateManager.scale(1.0f, 1.0f, 1.0f);
            GlStateManager.popMatrix();
        }
    }
}
