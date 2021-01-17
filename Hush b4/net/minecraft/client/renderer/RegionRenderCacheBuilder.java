// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer;

import net.minecraft.util.EnumWorldBlockLayer;

public class RegionRenderCacheBuilder
{
    private final WorldRenderer[] worldRenderers;
    
    public RegionRenderCacheBuilder() {
        (this.worldRenderers = new WorldRenderer[EnumWorldBlockLayer.values().length])[EnumWorldBlockLayer.SOLID.ordinal()] = new WorldRenderer(2097152);
        this.worldRenderers[EnumWorldBlockLayer.CUTOUT.ordinal()] = new WorldRenderer(131072);
        this.worldRenderers[EnumWorldBlockLayer.CUTOUT_MIPPED.ordinal()] = new WorldRenderer(131072);
        this.worldRenderers[EnumWorldBlockLayer.TRANSLUCENT.ordinal()] = new WorldRenderer(262144);
    }
    
    public WorldRenderer getWorldRendererByLayer(final EnumWorldBlockLayer layer) {
        return this.worldRenderers[layer.ordinal()];
    }
    
    public WorldRenderer getWorldRendererByLayerId(final int id) {
        return this.worldRenderers[id];
    }
}
