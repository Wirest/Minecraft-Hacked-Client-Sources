package net.minecraft.client.resources;

import java.io.IOException;

import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.ColorizerFoliage;

public class FoliageColorReloadListener implements IResourceManagerReloadListener {
    private static final ResourceLocation field_130079_a = new ResourceLocation("textures/colormap/foliage.png");
    private static final String __OBFID = "CL_00001077";

    public void onResourceManagerReload(IResourceManager p_110549_1_) {
        try {
            ColorizerFoliage.setFoliageBiomeColorizer(TextureUtil.readImageData(p_110549_1_, field_130079_a));
        } catch (IOException var3) {
            ;
        }
    }
}
