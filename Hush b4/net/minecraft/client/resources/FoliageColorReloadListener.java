// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.resources;

import java.io.IOException;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ResourceLocation;

public class FoliageColorReloadListener implements IResourceManagerReloadListener
{
    private static final ResourceLocation LOC_FOLIAGE_PNG;
    
    static {
        LOC_FOLIAGE_PNG = new ResourceLocation("textures/colormap/foliage.png");
    }
    
    @Override
    public void onResourceManagerReload(final IResourceManager resourceManager) {
        try {
            ColorizerFoliage.setFoliageBiomeColorizer(TextureUtil.readImageData(resourceManager, FoliageColorReloadListener.LOC_FOLIAGE_PNG));
        }
        catch (IOException ex) {}
    }
}
