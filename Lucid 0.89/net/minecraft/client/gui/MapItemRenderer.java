package net.minecraft.client.gui;

import java.util.Iterator;
import java.util.Map;

import com.google.common.collect.Maps;

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
    
    public MapItemRenderer(TextureManager textureManagerIn)
    {
	this.textureManager = textureManagerIn;
    }
    
    /**
     * Updates a map texture
     */
    public void updateMapTexture(MapData mapdataIn)
    {
	this.getMapRendererInstance(mapdataIn).updateMapTexture();
    }
    
    public void renderMap(MapData mapdataIn, boolean p_148250_2_)
    {
	this.getMapRendererInstance(mapdataIn).render(p_148250_2_);
    }
    
    /**
     * Returns {@link net.minecraft.client.gui.MapItemRenderer.Instance MapItemRenderer.Instance} with given map data
     */
    private MapItemRenderer.Instance getMapRendererInstance(MapData mapdataIn)
    {
	MapItemRenderer.Instance var2 = (MapItemRenderer.Instance) this.loadedMaps.get(mapdataIn.mapName);
	
	if (var2 == null)
	{
	    var2 = new MapItemRenderer.Instance(mapdataIn, null);
	    this.loadedMaps.put(mapdataIn.mapName, var2);
	}
	
	return var2;
    }
    
    /**
     * Clears the currently loaded maps and removes their corresponding textures
     */
    public void clearLoadedMaps()
    {
	Iterator var1 = this.loadedMaps.values().iterator();
	
	while (var1.hasNext())
	{
	    MapItemRenderer.Instance var2 = (MapItemRenderer.Instance) var1.next();
	    this.textureManager.deleteTexture(var2.location);
	}
	
	this.loadedMaps.clear();
    }
    
    class Instance
    {
	private final MapData mapData;
	private final DynamicTexture mapTexture;
	private final ResourceLocation location;
	private final int[] mapTextureData;
	
	private Instance(MapData mapdataIn)
	{
	    this.mapData = mapdataIn;
	    this.mapTexture = new DynamicTexture(128, 128);
	    this.mapTextureData = this.mapTexture.getTextureData();
	    this.location = MapItemRenderer.this.textureManager.getDynamicTextureLocation("map/" + mapdataIn.mapName, this.mapTexture);
	    
	    for (int var3 = 0; var3 < this.mapTextureData.length; ++var3)
	    {
		this.mapTextureData[var3] = 0;
	    }
	}
	
	private void updateMapTexture()
	{
	    for (int var1 = 0; var1 < 16384; ++var1)
	    {
		int var2 = this.mapData.colors[var1] & 255;
		
		if (var2 / 4 == 0)
		{
		    this.mapTextureData[var1] = (var1 + var1 / 128 & 1) * 8 + 16 << 24;
		}
		else
		{
		    this.mapTextureData[var1] = MapColor.mapColorArray[var2 / 4].func_151643_b(var2 & 3);
		}
	    }
	    
	    this.mapTexture.updateDynamicTexture();
	}
	
	private void render(boolean p_148237_1_)
	{
	    byte var2 = 0;
	    byte var3 = 0;
	    Tessellator var4 = Tessellator.getInstance();
	    WorldRenderer var5 = var4.getWorldRenderer();
	    float var6 = 0.0F;
	    MapItemRenderer.this.textureManager.bindTexture(this.location);
	    GlStateManager.enableBlend();
	    GlStateManager.tryBlendFuncSeparate(1, 771, 0, 1);
	    GlStateManager.disableAlpha();
	    var5.startDrawingQuads();
	    var5.addVertexWithUV(var2 + 0 + var6, var3 + 128 - var6, -0.009999999776482582D, 0.0D, 1.0D);
	    var5.addVertexWithUV(var2 + 128 - var6, var3 + 128 - var6, -0.009999999776482582D, 1.0D, 1.0D);
	    var5.addVertexWithUV(var2 + 128 - var6, var3 + 0 + var6, -0.009999999776482582D, 1.0D, 0.0D);
	    var5.addVertexWithUV(var2 + 0 + var6, var3 + 0 + var6, -0.009999999776482582D, 0.0D, 0.0D);
	    var4.draw();
	    GlStateManager.enableAlpha();
	    GlStateManager.disableBlend();
	    MapItemRenderer.this.textureManager.bindTexture(MapItemRenderer.mapIcons);
	    int var7 = 0;
	    Iterator var8 = this.mapData.playersVisibleOnMap.values().iterator();
	    
	    while (var8.hasNext())
	    {
		Vec4b var9 = (Vec4b) var8.next();
		
		if (!p_148237_1_ || var9.func_176110_a() == 1)
		{
		    GlStateManager.pushMatrix();
		    GlStateManager.translate(var2 + var9.func_176112_b() / 2.0F + 64.0F, var3 + var9.func_176113_c() / 2.0F + 64.0F, -0.02F);
		    GlStateManager.rotate(var9.func_176111_d() * 360 / 16.0F, 0.0F, 0.0F, 1.0F);
		    GlStateManager.scale(4.0F, 4.0F, 3.0F);
		    GlStateManager.translate(-0.125F, 0.125F, 0.0F);
		    byte var10 = var9.func_176110_a();
		    float var11 = (var10 % 4 + 0) / 4.0F;
		    float var12 = (var10 / 4 + 0) / 4.0F;
		    float var13 = (var10 % 4 + 1) / 4.0F;
		    float var14 = (var10 / 4 + 1) / 4.0F;
		    var5.startDrawingQuads();
		    var5.addVertexWithUV(-1.0D, 1.0D, var7 * 0.001F, var11, var12);
		    var5.addVertexWithUV(1.0D, 1.0D, var7 * 0.001F, var13, var12);
		    var5.addVertexWithUV(1.0D, -1.0D, var7 * 0.001F, var13, var14);
		    var5.addVertexWithUV(-1.0D, -1.0D, var7 * 0.001F, var11, var14);
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
