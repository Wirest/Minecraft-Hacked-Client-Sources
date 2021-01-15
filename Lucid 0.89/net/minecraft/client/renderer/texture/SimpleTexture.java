package net.minecraft.client.renderer.texture;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.util.ResourceLocation;

public class SimpleTexture extends AbstractTexture
{
    private static final Logger logger = LogManager.getLogger();
    protected final ResourceLocation textureLocation;

    public SimpleTexture(ResourceLocation textureResourceLocation)
    {
        this.textureLocation = textureResourceLocation;
    }

    @Override
	public void loadTexture(IResourceManager resourceManager) throws IOException
    {
        this.deleteGlTexture();
        InputStream var2 = null;

        try
        {
            IResource var3 = resourceManager.getResource(this.textureLocation);
            var2 = var3.getInputStream();
            BufferedImage var4 = TextureUtil.readBufferedImage(var2);
            boolean var5 = false;
            boolean var6 = false;

            if (var3.hasMetadata())
            {
                try
                {
                    TextureMetadataSection var7 = (TextureMetadataSection)var3.getMetadata("texture");

                    if (var7 != null)
                    {
                        var5 = var7.getTextureBlur();
                        var6 = var7.getTextureClamp();
                    }
                }
                catch (RuntimeException var11)
                {
                    logger.warn("Failed reading metadata of: " + this.textureLocation, var11);
                }
            }

            TextureUtil.uploadTextureImageAllocate(this.getGlTextureId(), var4, var5, var6);
        }
        finally
        {
            if (var2 != null)
            {
                var2.close();
            }
        }
    }
}
