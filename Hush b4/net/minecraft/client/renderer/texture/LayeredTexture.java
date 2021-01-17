// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.texture;

import java.io.InputStream;
import java.util.Iterator;
import java.io.IOException;
import java.awt.image.ImageObserver;
import java.awt.Image;
import java.awt.image.BufferedImage;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.resources.IResourceManager;
import com.google.common.collect.Lists;
import org.apache.logging.log4j.LogManager;
import java.util.List;
import org.apache.logging.log4j.Logger;

public class LayeredTexture extends AbstractTexture
{
    private static final Logger logger;
    public final List<String> layeredTextureNames;
    
    static {
        logger = LogManager.getLogger();
    }
    
    public LayeredTexture(final String... textureNames) {
        this.layeredTextureNames = Lists.newArrayList(textureNames);
    }
    
    @Override
    public void loadTexture(final IResourceManager resourceManager) throws IOException {
        this.deleteGlTexture();
        BufferedImage bufferedimage = null;
        try {
            for (final String s : this.layeredTextureNames) {
                if (s != null) {
                    final InputStream inputstream = resourceManager.getResource(new ResourceLocation(s)).getInputStream();
                    final BufferedImage bufferedimage2 = TextureUtil.readBufferedImage(inputstream);
                    if (bufferedimage == null) {
                        bufferedimage = new BufferedImage(bufferedimage2.getWidth(), bufferedimage2.getHeight(), 2);
                    }
                    bufferedimage.getGraphics().drawImage(bufferedimage2, 0, 0, null);
                }
            }
        }
        catch (IOException ioexception) {
            LayeredTexture.logger.error("Couldn't load layered image", ioexception);
            return;
        }
        TextureUtil.uploadTextureImage(this.getGlTextureId(), bufferedimage);
    }
}
