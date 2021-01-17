// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.texture;

import java.io.IOException;
import java.awt.image.BufferedImage;
import net.minecraft.client.resources.IResource;
import java.io.InputStream;
import shadersmod.client.ShadersTex;
import optifine.Config;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.client.resources.IResourceManager;
import org.apache.logging.log4j.LogManager;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Logger;

public class SimpleTexture extends AbstractTexture
{
    private static final Logger logger;
    protected final ResourceLocation textureLocation;
    private static final String __OBFID = "CL_00001052";
    
    static {
        logger = LogManager.getLogger();
    }
    
    public SimpleTexture(final ResourceLocation textureResourceLocation) {
        this.textureLocation = textureResourceLocation;
    }
    
    @Override
    public void loadTexture(final IResourceManager resourceManager) throws IOException {
        this.deleteGlTexture();
        InputStream inputstream = null;
        try {
            final IResource iresource = resourceManager.getResource(this.textureLocation);
            inputstream = iresource.getInputStream();
            final BufferedImage bufferedimage = TextureUtil.readBufferedImage(inputstream);
            boolean flag = false;
            boolean flag2 = false;
            if (iresource.hasMetadata()) {
                try {
                    final TextureMetadataSection texturemetadatasection = iresource.getMetadata("texture");
                    if (texturemetadatasection != null) {
                        flag = texturemetadatasection.getTextureBlur();
                        flag2 = texturemetadatasection.getTextureClamp();
                    }
                }
                catch (RuntimeException runtimeexception) {
                    SimpleTexture.logger.warn("Failed reading metadata of: " + this.textureLocation, runtimeexception);
                }
            }
            if (Config.isShaders()) {
                ShadersTex.loadSimpleTexture(this.getGlTextureId(), bufferedimage, flag, flag2, resourceManager, this.textureLocation, this.getMultiTexID());
            }
            else {
                TextureUtil.uploadTextureImageAllocate(this.getGlTextureId(), bufferedimage, flag, flag2);
            }
        }
        finally {
            if (inputstream != null) {
                inputstream.close();
            }
        }
        if (inputstream != null) {
            inputstream.close();
        }
    }
}
