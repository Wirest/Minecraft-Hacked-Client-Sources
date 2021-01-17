// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.resources;

import java.io.IOException;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ResourceLocation;

public class GrassColorReloadListener implements IResourceManagerReloadListener
{
    private static final ResourceLocation LOC_GRASS_PNG;
    
    static {
        LOC_GRASS_PNG = new ResourceLocation("textures/colormap/grass.png");
    }
    
    @Override
    public void onResourceManagerReload(final IResourceManager resourceManager) {
        try {
            ColorizerGrass.setGrassBiomeColorizer(TextureUtil.readImageData(resourceManager, GrassColorReloadListener.LOC_GRASS_PNG));
        }
        catch (IOException ex) {}
    }
}
